;; Normal order evaluation. Causes stack overflow because clojure uses applicative order.
;; λf.(λx.f (x x)) (λx.f (x x))
(defn Y [f]
  ((fn [x] (f (x x)))
   (fn [x] (f (x x)))))

;; Applicative order evaluation
;; λf.(λx.λy.(f (x x)) y) (λx.λy.(f (x x)) y)
(defn Y1 [f]
  ((fn [x] (fn [y] ((f (x x)) y)))
   (fn [x] (fn [y] ((f (x x)) y)))))

;; Reference: http"//www.righto.com/2009/03/y-combinator-in-arc-and-java.html"
;; λr.(λf.(f f)) λf.(r λx.((f f) x))
(defn Y2 [r]
  ((fn [f] (f f))
   (fn [f] (r (fn [x] ((f f) x))))))

;; Fixed point combinator with applicative order
;; λf.(λx.f (λv.(x x) y)) (λx.f (λv.(x x) y))
(defn Z [f]
  ((fn [x] (f (fn [v] ((x x) v))))
   (fn [x] (f (fn [v] ((x x) v))))))

;; Fibonacci
(defn fib-step [next-step]
  (fn [n]
    (if (<= n 1)
      n
      (+ (next-step (- n 1)) (next-step (- n 2))))))

(def fib (Y1 fib-step))
