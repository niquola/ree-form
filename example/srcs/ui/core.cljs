(ns ui.core
  (:require
   [cljsjs.react]
   [reagent.core :as reagent]
   [garden.core :as garden]
   [garden.color :as c]
   [garden.units :as u]

   [frames.xhr]

   [re-frame.core :as rf]
   [route-map.core :as route-map]
   [ree-form.core :as form]
   [clojure.string :as str]
   [ui.routing]
   [ui.routes :refer [routes href pages] :as ui-routes]

   [ui.index-page]
   [ui.popup-page]
   [ui.design-page]
   [ui.select-page]
   [ui.shape]
   [ui.cominator]
   ;; [ui.file-upload-page :as fup]
   ;; [ui.styles-page]
   ;; [ui.inputs-page]
   ;; [ui.select-page]
   ;; [ui.switchbox-page]
   ;; [ui.date-page]
   ;; [ui.checkbox-page]
   ;; [ui.textarea-page]
   ))

(def h3 38)
(def h2 24)
(def h 16)
(defn style [gcss]
  [:style (garden/css gcss)])

(defn current-page []
  (let [current-route (rf/subscribe [:route-map/current-route])]
    (fn []
      (let [{page :match params :params} @current-route]
        (if page
          (if-let [cmp (:cmp (ui-routes/resolve-page (:id page)))]
            [:div [cmp params]]
            [:div.not-found (str "Page not found [" (str page) "]" )])
          [:div.not-found (str "Route not found ")])))))

(defn navigation []
  (let [current-route (rf/subscribe [:route-map/current-route])]
    (fn []
      [:div.navigation
       (style [:.navigation {:padding {:top (u/px 20)
                                       :right (u/px 20)}
                             :background-color "white"}
               [:a.navitem {:display "block"
                            :color "#888"
                            :border-left "6px solid #ddd"
                            :font-family "lato"
                            :padding {:top (u/px 10)
                                      :bottom (u/px 10)
                                      :left (u/px 20)}}
                [:&.active {:color "hsla(226, 98%, 48%, 0.7)"
                            :background-color "hsla(225.51724137931035, 21.80451127819549%, 26.078431372549016%, 0.05)"
                            :border-color "hsla(226, 98%, 48%, 0.7)"}]]])
       (doall
        (for [[i p] (sort-by (fn [[_ x]] (:w x)) @ui-routes/pages)]
          [:a.navitem {:key i
                       :class (when (= i (get-in @current-route [:match :id]))
                                "active")
                       :href (href (name i))} (:title p)]))])))

(defn root-component []
  [:div
   (style
    (let [nav-width 200]
      [:body
       {:font-family "Roboto, sans-serif"}
       [:.topnav {:border {:bottom "1px solid hsla(225.51724137931035, 21.80451127819549%, 26.078431372549016%, 0.2)"}}
        [:.brand {:display "inline-block"
                  :font-size (u/px 30)
                  :font-weight "bold"
                  :margin {:left (u/px 20)}
                  :font-family "lato"
                  :padding (u/px 10)}]]
       [:.navigation {:width (u/px nav-width)
                      :position "absolute"
                      :top (u/px 67)
                      :bottom 0
                      :left 0}]
       [:.pane {:margin {:left (u/px (+ nav-width 100))
                         :right (u/px 40)}
                :padding (u/px 20 40)}]
       [:h1 {:margin-bottom (u/px 30)
             :font-weight :normal
             :font-size (u/px h3)
             :line-height (u/px* h3 1.5)}]
       #_(form/form-style-fn form/default-base-consts)
       [:.re-form-row {:margin-top (u/px h2)}]
       [:pre {:background-color "#f1f1f1" :padding "20px" :border "1px solid #ddd"} ]
       [:label {:display "block"
                :margin-bottom (u/px 2)
                :font-size (u/px h)
                :line-height (u/px (/ (* h 3) 2))
                :padding-right "10px"}]
       [:.errors {:color "red"}]]))

   [:div.topnav [:a.brand "re-form"]]
   [navigation]
   [:div.pane [current-page]]])

(rf/reg-event-fx
 ::initialize
 (fn [cofx]
   (.log js/console (routes))
   {:dispatch-n [[:route-map/init (routes)]]}))

(defn dispatch-routes [_]
  (let [fragment (.. js/window -location -hash)]
    (rf/dispatch [:fragment-changed fragment])))


(defn mount-root []
  (reagent/render [root-component] (.getElementById js/document "app")))

(defn init! []
  (.log js/console "1")
  (rf/dispatch [::initialize])
  (.log js/console "2")
  (mount-root))

