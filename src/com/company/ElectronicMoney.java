package com.company;

import java.util.Random;

public class ElectronicMoney {
    private final int p, q, N, c, d;
    private final Random random;

    public ElectronicMoney(int P, int Q){
        p = P;
        q = Q;
        N = P*Q;
        int fi = (P-1)*(Q-1);
        random = new Random();
        c = getCoprimeWithNumber(fi);
        d = getInverseNumber(c, fi);
    }

    private int generateRandomNumber(int min, int max) {
        return random.nextInt(max - min + 1) + min;
    }

    private EuclidRow generalizedEuclidAlgorithm(int a, int b){
        if (b>a){
            a = a + b;
            b = a - b;
            a = a - b;
        }
        EuclidRow u = new EuclidRow(a, 1, 0);
        EuclidRow v = new EuclidRow(b, 0, 1);
        EuclidRow t = new EuclidRow(0,0,0);

        while (v.gcd!=0){
            int q = u.gcd / v.gcd;
            t.gcd = u.gcd % v.gcd;
            t.a = u.a - q * v.a;
            t.b = u.b - q * v.b;
            u.set(v);
            v.set(t);
        }
        return u;
    }

    private int getCoprimeWithNumber(int number){
        int coprime = generateRandomNumber(2, number-1);
        while (generalizedEuclidAlgorithm(number, coprime).gcd!=1){
            coprime = generateRandomNumber(2, number-1);
        }
        return coprime;
    }

    private int getInverseNumber(int number, int modNumber){
        int inverseNumber = generalizedEuclidAlgorithm(modNumber, number).b;
        if (inverseNumber<0)
            return inverseNumber + modNumber;
        return inverseNumber;
    }

    private int calculatePowerByMod(int base, int power, int mod) {
        int result = 1;
        while (power > 0) {
            if ((power & 1) == 1)
                result = (result * base) % mod;
            base = (base * base) % mod;
            power = power >> 1;
        }
        return result;
    }

    public void buy(){
        System.out.println("Bank's secret keys are: ");
        System.out.println("P = "+p+" Q = "+q+" c = "+c);
        System.out.println("Bank's open keys are: ");
        System.out.println("N = P*Q = "+N+" d = "+d);
        int n = generateRandomNumber(2, N-1);
        System.out.println("Buyer generated random number n = "+n);
        int r = getCoprimeWithNumber(N);
        System.out.println("Buyer generated random number r = "+r+" which is coprime with N = "+N);
        int rInv = getInverseNumber(r, N);
        System.out.println("Buyer calculates r^(-1) mod N = "+rInv);
        int rd = calculatePowerByMod(r, d, N);
        int n1 = n*rd % N;
        System.out.println("Buyer calculates n1 = (n*r^d) mod N = "+n+"*"+rd+" mod "+N+" = "+n1+" and sends the number to Bank");
        int s1 = calculatePowerByMod(n1, c, N);
        System.out.println("Bank calculates s1 = n1^c mod N = "+n1+"^"+c+" mod "+N+" = "+s1);
        System.out.println("Bank withdraws money from buyer's account and sends s1 number back to buyer");
        int s = s1 * rInv % N;
        System.out.println("Buyer calculates bank's signature s = s1 * r^(-1) mod N = "+s1+"*"+rInv+" mod N = "+s);
        System.out.println("Buyer shapes banknote <n, s> = <"+n+", "+s+">");
        System.out.println("Buyer presents banknote <"+n+", "+s+"> to shop. Shop sends the banknote to bank for checking its authenticity");
        int check = calculatePowerByMod(s, d, N);
        System.out.println("Bank calculates s^d mod N = "+check+". s^d mod N = n^(c*d) mod N = n = "+n);
        if (n == check)
            System.out.println("s^d mod N = n. Banknote is authentic. Bank credits moneys to shop's account. Bank adds the banknote to list so it can't be used anymore");
        else
            System.out.println("s^d mod N != n. Banknote is not authentic.");
    }
}
