(ns datomic_blog.repository_test
  (:require [clojure.test :refer :all]
            [datomic_blog.repository :refer :all]
            [datomic.api :as d]
  )
)

(def post-schema [{
                    :db/ident       :post/title
                    :db/valueType   :db.type/string
                    :db/cardinality :db.cardinality/one
                    }
                   {
                    :db/ident       :post/body
                    :db/valueType   :db.type/string
                    :db/cardinality :db.cardinality/one
                    }]
)

(defn create-db []
  (d/create-database db-uri)
  (d/transact (connection) post-schema)
)

(defn destroy-db []
  (d/delete-database db-uri)
)

(defn prepare-db [f] (create-db)  (f) (destroy-db))

(use-fixtures :once prepare-db)

(deftest retrieve-a-blog-post-key-by-title
  (d/transact (connection) [{:post/title "new blogpost" :post/body "content"}])

  (is (not= (blog-post-key-by-title "new blogpost") nil))
)
