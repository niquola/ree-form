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
(def w 8)
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
   [:.re-row {:display "flex"
              :padding {:top (u/px 5)
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
        :padding 0})]]
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
   

   [:.debug {:background-color (c/rgba (conj clr 0.05))
             :padding {:left (u/px* 2 w) :right (u/px* 2 w)}}]

   [:.re-contrast {:background {:color (c/rgba (conj clr 0.05))}}]
   [:.re-box
    {:display "flex"
     :line-height (u/px* h 4)
     :background-color "white"
     :margin {:right (u/px* 1.5 w)}

     :border "none"
     :box-shadow "none"
     :outline "none"
     :padding {:top (u/px- (/ h 2) 1)
               :left (u/px* 1.5 w)
               :right (u/px* 1.5 w)
               :bottom (u/px- (/ h 2) 1)}}
    [:&.contrast {:background {:color (c/rgba (conj clr 0.05))}}]
    [(keyword ".re-box:last-child") {:margin {:right 0}}]
    [:&.label {:display "block"
               :text-align "right"}]

    [:&.radius {:border-radius ".25rem"}]
    [:&.circle {:border-radius "100px"}]

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
    [:&.contrast  {:background-color (c/rgba (conj clr 0.05))}]
    [:&.selection {:background-color "hsla(226, 98%, 48%, 0.7)"
                   :color "white"}]
    [:&.inverse   {:background-color (c/rgba (conj clr 0.8))
                   :color "white"}]
    [:&.hover
     {:cursor "pointer"
      :transition "background-color .15s ease-in-out"}
     [:&:hover {:background-color (c/rgba (conj clr 0.05))}]]

    [:&.hover-selection
     {:cursor "pointer"
      :transition "background-color .15s ease-in-out"}
     [:&:hover {:background-color "hsla(226, 98%, 48%, 0.8)"
                :color "white"}]]

    [:&.underline
     {:border {:bottom {:width (u/px 1)
                       :style "solid"
                       :color (c/rgba (conj clr 0.4))}}
      :padding {:top (u/px- (/ h 2) 1)
                :left (u/px* 2 w)
                :right (u/px* 2 w)
                :bottom (u/px- (/ h 2) 2)}}]

    [:&.border
     {:border {:width (u/px 1)
               :style "solid"
               :color (c/rgba (conj clr 0.4))}
      :padding {:top (u/px- (/ h 2) 2)
                :left (u/px* 2 w)
                :right (u/px* 2 w)
                :bottom (u/px- (/ h 2) 2)}}]

    [:&.schd-1 {:box-shadow "0 1px 3px rgba(0,0,0,0.12), 0 1px 2px rgba(0,0,0,0.24)"}]
    [:&.schd-2 {:box-shadow "0 3px 6px rgba(0,0,0,0.16), 0 3px 6px rgba(0,0,0,0.23)"}]
    [:&.schd-3 {:box-shadow "0 10px 20px rgba(0,0,0,0.19), 0 6px 6px rgba(0,0,0,0.23)"}]
    [:&.schd-4 {:box-shadow "0 14px 28px rgba(0,0,0,0.25), 0 10px 10px rgba(0,0,0,0.22)"}]
    [:&.schd-5 {:box-shadow "0 19px 38px rgba(0,0,0,0.30), 0 15px 12px rgba(0,0,0,0.22)"}]

    [:&.f1 {:flex 1}]
    [:&.f2 {:flex 2}]
    [:&.f3 {:flex 3}]
    [:&.f4 {:flex 4}]
    [:&.f5 {:flex 5}]
    [:&.f6 {:flex 6}]



    ]])
