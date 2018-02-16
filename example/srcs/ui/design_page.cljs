(ns ui.design-page
  (:require
   [ree-form.core :as rform]
   [ree-form.model :as fmodel]
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


(def colors {1 1
             2 0.5
             3 0.2
             4 0.1
             5 0.05})

(def h 8)
(def w 6)
(def clr [52 59 81])

(defn block-style [h w clr]
  {:line-height (u/px* h 4)

   :padding {:top (u/px- (/ h 2) 1)
             :left (u/px* 2 w)
             :bottom (u/px- (/ h 2) 1)}

   :border {:width (u/px 1)
            :style "solid"
            :color (c/rgba (conj clr 0.2))}})

(def style
  [:body
   [:.re-row {:display "flex"}]
   [(keyword ".re-box > .re-box > .re-box")
    {:line-height (u/px (+ (* h 2) 4))
     :padding {:top (u/px- (/ h 2) 2)
               :bottom (u/px- (/ h 2) 2)}}
    [:&.icon
     (let [r (+ (* h 3) 4)]
       {:width (inc (u/px r))
        :height (u/px r)
        :line-height (u/px r)
        :display "block"
        :text-align "center"
        :padding 0})]
    ]
   [(keyword ".re-box > .re-box")
    {:line-height (u/px (+ (* h 3) 4))
     :padding {:top (u/px- (/ h 2) 2)
               :bottom (u/px- (/ h 2) 2)}}
    [:&.bordered
     #_{:padding {:top (u/px- (/ h 2) 3)
                :left (u/px* 2 w)
                :right (u/px* 2 w)
                :bottom (u/px- (/ h 2) 3)}}]
    [:&.icon
     (let [r (+ (* h 4) 2)]
       {:width (u/px (inc r))
        :height (u/px r)
        ;; :margin {:left (u/px* 1.5 w)}
        :line-height (u/px r)
        :display "block"
        :text-align "center"
        :padding 0})]]
   

   [:.re-box
    {:display "flex"
     :line-height (u/px* h 4)
     :margin {:right (u/px* 1.5 w)}

     :padding {:top (u/px- (/ h 2) 1)
               :left (u/px* 1.5 w)
               :right (u/px* 1.5 w)
               :bottom (u/px- (/ h 2) 1)}}
    [:&.contrast {:background {:color (c/rgba (conj clr 0.05))}}]
    [(keyword ".re-box:last-child") {:margin {:right 0}}]

    [:&.icon
     {:width (u/px (+ (* h 5) 2))
      :height (u/px* h 5)
      :padding {:top (u/px- (/ h 2) 1)
                :left 0
                :right 0
                :bottom (u/px- (/ h 2) 1)}
      :display "block"
      :text-align "center"
      :color (c/rgba (conj clr 0.5))}]

    [:&.lm {:margin {:left (u/px* 1.5 w)}}]
    [:&.bordered.icon
     {:padding {:left 0
                :right 0}}]

    [:&.bordered
     {:border {:width (u/px 1)
               :style "solid"
               :color (c/rgba (conj clr 0.2))}
      :padding {:top (u/px- (/ h 2) 2)
                :left (u/px* 2 w)
                :right (u/px* 2 w)
                :bottom (u/px- (/ h 2) 2)}}]]])

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
       [:style (garden/css style)]

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
        [:a.re-box.bordered.icon {:title ":a.re-box.icon"} "⇩"]
        [:a.re-box.contrast  ":a.re-box.contrast"]

        [:a.re-box.bordered.icon
         [:a.re-box.icon.contrast]]

        [:a.re-box.bordered
         [:a.re-box ".re-box"]

         [:a.re-box.contrast ".re-box.contrast "
          [:a.re-box.icon.bordered.lm {:title ":a.re-box.icon"} "⇧"]]

         [:a.re-box.bordered ".re-box.contrast "
          [:a.re-box.icon {:title ":a.re-box.icon"} "✕"]]

         [:a.re-box.icon.contrast.bordered {:title ":a.re-box.icon"} "⇩"]

         [:a.re-box.bordered ".re-box.contrast"]

         [:a.re-box.icon.contrast {:title ":a.re-box.icon"} "⇩"]

         [:a.re-box.icon {:title ":a.re-box.icon"} "✕"]
         [:a.re-box.icon.bordered {:title ":a.re-box.icon"} "▼"]

         

         ]]]

      )))

(ui-routes/reg-page
 :design {:title "Design"
          :w 2
          :cmp index})

