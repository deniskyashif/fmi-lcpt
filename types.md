Задача (слайд 15/23 - Типово λ-смятане):  
Покажете най-общ тип за I, K, KI, K∗, S, SK, SKK, c0, c1, c2

I = λx.x  
I : s → s

K = λxy.x  
K : s → t → s

KI = (λxy.x)I → λy.I  
KI : t → s → s

K* = λxy.y  
K* : s → t → t

S = λxyz.xz(yz)  
z : s  
y : s → t  
x : s → t → u  
S : (s → t → u) → (s → t) → s → u  

SK = (λxyz.xz(yz))K  
   → λyz.Kz(yz)  
   → λyz.(λxy.x)z(yz)  
   → λyz.(λy.z)(yz)  
   → λyz.z  
z : s  
y : s → t  
SK : (s → t) → s → s  

SKK = (λxyz.xz(yz))KK  
    → (λyz.(λxy.x)z(yz))K  
    → (λyz.(λy.z)(yz))K  
    → (λyz.z)K  
    → λz.z  
SKK : s → s

c0 = λfx.x  
c0 : s → t → t  

c1 = λfx.fx  
c1 : (s → t) → s → t  

c2 = λfx.f(fx)  
c2 : (s → t) → s → t  
