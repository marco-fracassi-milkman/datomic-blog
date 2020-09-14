(ns datomic-blog.schema
  (:require [datomic.api :as d]
            [datomic_blog.repository :refer :all])
  )

(def authors [{
               :db/ident       :author/name
               :db/valueType   :db.type/string
               :db/cardinality :db.cardinality/one
               }
              {
               :db/ident       :author/age
               :db/valueType   :db.type/long
               :db/cardinality :db.cardinality/one
               }]
  )

(def posts [{
             :db/ident       :post/title
             :db/valueType   :db.type/string
             :db/cardinality :db.cardinality/one
             }
            {
             :db/ident       :post/body
             :db/valueType   :db.type/string
             :db/cardinality :db.cardinality/one
             }
            {
             :db/ident       :post/author
             :db/valueType   :db.type/ref
             :db/cardinality :db.cardinality/one
             }]
  )

(defn create-db []
  (d/create-database db-uri)
  (d/transact (connection) authors)
  (d/transact (connection) posts)
  )

(defn destroy-db []
  (d/delete-database db-uri)
  )
