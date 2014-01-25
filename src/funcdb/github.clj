(ns funcdb.github
  (:require [funcdb.core :refer :all]
            [tentacles.core :as tent]
            [tentacles.repos :as repos]))

;; :full_name
;; :language
;; :updated_at - last commit
;; :fork - true/false

(def my-repos (repos/user-repos "bluesxman"))

(keys (first my-repos))

(:owner (first my-repos))

(count my-repos)

(map #(select-keys % [:forks :forks_count :fork :name ]) my-repos)

(slurp "https://api.github.com/repos/bluesxman/om/languages")
(slurp "https://api.github.com/repos/bluesxman/FSharp/subscribers")

(repos/contents "bluesxman" "funcdb-clojure" "project.clj" {:keys [true]})
(slurp "https://api.github.com/repos/bluesxman/funcdb-clojure/contents/project.clj?ref=master")

(read-string (slurp "https://raw.github.com/bluesxman/funcdb-clojure/master/project.clj"))



