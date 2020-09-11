(ns datomic_blog.repository
  (:require [datomic.api :as d])
)

(def db-uri (str "datomic:mem://" (d/squuid)))

(defn connection []
  (d/connect db-uri)
)

(defn blog-post-id-by-title [title]
  (let [result (d/q '[:find ?e
                      :in $ ?title
                      :where [?e :post/title ?title]
                      ] (d/db (connection)) title)]
    (ffirst result)
  )
)

(defn blog-post-by-title [title]
  (let [id (blog-post-id-by-title title)]
    (d/entity (d/db (connection)) id)
  )
)

(defn blog-post-ids-by-title-contains [title-part]
  (d/q '[:find ?e
         :in $ ?title-part
         :where [?e :post/title ?title] [(.contains ^String ?title ?title-part)]
         ] (d/db (connection)) title-part)
)

(defn update-blog-post-body [id body]
  (d/transact (connection) [{:db/id id :post/body body}])
)

(defn history-of [id]
  (d/q '[:find ?title ?body
         :in $ ?id
         :where [?id :post/title ?title] [?id :post/body ?body]
         ] (d/history (d/db (connection))) id)
)