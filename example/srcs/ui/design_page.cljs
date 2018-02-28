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


(defmulti mixin (fn [k style]
                  (if (vector? k) (first k) k)))

(defmethod mixin
  :bordered
  [_ style]
  (assoc style :border {:width (u/px 1)
                        :style "solid"
                        :color "#ddd"}))

(defmethod mixin
  :radius
  [_ style]
  (merge-with merge style {:border
                           {:top {:left {:radius (u/px 5)}
                                  :right {:radius (u/px 5)}}
                            :bottom {:left {:radius (u/px 5)}
                                     :right {:radius (u/px 5)}}}}))


(defmethod mixin
  :underline
  [_ style]
  (assoc style :border {:bottom {:width (u/px 1)
                                 :style "solid"
                                 :color "#ddd"}}))

(defmethod mixin
  :flex-row
  [_ style] (assoc style :display "flex"))

(defmethod mixin
  :small [_ style] (assoc style :font-size (u/px 12)))

(defmethod mixin
  :xsmall [_ style] (assoc style :font-size (u/px 10)))

(defmethod mixin
  :flex-col
  [_ style] (merge style {:display "flex" :flex-direction "column"}))

(defmethod mixin
  :contrast
  [_ style] (merge style {:background "#f6f6f6"}))


(def pallet {:gray "gray"})

(defmethod mixin :f  [[_ x] style] (assoc style :flex x))
(defmethod mixin :w  [[_ x] style] (assoc style :width (u/px* 5 x)))
(defmethod mixin :c  [[_ c] style] (assoc style :color (get pallet c)))
(defmethod mixin :t  [[_ a] style] (assoc style :text-align a))
(defmethod mixin :ml [_ style] (merge-with merge style {:margin  {:left "5px"}}))
(defmethod mixin :mr [_ style] (merge-with merge style {:margin  {:right "5px"}}))
(defmethod mixin :mh [_ style] (merge-with merge style {:margin  {:right "5px" :left "5px"}}))
(defmethod mixin :ph [_ style] (merge-with merge style {:padding {:right "5px" :left "5px"}}))
(defmethod mixin :pv [_ style] (merge-with merge style {:padding {:top "5px" :bottom "5px"}}))
(defmethod mixin :mv [_ style] (merge-with merge style {:margin {:top "5px" :bottom "5px"}}))
(defmethod mixin :-mt [_ style] (merge-with merge style {:margin {:top "-1px"}}))
(defmethod mixin :-ml [_ style] (merge-with merge style {:margin {:left "-1px"}}))
(defmethod mixin :p  [_ style] (merge-with merge style {:padding {:right "5px"
                                                                      :top "5px"
                                                                      :bottom "5px"
                                                                      :left "5px"}}))


(defn mixins [xs]
  (reduce (fn [acc x] (mixin x acc)) {} xs))

(def styles
  (garden/css
   [
    [:input {:border "none" :padding 0 :margin 0}]
    [:.xcol (mixins [:flex-col [:f 1]])]
    [:.xrow (mixins [:flex-row])]
    [:.xform (mixins [[:w 100]])]
    [:.comment (mixins [:mv [:c :gray] :xsmall])]
    [:.normal
     [:label (mixins [:p :mr [:c :gray] [:t :right] [:w 30]])]
     [:input (mixins [:bordered [:f 1] :p :mr :contrast :radius])]]

    [:.exel
     [:input (mixins [:bordered [:f 1] :p :-mt :-ml :contrast])]]

    [:div.options (mixins [:bordered :radius :mv])
     [:div.option (mixins [:underline :p])
      [:&:hover (mixins [:contrast])]]]

    [:.material
     [:label (mixins [:pv [:c :gray] [:t :right] [:w 30]])]
     [:input  (mixins [:pv :underline [:f 1]])]]]))


