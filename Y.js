// Normal order evaluation. Causes stack overflow because js uses applicative order.
// λf.(λx.f (x x)) (λx.f (x x))
function Y(f) {
    const g = function (x) {
        return f(x(x));
    };
    return g(g);
};

// λf.(λx.λy.(f (x x)) y) (λx.λy.(f (x x)) y)
// This combinator allows us to recurse functions with multiple arguments; Applicative order.
function Y1(f) {
    const g = function (x) {
        return function () {
            return f(x(x)).apply(this, arguments);
        };
    };

    return g(g);
};

// Reference: http://www.righto.com/2009/03/y-combinator-in-arc-and-java.html
// λr.(λf.(f f)) λf.(r λx.((f f) x))
function Y2(r) {
    const fn = function (f) {
        return f(f);
    };
    
    const arg = function (f) {
        return r(x => f(f)(x));
    };

    return fn(arg);
}

// Fixed point combinator with applicative order
function Z (f) {
    const g = function (x) {
        const h = function (v) {
            return (x(x))(v);
        };

        return f(h);
    };
    return g(g);
}

// Factorial Implementation
function factStep (nextStep) {
    return function (x) {
        return x === 0
            ? 1
            : x * nextStep(x - 1);
    };
}

const fact = Y1(factStep);

console.log(fact(5));

// Fibonacci Implementation
function fibStep (nextStep) {
    return function (x) {
        return x <= 1
            ? x
            : nextStep(x - 1) + nextStep(x - 2);
    };
};

const fib = Z(fibStep);

const range = (from, to) => [...Array(to).keys()].splice(from, to);

console.log(range(0, 10).map(fib));

// Ackermann Implementation
function ackermannStep (nextStep) {
    return function (m, n) {
        if (m === 0)
            return n + 1;
        if (n === 0)
            return nextStep(m - 1, 1);
        return nextStep(m - 1, nextStep(m, n - 1));
    };
};

function ackermannStepCurried (nextStep) {
    return function (m) {
        return function (n) {
            if (m === 0)
                return n + 1;
            if (n === 0)
                return nextStep(m - 1)(1);
            return nextStep(m - 1)(nextStep(m)(n - 1));
        };
    };
};

const ackermann = Y1(ackermannStep);
const ackermann = Y1(ackermannStepCurried);

console.log(ackermann(0)(5));
