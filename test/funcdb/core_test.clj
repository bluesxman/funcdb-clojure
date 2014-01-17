(ns funcdb.core-test
  (:require [funcdb.core :refer :all]
            [simple-check.core :as sc]
            [simple-check.generators :as gen]
            [simple-check.properties :as prop]))

(gen/sample (gen/map gen/keyword gen/any))