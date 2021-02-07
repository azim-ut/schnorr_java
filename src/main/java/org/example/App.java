package org.example;

import org.example.helper.KeyPair;

import java.math.BigInteger;

/**
 * Hello world!
 */
public class App {
    public static void main(String[] args) {
        KeyPair aliceKeys = new KeyPair("Alice");
        BigInteger encodedMsg = aliceKeys.code1ToSend();
        System.out.println("Alice send: " + encodedMsg);
        BigInteger bobCheckMessage = BigInteger.valueOf(124343249);
        System.out.println("Bob's answer: " + bobCheckMessage);
        BigInteger aliceCalculateResponse = aliceKeys.calculateCheckResponse(bobCheckMessage);
        System.out.println("Alice back: " + aliceCalculateResponse);
        BigInteger bobCheckResponse = aliceKeys.approveCheckResponse(aliceCalculateResponse, bobCheckMessage);
        System.out.println("Bob receive: " + bobCheckResponse);
        System.out.println("Bob compare: " + ((encodedMsg.compareTo(bobCheckResponse) == 0) ? "Ok" : "Error"));
    }
}
