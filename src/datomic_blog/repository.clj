(ns datomic_blog.repository
  (:require [datomic.api :as d])
)

(def db-uri (str "datomic:mem://" (d/squuid)))

(defn connection []
  (d/connect db-uri)
)

(defn blog-post-key-by-title [title]
  (let [result (d/q '[:find ?e :in $ ?title :where [?e :post/title ?title]] (d/db (connection)) title)]
    (first (first result))
  )
)

(defn blog-post-keys-by-title-contains [title-part]
  (d/q '[:find ?e :in $ ?title-part :where [?e :post/title ?title] [(.contains ^String ?title ?title-part)]] (d/db (connection)) title-part)
)