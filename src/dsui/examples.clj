(ns dsui.examples
  (:use [dsui.core])
  (:require [clojure.spec.alpha :as s]
            [clojure.spec.test.alpha :as test]))


(def tbl [{:surname "Jane" :lastname "Doe" :occupation "unknown"}
          {:surname "Jimmy" :lastname "McNulty" :occupation "Criminal Investigator"}
          {:surname "Bunk" :lastname "Moreland" :occupation "Criminal Investigator"}
          {:surname "Omar" :lastname "Little" :occupation "?"}
          ])
(def mage
  {:id (. java.util.UUID randomUUID)
   :name "John Doe"
   :player ""
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
                :rotes []
                :weapons []
                :experience-total 0
                :experience-current 0}
   :ledger {:chronicle ""
            :bashing 0
            :lethal 0
            :aggravated 0
            :mana 5
            :size-current 5
            :armor {:bashing 0
                    :lethal 0
                    :aggravated 0}
            :spells-active 0
            :spells-cast-upon-self 0
            :spells-protective 0
            :nimbus :none
            :paradox-marks :none}
   :historicProperties [1 2 3 4]})

(def a {:indexed-tabbedpane [{:a 1 :b 2} {:a "x" :b "y"}] :form {:some "other stuff"} :table tbl :heterogeneous-form mage})
(def l [4 8 15 16 23 42])
(def b [a {:c 23 :d 42} :list l])
(def c {:a [{:list "of stuff"}] :b 3})
(def ts {:a 1 :b "2" :c 42 :d 23 :? "argh"})

#_(dsui a)

#_(test/instrument `dsui)
#_(s/explain ::dsui.core/dsui-spec a)
#_(s/conform ::dsui.core/dsui-spec a)

;; primitives currently not supported
#_(dsui "2")






