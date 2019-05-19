(load-file "./ackermann.clj")

;; Tests
(defn add-one [x] (+ x 1))

(assert (=
         ((((ackermann c0) c0) add-one) 0)
         1))
(assert (=
         ((((ackermann c1) c1) add-one) 0)
         (ackermann-rec 1 1)))
(assert (=
         ((((ackermann c0) c1) add-one) 0)
         2))
(assert (=
         ((((ackermann c1) c2) add-one) 0)
         (ackermann-rec 1 2)))
