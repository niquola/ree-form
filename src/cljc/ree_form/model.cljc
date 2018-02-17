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
                     (let [*v (mk-form-data *sch (get val k))]
                       (assoc acc k *v)))
                   {} (:fields sch)))

    (= "coll" (:type sch))
    (->> (map-indexed (fn [i *val] [i (mk-form-data (dissoc (:item sch) :value) *val)]) val)
         (into {}))

    :else (assoc sch :value val)))

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


(def interceptors {::trim str/trim})

(defn apply-interceptors [ins v]
  (reduce (fn [v i] ((get interceptors i) v)) v ins))

(defn set-state [db {fp :form-path p :path s :state}]
  (let [pth (into fp (get-path p))
        d (get-in db pth)
        d* (update d :state (fn [x] (merge (or x {}) s)))]
    (assoc-in db pth d*)))


(rf/reg-event-db
 ::set-state
 (fn [db [_ arg]] (set-state db arg)))

(defn get-state [db {fp :form-path p :path}]
  (:state (get-in db (into fp (get-path p)))))

;; todo use fx for this
(def global-popup (atom nil))

(defn in-popup [ev]
  (.stopPropagation ev)
  (.stopImmediatePropagation (.-nativeEvent ev)))

(rf/reg-event-db
 ::open-popup
 (fn [db [_ {fp :form-path p :path :as args}]]
   (let [pth (into fp (get-path p))
         d (get-in db pth)
         d* (assoc-in d [:state :popup] true)]
     (.removeEventListener js/document "click" @global-popup)
     (let [f (fn [ev] (rf/dispatch [::close-popup args]))]
       (reset! global-popup f)
       (.addEventListener js/document "click" f))
     
     (assoc-in db pth d*))))

(rf/reg-event-db
 ::close-popup
 (fn [db [_ {fp :form-path p :path}]]
   (let [pth (into fp (get-path p))
         d   (get-in db pth)
         d*  (assoc-in d [:state :popup] false)]
     (assoc-in db pth d*))))

(rf/reg-event-db
 ::on-change
 (fn [db [_ {fp :form-path p :path v :value}]]
   (let [pth (into fp (get-path p))
         d (get-in db pth)
         inters (:interceptors d)
         v (if inters (apply-interceptors inters v) v)
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

(rf/reg-event-db
 ::item-down
 (fn [db [_ {fp :form-path p :path :as arg}]]
   (let [s (get-state db arg)
         idx (or (:selection s) 0)]
     (set-state db (assoc arg :state {:selection (inc idx)})))))

(rf/reg-event-db
 ::item-up
 (fn [db [_ {fp :form-path p :path :as arg}]]
   (let [s (get-state db arg)
         idx (or (:selection s) 0)]
     (set-state db (assoc arg :state {:selection (dec idx)})))))

(rf/reg-event-db
 ::item-select
 (fn [db [_ {fp :form-path p :path}]]
   db))

(defn set-items [db {fp :form-path p :path is :items}]
  (let [pth (into fp (get-path p))
        d   (get-in db pth)
        d*  (update d :state assoc :items is)]
    (assoc-in db pth d*)))

(rf/reg-event-db
 ::set-items
 (fn [db [_ args]] (set-items db args)))
