(ns ree-form.core
  (:require
   [reagent.core :as r]
   [cljs.pprint :as pp]
   [clojure.string :as str]))


(declare awesome-print*)

(def awesome-styles
  [:.awesome-print {:font-family "Hack, monospace" :background "white" :padding "10px" :margin "10px 0"}
   [(keyword ".paren") {:font-weight "bold" :$color :bracket}]
   [(keyword ".key::after") {:content "':'" :$push-right 0.5 :$color :bracket}]
   [:.kv {:display "block" :margin-left "1em"}
    [:.key {:color "#DA023D" :margin-right "5px"}]]
   [:.arr {:margin-left "1em"}]
   [(keyword ".arr > *") {:display "block"}]
   [(keyword ".key > .str") {:$color :dark-blue}]
   [:.bool :.nil {:$color :dark-blue }]
   [:.arr :.map {:display "block" :$border [:left :solid 1 :transparent]} ]
   [:.arr [:&:hover {:$border [:left :dashed 1 :gray]}] ]
   [:.map
    {:border-left "1px solid #ddd"
     :margin-bottom "1em"}
    [:&:hover {:$border [:left :dashed 1 :gray]}] ]
   [(keyword ".map>*") (keyword ".arr>*")  {:$margin [0 0 0 1]} ]])

(defn kv [k v]
  (let [opened (r/atom false)
        switch (fn [_] (swap! opened #(not %)))]
    (fn []
      [:span.kv
       [:a.key {:on-click switch} [awesome-print* k] [:span.str (if @opened " ▴" " ▾")]]
       (if @opened
         [:span.val [awesome-print* v]]
         [:a.val {:on-click switch}  (if (map? v) "{...}" "[...]")])])))

(defn- awesome-print* [x]
  (cond
    (nil? x)    [:span.nil "null"]
    (string? x) [:span.str [:span.quote.begin "\""] x [:span.quote.end "\""]]
    (number? x) [:span.num x]
    (or (= x false) (= x true )) [:span.bool (str x)]
    (or (symbol? x) (keyword? x)) [:span.str (name x)]

    (or (list? x) (vector? x))
    [:div [:span.arr (for [i x] [:span {:key (str i)} [awesome-print* i]])]]

    (map? x)
    [:div
     ;; [:span.paren.curly.begin "{"]
     [:span.map
      (doall (for [[k v] x]
               (cond 
                 (and (or (list? v) (vector? v)) (or (string? (first v)) (number? (first v))))
                 [:span.kv {:key (str k)}
                  [:span.key [awesome-print* k]]
                  [:span.iarr "[" (str/join ", " v) "]"]]

                 (or (map? v) (vector? v) (list? v))
                 ^{:key (str k)} [kv k v]

                 :else
                 [:span.kv {:key (str k)}
                  [:span.key [awesome-print* k]]
                  [:span.val [awesome-print* v]]]
                 )))]
     ;; [:span.paren.curly.begin "}"]
     ]

    :else [:div.err "Do not know how to draw: " (pr-str x)]))

(defn awesome-print [x]
  [:div.awesome-print [awesome-print* x]])
