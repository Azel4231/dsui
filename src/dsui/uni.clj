(ns dsui.uni
  (:use [dsui.core])
  (:require [clojure.spec :as s]))

(def uni {:name "Foo-University of Bar"
          :students [{:name "John Doe" :student-id "12345"}
                     {:name "Jane Doe" :student-id "11111"}
                     {:name "Dr. Who" :student-id "?"}]
          :courses [{:name "Linear Algebra" :max-students 15 :room "Gauss" :registered ["11111" "?"]}
                    {:name "Introduction to Algorithms" :max-students 25 :room "Dijkstra" :registered ["12345" "?"]}]})

;;(prn (s/conform :dsui.core/dsui-spec uni))
;;(dsui uni)


