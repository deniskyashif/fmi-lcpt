(load-file "./fixed-point.clj")

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
          (= m 0) (+ n 1)
          (= n 0) ((next-step (- m 1)) 1)
          :else ((next-step (- m 1))
                 ((next-step m) (- n 1))))))))

(def ackermann (Y1 ackermann-step))

;; ((ackermann 0) 1)
