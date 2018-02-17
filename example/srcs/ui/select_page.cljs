(ns ui.select-page
  (:require
   [ree-form.core :as rform]
   [ree-form.model :as fmodel]
   [ree-form.design :as design]
   [ui.routes :as ui-routes]
   [cljs.pprint :as pprint]
   [garden.core :as garden]
   [re-frame.core :as rf]
   [garden.units :as u]
   [cljs-time.format :as f]))


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

(defn select [{fp :form-path p :path ph :placeholder os :on-search}]
  (let [data  (rf/subscribe  [::fmodel/form-path fp p])
        close #(rf/dispatch [::fmodel/close-popup {:form-path fp :path p}])
        open  (fn [] (rf/dispatch [::fmodel/open-popup {:form-path fp :path p}]))
        on-key-down (fn [ev]
                      (let [k (.-which ev)]
                        (cond
                          (= 40 k) (rf/dispatch [::fmodel/item-down   {:form-path fp :path p}])
                          (= 38 k) (rf/dispatch [::fmodel/item-up     {:form-path fp :path p}])
                          (= 13 k) (rf/dispatch [::fmodel/item-select {:form-path fp :path p}]))))
        on-change (fn [ev]
                    (let [txt (.. ev -target -value)]
                      (when os (os txt))))
        on-click (fn [x] (fn [_]
                           (rf/dispatch [::fmodel/close-popup {:form-path fp :path p}])
                           (rf/dispatch [::fmodel/on-change {:form-path fp :path p :value x}])))]
    (fn [_]
      (let [*data @data]
        [:div.re-box.radius.border.re-col.pop-container {:id "input"
                                                         :tab-index 0
                                                         :on-focus #(open) 
                                                         :on-click fmodel/in-popup}
         [:div.re-box.re-row.--hm {:on-click open}
          [:div.re-box.icon.text- "▾"]
          [:div.re-box.text- (pr-str (or (:value *data) (or ph "Search...")))]]
         (when (get-in *data [:state :popup])
           [:div.pop.--hm
            [:div.re-row [:input.re-box.radius.border.--hm.hp.f1
                          {:auto-focus true
                           :on-key-down on-key-down
                           :on-change on-change}]]
            (doall
             (->> (get-in *data [:state :items])
                  (map-indexed (fn [i x]
                                 [:div.re-box.--hm.hp.underline.hover.text-
                                  {:key i :on-click (on-click x)
                                   :class (when  (= i (:selection (:state *data))) "contrast")}
                                  (pr-str "item-" x)]))))])]))))


(rf/reg-event-db
 ::search-pt
 (fn [db [_ {nm :value fp :form-path p :path :as args}]]
   (fmodel/set-items db (assoc args :items
                            (for [x (range 10)]
                              {:id x :display (str nm "-" x)})))))

(defn index []
  (let [form-path [:forms :popups]
        on-pt-search #(rf/dispatch [::search-pt {:value %
                                                 :form-path form-path
                                                 :path [:patient]}])
        data (rf/subscribe [::fmodel/form-path form-path])]
    (rf/dispatch [::init form-path {}])

    (fn []
      [:div
       [:h3 "Select"]
       [:style (garden/css [design/style
                            [:.re-box.pop-container
                             {:position "relative"}]
                            [:.re-box.pop {:position "absolute"
                                           :left 0
                                           :right 0
                                           :height "200px"
                                           :overflow-y "scroll"
                                           :border "1px solid #ddd"
                                           :z-index 1000
                                           :background "white"}]])]
       [:div.row
        [:div.col
         [select {:form-path form-path
                  :on-search on-pt-search
                  :path [:patient]}]
         [select {:form-path form-path :path [:nurse] :items ["a" "b" "c"]}]

         #_[:div.re-row
          [:div.re-box.border.radius.schd-2.hp--.hm
           [:div.re-box.icon.text- "▾"]
           [:div.re-box.text- "Nikolai"]
           [:div.re-box.icon.text- "✕"]]

          [:div.re-box.underline.hp--.hm
           [:div.re-box.icon.text- "▾"]
           [:div.re-box.text-- "Nikolai"]
           [:div.re-box.icon.text- "✕"]]

          [:div.re-box.hp--.hm
           [:div.re-box.icon.text- "▾"]
           [:div.re-box "Nikolai"]
           [:div.re-box.icon.text- "✕"]]

          [:div.re-box.border.hp--.hm
           [:div.re-box.icon.text- "▾"]
           [:div.re-box "Nikolai"]
           [:div.re-box.icon.text- "✕"]]


          [:div.re-box.border.circle.hp--.hm
           [:div.re-box.icon.circle.border "▾"]
           [:div.re-box "Nikolai"]
           [:div.re-box.icon "✕"]]]]
        [:div.col [:pre (with-out-str (cljs.pprint/pprint @data))]]]

       






       ]



      )))

(ui-routes/reg-page
 :selects {:title "Selects"
          :w 5
          :cmp index})
