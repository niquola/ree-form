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

      )))

(ui-routes/reg-page
 :design {:title "Design System"
          :w 0
          :cmp index})

