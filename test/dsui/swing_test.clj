(ns dsui.swing-test
  (:use [clojure.test])
  (:require [dsui.swing :as swing]))

(deftest table-model-test
  (let [tm (swing/table-model '("A" "B" "c") [[1 2 3]
                                        [:1 :2 :3]])]
    (is (= 3 (.getColumnCount tm)))
    (is (= 2 (.getRowCount tm)))))
