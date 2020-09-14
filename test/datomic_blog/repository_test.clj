(ns datomic_blog.repository_test
  (:require [clojure.test :refer :all]
            [datomic_blog.repository :refer :all])
  (:use [datomic-blog.schema]
        [datomic-blog.post-builder :as post-builder]))

(use-fixtures :each #(do (create-db) (%) (destroy-db)))

(deftest retrieve-a-blog-post-id-by-title
  (post-builder/save "new blogpost" "content")

  (is (not= (blog-post-id-by-title "new blogpost") nil))
  )

(deftest retrieve-all-blog-post-ids-with-a-given-string-in-the-title
  (post-builder/save "old blogpost" "content")
  (post-builder/save "new blogpost" "content")
  (post-builder/save "other post" "content")

  (let [results (blog-post-ids-by-title-contains "blogpost")]
    (is (= (count results) 2))
    )
  )

(deftest retrieve-a-blog-post-entity-by-title
  (post-builder/save "new blogpost" "content" {:name "Bob" :age 50})

  (let [result (blog-post-by-title "new blogpost")]
    (is (= (:post/title result) "new blogpost"))
    (is (= (:post/body result) "content"))
    (is (= (:author/name (:post/author result)) "Bob"))
    (is (= (:author/age (:post/author result)) 50))
    )
  )

(deftest update-a-blog-post-content
  (post-builder/save "title" "old content")

  (let [blog-post-id (blog-post-id-by-title "title")]

    (update-blog-post-body blog-post-id "new content")

    (let [result (blog-post-by-title "title")]
      (is (= (:post/body result) "new content"))
      )
    )
  )

(deftest retrieve-the-history-of-a-blog-post
  (post-builder/save "title" "old content")

  (let [blog-post-id (blog-post-id-by-title "title")]
    (update-blog-post-body blog-post-id "new content")
    (update-blog-post-body blog-post-id "newer content")

    (let [results (history-of blog-post-id)]
      (is (= (map #(second %) results) ["old content" "new content" "newer content"]))
      )
    )
  )