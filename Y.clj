;; Normal order evaluation. Causes stack overflow because clojure uses applicative order.
;; λf.(λx.f (x x)) (λx.f (x x))
(defn Y [f]
  ((fn [x] (f (x x)))
   (fn [x] (f (x x)))))

;; Applicative order evaluation
;; λf.(λx.λx.(f (x x)) y) (λx.λx.(f (x x)) y)
(defn Y1 [f]
  ((fn [x] (fn [y] ((f (x x)) y)))
   (fn [x] (fn [y] ((f (x x)) y)))))

;; λr.(λf.(f f)) λf.(r λx.((f f) x))
(defn Y2 [r]
  ((fn [f] (f f))
   (fn [f] (r (fn [x] ((f f) x))))))

(defn fact-step [f]
  (fn [n]
    (if (= n 0)
      1
      (* (f (- n 1)) n))))

(def fact (Y1 fact-step))
(fact 5)

(defn fib-step [f]
  (fn [n]
    (if (<= n 1)
      x
      (+ (f (- n 1)) (f (- n 2))))))

(def fib (Y fib-step))
(map fib (range 10))
