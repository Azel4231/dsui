(ns dsui.core
  (:require [clojure.spec :as s])
  (:import [java.util Vector Collection]
           [javax.swing JComponent JFrame JLabel JPanel JTextField JTabbedPane JTable]
           [javax.swing.table DefaultTableModel]
           [java.awt.event KeyAdapter WindowAdapter]
           [java.awt GridLayout]))

(set! *warn-on-reflection* true)

(defn primitive? [v]
  ((complement coll?) v))
(defn homogeneous? [coll]
  (and (> (count coll) 2)
       (every? #(= (keys %) (keys (first coll))) coll)))
(defn primitive-map? [m]
  (every? primitive? (vals m)))
(s/def ::dsui-spec (s/or ::table ::tbl-spec ::named-tabsheet ::named-ts-spec ::indexed-tabsheet ::indexed-ts-spec ::form ::form-spec))
(s/def ::tbl-spec (s/and (s/coll-of map?) (s/every primitive-map?) homogeneous?))
(s/def ::named-ts-spec (s/map-of any? ::dsui-spec))
(s/def ::indexed-ts-spec (s/coll-of ::dsui-spec))
(s/def ::form-spec (s/map-of any? (s/or ::field primitive? ::nested-ui ::dsui-spec)))

(defn ^JFrame frame [title close-f]
  (doto (new JFrame)
    (.setSize (new java.awt.Dimension 400 700))
    (.addWindowListener (proxy [WindowAdapter] [] (windowClosing [e] close-f)))
    (.setTitle title)
    (.setVisible true)))
(defn add [^JComponent component & children]
  (doseq [^JComponent c (flatten children)] (.add component c)))
(defn panel []
  (doto (new JPanel)
    ; Gridlayout for a start
    (.setLayout (new GridLayout 0 6 2 2))))
(defn field [value]
  (doto (new JTextField (str value))
    (.setEditable false)))
(defn label [l]
  (new JLabel (str l)))
(defn table [tm]
  (new JTable tm))
(defn keys-to-v [ms]
  (let [^Collection ks (->> ms first keys vec)]
    (new Vector ks)))
(defn vals-to-vofv [ms]
  (let [rows (->> ms (map vals) (map vec))
        ^Collection vs (->> rows (map (fn [^Collection v] (new Vector v))) vec)]
    (new Vector vs)))
(defn table-model [ms]
  (let [^Vector colnames (keys-to-v ms)
        ^Vector content (vals-to-vofv ms)]
    (new DefaultTableModel content colnames)))

(defmulti create first)
(defmethod create ::named-tabsheet [[_ ds]]
  (let [tpane (new JTabbedPane)]
    (doseq [[k v] ds]
      (.addTab tpane (str k) (create v)))
    tpane))
(defmethod create ::indexed-tabsheet [[_ ds]]
  (let [tpane (new JTabbedPane)]
    (->> ds
         (map-indexed (fn [i v] (.addTab tpane (str i) (create v))))
         doall)
    tpane))
(defmethod create ::form [[_ ds]]
  (let [p (panel)]
    (add p (for [[kw childDs] ds] [(label kw) (create childDs)]))
    p))
(defmethod create ::table [[_ ds]]
  (table (table-model ds)))
(defmethod create ::field [[_ ds]]
  (field ds))
(defmethod create ::nested-ui [[_ ds]]
  (create ds))

(defn dsui-panel
  [ds]
  (create (s/conform ::dsui-spec ds)))
(defn dsui
  "Creates a (write-only) Swing UI for an arbitrary nested data structure."
  [ds]
  (.setContentPane (frame "DS-UI" #(print "Exiting...")) (dsui-panel ds)))



