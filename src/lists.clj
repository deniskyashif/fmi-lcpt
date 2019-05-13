(def c-true (fn [x] (fn [y] x)))
(def c-false (fn [x] (fn [y] y)))

(def c-first (fn [p] (p c-true)))
(def c-second (fn [p] (p c-false)))

(def c-cons
  (fn [p]
    (fn [q]
      (fn [f]
        ((f p) q)))))

(def pair c-cons)
(def head c-first) ;; cat
(def tail c-second) ;; cdr; reference to the next node

(def null (fn [f] c-true))
(def empty-list c-false)
(def is-empty
  (fn [list]
    ((list
      (fn [h]
        (fn [t]
          c-false)))
     c-true)))

;; --- Example
(let [list012 ((pair 0) ((pair 1) ((pair 2) null)))]
  (println (is-empty empty-list))
  (println (is-empty list012))
  ;; print the elements of the list
  (println (head list012))
  (println (head (tail list012)))
  (println (head (tail (tail list012)))))
;; ---
