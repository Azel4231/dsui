(ns dsui.uni
  (:use [dsui.core])
  (:require [clojure.spec.alpha :as s]))


(def uni {:name "Foo-University of Bar"
          :students [{:name "John Doe" :student-id "12345"}
                     {:name "Jane Doe" :student-id "11111"}
                     {:name "Dr. Who" :student-id "?"}]
          :courses [{:name "Linear Algebra" :max-students 15 :room "Gauss" :registered ["11111" "?"]}
                    {:name "Introduction to Algorithms" :max-students 25 :room "Dijkstra" :registered ["12345" "?"]}]})

#_(prn (s/conform :dsui.core/structure-spec uni))
(dsui uni)
(s/conform :dsui.core/ds (s/conform :dsui.core/ds uni))
(dsui (s/conform :dsui.core/ds uni))

(s/conform :dsui.core/labeled-ds (clojure.lang.MapEntry. :key ["a" "b" "c"]))
(s/conform :dsui.core/ds (clojure.lang.MapEntry. :dsui.core/property "c"))






