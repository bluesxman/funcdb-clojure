(ns funcdb.core)

(defn foo
  "I don't do a whole lot."
  [x]
  (println x "Hello, World!"))

(foo "Blah! ")

; Types
; Everything has a notional timestamp which is really a version number.

(defstruct fact :value :time)

; attributes are mapping of attribute name to a list of facts
(defstruct entity :id :attributes :time)

(defstruct database :name :entities :time)



; Public API starts here

(defn create
  "Creates an empty DB"
  [name]
  (println "Creating database" name))

(defn addAll
  "Adds entities to a DB.  An entity is described by a map of named values."
  [entities db idGen]
  ())

(defn update
  "Assign the list of attribute values to entities matching the predicate"
  [where attibutes db]
  ())

(defn selectNow
  "Creates a sequence of entities which match the predicate"
  [where db]
  ())

(defn merge
  "Attempts to merge the src database into the dst database.  A new database is
  created with the merged changes with a list of entities which failed to merge"
  [src dst]
  ())