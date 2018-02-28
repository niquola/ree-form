(ns ui.shape
  (:require
   [ree-form.core :as rform]
   [ree-form.model :as fmodel]
   [ui.routes :as ui-routes]
   [cljs.pprint :as pprint]
   [garden.core :as garden]
   [re-frame.core :as rf]
   [garden.units :as u]))


(def ratio (/ 1 1.61))
(def scale 100)





(def styles
  (into 
   [[:.gray  {:background "#aaa"}]
    [:.white {:background "#fff"}]
    [:.t {:width "45em"}]
    ]
   (for [s [5, 7, 9, 12, 16, 21, 28, 37, 50, 67, 83]]
     [(keyword (str ".f-" s))
      {:font-size (u/px s)
       :margin-bottom "0.5em"
       :line-height "2em"}])))

(defn index []
  (let []
    (fn []
      [:div
       [:div.f-67 "Shape"] 
       [:br]
       [:style (garden/css styles)]

       [:div.f-37 "This is a title" ]
       [:span.f-28 " Some note"]
       [:div.t.f-16
        "One of the best ways to establish a clear visual hierarchy, and create a design that converts, is to change the sizes of the fonts you use."
        "One of the best ways to establish a clear visual hierarchy, and create a design that converts, is to change the sizes of the fonts you use."]
       [:div.t.f-16
        "One of the best ways to establish a clear visual hierarchy, and create a design that converts, is to change the sizes of the fonts you use."
        "One of the best ways to establish a clear visual hierarchy, and create a design that converts, is to change the sizes of the fonts you use."]

       [:span.f-28 " Some note"]
       [:div.t.f-16 "One of the best ways to establish a clear visual hierarchy, and create a design that converts, is to change the sizes of the fonts you use."]

       [:hr]
       [:div.t.f-12 "Here is some foot notes"]
       [:hr]

       [:div
        (for [s (reverse [5, 7, 9, 12, 16, 21, 28, 37, 50, 67, 83])]
          [:div {:class (keyword (str "f-" s))}
           (str "Size - " s)])]

       ])))

(ui-routes/reg-page
 :shape {:title "Shape"
         :w 1
         :cmp index})
