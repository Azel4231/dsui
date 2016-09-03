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

(def dsui-spec (s/map-of keyword? sequential?))

(defn dsui
  "Creates a Swing ui for a nested data structure. Expects a Dereferable containing the data structure."
  [d]
  (let [conformed (s/conform dsui-spec @d)
        fr (setup-frame (str conformed))]
    
    ))


(def a (atom {:a [:b]}))
(dsui a)


