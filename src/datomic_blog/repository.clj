(ns datomic_blog.repository
  (:require [datomic.api :as d])
)

(def db-uri (str "datomic:mem://" (d/squuid)))

(defn connection []
  (d/connect db-uri)
)

(defn db []
  (d/db (connection))
)

(defn blog-post-id-by-title [title]
  (let [result (d/q '[:find ?e
                      :in $ ?title
                      :where [?e :post/title ?title]
                      ] (db) title)]
    (ffirst result)
  )
)

(defn blog-post-by-title [title]
  (let [id (blog-post-id-by-title title)]
    (d/entity (db) id)
  )
)

(defn blog-post-ids-by-title-contains [title-part]
  (d/q '[:find ?e
         :in $ ?title-part
         :where [?e :post/title ?title] [(.contains ^String ?title ?title-part)]
         ] (db) title-part)
)

(defn blog-post-by-author-older-than [age]
  (sort-by #(:author/age (:post/author %)) (let [ids (d/q '[:find ?e
         :in $ ?in-age
         :where [?e :post/author ?author-id] [?author-id :author/age ?age] [(> ?age ?in-age)]
         ] (db) age)]
      (map #(d/entity (db) (first %)) ids)
    ))
  )

(defn update-blog-post-body [id body]
  (d/transact (connection) [{:db/id id :post/body body}])
)

(defn history-of [id]
  (d/q '[:find ?title ?body
         :in $ ?id
         :where [?id :post/title ?title] [?id :post/body ?body]
         ] (d/history (db)) id)
)