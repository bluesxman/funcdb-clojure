(ns funcdb.proto
  (:require [funcdb.core :refer :all]))

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

