(load-file "./church.clj")
(load-file "./fixed_point.clj")

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
          (= (c-zero m) c-true) (c-succ n)
          (= (c-zero n) c-true) ((next-step (c-pred m)) c1)
          :else ((next-step (c-pred m))
                 ((next-step m) (c-pred n))))))))

(def ackermann (Z ackermann-step))
