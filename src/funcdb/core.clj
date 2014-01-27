(ns funcdb.core
  "A simple persistent data type which never forgets.")

;; Constraints:
;; 1) Versions are unique in the database; i.e. no two transactions have the same version
;; 2) The natural ordering of versions represents the order of transactions, e.g. ver 5 is more recent than ver 2
;; 3) Identities are unique within a transaction; i.e. an entity only appears once in a transaction


(defn rev-sequential
  "Creates simple generator of version numbers.  Synchronized so no two DB transactions can create same version."
  [first-ver]
  (let [next (atom (dec first-ver))]
    (fn [] (swap! next inc))))

(defn identify-sequential
  "Creates simple generator of identity numbers.  Synchronized so an identity is only produced once."
  [first-id]
  (let [next (atom (dec first-id))]
    (fn [] (swap! next inc))))

(defn identify
  "Takes a sequence of collections of attributes for new entities.  Sets :id"
  [idfn attributes]
  (assoc attributes :id (idfn)))

(defn transact
  "Associates a new version with the sequence of entities, and creates a new version in the database
  by associating the latest version of the database with the sequence of versioned entities."
  [rev db entities]
  (let [new-ver (rev)]
    (assoc db new-ver (reduce #(assoc %1 (:id %2) %2) (first (vals db)) (map #(assoc % :ver new-ver) entities)))))

(defn new-db
  "Creates a new database using the provided revision generator.  The database contains 1 version with 0 entities."
  [rev]
  (sorted-map-by > (rev) {}))
