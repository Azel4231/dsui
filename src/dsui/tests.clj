(ns dsui.tests
  (:require  [clojure.test :as t]
             [clojure.spec.alpha :as s]
             [dsui.core]
             [dsui.swing :as swing]
             [dsui.fx :as fx]
             
             [fn-fx.fx-dom :as dom]
             [fn-fx.controls :as ui]
             [fn-fx.diff :refer [component defui render should-update?]]))

(s/conform :dsui.core/ds [{:a  "1"
                   :b  "2"
                   :c  [1 2 3 4 5]}
                 "B"])

(s/conform :dsui.core/ds [1 2])

(s/conform :dsui.core/ds [1 ["a" "B"]])


#_(ui/dsui (dsui.core/to-ui
          (s/conform :dsui.core/ds {:a  "1"
                                    :c  [1 2 3 4 5]})))

#_(ui/dsui (dsui.core/to-ui
          (s/conform :dsui.core/ds [1 ["a" "B"]])))


#_(def test-ui-ds (dsui.core/to-ui
                 (s/conform :dsui.core/ds {:a "A" :b "B"})))
#_(str test-ui-ds)


#_(fx/-main (dsui.core/to-ui
                 (s/conform :dsui.core/ds {:a "A" :b "B"})))

#_(fx/-main test-ui-ds)

(def ui-ds (dsui.core/to-ui
        (s/conform :dsui.core/ds {:a "A" :c "ceci n'est pas une pipe" :b ["B" "C"]})))


(dsui.fx/transform-maps #(update % :type dsui.fx/to-fx-type)
                {:type :grid-pane :b [{:a :Z} "C"]})

(def ui-ui-ds (dsui.fx/transform-maps #(update % :type dsui.fx/to-fx-type)
                                      ui-ds))



(fx/to-fx-ds ui-ds)

#_(fx/create-ui ui-ds)
