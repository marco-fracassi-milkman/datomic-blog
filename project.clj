(defproject datomic-blog "0.1.0-SNAPSHOT"
  :description "Write blog data in Datomic"
  :url "http://example.com/FIXME"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.10.1"]
                 [com.datomic/datomic-free "0.9.5656"]]
  :repl-options {:init-ns datomic-blog.core})
