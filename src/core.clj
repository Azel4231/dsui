(ns dsui.core
  (:require [clojure.spec :as s])
  (:import [javax.swing JComponent JFrame JLabel JPanel JTextField JRadioButton JTabbedPane JCheckBox JComboBox JScrollPane SpringLayout]
           [java.awt.event KeyAdapter WindowAdapter]
           [java.awt GridLayout]))

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


; Todo Map with settings.
(defn ^JFrame frame [close-f]
  (doto (new JFrame)
    (.pack)
    (.setSize (new java.awt.Dimension 400 700))
    (.addWindowListener (proxy [WindowAdapter] [] (windowClosing [e] close-f)))
    (.setVisible true)))

(defn add [^JComponent component & children]
  (doseq [c (flatten children)] (.add component c)))

(defn panel []
  (doto (new JPanel)
    (.setLayout (new GridLayout 0 6 2 2))))

(defn field [value]
  (doto (new JTextField (str value))
    (.setEditable false)))

(defn label [l]
  (new JLabel (str l)))

(defn setup-frame [title]
  (doto (frame #(print "Exiting..."))
    (.setTitle title)))

(defmulti create (fn [kw ds] kw))

(defmethod create ::named-tabsheet [this ds]
  (let [tpane (new JTabbedPane)]
    (doseq [[k v] ds]
      (.addTab tpane k (apply create v)))
    tpane))

(defmethod create ::form [this ds]
  (let [p (panel)]
    (->> (for [[kw childDs] ds] [(label kw) (apply create childDs)])
         (add p))
    p))

(defmethod create ::field [this ds]
  (field ds))

(defmethod create ::nested-ui [this [kw v]]
  (create kw v))

(defn dsui
  "Creates a (write-only) Swing UI for a nested data structure."
  [ds]
  (let [^JFrame fr (setup-frame "DS UI")
        [kw v] (s/conform ::dsui-spec ds)]
    (.setContentPane fr (create kw v))))



(def a {:t [{:a 1 :b 2} {:a "x" :b "y"}] :b {:some "other stuff"}})
(def b [a {:c 23 :d 42}])
(def c {:a [{:list "of stuff"}] :b 3})
(def ts {:a 1 :b "2" :c 42 :d 23 :? "argh"})
(def mage {:id (. java.util.UUID randomUUID)
   :name name
   :player ""
   :chronicle ""
   :concept ""
   :virtue ""
   :vice ""
   :size-natural 5
   :properties {:path :obrimos
                :order :mysterium
                :cabal ""
                :gnosis 1
                :willpower 1
                :wisdom 7
                :merits #{}
                :flaws #{}
                :attributes {:strength 1 :dexterity 5 :stamina 1,
                             :intelligence 1 :wits 1 :resolve 1,
                             :presence 1 :manipulation 1 :composure 1}
                :skills {:academics 0 :computer 0 :crafts 0 :investigation 0 :medicine 0 :occult 0 :politics 0 :science 0,
                         :athletics 0 :brawl 0 :drive 0 :firearms 0 :larceny 0 :stealth 0 :survival 0 :weaponry 0,
                         :animalKen 0 :empathy 0 :expression 0 :intimidation 0 :persuasion 0 :socialize 0 :streetwise 0 :subterfuge 0}
                :arcana {:death 0
                         :fate 0
                         :forces 0
                         :life 0
                         :matter 0
                         :mind 0
                         :prime 0
                         :space 0
                         :spirit 0
                         :time 0}
                :rotes #{}
                :weapons #{}
                :experience-total 0
                :experience-current 0}
   :ledger {:bashing 0
            :lethal 0
            :aggravated 0
            :mana 5
            :size-base 5
            :size-current 5
            :armor {:bashing 0
                    :lethal 0
                    :aggravated 0}
            :spells-active 0
            :spells-cast-upon-self 0
            :spells-protective 0
            :nimbus :none
            :paradox-marks :none}
   :historicProperties []})


(dsui ts)

; primitives currently not supported
#_(dsui "2")

(s/conform ::dsui-spec ts)

