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
(defn Z [f]
  ((fn [x] (f (fn [v] ((x x) v))))
   (fn [x] (f (fn [v] ((x x) v))))))

(defn fact-step [next-step]
  (fn [n]
    (if (= n 0)
      1
      (* (next-step (- n 1)) n))))

(def fact (Z fact-step))
;; (fact 5)

;; Factorial reduction using the Y combinator
;; ------------------------------------------
;; Y = λf.(λx.f (x x)) (λx.f (x x))
;; fact-step = λf.λn.if (iszero n) 1 (mult (f (pred n) n))

;; fact == Y fact-step
;;      == (λf.(λx.f (x x)) (λx.f (x x))) fact-step
;;      -> (λx.fact-step (x x)) (λx.fact-step (x x)) -- can't go beyond this point in an applicative order evaluation language
;;      -> fact-step ((λx.fact-step (x x)) (λx.fact-step (x x)))
;;      == (λf.λn.if (iszero n) 1 (mult (f (pred n) n))) ((λx.fact-step (x x)) (λx.fact-step (x x)))
;;      -> λn.(if (iszero n) 1 (mult ( (((λx.fact-step (x x)) (λx.fact-step (x x))) (pred n) ) n)))

;; fact 1 == (λn.(if (iszero n) 1 (mult (((λx.fact-step (x x)) (λx.fact-step (x x))) (pred n)) n))) 1
;;        ->      if (iszero 1) 1 (mult (((λx.fact-step (x x)) (λx.fact-step (x x))) (pred 1)) 1)
;;        ->                       mult (((λx.fact-step (x x)) (λx.fact-step (x x))) 0) 1
;;        ->                       mult     ((fact-step ((λx.fact-step (x x)) (λx.fact-step (x x)))) 0) 1
;;        ==                       mult     ((λn.(if (iszero n) 1 (mult (((λx.fact-step (x x)) (λx.fact-step (x x))) (pred n) n)))) 0) 1
;;        ->                       mult          (if (iszero 0) 1 (mult (((λx.fact-step (x x)) (λx.fact-step (x x))) (pred 0) 0))) 1
;;        ->                       mult 1 1
;;        ->                       1

;; Factorial reduction using the Y1 combinator
;; ------------------------------------------
;; Y1 = λf.(λx.λy.(f (x x)) y) (λx.λy.(f (x x)) y)
;; fact-step = λf.λn.if (iszero n) 1 (mult (f (pred n) n))

;; fact == Y1 fact-step
;;      == (λf.(λx.λy.(f (x x)) y) (λx.λy.(f (x x)) y)) fact-step
;;      ->     (λx.λy.(fact-step (x x)) y) (λx.λy.(fact-step (x x)) y)
;;      ->         λy.((fact-step ((λx.λy.(fact-step (x x)) y) (λx.λy.(fact-step (x x)) y))) y)

;; fact 1 == λy.((fact-step ((λx.λy.(fact-step (x x)) y) (λx.λy.(fact-step (x x)) y))) y) 1
;;        ->    ((fact-step ((λx.λy.(fact-step (x x)) y) (λx.λy.(fact-step (x x)) y))) 1)
;;        ==    ( (λf.λn.if (iszero n) 1 (mult (f (pred n) n))) ((λx.λy.(fact-step (x x)) y) (λx.λy.(fact-step (x x)) y)) ) 1
;;        ->         (λn.if (iszero n) 1 (mult (((λx.λy.(fact-step (x x)) y) (λx.λy.(fact-step (x x)) y)) (pred n)) n)) 1
;;        ->             if (iszero 1) 1 mult (((λx.λy.(fact-step (x x)) y) (λx.λy.(fact-step (x x)) y)) (pred 1)) 1
;;        ->                             mult (((λx.λy.(fact-step (x x)) y) (λx.λy.(fact-step (x x)) y)) (pred 1)) 1
;;        ->                             mult (((λx.λy.(fact-step (x x)) y) (λx.λy.(fact-step (x x)) y)) 0) 1
;;        ->                             mult ((λy.(fact-step ((λx.λy.(fact-step (x x)) y) (λx.λy.(fact-step (x x)) y))) y) 0) 1
;;        ->                             mult      (fact-step ((λx.λy.(fact-step (x x)) y) (λx.λy.(fact-step (x x)) y)) 0) 1
;;        ==                             mult (    ((λf.λn.if (iszero n) 1 (mult (f (pred n) n))) ((λx.λy.(fact-step (x x)) y) (λx.λy.(fact-step (x x)) y))) 0) 1
;;        ->                             mult (        (λn.if (iszero n) 1 (mult (((λx.λy.(fact-step (x x)) y) (λx.λy.(fact-step (x x)) y)) (pred n)) n) ) 0) 1
;;        ->                             mult (            if (iszero 0) 1 (mult (((λx.λy.(fact-step (x x)) y) (λx.λy.(fact-step (x x)) y)) (pred 0)) n) ) 1
;;        ->                             mult 1 1
;;        ->                             1

(defn fib-step [next-step]
  (fn [n]
    (if (<= n 1)
      n
      (+ (next-step (- n 1)) (next-step (- n 2))))))

(def fib (Y1 fib-step))
;; (map fib (range 10))

;; The Ackermann function using direct recursion
(defn ackermann-clj [m n]
  (cond
    (= m 0) (+ n 1)
    (= n 0) (ackermann-clj (- m 1) 1)
    :else (ackermann-clj (- m 1) (ackermann-clj m (- n 1)))))

(defn ackermann-step-curried [next-step]
  (fn [m]
    (fn [n]
      (cond
        (= m 0) (+ n 1)
        (= n 0) ((next-step (- m 1)) 1)
        :else ((next-step (- m 1))
               ((next-step m) (- n 1)))))))

(def ackermann-curried (Y1 ackermann-step))
;; ((ackermann-curried 0) 1)
