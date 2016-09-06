(ns dsui.core-test
  (:use dsui.core)
  (:use clojure.test))



(deftest primitive-test
  (is (primitive? 1))
  (is (not (primitive? []))))

(def prmap {:a 1 :b "2" :c 1/2})
(def nestmap {:a []})

(deftest primitive-map-test
  (is (primitive-map? prmap))
  (is (not (primitive-map? nestmap))))


(def homo '({:a 1 :b 2} {:a "1" :b "2"} {:a 1/3 :b 3/7}))
(def inhomo  [{:a 1} {:b 2}])
(def inhomo2  [{:a 1} {:b 2}])
(deftest homo-test
  (is (= true (homogenous? homo)
         (not (homogenous? inhomo))
         (not (homogenous? inhomo2)))))
