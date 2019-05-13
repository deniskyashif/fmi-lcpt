(defn repeated [n f x]
  (if (= n 0)
    x
    (f (repeated (- n 1) f x))))

;; (repeated 5 f x) -> (f (f (f (f (f x)))))

;; Church numerals
(defn c [n]
  (fn [f]
    (fn [x]
      (repeated n f x))))

(def c0 (c 0))
(def c1 (c 1))

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
      (m n))))

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
    (c-first
     ((n (fn [u]
           ((c-cons (c-succ (c-first u)))
            (c-second u))))
      ((c-cons c0) c0)))))

(def is-zero
  (fn [n]
    ((n (fn [x] c-false)) c-true)))

(def S
  (fn [x]
    (fn [y]
      (fn [z]
        ((x z) (y z))))))
