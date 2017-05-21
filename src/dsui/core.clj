(ns dsui.core
  (:require [clojure.spec :as s])
  (:import [java.util Vector Collection]
           [javax.swing SwingUtilities JFrame JLabel JPanel JTextField JTabbedPane JTable JList DefaultListModel JScrollPane]
           [javax.swing.table DefaultTableModel]
           [java.awt.event KeyAdapter WindowAdapter]
           [java.awt Container GridBagLayout GridBagConstraints]))

(set! *warn-on-reflection* true)

(def colnumber 3)

(defn scalar? [v]
  ((complement coll?) v))

(defn homogeneous? [ms]
  (and (>= (count ms) 2)
       (every? #(= (keys %) (keys (first ms))) ms)))

(defn scalar-map? [m]
  (every? scalar? (vals m)))

(s/def ::structure-spec (s/or ::table-of-entities ::table-of-entities
                              ::matrix ::matrix
                              ::list-of-scalars ::list-of-scalars
                              ::list-of-structures ::list-of-structures
                              ::entity ::entity))

(s/def ::table-of-entities (s/and (s/coll-of map?) (s/every scalar-map?) homogeneous?))
(s/def ::matrix (s/coll-of ::list-of-scalars))
(s/def ::list-of-scalars (s/and (complement map-entry?) (s/coll-of scalar?)))
(s/def ::list-of-structures (s/coll-of ::structure-spec))
(s/def ::entity (s/map-of any? (s/or ::property scalar?
                                     ::nested-structure ::structure-spec)))


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

(defn table-model [^Collection colnames rows]
  (let [^Vector cns (new Vector colnames)
        ^Collection rs (mapv (fn [^Collection r] (new Vector r)) rows)
        ^Vector content (new Vector rs)]
    (doto (new DefaultTableModel content cns)
      (.setColumnIdentifiers cns))))

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

(defn- create-ts [m]
  (let [tpane (new JTabbedPane)]
    (doseq [[k v] m]
      (.addTab tpane (str k) (create v)))
    tpane))



(defmulti create first)

(defmethod create ::list-of-structures [[_ ds]]
  (let [tpane (new JTabbedPane)]
    (->> ds
         (map-indexed (fn [i v] (.addTab tpane (str i) (create v))))
         doall)
    tpane))

(defmethod create ::entity [[_ ds]]
  (let [p (panel)
        layout-cols (* 2 colnumber)
        fields (filter #(= ::property (first (second %))) ds)
        nested-uis (filter #(= ::nested-structure (first (second %))) ds)]
    (add p
         (for [[kw childDs] fields]
           [(label kw) (create childDs)])
         (for [i (range (* 2 (count fields)))]
           (field-gbc (mod i layout-cols) (quot i layout-cols))))
    (add p [(create-ts (into {} nested-uis))]
         [(panel-gbc (inc (quot (count fields) colnumber)))])
    p))

(defmethod create ::table-of-entities [[_ ds]]
  (table (table-model (keys (first ds)) (map vals ds))))

(defmethod create ::matrix [[_ ds]]
  (table (table-model (range (count (first ds))) ds)))

(defmethod create ::list-of-scalars [[_ ds]]
  (jlist (list-model ds)))

(defmethod create ::property [[_ ds]]
  (field ds))

(defmethod create ::nested-structure [[_ ds]]
  (create ds))

(defn dsui-panel
  [ds]
  (create (s/conform ::structure-spec ds)))

(s/fdef dsui
        :args ::structure-spec
        :ret any?)
(defn dsui
  "Creates a (write-only) Swing UI for an arbitrary nested data structure."
  [ds]
  (doto (frame "DS-UI" #(print "Exiting..."))
    (.setContentPane (dsui-panel ds))
    (#(. SwingUtilities updateComponentTreeUI %)))) 



