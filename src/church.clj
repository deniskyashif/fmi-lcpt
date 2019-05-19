(load-file "fixed_point.clj")

(def repeated-step
  (fn [next-step]
    (fn [n]
      (fn [f]
        (fn [x]
          (if (= n 0)
            x
            (f (((next-step (- n 1)) f) x))))))))

(def repeated (Z repeated-step))

;; (repeated 5 f x) -> (f (f (f (f (f x)))))

(def c0 (repeated 0))
(def c1 (repeated 1))
(def c2 (repeated 2))
(def c3 (repeated 3))
(def c4 (repeated 4))
(def c5 (repeated 5))

(def c-add
  (fn [m]
    (fn [n]
      (fn [f]
        (fn [x]
          ((m f) ((n f) x)))))))

(def c-mult
  (fn [m]
    (fn [n]
      (fn [f]
        (m (n f))))))

(def c-exp
  (fn [m]
    (fn [n]
      (n m))))

(def I (fn [x] x))
(def K (fn [x] (fn [y] x)))
(def K* (fn [x] (fn [y] y)))

(def c-true K)
(def c-false K*)

;; Logical operators 
(def c-and
  (fn [p]
    (fn [q]
      ((p q) c-false))))

(def c-or
  (fn [p]
    (fn [q]
      (p c-true) q)))

(def c-not
  (fn [p]
    ((p c-false) c-true)))

(def c-xor
  (fn [p]
    (fn [q]
      ((p (c-not q)) q))))  

(def c-cons
  (fn [p]
    (fn [q]
      (fn [f]
        ((f p) q)))))

;; First of a pair
(def c-first
  (fn [p] (p c-true)))

;; Second of a pair
(def c-second
  (fn [p] (p c-false)))

(def c-succ
  (fn [n]
    (fn [f]
      (fn [x]
        (f ((n f) x))))))

(def c-pred
  (fn [n]
    (c-second
     ((n (fn [u]
           ((c-cons (c-succ (c-first u)))
            (c-first u))))
      ((c-cons c0) c0)))))

(def c-zero
  (fn [n]
    ((n (fn [m] c-false))
     c-true)))

(def S
  (fn [x]
    (fn [y]
      (fn [z]
        ((x z) (y z))))))

(def c-if
  (fn [b]
    (fn [x]
      (fn [y]
        ((b x) y)))))
