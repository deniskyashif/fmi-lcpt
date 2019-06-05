/*
   Gödel’s System T in TypeScript
*/

const Zero = 0;
const Succ = (x: number): number => x + 1;

// A generic If-Then-Else
function Cases<T>(cond: boolean, a: T, b: T): T {
    return cond ? a : b;
}

/* Primitive recursor;
   Reductions: 
     Rec 0 s t = s
     Rec sn s t = t n (Rec n s t)
     "sn" stands for "the successor of n"
*/
function Rec<T>(sn: number, s: T, t: (z: number, acc: T) => T): T {
    return sn === Zero ? s : t(sn - 1, Rec(sn - 1, s, t));
}

function add(x: number, y: number): number {
    return Rec<number>(x, y, (z, acc) => Succ(acc));
}

function multiply(x: number, y: number): number {
    return Rec<number>(y, Zero, (z, acc) => add(x, acc));
}

function exp(x: number, y: number): number {
    return Rec<number>(y, 1, (z, acc) => multiply(x, acc));
}

function double(x: number): number {
    return Rec<number>(x, Zero, (z, acc) => Succ(Succ(acc)));
}

function pred(x: number): number {
    return Rec<number>(x, Zero, (z, acc) => z);
}

function subtract(x: number, y: number): number {
    return Rec<number>(y, x, (z, acc) => pred(acc));
}

const isZero = (x: number): boolean => {
    return Rec<boolean>(x, true, (z, acc) => false);
}

function remainder(x: number, y: number): number {
    return Cases<number>(
        lt(x, y),
        Zero,
        Rec<number>(
            x,
            Zero,
            (z, acc) => Cases<number>(
                eq(pred(y), acc),
                Zero,
                Succ(acc)
            ))
    );
}

function divide(x: number, y: number): number {
    return Cases<number>(
        lt(x, y),
        Zero,
        (Rec<number>(
            x,
            1,
            (z, acc) => Cases<number>(
                eq(pred(y), remainder(z, y)),
                Succ(acc),
                acc
            )))
    );
}

function isPrime(x: number): boolean {
    return Cases<boolean>(
        eq(x, 1),
        true,
        eq(2,
           (Rec<number>(
               x,
               Zero,
               (z, acc) => Cases<number>(
                   isZero(remainder(x, z)),
                   Succ(acc),
                   acc
               )))));
}

function not(x: boolean): boolean {
    return Cases<boolean>(x, false, true);
}

function and(x: boolean, y: boolean): boolean {
    return Cases<boolean>(x, y, false);
}

function or(x: boolean, y: boolean): boolean {
    return Cases<boolean>(x, true, y);
}

function xor(x: boolean, y: boolean): boolean {
    return Cases<boolean>(x, not(y), y);
}

function eq(x: number, y: number): boolean {
    return and(isZero(subtract(x, y)), isZero(subtract(y, x)));
}

function gt(x: number, y: number): boolean {
    return not(isZero(subtract(x, y)));
}

function lt(x: number, y: number): boolean {
    return not(isZero(subtract(y, x)));
}

function gte(x: number, y: number): boolean {
    return or(eq(x, y), gt(x, y));
}

function lte(x: number, y: number): boolean {
    return or(eq(x, y), lt(x, y));
}

type OneArityFn<T, K> = (x: T) => K;

function compose<T, K, V>(f: OneArityFn<K, V>, g: OneArityFn<T, K>): OneArityFn<T, V> {
    return x => f(g(x));
}

// Given a function f and a number n, it return a function that computes the n-th iterate of f.
// e.g. repeat(f, 3) = x => f(f(f(x)))
function repeat<T>(f: OneArityFn<T, T>, n: number): OneArityFn<T, T> {
    return Rec<OneArityFn<T, T>>(
        n,
        x => x,
        (z, acc) => compose(f, acc));
}

/*
  Definition:
    A(0)(n) = s(n)
    A(s(m))(n) = iter(A(m))(n)(A(m)(1))
  Demo: https://gfredericks.com/things/arith/ackermann 
*/
function ackermann(x: number): OneArityFn<number, number> {
    return Rec<OneArityFn<number, number>>(
        x,
        Succ,
        (z, acc) => y => repeat(acc, y)(acc(Succ(Zero))));
}

// Tests
console.assert(add(1, 2) === 3);
console.assert(add(4, 2) === 6);
console.assert(add(0, 0) === 0);

console.assert(multiply(1, 1) === 1);
console.assert(multiply(0, 3) === 0);
console.assert(multiply(3, 2) === 6);

console.assert(exp(2, 3) === 8);
console.assert(exp(1, 3) === 1);
console.assert(exp(3, 2) === 9);

console.assert(double(2) === 4);
console.assert(double(1) === 2);
console.assert(double(3) === 6);

console.assert(pred(2) === 1);
console.assert(pred(20) === 19);
console.assert(pred(0) === 0);

console.assert(subtract(10, 1) === 9);
console.assert(subtract(4, 4) === 0);
console.assert(subtract(2, 3) === 0);

console.assert(isZero(0) === true);
console.assert(isZero(9) === false);
console.assert(isZero(1) === false);

console.assert(remainder(4, 2) === 0);
console.assert(remainder(5, 2) === 1);
console.assert(remainder(10, 6) === 4);
console.assert(remainder(1, 2) === 0);

console.assert(divide(2, 2) === 1);
console.assert(divide(4, 2) === 2);
console.assert(divide(6, 2) === 3);
console.assert(divide(12, 3) === 4);
console.assert(divide(11, 11) === 1);
console.assert(divide(10, 5) === 2);
console.assert(divide(99, 11) === 9);
console.assert(divide(99, 100) === 0);

console.assert(isPrime(13) === true);
console.assert(isPrime(12) === false);
console.assert(isPrime(1) === true);
console.assert(isPrime(2) === true);
console.assert(isPrime(3) === true);
console.assert(isPrime(5) === true);
console.assert(isPrime(9) === false);
console.assert(isPrime(127) === true);

console.assert(not(true) === false);
console.assert(not(false) === true);

console.assert(and(true, true) === true);
console.assert(and(true, false) === false);
console.assert(and(false, true) === false);
console.assert(and(false, false) === false);

console.assert(or(true, true) === true);
console.assert(or(true, false) === true);
console.assert(or(false, true) === true);
console.assert(or(false, false) === false);

console.assert(xor(true, true) === false);
console.assert(xor(true, false) === true);
console.assert(xor(false, true) === true);
console.assert(xor(false, false) === false);

console.assert(eq(1, 2) === false);
console.assert(eq(1, 1) === true);
console.assert(eq(0, 0) === true);

console.assert(gt(1, 2) === false);
console.assert(gt(1, 1) === false);
console.assert(gt(5, 2) === true);

console.assert(lt(1, 2) === true);
console.assert(lt(1, 1) === false);
console.assert(lt(5, 2) === false);

console.assert(gte(1, 2) === false);
console.assert(gte(1, 1) === true);
console.assert(gte(5, 2) === true);

console.assert(lte(1, 2) === true);
console.assert(lte(1, 1) === true);
console.assert(lte(5, 2) === false);

console.assert(ackermann(1)(1) === 3);
console.assert(ackermann(0)(2) === 3);
console.assert(ackermann(1)(0) === 2);
console.assert(ackermann(0)(1) === 2);
console.assert(ackermann(2)(2) === 7);
console.assert(ackermann(3)(3) === 61);


