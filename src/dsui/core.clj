(ns dsui.core
  (:require [clojure.spec.alpha :as s]))

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

