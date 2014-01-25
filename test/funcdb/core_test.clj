(ns funcdb.core-test
  (:require [funcdb.core :refer :all]
            [simple-check.core :as sc]
            [simple-check.generators :as gen]
            [simple-check.properties :as prop]
            [clojure.set :refer :all]))


;;;; Helper fns
(defn no-reserved? [m]
  (every? false? (map #(contains? m %) [:id :ver])))



;;;; Generators

;; Generate collections of attribs, i.e. entities without a version or identity
(defn gen-attribs []
  (gen/vector (gen/such-that no-reserved? (gen/map gen/keyword gen/any))))

;; Generate a sequence of new entities
(defn gen-entities []
  (gen/fmap #(identify (identify-sequential 0) %) (gen-attribs)))

;; Generate modified entities
(defn gen-mods [db]
  (let [[ver emap] (first db)]
    (gen/choose (vals emap))))

;; Generate a db



;;;; Properties

;; After transact, the version property of all updated entities is identical to the version-map they
;; were added to
(def entity-ver-matches-db-ver
  )

;; After transact, the version of entities NOT updated remains the same as prior to transact.

;; The number of identified entities is the same as the sequence of attrib collections

;; The identified entities have an :id key
(def identify-creates-ids
  (prop/for-all [attribs (gen-attribs)]
                (every? #(contains? % :id) (identify (identify-sequential 0) attribs))))

;; The ids in identified entities are unique
(def identities-are-unique
  (prop/for-all [entities (gen-entities)]
                (loop [eseq entities
                       ids #{}]
                  (let [e (first eseq)]
                    (if e
                      (if (contains? ids (e :id))
                        false
                        (recur (rest eseq) (conj ids (e :id))))
                      true)))))

;; After a transaction a new version appears in the db




;;;; Execute the tests
(sc/quick-check 20 identify-creates-ids)
(sc/quick-check 20 identities-are-unique)

