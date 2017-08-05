(ns dsui.fx-test
  (:require [dsui.fx :as sut]
            [clojure.test :as t]))

(t/deftest test-handle-grid-pane
  (t/is (= (dsui.fx/handle-grid-pane {:type :grid-pane :a "a" :children [{:a "child1"} {:a "child2"}]})
           {:type :grid-pane,
            :a "a",
            :children
            [{:a "child1", :grid-pane/column-index 0, :grid-pane/row-index 0}
             {:a "child2", :grid-pane/column-index 1, :grid-pane/row-index 0}]})))