(defn index []
  (fn []
    [:div
     [:h1 "Design system"]
     [:style styles]

     [:div.xform.normal
      [:div.xcol
       [:div.xrow
        [:label "Given"]
        [:div.xcol.xrow
         [:input.box {:placeholder "input"}]
         [:div.comment "Some notes"]]]
       [:div.xrow
        [:label "Family"]
        [:input.box {:placeholder "input"}]]
       ]]

     [:br]

     
     [:div.exel
      [:div.xcol
       [:div.xrow
        [:input]
        [:input]
        [:input]
        [:input]
        [:input]]
       [:div.xrow
        [:input]
        [:input]
        [:input]
        [:input]
        [:input]]]]

     [:br]

     [:div.xform.material
      [:div.xcol
       [:div.xrow
        [:label "Given"]
        [:div.xcol.xrow
         [:input.box {:placeholder "input"}]
         [:div.comment "Some notes"]]]
       [:div.xrow
        [:label "Family"]
        [:div.xcol
         [:input {:placeholder "input"}]
         [:div.options
          [:div.option "Option 1"]
          [:div.option "Option 1"]
          [:div.option "Option 1"]]]]]]])

  #_(fn []
      [:div 
       [:style (garden/css design/style)]

       [:h4 "Atoms"]

       [:div "Borders: [] () _ none"]
       [:div "Padding & margin: ..[... ...]."]
       [:div "Flex: ^ - column; > - row; {flex:1} {{flex:2}} {{{flex:3}}}"]
       [:div "Text size: t++ t+ t t- t--"]
       [:div "Phrase: .[.^t.]."]


       (pr-str [:select {:container "[.^.]" :search    ".[t%]." :options   ".@_t_#."}])

       (pr-str [:button {:container ".@[..t..]#."}])
       (pr-str [:button-with-icon
                {:container ".@[.t..]#."
                 :icon ".|t%|.."}])

       (pr-str [:tags-input
                {:container ".[>. .]."
                 :tag ".[..t..].."
                 :close ".@|.i%.|."
                 :input ".|..t..|."
                 :icon ".|t%|.."}])

       (pr-str [:form-line
                {:container "|>|"
                 :label "|t>..|."
                 :fields "{||}"}])

       [:div.re-row [:div.re-box.l1.rm.text- "Icon:"]  [:div.re-box.icon.contrast.circle.hp "◵"]]
       [:div.re-row [:div.re-box.l1.rm.text- "Text:"]  [:div.re-box.text.hp "This is a text"]]
       [:div.re-row [:div.re-box.l1.rm.text- "Box:"]   [:div.re-box.contrast.hp "Box"]]

       [:div.re-row [:div.re-box.l1.rm.text- "Label:"]   [:div.re-box.label.hp "Label"]]
       [:div.re-row [:div.re-box.l1.rm.text- "Popup:"] [:div.re-box.re-col.border.schd-2.radius
                                                        [:input.re-box.text--.border.--hm.hp.radius {:value "Search...."}]
                                                        [:div.re-box.underline.--hm.hover.hp.text- "Option 1"]
                                                        [:div.re-box.underline.--hm.hover.hp.text- "Option 1"]
                                                        [:div.re-box.underline.--hm.hover.hp.selection.text- "Option 1"]
                                                        [:div.re-box.underline.--hm.hover.hp.text- "Option 1"]]]

       [:pre
        "[.^.]] .&(. .). .&_."

        "[. @ [.. ..].. [.. ..].. @. ]"
        "@ [.. ..].. [.. ..].. @. ]"
        ".t->. _"]

       [:br]
       [:h4 "Box Model"]

       [:div.re-row
        [:div.re-box.border.icon [:div.re-box.border.icon [:div.re-box.border.icon "3"]]]
        [:div.re-box.border.hp.hm
         "1st level"
         [:div.re-box.border.contrast.hp.hm
          "2nd level"
          [:div.re-box.border.hp.hm "3rd level"]]]]

       [:h4 "Modifiers: Borders"]

       [:div.re-row.debug
        [:input.re-box.border.hm.hp    {:placeholder".re-box.border"}]
        [:input.re-box.hm.hp.border-   {:placeholder".re-box.border-"}]
        [:input.re-box.hm.hp.underline {:placeholder ".re-box.underline"}]
        [:input.re-box.hm.hp           {:placeholder ".re-box (i.e. none)"}]]

       [:div.re-row
        [:input.re-box.border.hm.hp    {:placeholder".re-box.border"}]
        [:input.re-box.hm.hp.border-   {:placeholder".re-box.border-"}]
        [:input.re-box.hm.underline    {:placeholder ".re-box.underline"}]
        [:input.re-box.hm.hp           {:placeholder ".re-box (i.e. none)"}]]
       [:div.re-row
        [:input.re-box.border.hp    {:placeholder".re-box.border"}]
        [:input.re-box.border.hp.ll {:placeholder".re-box.border"}]
        [:input.re-box.border.hp.ll {:placeholder".re-box.border"}]]


       [:br]
       [:h4 "Modifiers: Contrast"]


       [:div.re-row
        [:input.re-box.border.hp {:placeholder "normal"}]
        [:input.re-box.contrast.hp {:value ".re-box.contrast"}]
        [:input.re-box.selection.hp {:value ".re-box.selection"}]
        [:input.re-box.inverse.hp  {:value ".re-box.inverse"}]]

       [:br]
       [:h4 "Modifiers: hover"]

       [:div.re-row

        [:div.re-box.border.hover ".hover"]
        [:div.re-box.border.hover-selection ".hover-selection"]]

       [:br]

       [:h4 "Modifiers: radius & circle"]

       [:div.re-row
        [:div.re-box.border.radius.hm.hp ".radius"]
        [:div.re-box.border.circle.hm
         [:div.re-box.icon.inverse.circle.--hm "☂"]
         ".circle.circle .circl.circle .circlee"]

        [:div.re-box.inverse.radius.hm.hp ".radius"]
        [:div.re-box.inverse.circle.hm.hp ".circle .circl.circle .circl.circle "]

        [:div.re-box.icon.inverse.circle.hm "☂"]

        [:div.z.i.rc.h.ci "☂"]

        [:div.re-box.icon.contrast.circle.hm "☂"]
        [:div.re-box.icon.border.circle.hm "☂"]
        [:div.re-box.icon.underline.circle.hm "☂"]


        [:div.re-box.icon.border.circle.hm
         [:div.re-box.icon.border.circle.contrast.--hm "☂"]]]


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
        [:div.re-box.contrast.border.hm.hp " nop"]
        [:div.re-box.contrast.border.schd-1.hm.hp ".schd-1"]
        [:div.re-box.contrast.border.schd-2.hm.hp ".schd-2"]
        [:div.re-box.contrast.border.schd-3.hm.hp ".schd-3"]
        [:div.re-box.contrast.border.schd-4.hm.hp ".schd-4"]
        [:div.re-box.contrast.border.schd-5.hm.hp ".schd-5"]
        [:div.re-box.contrast.border.schd-4.hm.hp ".schd-4"]
        [:div.re-box.contrast.border.schd-3.hm.hp ".schd-3"]
        [:div.re-box.contrast.border.schd-2.hm.hp ".schd-2"]
        [:div.re-box.contrast.border.schd-1.hm.hp ".schd-1"]
        [:div.re-box.contrast.border.hm.hp " nop"]
        ]
       

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

      ))

(ui-routes/reg-page
 :design {:title "Design System"
          :w 0
          :cmp index})

