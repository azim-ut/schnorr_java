package org.example;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Hello world!
 */
public class App {
    public static void main(String[] args) {
        Integer p = 48731;
        System.out.println("p: " + p);
        App app = new App();
        Integer q = app.primeDigit(p - 1);

        System.out.println("q: " + q);
        int g = app.getG(p, q);
        System.out.println("g: " + g);
        int w = app.getRandom(q);
        System.out.println("w: " + w);
        int y = app.getY(g, w, p);
        System.out.println("y: " + y);
        int r = app.getRandom(10000);
        System.out.println("Message: " + r);
        int x = app.aliceSend(g, p, r);
        System.out.println("Alice send: " + x);
        List<Integer> packet = new ArrayList<>() {{
            add(p);
            add(q);
            add(g);
            add(y);
        }};
        int e = 129;
        System.out.println("Bob send: " + e);
        int s = app.aliceCalculate(r, w, e, q);
        System.out.println("Alice calc: " + s);
        int z = app.bobCheck(g, p, y, s, e);
        System.out.println("Bob check: " + z);
        System.out.print(z + " == " + x + " ");
        System.out.println((z == x) ? "OK" : "ERROR");
    }

    private int aliceSend(int g, int p, int msg) {
        return BigInteger.valueOf(g).modPow(BigInteger.valueOf(msg), BigInteger.valueOf(p)).intValue();
    }

    private int bobCheck(int g, int p, int y, int s, int e) {
        BigInteger m1 = bof(g).pow(s);
        BigInteger m2 = bof(y).pow(e);
        return (m1.multiply(m2)).mod(bof(p)).intValue();
    }

    private int aliceCalculate(int r, int w, int e, int q) {
        return bof(w).multiply(bof(e)).add(bof(r)).mod(bof(q)).intValue();
    }

    private BigInteger bof(int val) {
        return BigInteger.valueOf(val);
    }

    private int getY(int g, int w, int p) {
        BigInteger G = BigInteger.valueOf(g);
        BigInteger P = BigInteger.valueOf(p);
        BigInteger res = G.modPow(BigInteger.valueOf(w * -1), P);
        return res.intValue();
    }
    
    private int getRandom(int max) {
        return ThreadLocalRandom.current().nextInt(2, max);
    }

    private int getG(int p, int q) {
        BigInteger P = BigInteger.valueOf(p);
        BigInteger res = BigInteger.ONE;
        List<Integer> values = new ArrayList<>();
        do {
            res = res.add(BigInteger.ONE);
            BigInteger val = res.pow(q).mod(P);

            if (val.equals(BigInteger.ONE)) {
                values.add(res.intValue());
            }
        } while (values.size() < 1000);
        int ind = getRandom(values.size());
        return values.get(ind);
    }

    private int primeDigit(int val) {
        int res = 0;
        int dec = val;
        while (dec-- > 0) {
            if (isPrimeDigit(dec) && val % dec == 0) {
                return dec;
            }
        }
        return res;
    }

    private boolean isPrimeDigit(int val) {
        //TODO divisibility rules
        for (int i = 2; i < val - 1; i++) {
            if (val % i == 0) {
                return false;
            }
        }
        return true;
    }
}
