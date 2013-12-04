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


;;; Helper functions for a database
(defn identify-sequential
  "Generates a unique value for a collection of attributes which identifies an
  entity.  No future entities will ever be given the same identity.  The second
  part of the tuple is a new version of the identify function."
  [attributes]
  ())

(defn identify-by-name
  "Unsynchronized.  Expects the all maps to contain a name attribute."
  [attributes]
  ()) 

(defn mark-sequential
  "Simple sequence of version numbers.  Synchronized so no two DB transactions can create same version."
  [changes db]
  ()) 

(defn mark-by-hash
  "Computes a hash for a set of changes."
  [changes db]
  ()) 

(defn mark-by-time
  "Synchronous mark by system time.  Marks are gauranteed unique time."
  [changes db]
  ())

; Maybe try to do something distributed using time ranges as mentioned in Google Spanner presentation if it even makes
; sense?  http://www.infoq.com/presentations/spanner-distributed-google
(defn mark-by-period
  "The mark is a tuple of an integer id, earliest possible time, and latest possible time.  DBs may exist with the
  same id and overlapping time."
  [changes db]
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