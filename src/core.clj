(ns dsui.core
  (:require [clojure.spec :as s])
  (:import [javax.swing JComponent JFrame JLabel JPanel JTextField JRadioButton JCheckBox JComboBox JScrollPane BoxLayout]
           [java.awt.event KeyAdapter WindowAdapter]))

(set! *warn-on-reflection* true)

; Map with settings. Define Spec with default values.
(defn ^JFrame frame [close-f]
  (doto (new JFrame)
    (.pack)
    (.setSize (new java.awt.Dimension 400 700))
    (.addWindowListener (proxy [WindowAdapter] [] (windowClosing [e] close-f)))
    (.setVisible true)))

(defn setup-frame [title]
  (doto (frame #(print "Exiting..."))
    (.setTitle title)))


(defn primitive? [v]
  ((complement coll?) v))
(defn homogenous? [coll]
  (or (empty? coll)
      (every? #(= (keys %) (keys (first coll))) coll)))
(defn primitive-map? [m]
  (every? primitive? (vals m)))

(def dsui-spec (s/map-of keyword? sequential?))
(s/def ::dsui (s/or :t ::table :ts ::tabsheet :fo ::form))
(s/def ::table (s/and (s/coll-of map?) (s/every primitive-map?) homogenous?))
(s/def ::tabsheet (s/map-of any? ::dsui))
(s/def ::form (s/map-of any? primitive?))

(defn dsui
  "Creates a Swing ui for a nested data structure. Expects a Dereferable containing the data structure."
  [ds]
  (let [conformed (s/conform dsui-spec ds)
        fr (setup-frame (str conformed))]
    
    ))


(def a {:t [{:a 1 :b 2} {:a "x" :b "y"}] :b {:some "other stuff"}})
;(dsui b)

(s/conform ::dsui a)

