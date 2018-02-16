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

(def sample
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
    [:a.re-box.icon.border {:title ":a.re-box.icon"} "▼"]]])

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

       [:h4 "Modifiers: Borders"]

       [:div.re-row.debug
        [:input.re-box.border {:placeholder".re-box.border"}]
        [:input.re-box.underline {:placeholder ".re-box.underline"}]
        [:input.re-box {:placeholder ".re-box (i.e. none)"}]]

       [:br]
       [:h4 "Modifiers: Contrast"]

       [:div.re-row
        [:input.re-box.border {:placeholder "normal"}]
        [:input.re-box.contrast {:value ".re-box.contrast"}]
        [:input.re-box.selection {:value ".re-box.selection"}]
        [:input.re-box.inverse  {:value ".re-box.inverse"}]]

       [:br]
       [:h4 "Modifiers: hover"]

       [:div.re-row

        [:div.re-box.border.hover ".hover"]
        [:div.re-box.border.hover-selection ".hover-selection"]]

       [:br]

       [:h4 "Modifiers: radius & circle"]

       [:div.re-row
        [:div.re-box.border.radius ".radius"]
        [:div.re-box.border.circle
         [:div.re-box.icon.inverse.circle "☂"]
         ".circle.circle .circl.circle .circlee"]

        [:div.re-box.inverse.radius ".radius"]
        [:div.re-box.inverse.circle ".circle .circl.circle .circl.circle "]

        [:div.re-box.icon.inverse.circle "☂"]
        [:div.re-box.icon.contrast.circle "☂"]
        [:div.re-box.icon.border.circle "☂"]
        [:div.re-box.icon.underline.circle "☂"]


        [:div.re-box.icon.border.circle
         [:div.re-box.icon.border.circle.contrast "☂"]]]


       [:br]

       [:h4 "Modifiers: size"]
       

       [:h5 "Large"]
       [:div.re-lg sample]
       [:h5 "Normal"]
       [:div sample]
       [:h5 "Small"]
       [:div.re-sm sample]

       [:br]

       [:h4 "Modifiers: shadow"]
       [:div.re-row
        [:div.re-box.contrast.border.schd-1 ".schd-1"]
        [:div.re-box.contrast.border.schd-2 ".schd-2"]
        [:div.re-box.contrast.border.schd-3 ".schd-3"]
        [:div.re-box.contrast.border.schd-4 ".schd-4"]
        [:div.re-box.contrast.border.schd-5 ".schd-5"]
        [:div.re-box.contrast.border.schd-4 ".schd-4"]
        [:div.re-box.contrast.border.schd-3 ".schd-3"]
        [:div.re-box.contrast.border.schd-2 ".schd-2"]
        [:div.re-box.contrast.border.schd-1 ".schd-1"]]
       

       [:br]
       [:h4 "Grid model"]

       [:div.re-row.debug
        [:div.re-box.border.f1 ".f1"]
        [:div.re-box.border.f2 ".f2"]
        [:div.re-box.border.f3 ".f3"]
        [:div.re-box.border.f4 ".f4"]
        [:div.re-box.border.f5 ".f5"]
        [:div.re-box.border.f6 ".f6"]]
       #_[:ul
        [:li "borders: border, underline, none"]
        [:li "background: white, contrast, selection, inverse"]
        [:li "hover, focus & selection"]
        [:li "size: small, normal, large"]
        [:li "radius & cycle"]
        [:li "grid model"]
        [:li "shadows & popups"]
        [:li "nesting & ligatures"]]

       [:br]

       [:h4 "Nesting"]
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
 :design {:title "Design System"
          :w 0
          :cmp index})

