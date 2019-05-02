// Causes stack overflow
// const Y = f => {
//     const fn = x => f(x(x));
//     const arg = x => f(x(x));

//     return fn(arg);
// };

const Y = f => {
    const fn = x => y => f(x(x))(y);
    const arg = x => y => f(x(x))(y);

    return fn(arg);
};

const factStep = f => x =>
      x === 0
      ? 1
      : x * f(x - 1);

const fact = Y(factStep);

console.log(fact(5));

const fibStep = f => x =>
      x <= 1
      ? x
      : fib(x - 1) + fib(x - 2);

const fib = Y(fibStep);

const range = (from, to) => [...Array(to).keys()].splice(from, to);

console.log(range(0, 10).map(fib));
