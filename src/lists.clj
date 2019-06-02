(load-file "fixed_point.clj")
(load-file "./church.clj")

;; Church Pairs - list representation
(def pair c-cons) ;; initialize a list node
(def head c-first) ;; retreive the value of a node
(def tail c-second) ;; points to the next node

(def empty-list c-false)

;; λlist.list (λh.λt.λd.c-false) c-true
(def is-empty
  (fn [list]
    ((list
      (fn [h]
        (fn [t]
          (fn [d]
            c-false))))
     c-true)))

;; λcondition.λthen.λelse.condition then else
(def my-if
  (fn [condition]
    (fn [then]
      (fn [else]
        ((condition then) else)))))

;; λnext-step.λlist1.λlist2.
;;   my-if (is-empty list1)
;;         list2
;;         ((pair (head list1)) (tail list1) list2)
(def my-append-step
  (fn [next-step]
    (fn [list1]
      (fn [list2]
        ;; wrap both branches inside a function and call the result to simulate lazy evaluation
        ((((my-if (is-empty list1))
           (fn [] list2))
          (fn [] ((pair (head list1)) ((next-step (tail list1)) list2)))))))))

(def my-append (Z my-append-step))

;; λnext-step.λf.λlist.
;;   my-if (is-empty list)
;;         list
;;         ((pair (f (head list))) ((next-step f) (tail-list)))
(def my-map-step
  (fn [next-step]
    (fn [f]
      (fn [list]
        ;; wrap both branches inside a function and call the result to simulate lazy evaluation
        ((((my-if (is-empty list))
           (fn [] list))
          (fn [] ((pair (f (head list))) ((next-step f) (tail list))))))))))

(def my-map (Z my-map-step))

;; λnext-step.λlist.
;;   my-if (is-empty list)
;;         empty-list
;;         ((my-append (next-step (tail list))) ((pair (head list)) empty-list))
(def my-reverse-step
  (fn [next-step]
    (fn [list]
      ((((my-if (is-empty list))
         (fn [] empty-list))
        (fn [] ((my-append (next-step (tail list))) ((pair (head list)) empty-list))))))))

(def my-reverse (Z my-reverse-step))

;; λnext-step.λlist.
;;   my-if (is-empty list)
;;         0
;;         (succ (next-step (tail list)))
(def my-length-step
  (fn [next-step]
    (fn [list]
      ((((my-if (is-empty list))
         (fn [] 0))
        (fn [] (+ (next-step (tail list)) 1)))))))

(def my-length (Z my-length-step))

;; λnext-step.λpredicate.λlist.
;;   my-if (is-empty list)
;;         empty-list
;;         (my-if (predicate (head list))
;;                ((pair (head list) (next-step predicate (tail list))))
;;                (next-step predicate (tail list)))
(def my-filter-step
  (fn [next-step]
    (fn [predicate]
      (fn [list]
        ((((my-if (is-empty list))
           (fn [] empty-list))
          (fn []
            ((((my-if (predicate (head list)))
               (fn [] ((pair (head list)) ((next-step predicate) (tail list)))))
              (fn [] ((next-step predicate) (tail list))))))))))))

(def my-filter (Z my-filter-step))

