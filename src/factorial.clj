(load-file "./fixed-point.clj")

;; Factorial
(def fact-step
  (fn [next-step]
    (fn [n]
      (if (= n 0)
        1
        (* (next-step (- n 1)) n)))))

(def fact (Y1 fact-step))

;; Factorial reduction using the Y combinator
;; ------------------------------------------
;; Y = λf.(λx.f (x x)) (λx.f (x x))
;; fact-step = λf.λn.if (iszero n) 1 (mult (f (pred n) n))

;; fact == Y fact-step
;;      == (λf.(λx.f (x x)) (λx.f (x x))) fact-step
;;      -> (λx.fact-step (x x)) (λx.fact-step (x x)) !-- at this point in an applicative order evaluation language we're getting into an infinite expansion
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

;; Factorial reduction using the the applicative order fixed-point combinator - Y1
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
