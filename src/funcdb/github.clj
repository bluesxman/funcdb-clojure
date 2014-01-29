(ns funcdb.github
  (:require [funcdb.core :refer :all]
            [tentacles.core :as tent]
            [tentacles.repos :as repos]
            [tentacles.users :as users]))


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
  (map #(assoc % :owner user) (map idfy (remove :fork? (map to-Repo (remove empty? (repos/user-repos (:name user))))))))

(defn get-watchers
  [idfy user repo]
  (map idfy (map to-User (remove empty? (repos/watchers (:name user) (:name repo))))))

(defn get-watchers-for-all
  [idfy user repos]
  (remove empty? (map #(get-watchers idfy user %) repos)))

(defn create-watches
  [idfy users repo]
  (map idfy (map #(Watches. nil nil % repo) users)))

(defn create-watches-for-all
  [idfy watchers repos]
  (flatten (map (fn [[w r]] (create-watches idfy w r)) (map vector watchers repos))))

(defn get-degs
  [idfy
   root-login
   degs]
  (let [user (get-user idfy root-login)
        repos (get-repos idfy user)
        watchers (get-watchers-for-all idfy user repos)
        watches (create-watches-for-all idfy watchers repos)]
    (cons user (lazy-cat repos (flatten watchers) watches))))

;; git-bacon
;; start with a user
;; for a user, get their repos
;; for a repo, get its watchers
;; foreach watcher, recurse unless limit
(def idfy (partial identify (identify-sequential 0)))

(def root-user (get-user idfy "bluesxman"))
(identity root-user)
(def root-repos (get-repos idfy root-user))
(identity root-repos)
(def root-watchers (get-watchers-for-all idfy root-user root-repos))
(identity root-watchers)



(def root-watches (create-watches-for-all idfy root-watchers root-repos))
(identity root-watches)

(def my-degs (get-degs idfy "bluesxman" 1))

(map type my-degs)
