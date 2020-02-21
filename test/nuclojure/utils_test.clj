(ns nuclojure.utils-test
  (:require [clojure.test :refer :all]
						[nuclojure.utils.validation :as val]
            [clojure.string :as str]))


(deftest validation
  (testing "check-errors"
    (def validations {:email {:message "Invalid email"
                              :required true
                              :validator #(str/blank? %)}
                      :password {:message "Invalid password"
                                 :validator #(< (count %) 5)}})
    (testing "check required"
      (def body {:password "12345"})
      (let [result (val/check-errors body validations)]
        (is (contains? result :email))
        (is (= (:email result) "Required"))))

    (testing "check validator"
      (def body {:email "" :password "12345"})
      (let [result (val/check-errors body validations)]
        (is (contains? result :email))
        (is (= (:email result) "Invalid email"))))

    (testing "check multiple attributes validation"
      (def body {:email "" :password "123"})
      (let [result (val/check-errors body validations)]
        (is (and (contains? result :email) (contains? result :password)))
        (is (= (:email result) "Invalid email"))
        (is (= (:password result) "Invalid password"))))))