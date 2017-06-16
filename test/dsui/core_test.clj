(ns dsui.core-test
  (:use dsui.core)
  [:use clojure.test]
  (:require [clojure.spec.alpha :as s]
            [dsui.core :as core]
            [dsui.swing :as swing]))

(deftest scalar-test
  (is (scalar? 1))
  (is (not (scalar? []))))

(def scalarmap {:a 1 :b "2" :c 1/2})
(def nestmap {:a []})
(deftest scalar-map-tets
  (is (scalar-map? scalarmap))
  (is (not (scalar-map? nestmap))))

(def homo '({:a 1 :b 2} {:a "1" :b "2"} {:a 1/3 :b 3/7}))
(def inhomo  [{:a 1} {:b 2}])
(def inhomo2  [{:a 1} {:a 2 :b 3}])
(deftest homo-test
  (is (homogeneous? homo))
  (is (not (homogeneous? inhomo)))
  (is (not (homogeneous? inhomo2))))



(def list-data ["a" "B" 1 2 3])
(deftest conform_conformed_data
  (let [conformed-data (s/conform :dsui.core/ds list-data)
        conformed²-data (s/conform :dsui.core/ds conformed-data)]
    (is (= conformed-data
           [:dsui.core/list-of-scalars ["a" "B" 1 2 3]]))
    (is (map-entry? conformed-data))
    (is (= conformed²-data
           [:dsui.core/labeled-ds [:dsui.core/list-of-scalars
                                   [:dsui.core/nested-ds
                                    [:dsui.core/list-of-scalars ["a" "B" 1 2 3]]]]]))))

(deftest matrix-test
  (is (= (s/conform :dsui.core/ds [["a" "B"] [1 2]])
         [:dsui.core/matrix [["a" "B"] [1 2]]]))
  ;; test same structure, only with map entries
  (is (= (s/conform :dsui.core/ds (seq {"a" "B" 1 2}))
         [:dsui.core/matrix [["a" "B"] [1 2]]])))

(deftest list-of-scalars-test
  (is (= (s/conform :dsui.core/ds ["a" "B"])
         [:dsui.core/list-of-scalars ["a" "B"]])))

(deftest scalar-entity-test
  (is (= (s/conform :dsui.core/ds {:a "A"
                                   :b 2
                                   :c 1/3
                                   :d 4.0})
         [:dsui.core/entity {:a [:dsui.core/scalar "A"]
                             :b [:dsui.core/scalar 2]
                             :c [:dsui.core/scalar 1/3]
                             :d [:dsui.core/scalar 4.0]}])))

;; match table if there is more than one entity, otherwise detect entity in a list
(deftest table-test
  (is (= [:dsui.core/table-of-entities [{:a 1}
                                        {:a 2}]]
         (s/conform :dsui.core/ds [{:a 1}
                                   {:a 2}])))   
  (is (= [:dsui.core/list-of-dss
          [[:dsui.core/nested-ds
            [:dsui.core/entity {:a [:dsui.core/scalar 1]}]]]]
         (s/conform :dsui.core/ds [{:a 1}]))))

