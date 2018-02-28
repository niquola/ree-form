(ns ui.cominator
  (:require
   [ree-form.core :as rform]
   [ree-form.model :as fmodel]
   [ui.routes :as ui-routes]
   [cljs.pprint :as pprint]
   [garden.core :as garden]
   [re-frame.core :as rf]
   [garden.units :as u]))


(def styles
  [
   [:.material
    [:.xbtn {:border "none"
             :background "#666"
             :display "inline-block"
             :cursor "pointer"
             :border-radius "3px"
             :padding "6px 30px"
             :color "white"
             :box-shadow "0 3px 6px 0 rgba(0,0,0,0.2)"}]
    [:.xform-control
     {:display "inline-block"
     :border "none"
     :padding "6px 0"
     :border-bottom "solid 1px #999"}]]

   [:.mailchimp
    [:.xbtn {:font-weight "500"
             :font-family "'Open Sans','Helvetica Neue',Arial,Helvetica,Verdana,sans-serif"
             :text-align "center"
             :vertical-align "middle"
             :text-transform "capitalize"
             :user-select "none"
             :padding "0 18px"
             :border "0 none"
             :border-radius "3px"
             :display "inline-block"
             :font-size "13px"
             :height "32px"
             :line-height "32px"
             :color "white"
             :background-color "#666"}]

    [:.xform-control
     {:display "inline-block"
      :padding "0 .4em 0 .4em"
      :vertical-align "middle"
      :border-radius "3px"
      :min-width "50px"
      :max-width "635px"
      :min-height "32px"
      :background-color "#ffffff"
      :border "2px solid #c9c9c9"}]]
   [:.jira
    [:.xbtn {:margin-right "4px"
             :height "32px"
             :min-width "24px"
             :font-family "Roboto"
             :display "inline-block"
             :border-radius "3px"
             :line-height "32px"
             :padding "0 10px"
             :color "white"
             :background-color "#666"
             }]
    [:.xform-control
     {:height "32px"
      :font-family "Roboto"
      :color "#172b4d"
      :font-size "14px"
      :font-weight "400"
      :font-style "normal"
      :line-height "20px"
      :background-color "#f4f5f7"
      :border "1px solid #dfe1e6"
      :padding "4px 7px 5px"}]]

   [:.github
    {:font-family "-apple-system, system-ui, \"Segoe UI\", Helvetica, Arial, sans-serif, \"Apple Color Emoji\", \"Segoe UI Emoji\", \"Segoe UI Symbol\""}
    [:.xbtn
     {:position "relative"
      :display "inline-block"
      :padding "6px 12px"
      :font-size "14px"
      :font-weight "600"
      :line-height "20px"
      :background-color "#eee"
      :white-space "nowrap"
      :vertical-align "middle"
      :font-family "Helvetica"
      :cursor "pointer"
      :border "1px solid rgba(27,31,35,0.2)"
      :border-radius "0.25em"}]
    [:.xbtn.xsm
     {:padding "3px 10px"
      :font-size "12px"
      :line-height "20px"}]
    [:.xform-control
     {:min-height "34px"
      :font-family "-apple-system, system-ui, \"Segoe UI\", Helvetica, Arial, sans-serif, \"Apple Color Emoji\", \"Segoe UI Emoji\", \"Segoe UI Symbol\""
      :padding "6px 8px"
      :font-size "16px"
      :line-height "20px"
      :color "#24292e"
      :vertical-align "middle"
      :background-color "#fafbfc"
      :border "1px solid #d1d5da"
      :border-radius "3px"
      :outline "none"
      :box-shadow "inset 0 1px 2px rgba(27,31,35,0.075)"}]]]
  )
(defn index []
  (let []
    (fn []
      [:div
       [:h3 "Cominator"] 
       [:br]
       [:style (garden/css styles)]
       [:h3 "Github"]
       [:div.github
        [:input.xform-control {:placeholder "Input"}]
        " "
        [:div.xbtn "Github button"]
        " "
        [:div.xbtn.xsm "Github button"]
        " "]

       [:br]
       [:h3 "Jira"]
       [:div.jira
        [:input.xform-control {:placeholder "Input"}]
        " "
        [:div.xbtn "Jira button"]
        " "
        [:div.xbtn.xsm "Jira button"]
        " "]

       [:br]
       [:h3 "Mailchimp"]
       [:div.mailchimp
        [:input.xform-control {:placeholder "Input"}]
        " "
        [:div.xbtn "Standard"]
        " "
        [:div.xbtn.xsm "Standard"]
        " "]

       [:br]
       [:h3 "Material"]
       [:div.material
        [:input.xform-control {:placeholder "Input"}]
        " "
        [:div.xbtn "Standard"]
        " "
        [:div.xbtn.xsm "Standard"]
        " "]

       ])))

(ui-routes/reg-page
 :combinator {:title "Combinator"
              :w 1
              :cmp index})
