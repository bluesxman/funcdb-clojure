(ns funcdb.github
  (:require [funcdb.core :refer :all]
            [tentacles.core :as tent]
            [tentacles.repos :as repos]
            [tentacles.users :as users]))

;; :full_name
;; :language
;; :updated_at - last commit
;; :fork - true/false

(def my-repos (repos/user-repos "bluesxman"))

(keys (first my-repos))

(-> (first my-repos) :owner :login)

(count my-repos)

(map #(select-keys % [:forks :forks_count :fork :name ]) my-repos)

(slurp "https://api.github.com/repos/bluesxman/om/languages")
(slurp "https://api.github.com/repos/bluesxman/FSharp/subscribers")

(repos/contents "bluesxman" "funcdb-clojure" "project.clj" {:keys [true]})
(slurp "https://api.github.com/repos/bluesxman/funcdb-clojure/contents/project.clj?ref=master")

(read-string (slurp "https://raw.github.com/bluesxman/funcdb-clojure/master/project.clj"))

(def fsharp-watchers (remove empty? (repos/watchers "bluesxman" "FSharp")))
(identity fsharp-watchers)
(keys (first fsharp-watchers))
(map :login (users/followers "bluesxman"))
(map :login (users/following "bluesxman"))

;;;;;;;;;;;;;;;;;;;

(defrecord Repo [id ver owner name updated-at fork? language])
(defrecord User [id ver name type])
(defrecord Watches [id ver user repo])

(defn to-Repo
  [m]
  (Repo. nil nil (-> m :owner :login) (m :name) (m :updated_at) (m :fork) (m :language)))

(defn to-User
  [m]
  (User. nil nil (m :login) (m :type)))

(defn get-user [idf login]
  (idfy (to-User (users/user login))))

(defn get-repos [idfy user]
  (map #(relate % :owner user) (map idfy (remove :fork? (map to-Repo (remove empty? (repos/user-repos (:name user))))))))

(defn get-watchers
  [idfy user repo]
  (map idfy (map to-User (remove empty? (repos/watchers (:name user) (:name repo))))))

(defn relate [child fkey parent]
  (assoc child fkey (:id parent)))

;; git-bacon
;; start with a user
;; for a user, get their repos
;; for a repo, get its watchers
;; foreach watcher, recurse unless limit
(def idfy (partial identify (identify-sequential 0)))

(def root-user (get-user idfy "bluesxman"))
(identity root-user)
(def root-repos (get-repos idfy root-user))
(def root-watchers (flatten (remove empty? (map #(get-watchers idfy root-user %) root-repos))))
(identity root-watchers)

(def fsharp-repo (first (filter #(= (:name %) "FSharp") root-repos)))
(identity fsharp-repo)

(:name root-user)
(:id fsharp-repo)

(get-watchers idfy root-user fsharp-repo)

(->> fsharp-repo :owner)
