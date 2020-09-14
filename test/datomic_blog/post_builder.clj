(ns datomic-blog.post-builder
  (:require [datomic.api :as d]
            [datomic_blog.repository :refer :all])
  )

(defn save
  ([title body]
   (save title body {:name "Jhon" :age 33})
   )
  ([title body author]
   (d/transact (connection) [{:author/name (:name author) :author/age (:age author)}])
   (let [author-id (d/q '[:find ?e
                          :in $ ?name ?age
                          :where [?e :author/name ?name] [?e :author/age ?age]
                          ] (db) (:name author) (:age author))]
     (d/transact (connection) [{:post/title title :post/body body :post/author (d/pull (db) '[*] (ffirst author-id))}])
     )
   )
  )