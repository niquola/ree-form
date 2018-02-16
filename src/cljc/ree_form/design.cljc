(ns ree-form.design
  (:require
   [garden.core :as garden]
   [garden.units :as u]
   [garden.color :as c]))


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
   [:.re-row {:display "flex" :margin {:top (u/px 5)
                                       :bottom (u/px 5)}}]
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
    [:&.border
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
    [:&.label {:display "block"
               :text-align "right"}]

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
    [:&.border.icon
     {:padding {:left 0
                :right 0}}]

    [:&.border
     {:border {:width (u/px 1)
               :style "solid"
               :color (c/rgba (conj clr 0.2))}
      :padding {:top (u/px- (/ h 2) 2)
                :left (u/px* 2 w)
                :right (u/px* 2 w)
                :bottom (u/px- (/ h 2) 2)}}]]])
