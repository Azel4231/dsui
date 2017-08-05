(ns dsui.fx
  (:require [fn-fx.fx-dom :as dom]
            [fn-fx.controls :as ui]
            [fn-fx.diff :refer [component defui render should-update?]]
           ))




(defn fx-component [{type :type :as m}]
  (let [props (dissoc m :type)]
    (fn-fx.diff/component type props)))

(def to-fx-type
  {:grid-pane :javafx.scene.layout.GridPane
   :flow-pane :javafx.scene.layout.FlowPane
   :label :javafx.scene.control.Label
   :text-field :javafx.scene.control.TextField
   
   })

(def fx-values {:insets :javafx.geometry.Insets})

(defn fx-type [kw]
  (let [fx-t (to-fx-type kw)]
    (if fx-t fx-t
        kw)))

(def layout-cols 6)

(defn grid-positions [cols]
  (map (fn [i] [(mod i cols) (quot i cols)]) (range)))

#_(take 10 (grid-positions layout-cols))

(defn update-xy [[x y] child]
  (-> child
      (assoc :grid-pane/column-index x)
      (assoc :grid-pane/row-index y)))

(defn update-grid-pos [m]
  (update m :children (partial map update-xy (grid-positions layout-cols))))


(defn handle-grid-pane [m]
  (if (= (:type m) :grid-pane)
    (-> m
        (update-grid-pos)
        
         #_(assoc :padding {:type :insets
                          :bottom 15
                          :left 15
                          :right 15
                          :top 15}))
    m))







(defn map-vals [f m]
  (into {} (for [[k v] m] [k (f v)])))

(defn transform-maps [f ds]
  (cond (map? ds) (f (map-vals (partial transform-maps f) ds))
        (sequential? ds) (map (partial transform-maps f) ds)
        :else ds))


(defn window [dom]
  (let [u (ui/stage
           :title "dsui"
           :shown true
           :min-width 800
           :min-height 600
           :scene (ui/scene
                   :root dom))
        handler-fn (fn [evt]
                     (println "Received Event: " evt))]
    #_(println u)
    (dom/app u handler-fn)))

(defn to-fx-ds [ds]
  (transform-maps (comp #(update % :type fx-type)
                        #(handle-grid-pane %)) ds))

(defn create-ui [ds]
  (->> ds
       to-fx-ds
       (transform-maps #(fx-component %))
       window))

