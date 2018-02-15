(ns ui.popup-page
  (:require
   [ree-form.core :as rform]
   [ree-form.model :as fmodel]
   [ui.routes :as ui-routes]
   [cljs.pprint :as pprint]
   [garden.core :as garden]
   [re-frame.core :as rf]
   [garden.units :as u]))

(def form-schema
  {:type "form"
   :fields {:nurse {:type "object"
                      :validators {::fmodel/required {:message "Patient is required"}}}
            :patient {:type "object"
                      :validators {::fmodel/required {:message "Patient is required"}}}}})


(rf/reg-event-db
 ::init
 (fn [db [_ fpth v]]
   (assoc-in db fpth (fmodel/mk-form-data form-schema v))))

(defn popup [{fp :form-path p :path}]
  (let [data (rf/subscribe [::fmodel/form-path fp p])
        close #(rf/dispatch [::fmodel/close-popup {:form-path fp :path p}])]
    (fn [_]
      [:div.col-3
       [:div.popup-container {:on-click fmodel/in-popup}

        [:b (str (:value @data))]
        [:input.form-control {;;:value (:value @data)
                              :on-focus #(rf/dispatch [::fmodel/open-popup {:form-path fp :path p}])
                              :on-blur close}]
        (when (get-in @data [:state :popup])
          [:div.popup
           (for [x (range 100)]
             [:div.item {:key x
                    :on-click #(rf/dispatch [::fmodel/on-change {:form-path fp :path p :value {:id x :display (str "item-" x)}}])}
              (str "item-" x)])])]])))

(defn index []
  (let [form-path [:forms :popups]
        data (rf/subscribe [::fmodel/form-path form-path])]
    (rf/dispatch [::init form-path {:name "Nikolai"
                                    :email "niquola@gmail.com"
                                    :is-admin true
                                    :address {:city "SPb"
                                              :zip "166155"}}])
    (fn []
      [:div [:h3 "Popup1"]
       [:style (garden/css [:.popup-container {:position "relative"}
                            [:.popup {:position "absolute"
                                      :left 0
                                      :right 0
                                      :max-height "200px"
                                      :overflow-y "scroll"
                                      :border "1px solid #ddd"
                                      :z-index 1000
                                      :background "white"}
                             [:.item {:padding "5px 10px"
                                      :border-bottom "1px solid #ddd"}
                              [:&:hover {:background-color  "#f1f1f1"}]
                              ]
                             ]])]
       [popup {:form-path form-path :path [:patient]}]
       [:br]
       [popup {:form-path form-path :path [:nurse]}]]



      )))

(ui-routes/reg-page
 :popups {:title "Popups"
          :w 2
          :cmp index})
