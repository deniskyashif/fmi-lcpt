(load-file "./church.clj")
(load-file "./fixed_point.clj")

;; minimize-step :=
;;   λnext-step.λf.λcn.
;;     c-if (c-zero (f cn))
;;          cn
;;          ((next-step f) (c-succ cn))
(def minimize-step
  (fn [next-step]
    (fn [f]
      (fn [cn]
        ((((c-if (c-zero (f cn)))
           ;; evaluate on demand
           (fn [] cn))
          (fn [] ((next-step f) (c-succ cn)))))))))

;; λf.Z minimize-step f c0
(def minimize
  (fn [f]
    (((Z minimize-step) f) c0)))

;; Test

(def id (fn [x] x))
(assert
 (=
  (minimize id)
  c0))

;; used for testing the resulting Church numerals
(defn add-one [x] (+ x 1))

;; f(x) = 3 - x
(def subtract-from-c3
  (fn [cn]
    ((c-subtract c3) cn)))
(assert
 (=
  (((subtract-from-c3 c2) add-one) 0)
  1))
(assert
 (=
  (((subtract-from-c3 c0) add-one) 0)
  3))
(assert
 (=
  (((minimize subtract-from-c3) add-one) 0)
  3))

;; f(x) = 5 - (2 + x)
(def subtract-from-c5-add-c2
  (fn [cn] ((c-subtract c5) ((c-add c2) cn))))
(assert
 (=
  (((minimize subtract-from-c5-add-c2) add-one) 0)
  3))
