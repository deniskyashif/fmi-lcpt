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

;; --- Test
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

(def append-step
  (fn [next-step]
    (fn [list1]
      (fn [list2]
        ;; wrap both branches inside a function and call the result to simulate lazy evaluation
        ((((my-if (is-empty list1))
           (fn [] list2))
          (fn [] ((pair (head list1)) ((next-step (tail list1)) list2)))))))))

(def append (Y1 append-step))

;; --- Test
(let [l1 ((pair 0) empty-list)
      l2 ((pair 1) empty-list)
      l12 ((append l1) l2)]
  (assert (= (head l12) 0))
  (assert (= (head (tail l12)) 1))
  (assert (= (is-empty (tail (tail l12)))) c-true))
;; ---

(def my-map-step
  (fn [next-step]
    (fn [f]
      (fn [list]
        ((((my-if (is-empty list))
          (fn [] list))
         (fn [] ((pair (f (head list))) ((next-step f) (tail list))))))))))

(def my-map (Y1 my-map-step))

;; --- Test
(let [list012 ((pair 2) ((pair 3) ((pair 4) empty-list)))
      squared012 ((my-map (fn [x] (* x x))) list012)]
  (assert (= ((my-map identity) empty-list)))  
  (assert (= (head squared012) 4))
  (assert (= (head (tail squared012))))
  (assert (= (head (tail (tail squared012))))))
;; ---

