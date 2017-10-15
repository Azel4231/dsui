(ns dsui.examples
  (:require [clojure.spec.alpha :as s]
            [dsui.swing :as swing]))


(def uni {:name "Foo-University of Bar"
          :students [{:name "John Doe" :student-id "12345"}
                     {:name "Jane Doe" :student-id "11111"}
                     {:name "Dr. Who" :student-id "?"}]
          :courses [{:name "Linear Algebra" :max-students 15 :room "Gauss" :registered ["11111" "?"]}
                    {:name "Introduction to Algorithms" :max-students 25 :room "Dijkstra" :registered ["12345" "?"]}]})

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

(def example {:scalar-value 42
              :list-of-scalars [1 4 9 16]
              :list-of-datastructures ["this" "is" "a" "heterogeneous" "list" #{:with :nested :stuff}]
              :table tbl
              :matrix [[1 2 3] ["a" "b" "c"] [1.2 1/4 4.5]]
              :conformed-data (s/conform :dsui.core/ds uni)
              :small-example uni
              :larger-example mage
              })

(swing/dsui example)

;; primitives currently not supported
#_(swing/dsui 42)




(defn apple? [{d ::diameter c ::color}]
  (and (<= 5 d 10)
       (#{:green :red} c)))

(defn orange? [{d ::diameter c ::color}]
  (and (<= 6 d 12)
       (= c :orange)))

(s/def ::fruit (s/and (s/keys :req [::diameter ::color])
                      (s/or :apple apple?
                            :orange orange?)))

;; spec finds orange
#_(swing/conform-ui ::fruit {::diameter 8
                           ::color :orange})

;; spec finds apple
#_(swing/conform-ui ::fruit {::diameter 8
                           ::color :red})

;; data does not conform -> explain UI
#_(swing/conform-ui ::fruit {::diameter 1
                           ::color :blue})




;; displays the last exception as a ui
#_(-> *e Throwable->map swing/dsui)






