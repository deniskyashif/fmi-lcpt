;; Causes stack overflow
;; (defn Y [f]
;;   ((fn [x] (f (x x)))
;;    (fn [x] (f (x x)))))

(defn Y [f]
  ((fn [x] (fn [y] ((f (x x)) y)))
   (fn [x] (fn [y] ((f (x x)) y)))))

(defn fact-step [f]
  (fn [x]
    (if (= x 0)
      1
      (* (f (- x 1)) x))))

(def fact (Y fact-step))
(fact 5)

(defn fib-step [f]
  (fn [x]
    (if (<= x 1)
      x
      (+ (f (- x 1)) (f (- x 2))))))

(def fib (Y fib-step))
(map fib (range 10))
