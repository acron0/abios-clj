(ns abios-clj.core-test
  (:require [clojure.test :refer :all]
            [abios-clj.core :refer :all]))

(deftest abios-clj-core-test
  (testing "Testing `replace-keys-from-map'"
    (is (= (replace-keys-from-map "foo" {}) "foo"))
    (is (= (replace-keys-from-map "foo:bar" {}) "foo"))
    (is (= (replace-keys-from-map "foo:bar" {:bar "baz"}) "foobaz"))
    (is (= (replace-keys-from-map "foo/:bar" {:bar "baz"}) "foo/baz"))
    (is (= (replace-keys-from-map ":a:b:c" {:a 1 :b 2 :c 3}) "123")))
  (testing "Testing `abios-endpoint`"
    (is (= (abios-endpoint "matches/:id" {:id "12431"})
           "https://api.abiosgaming.com/v1/matches/12431"))))
