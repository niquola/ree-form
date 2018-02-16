(ns ui.design-page
  (:require
   [ree-form.core :as rform]
   [ree-form.model :as fmodel]
   [ree-form.design :as design]
   [ui.routes :as ui-routes]
   [cljs.pprint :as pprint]
   [garden.core :as garden]
   [re-frame.core :as rf]
   [garden.units :as u]
   [garden.color :as c]
   ))

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


(defn index []
  (let [form-path [:forms :popups]
        data (rf/subscribe [::fmodel/form-path form-path])]
    (rf/dispatch [::init form-path {:name "Nikolai"
                                    :email "niquola@gmail.com"
                                    :is-admin true
                                    :address {:city "SPb"
                                              :zip "166155"}}])
    (fn []
      [:div 
       [:style (garden/css design/style)]

       [:h1 "Box model"]

       [:h4 "Dimentions"]
       [:ul
        [:li "borders: border, underline, none"]
        [:li "background: white, contrast, selection, inverse"]
        [:li "hover, focus & selection"]
        [:li "size: small, normal, large"]
        [:li "radius & cycle"]
        [:li "grid model"]
        [:li "shadows & popups"]
        [:li "nesting & ligatures"]]

       [:br]

       [:a.re-row
        [:a.re-box.border.icon {:title ":a.re-box.icon"} "⇩"]
        [:a.re-box.contrast  ":a.re-box.contrast"]

        [:a.re-box.border.icon
         [:a.re-box.icon.contrast]]

        [:a.re-box.border
         [:a.re-box ".re-box"]

         [:a.re-box.contrast ".re-box.contrast "
          [:a.re-box.icon.border.lm {:title ":a.re-box.icon"} "⇧"]]

         [:a.re-box.border ".re-box.contrast "
          [:a.re-box.icon {:title ":a.re-box.icon"} "✕"]]

         [:a.re-box.icon.contrast.border {:title ":a.re-box.icon"} "⇩"]

         [:a.re-box.border ".re-box.contrast"]

         [:a.re-box.icon.contrast {:title ":a.re-box.icon"} "⇩"]

         [:a.re-box.icon {:title ":a.re-box.icon"} "✕"]
         [:a.re-box.icon.border {:title ":a.re-box.icon"} "▼"]

         ]]]

      )))

(ui-routes/reg-page
 :design {:title "Design"
          :w 2
          :cmp index})

