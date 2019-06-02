(load-file "./church.clj")

;; used for testing the resulting Church numerals
(defn add-one [x] (+ x 1))

(assert
 (=
  ((c0 add-one) 0)
  0))
(assert
 (=
  ((c1 add-one) 0)
  1))
(assert
 (=
  ((c5 add-one) 0)
  5))
(assert
 (=
  (((c-succ c1) add-one) 0)
  2))
(assert
 (=
  (((c-succ c4) add-one) 0)
  5))
(assert
 (=
  ((((c-add c1) c2) add-one) 0)
  3))
(assert
 (=
  ((((c-add c5) c2) add-one) 0)
  7))
(assert
 (=
  ((((c-mult c5) c2) add-one) 0)
  10))
(assert
 (=
  ((((c-exp c5) c2) add-one) 0)
  25))
(assert
 (=
  (((c-pred c4) add-one) 0)
  3))
(assert
 (=
  (((c-pred c5) add-one) 0)
  4))
(assert
 (=
  (c-zero c0)
  c-true))
(assert
 (=
  (c-zero c1)
  c-false))
(assert
 (=
  ((((c-subtract c2) c1) add-one) 0)
  1))
(assert
 (=
  ((((c-subtract c5) c2) add-one) 0)
  3))
(assert
 (=
  ((((c-subtract c3) c3) add-one) 0)
  0))
