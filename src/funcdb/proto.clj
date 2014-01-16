(ns funcdb.proto)

;; Constraints:
;; 1) Versions are unique in the database; i.e. no two transactions have the same version
;; 2) The natural ordering of versions represents the order of transactions
;; 3) Identities are unique within a transaction; i.e. an entity only appears once in a transaction


(defn rev-sequential
  "Simple sequence of version numbers.  Synchronized so no two DB transactions can create same version."
  [first-ver]
  (let [next (atom (dec first-ver))]
    (fn [] (swap! next inc))))

(defn identify-sequential
  "Synchronized version"
  [first-id]
  (let [next (atom (dec first-id))]
    (fn [] (swap! next inc))))

(defn identify
  "attribs -> entities. sets :id, :ver is nil"
  [idfn attributes]
  (map #(assoc % :id (idfn)) attributes))

(defn transact
  "sets a version on the entities and creates a new version of the entities in the db"
  [rev db entities]
  (let [new-ver (rev)]
    (assoc db new-ver (reduce #(assoc %1 (:id %2) %2) (first (vals db)) (map #(assoc % :ver new-ver) entities)))))

(defn new-db
  [rev]
  (sorted-map-by > (rev) {}))
(first (sorted-map-by > 5 "a" 1 "b" 3 "c"))

(println
 (System/nanoTime) "\n"
 (System/currentTimeMillis))
(def nano (System/nanoTime))
(identity nano)

(* (System/currentTimeMillis) 1e6)


(def rev (rev-sequential 0))
(def db (new-db rev))
(identity db)

(defrecord Ship [id ver nm x y az health])
(type Ship)
(type {})

(def ships [(Ship. nil nil "a" 1 1 180 100) (Ship. nil nil "b" 5 5 215 100)])
(identity ships)

(def idfn (identify-sequential 1))
(def entities (identify idfn ships))
(identity entities)

(reduce #(assoc %1 (:id %2) %2) {} [{:id 1 :val 10} {:id 2 :val 34}])
(assoc {} (:id {:id 1 :val 10}) {:id 1 :val 10})

(def db2 (transact rev db entities))
(identity db2)

(defn- nano-raw
  ([] (nano-raw (System/nanoTime)))
  ([t] (cons t (lazy-seq (nano-raw (System/nanoTime))))))

(defn nano-seq
  "Sequence of real times "
  []
  (let [base-time (* 1000000 (System/currentTimeMillis))
        origin-nano (System/nanoTime)]
    ()))

(defn unique-time
  "Returns the current time without repeating a value.  Units are in nanoseconds."
  []
  )

(+ (* 10 365 24 60 60 1000000000) (System/currentTimeMillis))

(- (apply * (repeat 62 2)) (* 1000000 (System/currentTimeMillis)))

(def foo (vec (take 100000 (nano-raw))))

(apply max (map #(- (foo %) (foo (inc %))) (range 0 (dec (count foo)))))

