package org.example.helper;

import lombok.Getter;
import org.example.bean.PrivateKey;
import org.example.bean.PublicKey;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

@Getter
public class KeyPair {

    List<BigInteger> primeList = new ArrayList<>();

    private String name;
    private PrivateKey privateKey;
    private PublicKey publicKey;

    private BigInteger r;

    public KeyPair(String name) {
        this.name = name;
        BigInteger p = BigInteger.ZERO;
        BigInteger q = BigInteger.ONE;

        while (p.compareTo(BigInteger.ONE) <= 0) {
            p = BigInteger.probablePrime(1024, new Random());
            q = defineQ(p.subtract(BigInteger.ONE));
        }

        BigInteger w = getRandom(q);

        BigInteger g = getG(p, q);
        BigInteger y = getY(g, w, p);
        publicKey = PublicKey.builder().y(y).p(p).q(q).g(g).build();
        privateKey = PrivateKey.builder().w(w).build();
    }

    public BigInteger calculateCheckResponse(BigInteger e) {
        return ((getPrivateKey().getW().multiply(e)).add(r)).mod(getPublicKey().getQ());
    }

    public BigInteger approveCheckResponse(BigInteger s, BigInteger e) {
        BigInteger m1 = getPublicKey().getG().modPow(s, getPublicKey().getP());
        BigInteger m2 = getPublicKey().getY().modPow(e, getPublicKey().getP());
        return (m1.multiply(m2)).mod(getPublicKey().getP());
    }

    public BigInteger code1ToSend() {
        r = this.getRandom(this.getPublicKey().getQ());
        return this.getPublicKey().calc(r);
    }


    private BigInteger getY(BigInteger g, BigInteger w, BigInteger p) {
        return g.modPow(w.multiply(bof(-1)), p);
    }

    public BigInteger getRandom(BigInteger max) {
        BigInteger randomNumber;
        Random random = new Random();
        do {
            randomNumber = new BigInteger(max.bitLength(), random);
        } while (randomNumber.compareTo(max) >= 0);
        return randomNumber;
    }

    private BigInteger bof(int val) {
        return BigInteger.valueOf(val);
    }

    private BigInteger getG(BigInteger p, BigInteger q) {
        BigInteger res = BigInteger.ONE;
        List<BigInteger> values = new ArrayList<>();
        boolean stop = false;
        long last = (new Date()).getTime();
        while (values.size() < 500 && !stop) {
            res = res.add(BigInteger.ONE);
            BigInteger val = res.modPow(q, p);

            if (val.equals(BigInteger.ONE)) {
                values.add(res);
                long now = (new Date()).getTime();
                if (now - last > 900) {
                    stop = true;
                }
                last = now;
            }
        }

        int ind = new Random().nextInt(values.size());
        return values.get(ind);
    }


    private BigInteger defineQ(BigInteger target) {
        List<BigInteger> list = new ArrayList<>();
        BigInteger divider = nextDivider(target);
        while (divider.compareTo(BigInteger.ONE) != 0) {
            target = target.divide(divider);
            list.add(target);
            divider = nextDivider(target);
        }

        int ind = new Random().nextInt(list.size());
        return list.get(ind);
    }

    public BigInteger nextDivider(BigInteger target) {
        String str = target.toString();
        int sumOfDigits = 0;
        int elevenSum = 0;
        for (int i = 0; i < str.length(); i++) {
            int current = Integer.parseInt(str.substring(i, i + 1));
            sumOfDigits += current;
            if (i % 2 == 0) {
                elevenSum += current;
            } else {
                elevenSum -= current;
            }
        }
        int lastDigit = Integer.valueOf(target.toString().substring(target.toString().length() - 1));

        if (target.toString().length() > 1) {

            int last2Digit = Integer.valueOf(target.toString().substring(target.toString().length() - 2));

            if (elevenSum % 11 == 0) {
                return BigInteger.valueOf(11);
            }

            if (last2Digit % 4 == 0) {
                return BigInteger.valueOf(4);
            }
        }

        if (lastDigit % 2 == 0) {
            return BigInteger.valueOf(2);
        }

        if (sumOfDigits % 3 == 0) {
            return BigInteger.valueOf(3);
        }

        if (lastDigit == 5) {
            return BigInteger.valueOf(5);
        }

        if (sumOfDigits % 9 == 0) {
            return BigInteger.valueOf(9);
        }

        if (lastDigit == 0) {
            return BigInteger.valueOf(10);
        }
        return BigInteger.ONE;
    }

}
