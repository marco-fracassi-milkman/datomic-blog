(ns datomic_blog.repository_test
  (:require [clojure.test :refer :all]
            [datomic_blog.repository :refer :all]
            [datomic.api :as d]
  )
  (:use [datomic-blog.schema])
)

(defn prepare-db [f] (create-db)  (f) (destroy-db))

(defn save-blog-post
  ([title body]
    (save-blog-post title body {:name "Jhon" :age 33})
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
  (save-blog-post "new blogpost" "content" {:name "Bob" :age 50})

  (let [result (blog-post-by-title "new blogpost")]
    (is (= (:post/title result) "new blogpost"))
    (is (= (:post/body result) "content"))
    (is (= (:author/name (:post/author result)) "Bob"))
    (is (= (:author/age (:post/author result)) 50))
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

(deftest retrieve-the-history-of-a-blog-post
  (save-blog-post "title" "old content")

  (let [blog-post-id (blog-post-id-by-title "title")]
    (update-blog-post-body blog-post-id "new content")
    (update-blog-post-body blog-post-id "newer content")

    (let [results (history-of blog-post-id)]
      (is (= (map #(second %) results) ["old content" "new content" "newer content"]))
    )
  )
)