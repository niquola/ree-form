(ns ree-form.model
  (:require-macros [reagent.ratom :refer [reaction run!]])
  (:require [reagent.core :as r]
            [re-frame.core :as rf]
            [clojure.string :as str]))

(defn validate-email
  [email]
  (let [pattern #"[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?"]
    (and (string? email) (re-matches pattern email))))

(def validators (atom {::required #(not (str/blank? %))
                       ::email validate-email}))

(defn reg-validator [k f]
  (swap! validators assoc k f))

(defn errors [vals v]
  (let [res (reduce-kv (fn [errs k vcfg]
                         (if-let [vld (get @validators k)]
                           (if-not (vld v)
                             (assoc errs k (or (:message vcfg) "Ups :("))
                             errs)
                           (assoc errs k (str "Could not resolve validator: " k " expected one of " (keys @validators)))))
                       {} vals)]
    (when-not (empty? res) res)))


(defn mk-form-data [sch val]
  (cond
    (= "form" (:type sch))
    (assoc sch :value 
           (reduce (fn [acc [k *sch]]
                     (assoc acc k (assoc (dissoc *sch :value) :value (mk-form-data *sch (get val k))))) {} (:fields sch)))

    (= "coll" (:type sch))
    (->> (map-indexed (fn [i *val] [i (mk-form-data (dissoc (:item sch) :value) *val)]) val)
         (into {}))

    :else val))

(defn get-value [form-data]
  (cond
    (= (:type form-data) "form")
    (reduce-kv (fn [m k v]
                 (assoc m k (get-value v)))
               {} (:value form-data))

    (= (:type form-data) "coll")
    (->> (:value form-data)
         (mapv (fn [[i v]] [i (get-value v)]))
         (sort-by first)
         (mapv second))

    :else (:value form-data)))

(comment
  (get-value {:type "form"
              :value {:k {:value "x"}
                      :xs {:type "coll"
                           :value {0 {:value "a"}
                                   1 {:value "b"}}}}}))

(defn get-path  [pth]
  (reduce (fn [acc x]
            (conj acc :value x))
          [] pth))


(rf/reg-event-db
 ::on-change
 (fn [db [_ {fp :form-path p :path v :value}]]
   (let [pth (into fp (get-path p))
         d (get-in db pth)
         vals (:validators d)
         d* (merge d {:value v
                      :errors (errors vals v)
                      :touched true})
         db (assoc-in db pth d*)]
     (loop [lp p]
       (let [ipth (into fp (get-path lp))
             node (get-in db ipth)]
         (when-let [on-change (:on-change node)]
           (rf/dispatch [(:event on-change)
                         (assoc on-change
                                :value (get-value node)
                                :form-path fp
                                :path lp)]))
         (when-not (empty? lp)
           (recur (butlast lp)))))
     db)))

(rf/reg-event-db
 ::on-blur
 (fn [db [_ {fp :form-path p :path}]]
   (let [pth (into fp (get-path p))
         d (get-in db pth)
         v  (:value d)
         vals (:validators d)
         d* (merge d {:errors (errors vals v)
                      :touched true})
         db (assoc-in db pth d*)]
     (loop [lp p]
       (let [ipth (into fp (get-path lp))
             node (get-in db ipth)]
         (when-let [on-change (:on-change node)]
           (rf/dispatch [(:event on-change)
                         (assoc on-change
                                :value (get-value node)
                                :form-path fp
                                :path lp)]))
         (when-not (empty? lp)
           (recur (butlast lp)))))
     db)))

(rf/reg-sub-raw
 ::form-path
 (fn [db [_ fp p]]
   (let [cur (r/cursor db (into fp (get-path p)))]
     (reaction @cur))))

(rf/reg-sub-raw
 ::value
 (fn [db [_ fp]]
   (let [cur (r/cursor db fp)]
     (reaction (get-value @cur)))))
