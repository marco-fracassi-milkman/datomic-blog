(ns datomic_blog.repository_test
  (:require [clojure.test :refer :all]
              [datomic_blog.repository :refer :all]
            [datomic.api :as d]
  )
)

(def db-uri-base "datomic:mem://")
(defn scratch-conn
  "Create a connection to an anonymous, in-memory database."
  []
  (let [uri (str db-uri-base (d/squuid))]
    (d/delete-database uri)
    (d/create-database uri)
    (d/connect uri)))

(def post-schema [{
                      :db/ident       :post/title
                      :db/valueType   :db.type/string
                      :db/cardinality :db.cardinality/one
                      }
                     {
                      :db/ident       :post/body
                      :db/valueType   :db.type/string
                      :db/cardinality :db.cardinality/one
                      }])
(def conn (scratch-conn))

(defn create-db []
  (d/transact conn post-schema)
)

(defn destroy-db []
  (println "dopo")
)

(defn prepare-db [f] (create-db)  (f) (destroy-db))

(use-fixtures :once prepare-db)

(deftest a-test
  (d/transact conn [{:post/title "new blogpost" :post/body "content"}])
  (let [results (d/q '[:find ?e ?body
                       :where [?e :post/title "new blogpost"]
                              [?e :post/body ?body]] (d/db conn))]

    (is (= (second (first results)) "content"))
  )
)
