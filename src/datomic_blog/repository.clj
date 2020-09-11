(ns datomic_blog.repository
  (:require [datomic.api :as d])
)

(def db-uri (str "datomic:mem://" (d/squuid)))

(defn connection []
  (d/connect db-uri)
)

(defn blog-post-key-by-title [title]
  (let [result (d/q '[:find ?e :in $ ?title :where [?e :post/title ?title]] (d/db (connection)) title)]
    (println "result --> " result)
    (first (first result))
  )
)