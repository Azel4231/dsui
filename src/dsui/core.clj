(ns dsui.core
  (:require [clojure.spec :as s])
  (:import [java.util Vector Collection]
           [javax.swing JFrame JLabel JPanel JTextField JTabbedPane JTable JList DefaultListModel JScrollPane]
           [javax.swing.table DefaultTableModel]
           [java.awt.event KeyAdapter WindowAdapter]
           [java.awt Container GridBagLayout GridBagConstraints]))

(set! *warn-on-reflection* true)

(def colnumber 3)

(defn scalar? [v]
  ((complement coll?) v))
(defn homogeneous? [coll]
  (and (> (count coll) 2)
       (every? #(= (keys %) (keys (first coll))) coll)))
(defn scalar-map? [m]
  (every? scalar? (vals m)))
(s/def ::dsui-spec (s/or ::table ::tbl-spec
                         ::list ::list-spec
                         ::named-tabsheet ::named-ts-spec
                         ::indexed-tabsheet ::indexed-ts-spec
                         ::form ::form-spec))
(s/def ::tbl-spec (s/and (s/coll-of map?) (s/every scalar-map?) homogeneous?))
(s/def ::list-spec (s/and sequential? (s/coll-of scalar? :min-count 3)))
(s/def ::named-ts-spec (s/map-of any? ::dsui-spec))
(s/def ::indexed-ts-spec (s/coll-of ::dsui-spec))
(s/def ::form-spec (s/map-of any? (s/or ::field scalar?
                                        ::nested-ui ::dsui-spec)))

(defn ^JFrame frame [title close-f]
  (doto (new JFrame)
    (.setSize (new java.awt.Dimension 800 800))
    (.addWindowListener (proxy [WindowAdapter] [] (windowClosing [e] close-f)))
    (.setTitle title)
    (.setVisible true)))
(defn add
  ([^Container p cs]
   (add p cs (repeat nil)))
  ([^Container p cs lds]
   (doall (map (fn [^Container c ld] (.add p c ld)) (flatten cs) (flatten lds)))
   p))

(defn panel []
  (doto (new JPanel)
    (.setLayout (new GridBagLayout))))
(defn field [value]
  (doto (new JTextField (str value))
    (.setEditable false))) 
(defn label [l]
  (new JLabel (str l)))
(defn table [tm]
  (new JScrollPane (doto (new JTable tm)
                       (.setEnabled false))))
(defn ^Vector keys-to-v [ms]
  (let [^Collection ks (->> ms first keys vec)]
    (new Vector ks)))
(defn ^Vector vals-to-vofv [ms]
  (let [rows (->> ms (map vals) (map vec))
        ^Collection vs (->> rows (map (fn [^Collection v] (new Vector v))) vec)]
    (new Vector vs)))
(defn table-model [ms]
  (let [colnames (keys-to-v ms)
        content (vals-to-vofv ms)]
    (doto (new DefaultTableModel content colnames)
      (.setColumnIdentifiers colnames))))
(defn jlist [^DefaultListModel lm]
  (new JList lm))
(defn ^DefaultListModel list-model [coll]
  (let [lm (new DefaultListModel)]
    (doseq [el coll] (.addElement lm el))
    lm))
(defn gb-constraints [{:keys [x y fl gw wx wy a]}]
  (let [gbc (new GridBagConstraints)]
    (set! (. gbc gridx) x)
    (set! (. gbc gridy) y)
    (set! (. gbc fill) fl)
    (set! (. gbc gridwidth) gw)
    (set! (. gbc weightx) wx)
    (set! (. gbc weighty) wy)
    (set! (. gbc anchor) a)
    gbc))
(defn panel-gbc [y]
  (gb-constraints {:x 0 :y y :fl GridBagConstraints/BOTH :gw (* 2 colnumber) :wx 1.0 :wy 1.0 :a GridBagConstraints/PAGE_START}))
(defn field-gbc [x y]
  (gb-constraints {:x x :y y :fl GridBagConstraints/HORIZONTAL :gw 1 :wx 1.0 :wy 0.0 :a GridBagConstraints/PAGE_START}))

(defmulti create first)
(defn- create-ts [m]
  (let [tpane (new JTabbedPane)]
    (doseq [[k v] m]
      (.addTab tpane (str k) (create v)))
    tpane))
(defmethod create ::named-tabsheet [[_ ds]]
  (create-ts ds))
(defmethod create ::indexed-tabsheet [[_ ds]]
  (let [tpane (new JTabbedPane)]
    (->> ds
         (map-indexed (fn [i v] (.addTab tpane (str i) (create v))))
         doall)
    tpane))
(defmethod create ::form [[_ ds]]
  (let [p (panel)
        layout-cols (* 2 colnumber)
        fields (filter #(= ::field (first (second %))) ds)
        nested-uis (filter #(= ::nested-ui (first (second %))) ds)]
    (add p (for [[kw childDs] fields] [(label kw) (create childDs)])
         (for [i (range (* 2 (count fields)))] (field-gbc (mod i layout-cols) (quot i layout-cols))))
    (add p [(create-ts (into {} nested-uis))]
         [(panel-gbc (inc (quot (count fields) colnumber)))])
    p))

(defmethod create ::table [[_ ds]]
  (table (table-model ds)))
(defmethod create ::list [[_ ds]]
  (jlist (list-model ds)))
(defmethod create ::field [[_ ds]]
  (field ds))
(defmethod create ::nested-ui [[_ ds]]
  (create ds))

(defn dsui-panel
  [ds]
  (create (s/conform ::dsui-spec ds)))

(s/fdef dsui
        :args ::dsui-spec
        :ret any?)
(defn dsui
  "Creates a (write-only) Swing UI for an arbitrary nested data structure."
  [ds]
  (.setContentPane (frame "DS-UI" #(print "Exiting...")) (dsui-panel ds)))



