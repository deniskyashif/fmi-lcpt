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

;; Factorial reduction using the Y combinator
;; ------------------------------------------
;; Y = λf.(λx.f (x x)) (λx.f (x x))
;; fact-step = λf.λn.if (iszero n) 1 (mult (f (pred n) n))

;; fact = Y fact-step = (λf.(λx.f (x x)) (λx.f (x x))) fact-step
;;      -> (λx.fact-step (x x)) (λx.fact-step (x x))
;;      -> fact-step ((λx.fact-step (x x)) (λx.fact-step (x x)))
;;      == (λf.λn.if (iszero n) 1 (mult (f (pred n) n))) ((λx.fact-step (x x)) (λx.fact-step (x x)))
;;      -> λn.(if (iszero n) 1 (mult ( (((λx.fact-step (x x)) (λx.fact-step (x x))) (pred n) ) n)))

;; fact 1 == (λn.(if (iszero n) 1 (mult (((λx.fact-step (x x)) (λx.fact-step (x x))) (pred n)) n))) 1
;; 		  ->      if (iszero 1) 1 (mult (((λx.fact-step (x x)) (λx.fact-step (x x))) (pred 1)) 1)
;;        ->                       mult (((λx.fact-step (x x)) (λx.fact-step (x x))) 0) 1
;;        ->                       mult     ((fact-step ((λx.fact-step (x x)) (λx.fact-step (x x)))) 0) 1
;;        ==                       mult     ((λn.(if (iszero n) 1 (mult (((λx.fact-step (x x)) (λx.fact-step (x x))) (pred n) n)))) 0) 1
;;        ->                       mult          (if (iszero 0) 1 (mult (((λx.fact-step (x x)) (λx.fact-step (x x))) (pred 0) 0))) 1
;;        ->                       mult 1 1
;;        ->                       1

(defn fib-step [f]
  (fn [n]
    (if (<= n 1)
      n
      (+ (f (- n 1)) (f (- n 2))))))

(def fib (Y1 fib-step))
(map fib (range 10))
