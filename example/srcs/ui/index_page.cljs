(ns ui.index-page
  (:require
   [ree-form.core :as rform]
   [ree-form.model :as fmodel]
   [ree-form.design :as design]
   [ui.routes :as ui-routes]
   [cljs.pprint :as pprint]
   [garden.core :as garden]
   [re-frame.core :as rf]
   [garden.units :as u]))

(def form-schema
  {:type "form"
   :fields {:name {:type "string"
                   :validators {::fmodel/required {:message "Name is required"}}}
            :given {:type "string"
                   :validators {::fmodel/required {:message "Given is required"}}}
            :is-admin {:type "boolean"}
            :homepage {:type "string"
                       :interceptors [::fmodel/trim]}
            :email {:type "email"
                    :validators {::fmodel/required {:message "Email is required"}
                                 ::fmodel/email {:message "Invalid email format"}}}

            :address {:type "form"
                      :fields {:city {:type "string"}
                               :zip {:type "string"}}}
            }})


(rf/reg-event-db
 ::init
 (fn [db [_ fpth v]]
   (assoc-in db fpth (fmodel/mk-form-data form-schema v))))

(defn input [{fp :form-path p :path}]
  (let [data (rf/subscribe [::fmodel/form-path fp p])]
    (fn [_]
      [:input.re-box.border
       {:value (:value @data)
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
       [:label.re-box.label (:label opts)
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
      [:div#debug
       [:style (garden/css
                [:* [:#debug {:font-size (u/px 12)}]])]

       [:h4 "Schema"]
       [:pre (with-out-str (pprint/pprint form-schema))]
       [:h4 "Value"]
       [:pre (with-out-str (pprint/pprint @val))]
       [:h4 "Form Data"]
       [:pre (with-out-str (pprint/pprint @data))]])))

(defn index []
  (let [form-path [:forms :index]
        data (rf/subscribe [::fmodel/form-path form-path])]
    (rf/dispatch [::init form-path {:name "Nikolai"
                                    :email "niquola@gmail.com"
                                    :is-admin true
                                    :address {:city "SPb"
                                              :zip "166155"}}])
    (fn []
      (println "rerender")
      [:div.row
       [:style (garden/css [[:.label {:width "10em"
                                      :text-align "right"}]
                            [:.form {:background-color "#f1f1f1"}]
                            design/style])]
       [:div.col.form
        [:h1 "Welcome to ree-form"]
        [:div.re-row
         [:label.re-box.label "Name:"]
         [input {:form-path form-path :path [:name]}]
         [errors {:form-path form-path :path [:name]}]]

        [:div.re-row
         [:label.re-box.label "Given:"]
         [input {:form-path form-path :path [:given]}]
         [errors {:form-path form-path :path [:given]}]
         [:small " (focus and blur)"]
         ]

        [:div.re-row
         [:label.re-box.label "Email:"]
         [input {:form-path form-path :path [:email]}]
         [errors {:form-path form-path :path [:email]}]
         [:small " (enter non-email string)"]
         ]

        [:div.re-row
         [:label.re-box.label "Home page"]
         [input {:form-path form-path :path [:homepage]}]
         [errors {:form-path form-path :path [:homepage]}]
         [:small " (with trim interceptor - try white spaces)"]
         ]



        [:div.re-row
         [checkbox {:form-path form-path :path [:is-admin]
                    :label "Admin"}]]

        [:h3 "Address"]
        [:div.re-row
         [:label.re-box.label "City:"]
         [input {:form-path form-path :path [:address :city]}]
         [errors {:form-path form-path :path [:address :city]}]
         [:small " (focus and blur)"]]

        [:div.re-row
         [:label.re-box.label "ZIP:"]
         [input {:form-path form-path :path [:address :zip]}]
         [errors {:form-path form-path :path [:address :zip]}]]

        ]

       [:div.col [debug form-path]]])))

(ui-routes/reg-page
 :index {:title "All components"
         :w 1
         :cmp index})
