(ns reagentnative.core
  (:require [reagent.core :as r :refer [atom]]
            [reagent.impl.component :as ru]))

(set! js/React (js/require "react-native"))

(def text (r/adapt-react-class (.-Text js/React)))
(def view (r/adapt-react-class (.-View js/React)))
(def image (r/adapt-react-class (.-Image js/React)))
(def touchable-highlight (r/adapt-react-class (.-TouchableHighlight js/React)))
(def navigator (r/adapt-react-class (.-NavigatorIOS js/React)))
(def scroll (r/adapt-react-class (.-ScrollView js/React)))
(def input (r/adapt-react-class (.-TextInput js/React)))
(def switch (r/adapt-react-class (.-SwitchIOS js/React)))
(def list-view (r/adapt-react-class (.-ListView js/React)))
(def slider (r/adapt-react-class (.-SliderIOS js/React)))
(def tabs (r/adapt-react-class (.-TabBarIOS js/React)))
(def tabs-item (r/adapt-react-class (.-TabBarIOS.Item js/React)))

(defn alert[title content]
  (.alert (.-AlertIOS js/React) title content))

(defn create-style[s]
  (let [s1 (reduce #(assoc %1 (%2 0) (ru/camelify-map-keys (%2 1))) {} s)]
    (js->clj (.create (.-StyleSheet js/React) (clj->js s1)))))

(enable-console-print!)

(def styles (create-style 
  {:fullscreen {:position "absolute" 
          :top 0 
          :left 0 
          :bottom 0 
          :right 0}
   :green {:color "#00ff00"}
   :viewbg {:padding 10 
            :background-color "#ffffff"}
   :input {:height 35
           :border-color "gray"
           :border-width 1
           :padding-left 10
           :border-radius 5
           :margin 10}}))

(def global-state (r/atom 0))

(declare page-comp)

(def ds (React.ListView.DataSource. #js{:rowHasChanged (fn[a b] (= a b))}))
(def list-data (r/atom (clj->js (range 100))))
(.log js/console @list-data)

(def current-tab (r/atom "tab1"))

(defn page[{nav :navigator}]
    [tabs
     [tabs-item {:title "tab1" :on-press #(reset! current-tab "tab1") :selected (= "tab1" @current-tab)}
      [scroll {:always-bounce-vertical true 
               :bounces true 
               :style (styles "fullscreen")}
       [view {:style [(styles "viewbg") {:flexDirection "column"}]}
        [text @global-state]
        [text {:style (styles "green")} "native"]
        [input {:style (styles "input") 
                :value @global-state 
                :on-change-text #(reset! global-state %)}]
        [slider {:style {:width 200} 
                 :on-value-change #(reset! global-state %)}]
        [image {:source {:uri "https://assets-cdn.github.com/images/modules/microsoft_callout/corner.png"} 
                :style {:width 306 :height 104}}]

        [list-view {:dataSource (.cloneWithRows ds @list-data) 
                    :render-row (fn[row] 
                                  (r/as-element 
                                    [touchable-highlight {:style {:border-top-width 1 :border-bottom-color "#000"} :on-press #(alert "list" (str row))}
                                     [text row]]))
                    :style {:left 0 :right 0 :height 250 :border-width 1 :border-color "#000"}}]

        [switch {:on-value-change #(.push nav #js{:title "new" :component page-comp})}]]]]
     [tabs-item {:title "tab2" :selected (= "tab2" @current-tab) :on-press #(reset! current-tab "tab2")}
      [text {:style {:top 100}} "tab2"]]
     [tabs-item {:title "tab3" :selected (= "tab3" @current-tab) :on-press #(reset! current-tab "tab3")}
      [text {:style {:top 100}} "tab3"]]])

(def page-comp (r/reactify-component page))

(defn root[]
  [navigator {:initial-route {:title "App4" :component page-comp} :style (styles "fullscreen")}])

(.registerRunnable (.-AppRegistry js/React) "AwesomeProject" 
                   (fn [params]
                     (r/render [view {:style {:top 0 :left 0 :right 0 :bottom 0 :position "absolute"}}
                                 [root]] (.-rootTag params))))
                     
