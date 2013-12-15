(ns funcdb.core
  "A simple persistent data type which never forgets.")

; Types
; Everything has a notional timestamp which is really a version number.

(defrecord Fact [value ver])

; attributes are mapping of attribute name to a list of facts
(defrecord Entity [id attributes ver])

; identify is a fn that takes a map and returns unique identity for an entity
; mark is a fn that returns a unique identity for a transaction
(defrecord Database [name entities indentify mark])

;;; Helper functions for a database
(defn identify-sequential
  "Synchronized version"
  [first-id]
  (let [next (atom (dec first-id))]
    (fn [attibutes] (swap! next inc))))


(defn identify-by-key
  "Synchronized.  Ensures values for key attribute are unique. Reserved values cannot be identities.
  Returns nil if the key is already taken."
  [key-name reserved]
  (let [taken (atom reserved)]
    (fn [attributes] 
      (let [key-value (attributes key-name)]
        (if (not (contains? taken key-value))
          (swap! taken (conj taken key-value)))
        key-value
        ))))

(defn mark-sequential
  "Simple sequence of version numbers.  Synchronized so no two DB transactions can create same version."
  [first-ver]
  (let [next (atom (dec first-ver))]
    (fn [change accum] (swap! next inc))))

; apply the change and get an entity
; eval the entity and accumulate a value
; when finished, apply the accumulated value as the mark to all touched facts, entities and db

(defn mark-by-hash
  "Computes a hash for a sequence of changes."
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

(defn add-all
  "Adds entities to a DB.  An entity is described by a map of named values."
  [entities db idGen timeGen]
  ())

; probably delete this method
(defn update
  "Assign the list of attribute values to entities matching the predicate"
  [where attibutes db timeGen]
  ())

; filter entities, map a xform, merge new entity versions to db

(defn anytime
  "Creates a sequence of all entities at anytime.  Entities will appear more than once, representing the entity at
  different points in time."
  [db]
  ())

(defn now 
  "Creates a sequences of entities with their attributes at time now"
  [db]
  ())

(defn when
  "Creates a sequences of entities with their attributes at time now"
  [db time]
  ())

; Select can just be filters on the sequences

(defn merge-db
  "Attempts to merge the src database into the dst database.  A new database is
  created with the merged changes with a list of entities which failed to merge"
  [src dst] 
  ())

(defn merge-entities
  [entities db]
  ())