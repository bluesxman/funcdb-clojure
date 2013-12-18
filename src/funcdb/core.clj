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

(defn when-time
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

(defn- attrib-changed?
  [facts1 facts2]
  (let [ver1 (:ver (first facts1))
        ver2 (:ver (first facts2))]
    (= ver1 ver2)))

(defn- diff
  [entity ver1 ver2]
  (let [m1 (entity ver1)
        m2 (entity ver2)]
    ))

(defn- latest-ver
  [entity]
  (key (first (entity :attributes))))

(defn- find-common
  "Finds the version number that both entities have in common or nil if no common version exists."
  [entity1 entity2]
  (loop [remaining (seq (entity1 :attributes))]
    (let [ver1 (key (first remaining))]
      (if (or (nil? ver1) (contains? (entity1 :attributes) ver1))
        ver1
        (recur (rest remaining))))))

; if both entities have changed since their common ancestor
; and if an attribute has changed value in both entities
; and that value is different between the two entities
; then its a collision
(defn- collision?
  [entity1 entity2]
  (let [ver1 (latest-ver entity1)
        ver2 (latest-ver entity2)
        common-ver (find-common entity1 entity2)]
    (if (or (= common-ver ver1) (= common-ver ver2))
      false
      (let [diff1 (diff entity1 common-ver ver1)
            diff2 (diff entity2 common-ver ver2)]
        (loop [remaining (seq diff1)]
          (let [entry1 (first remaining)
                k1 (key entry1)
                v1 (val entry1)]
            (if (nil? entry1)
              false
              (if (and (contains? diff2 k1) (not= v1 (diff2 k1)))
                true
                (recur (rest remaining))))))))))