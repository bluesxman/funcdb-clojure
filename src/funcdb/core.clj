(ns funcdb.core)

; Types
; Everything has a notional timestamp which is really a version number.

(defrecord Fact [value time])

; attributes are mapping of attribute name to a list of facts
(defrecord Entity [id attributes time])

; identify is a fn that takes a map and returns a tuple of the a unique identity and a new version of itself
; mark is a fn that returns a tuple of the time of the transaction unique to that transaction and a new
; version of itself
(defrecord Database [name entities indentify mark])

(defn identify-sync
  "Generates a unique value for a collection of attributes which identifies an
  entity.  No future entities will ever be given the same identity.  The second
  part of the tuple is a new version of the identify function."
  [attributes]
  ())

; Public API starts here

(defn create
  "Creates an empty DB"
  [name idGen timeGen]
  (println "Creating database" name))

(defn addAll
  "Adds entities to a DB.  An entity is described by a map of named values."
  [entities db idGen timeGen]
  ())

(defn update
  "Assign the list of attribute values to entities matching the predicate"
  [where attibutes db timeGen]
  ())

(defn selectNow
  "Creates a sequence of entities which match the predicate"
  [where db]
  ())

(defn mergeDBs
  "Attempts to merge the src database into the dst database.  A new database is
  created with the merged changes with a list of entities which failed to merge"
  [src dst]
  ())