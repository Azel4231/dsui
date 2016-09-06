(ns dsui.core
  (:require [clojure.spec :as s])
  (:import [javax.swing JComponent JFrame JLabel JPanel JTextField JRadioButton JTabbedPane JCheckBox JComboBox JScrollPane BoxLayout]
           [java.awt.event KeyAdapter WindowAdapter]))

(set! *warn-on-reflection* true)

(defn primitive? [v]
  ((complement coll?) v))
(defn homogenous? [coll]
  (and (> (count coll) 2)
       (every? #(= (keys %) (keys (first coll))) coll)))
(defn primitive-map? [m]
  (every? primitive? (vals m)))

(s/def ::dsui-spec (s/or ::table ::tbl-spec ::named-tabsheet ::named-ts-spec ::indexed-tabsheet ::indexed-ts-spec ::form ::form-spec))
(s/def ::tbl-spec (s/and (s/coll-of map?) (s/every primitive-map?) homogenous?))
(s/def ::named-ts-spec (s/map-of any? ::dsui-spec))
(s/def ::indexed-ts-spec (s/coll-of ::dsui-spec))
(s/def ::form-spec (s/map-of any? (s/or ::field primitive? ::nested-ui ::dsui-spec)))


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

(defmulti create (fn [kw ds] kw))

(defmethod create ::named-tabsheet [this ds] (let [tpane (new JTabbedPane)]
    (->> ds
         (map #(.addTab tpane (key %) (new JLabel (str (val %)))) )
         doall)))

(defmethod create ::form [this ds]
  (new JLabel (str ds)))

(defmethod create ::field [this ds]
  (new JLabel (str ds)))

(defmethod create ::nested-ui [this [kw v]]
  (create kw v))

(defn dsui
  "Creates a Swing UI for a nested data structure."
  [ds]
  (let [^JFrame fr (setup-frame "DS UI")
        [kw v] (s/conform ::dsui-spec ds)]
    (.setContentPane fr (create kw v))))



(def a {:t [{:a 1 :b 2} {:a "x" :b "y"}] :b {:some "other stuff"}})
(def b [a {:c 23 :d 42}])
(def c {:a [{:list "of stuff"}] :b 3})
                                        ;(dsui b)
(def ts {:a 1 :b "2"})

(dsui ts)
(s/conform ::dsui-spec c)

