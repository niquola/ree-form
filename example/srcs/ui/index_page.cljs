(ns ui.index-page
  (:require
   [ree-form.core :as rform]
   [ree-form.model :as fmodel]
   [ui.routes :as ui-routes]
   [cljs.pprint :as pprint]
   [garden.core :as garden]
   [re-frame.core :as rf]))

(def form-schema
  {:type "form"
   :fields {:name {:type "string"
                   :validators {::fmodel/required {:message "Name is required"}}}
            :given {:type "string"
                   :validators {::fmodel/required {:message "Given is required"}}}
            :is-admin {:type "boolean"}
            :email {:type "email"
                    :validators {::fmodel/required {:message "Email is required"}
                                 ::fmodel/email {:message "Invalid email format"}}}}})


(rf/reg-event-db
 ::init
 (fn [db [_ fpth v]]
   (assoc-in db fpth (fmodel/mk-form-data form-schema v))))

(defn input [{fp :form-path p :path}]
  (let [data (rf/subscribe [::fmodel/form-path fp p])]
    (fn [_]
      [:input.form-control {:value (:value @data)
                            :class (when (:errors @data) "is-invalid")
                            :on-blur #(rf/dispatch [::fmodel/on-blur {:form-path fp :path p}])
                            :on-change #(rf/dispatch [::fmodel/on-change
                                                      {:form-path fp
                                                       :value (.. % -target -value)
                                                       :path p}])}])))

(defn checkbox [{fp :form-path p :path :as opts}]
  (let [data (rf/subscribe [::fmodel/form-path fp p])]
    (fn [opts]
      [:div.checkbox {:on-click #(rf/dispatch [::fmodel/on-change
                                               {:form-path fp
                                                :value (not (:value @data)) 
                                                :path p}])}
       [:label (:label opts)
        " "
        [:span.check {:style {:font-size "30px" :cursor "pointer"}}
         (if (:value @data) "☑" "☐")]]])))

(defn errors [{fp :form-path p :path}]
  (let [data (rf/subscribe [::fmodel/form-path fp p])]
    (fn [_]
      (if-let [err (:errors @data)]
        [:div.errors
         (for [[k v] err]
           [:span {:key k} v  ", "])]
        [:span]))))


(defn debug [form-path]
  (let [data (rf/subscribe [::fmodel/form-path form-path])
        val (rf/subscribe [::fmodel/value form-path])]
    (fn []
      [:div
       [:style (garden/css rform/awesome-styles)]
       [:h4 "Value"]
       [:pre (pr-str @val)]
       [:h4 "Form Data"]
       [:pre (with-out-str (pprint/pprint @data))]])))

(defn index []
  (let [form-path [:forms :index]
        data (rf/subscribe [::fmodel/form-path form-path])]
    (rf/dispatch [::init form-path {:name "Nikolai" :email "niquola@gmail.com" :is-admin true}])
    (fn []
      [:div.row
       [:div.col
        [:h1 "Welcome to ree-form"]
        [:div.form-group
         [:label "Name:"]
         [input {:form-path form-path :path [:name]}]
         [errors {:form-path form-path :path [:name]}]]

        [:div.form-group
         [:label "Given:"]
         [input {:form-path form-path :path [:given]}]
         [errors {:form-path form-path :path [:given]}]]

        [:div.form-group
         [:label "Email:"]
         [input {:form-path form-path :path [:email]}]
         [errors {:form-path form-path :path [:email]}]] 

        [:div.form-group
         [checkbox {:form-path form-path :path [:is-admin]
                    :label "Admin"}]]]

       [:div.col
        [debug form-path]]])))

(ui-routes/reg-page
 :index {:title "All components"
         :w 1
         :cmp index})
