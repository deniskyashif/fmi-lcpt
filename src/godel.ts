// Gödel’s System T in TypeScript

const IsZero = (x: number): boolean => x === 0;
const Succ = (x: number) => x + 1;

function Cases<T>(cond: boolean, a: T, b: T): T {
    return cond ? a : b;
}

function Rec<T>(sn: number, s: T, t: (z: number, w: T) => T) {
    return IsZero(sn) ? s : t(sn - 1, Rec(sn - 1, s, t));
}

function add(x: number, y: number) {
    return Rec<number>(x, y, (z, w) => Succ(w));
}

function multiply(x: number, y: number) {
    return Rec<number>(y, 0, (z, w) => add(x, w));
}

function exp(x: number, y: number) {
    return Rec<number>(y, 1, (z, w) => multiply(x, w));
}

function double(x: number) {
    return Rec<number>(x, 0, (z, w) => Succ(Succ(w)));
}

function pred(x: number) {
    return Rec<number>(x, 0, (z, w) => z);
}

function subtract(x: number, y: number) {
    return Rec<number>(y, x, (z, w) => pred(w));
}

function not(x: boolean) {
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

function eq(x: number, y: number) {
    return and(
        IsZero(subtract(x, y)),
        IsZero(subtract(y, x)));
}

function gt(x: number, y: number) {
    return not(IsZero(subtract(x, y)));
}

function lt(x: number, y: number) {
    return not(IsZero(subtract(y, x)));
}

function gte(x: number, y: number) {
    return or(eq(x, y), gt(x, y));
}

function lte(x: number, y: number) {
    return or(eq(x, y), lt(x, y));
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
