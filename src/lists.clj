(def c-true (fn [x] (fn [y] x)))
(def c-false (fn [x] (fn [y] y)))

(def c-first (fn [p] (p c-true)))
(def c-second (fn [p] (p c-false)))

(def c-cons
  (fn [p]
    (fn [q]
      (fn [f]
        ((f p) q)))))

;; Church Pairs - list representation
(def pair c-cons)
(def head c-first) ;; value of a node
(def tail c-second) ;; points to the next node

(def empty-list c-false)
(def is-empty
  (fn [list]
    ((list
      (fn [h]
        (fn [t]
          (fn [d]
            c-false))))
     c-true)))

;; --- Test list construction
(let [list012 ((pair 0) ((pair 1) ((pair 2) empty-list)))]
  (assert (= (is-empty empty-list) c-true))
  (assert (= (is-empty list012) c-false))
  (assert (= (head list012) 0))
  (assert (= (head (tail list012)) 1))
  (assert (= (head (tail (tail list012))) 2)))
;; ---

(def my-if
  (fn [condition]
    (fn [then]
      (fn [else]
        ((condition then) else)))))

;; Fixed point combinator for languages with applicative order evaluation
(defn Y1 [f]
  ((fn [x] (fn [y] ((f (x x)) y)))
   (fn [x] (fn [y] ((f (x x)) y)))))

(def my-append-step
  (fn [next-step]
    (fn [list1]
      (fn [list2]
        ;; wrap both branches inside a function and call the result to simulate lazy evaluation
        ((((my-if (is-empty list1))
           (fn [] list2))
          (fn [] ((pair (head list1)) ((next-step (tail list1)) list2)))))))))

(def my-append (Y1 my-append-step))

;; --- Test my-append
(let [l1 ((pair 0) empty-list)
      l2 ((pair 1) empty-list)
      l12 ((my-append l1) l2)]
  (assert (= (head l12) 0))
  (assert (= (head (tail l12)) 1))
  (assert (= (is-empty (tail (tail l12)))) c-true))
;; ---

(def my-map-step
  (fn [next-step]
    (fn [f]
      (fn [list]
        ;; wrap both branches inside a function and call the result to simulate lazy evaluation
        ((((my-if (is-empty list))
          (fn [] list))
         (fn [] ((pair (f (head list))) ((next-step f) (tail list))))))))))

(def my-map (Y1 my-map-step))

;; --- Test my-map
(let [list234 ((pair 2) ((pair 3) ((pair 4) empty-list)))
      squared234 ((my-map (fn [x] (* x x))) list234)]
  (assert (= ((my-map identity) empty-list)))  
  (assert (= (head squared234) 4))
  (assert (= (head (tail squared234))))
  (assert (= (head (tail (tail squared234))))))
;; ---

(def my-reverse-step
  (fn [next-step]
    (fn [list]
      ((((my-if (is-empty list))
         (fn [] empty-list))
        (fn []
          ((my-append (next-step (tail list))) ((pair (head list)) empty-list))))))))

(def my-reverse (Y1 my-reverse-step))

;; --- Test my-reverse
(let [l12 ((pair 1) ((pair 2) empty-list))
      revl12 (my-reverse l12)]
  (assert (= (head revl12) 2))
  (assert (= (head (tail revl12)) 1))
  (assert (= (my-reverse empty-list) empty-list)))
;; ---
