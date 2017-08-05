(ns dsui.core
  (:require [clojure.spec.alpha :as s]
            [dsui.swing :as swing]
            [fn-fx.fx-dom :as dom]
            [fn-fx.controls :as ui]
            [fn-fx.diff :refer [component defui render should-update?]]))

(set! *warn-on-reflection* true)

(defn scalar? [v]
  ((complement coll?) v))

(defn same? [f coll]
  (if (empty? coll) false
      (apply = (map f coll))))

(defn homogeneous? [ms]
  (same? keys ms))

(defn scalar-map? [m]
  (every? scalar? (vals m)))

(s/def ::ds (s/or ::entity ::entity
                  ::table-of-entities ::table-of-entities
                  ::matrix ::matrix
                  ::labeled-ds ::labeled-ds
                  ::list-of-scalars ::list-of-scalars
                  ::list-of-dss ::list-of-dss))

(s/def ::table-of-entities (s/and (s/coll-of map? :min-count 2)
                                  (s/every scalar-map?)
                                  homogeneous?))

(s/def ::matrix (s/and (s/coll-of ::list-of-scalars)
                       #(same? count %)))

(s/def ::list-of-scalars (s/coll-of ::scalar))

(s/def ::list-of-dss (s/and (s/coll-of ::ds-or-scalar)
                            (complement map?)))

(s/def ::entity (s/map-of any? ::ds-or-scalar))

(s/def ::labeled-ds (s/and map-entry?
                           (s/tuple keyword? ::ds-or-scalar)))

(s/def ::ds-or-scalar (s/or ::nested-ds ::ds
                            ::scalar ::scalar))

(s/def ::scalar scalar?)



;; Umformungen
;; - Entity: Aufteilung in zwei Kinder
;; - matrix: Konvertierung in List of Maps
;; - Tab Ausrichtung
;; - Indizes Felder
;; - Allgemein: übersetzung in Maps mit type statt labeled data


(defmulti to-ui first)

  
(defn tab [[n content-ds]]
  {:type :tab
   :name n
   :children [(to-ui content-ds)]})

(defn tab-pane [coll]
  {:type :tab-pane
   :children (map tab coll)})

(defn label [t]
  {:type :label
   :text (str t)})

(defn field [[k ds]]
  [(label k)
   (to-ui ds)])

(defn grid-pane [coll]
  {:type :grid-pane 
   :children (mapcat field coll)})

(defn list-item [v]
  {:type :list-cell
   :value (str v)})


(defmethod to-ui :default [[_ ds]]
  (label ds))

(defmethod to-ui ::list-of-dss [[_ ds]]
  (let [tabs (map-indexed (fn [i v] [i v]) ds)]
    (-> (tab-pane tabs)
        (assoc :tab-position :left)
        (assoc :tab-orientation :horizontal))))

(defmethod to-ui ::labeled-ds [[_ ds]]
  (-> (tab-pane [ds])  ;; one tab only (for the label)
      (assoc :tab-position :left)
      (assoc :tab-orientation :vertical)))

(defmethod to-ui ::entity [[_ ds]]
  (let [children (group-by #(key (val %)) ds)
        fields (::scalar children)
        nested-uis (::nested-ds children)
        children [(grid-pane fields) #_(tab-pane nested-uis)]]
    {:type :flow-pane
     :children children}))  

#_(defmethod to-ui :table-of-entities [[_ ds]]
(table (table-model (keys (first ds)) (map vals ds))))

#_(defmethod to-ui :matrix [[_ ds]]
(table (table-model (range (count (first ds))) ds)))

(defmethod to-ui ::list-of-scalars [[_ ds]]
  {:type :ListView
   :children (map list-item ds)})

(defmethod to-ui ::scalar [[_ ds]]
  {:type :text-field
   :text (str ds)})

(defmethod to-ui ::nested-ds [[_ ds]]
  (to-ui ds))










(defn dsui [ds]
  (->> ds
       (s/conform ::ds)
       #_to-ui
       swing/create))

(defn exception-ui [t]
  (-> t Throwable->map dsui))

(defn conform-ui [spec ds]
  (try (let [confed (s/conform spec ds)]
         (if (= :clojure.spec.alpha/invalid confed)
           (dsui (s/explain-data spec ds))
           (dsui confed)))
       (catch Throwable t (exception-ui t))))
