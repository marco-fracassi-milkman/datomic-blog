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

(defn save-blog-post [title body]
  (d/transact (connection) [{:post/title title :post/body body}])
)

(use-fixtures :each prepare-db)

(deftest retrieve-a-blog-post-id-by-title
  (save-blog-post "new blogpost" "content")

  (is (not= (blog-post-id-by-title "new blogpost") nil))
)

(deftest retrieve-all-blog-post-ids-with-a-given-string-in-the-title
  (save-blog-post "old blogpost" "content")
  (save-blog-post "new blogpost" "content")
  (save-blog-post "other post" "content")

  (let [results (blog-post-ids-by-title-contains "blogpost")]
    (is (= (count results) 2))
  )
)

(deftest retrieve-a-blog-post-entity-by-title
  (save-blog-post "new blogpost" "content")

  (let [result (blog-post-by-title "new blogpost")]
    (is (= (:post/title result) "new blogpost"))
    (is (= (:post/body result) "content"))
  )
)

(deftest update-a-blog-post-content
  (save-blog-post "title" "old content")

  (let [blog-post-id (blog-post-id-by-title "title")]

    (update-blog-post-body blog-post-id "new content")

    (let [result (blog-post-by-title "title")]
      (is (= (:post/body result) "new content"))
    )
  )
)