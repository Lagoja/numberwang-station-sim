(ns numberwang-station-sim.core
  (:require [reagent.core :as reagent :refer [atom]]))

(enable-console-print!)

(println "This text is printed from src/numberwang-station-sim/core.cljs. Go ahead and edit it and see reloading in action.")

;; define your app data so that it doesn't get over-written on reload

(defn ^:export speak [msg]
  (let [msg (js/SpeechSynthesisUtterance. msg)]
    (.speechSynthesis.speak js/window msg)))

(defonce state (atom "Enter a number"))

(defn nw-digit-button [number n-atom]
  [:div.col-xs-4 {:style {:padding "2px"}}
   [:button {:class    "btn btn-primary btn-block"
             :on-click #(when (<= @n-atom 100)
                          (swap! n-atom (fn [x] (+ number (* 10 x)))))}
    (str number)]])

(defn nw-clear-button [label n-atom]
  [:div.col-xs-4 {:style {:padding "2px"}}
   [:button {:class    "btn btn-primary btn-block"
             :on-click #(reset! n-atom 0)} label]])

(defn nw-enter-button [label n-atom state]
  [:div.col-xs-4 {:style {:padding "2px"}}
   [:button {:class    "btn btn-primary btn-block"
             :on-click #(do (speak (str @n-atom))
                            (reset! state @n-atom)
                            (reset! n-atom 0))} label]])

(defn nw-display [n-atom]
  [:input {:type  "text"
           :style {:width   "100%"
                   :display "block"}
           :value (str @n-atom)}])

(defn nw-numberwang-button [n-atom state]
  [:div {:style {:padding "2px"}}
   [:button {:class    "btn btn-primary btn-block"
             :on-click #(let [audio (js/Audio. "https://cdn.glitch.com/ad4e5b46-3cf9-4371-a163-73d3a6d8baa8%2Fnumberwang_0.mp4")]
                          (reset! n-atom 0)
                          (reset! state "THATS NUMBERWANG!")
                          (.play audio))} "Numberwang!"]])

(defn nw-title []
  [:div
   [:img {:src   "images/tower.svg"
          :style {:height       "40px"
                  :display      "inline"
                  :float        "left"
                  :margin-right "10px"}}]
   [:h1 {:display "inline"
         :style   {:position "relative"}} "RADIO KNWG"]])

(defn nw-panel-component []
  (let [n (atom 0)]
    [:div.row
     [:img]
     [:div.col-xs-8.col-xs-offset-2
      {:style {:background-color "white"}}
      [:img {:src   "images/On_air.gif"
             :width "100%"}]
      [:div.col-xs-12
       [:div.row
        [:h2 {:style {:text-align "center"}} (str @state)]]
       [:div.row
        [nw-display n]]
       [:div.row
        [nw-digit-button 1 n]
        [nw-digit-button 2 n]
        [nw-digit-button 3 n]]
       [:div.row
        [nw-digit-button 4 n]
        [nw-digit-button 5 n]
        [nw-digit-button 6 n]]
       [:div.row
        [nw-digit-button 7 n]
        [nw-digit-button 8 n]
        [nw-digit-button 9 n]]
       [:div.row
        [nw-clear-button "C" n]
        [nw-digit-button 0 n]
        [nw-enter-button "Enter" n state]]
       [:div.row
        [nw-numberwang-button n state]]]]]))

(defn hello-world []
  [:div
   [:div.row.header {:style {:margin-bottom "30px"
                             :background-color "white"}}
    [:div.col-s-4 {:style {:margin-left "20px"
                           :margin-top  "20px"}} [nw-title]]]
   [:div
    [:div.container-fluid
     [nw-panel-component]]]])

(reagent/render-component [hello-world]
                          (. js/document (getElementById "app")))

