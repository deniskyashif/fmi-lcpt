(load-file "./fixed-point.clj")

(def succ (fn [x] (+ x 1)))
(def pred (fn [x] (- x 1)))
(def is-zero (fn [x] (= x 0)))

;; The Ackermann function using direct recursion
(defn ackermann-rec [m n]
  (cond
    (= m 0) (+ n 1)
    (= n 0) (ackermann-rec (- m 1) 1)
    :else (ackermann-rec (- m 1) (ackermann-rec m (- n 1)))))

(def ackermann-step
  (fn [next-step]
    (fn [m]
      (fn [n]
        (cond
          (is-zero m) (succ n)
          (is-zero n) ((next-step (pred m)) 1)
          :else ((next-step (pred m))
                 ((next-step m) (pred n))))))))

(def ackermann (Z ackermann-step))

;; ((ackermann 0) 1)
