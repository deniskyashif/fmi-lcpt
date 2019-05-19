(load-file "./lists.clj")

;; --- Test list construction
(let [list012 ((pair 0) ((pair 1) ((pair 2) empty-list)))]
  (assert (= (is-empty empty-list) c-true))
  (assert (= (is-empty list012) c-false))
  (assert (= (head list012) 0))
  (assert (= (head (tail list012)) 1))
  (assert (= (head (tail (tail list012))) 2)))
;; ---

;; --- Test my-append
(let [l1 ((pair 0) empty-list)
      l2 ((pair 1) empty-list)
      l12 ((my-append l1) l2)]
  (assert (= (head l12) 0))
  (assert (= (head (tail l12)) 1))
  (assert (= (is-empty (tail (tail l12)))) c-true))
;; ---

;; --- Test my-map
(let [list234 ((pair 2) ((pair 3) ((pair 4) empty-list)))
      squared234 ((my-map (fn [x] (* x x))) list234)]
  (assert (= ((my-map identity) empty-list) empty-list))  
  (assert (= (head squared234) 4))
  (assert (= (head (tail squared234)) 9))
  (assert (= (head (tail (tail squared234))) 16)))
;; ---

;; --- Test my-reverse
(let [l12 ((pair 1) ((pair 2) empty-list))
      revl12 (my-reverse l12)]
  (assert (= (head revl12) 2))
  (assert (= (head (tail revl12)) 1))
  (assert (= (my-reverse empty-list) empty-list)))
;; ---

;; --- Test my-length
(let [list234 ((pair 2) ((pair 3) ((pair 4) empty-list)))]
  (assert (= (my-length empty-list) 0))
  (assert (= (my-length list234) 3))
  (assert (= (my-length ((pair 1) empty-list)) 1)))
;; ---

;; --- Test my-filter
(let [list234 ((pair 2) ((pair 3) ((pair 4) empty-list)))
      is-even (fn [x] (if (even? x) c-true c-false))
      evens ((my-filter is-even) list234)]
  (assert (= ((my-filter is-even) empty-list) empty-list))
  (assert (= (my-length evens) 2))
  (assert (= (head evens) 2))
  (assert (= (head (tail evens)) 4)))
;; ---
