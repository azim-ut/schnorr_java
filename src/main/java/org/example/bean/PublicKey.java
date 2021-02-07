package org.example.bean;

import lombok.Builder;
import lombok.Getter;
import org.apache.commons.codec.digest.DigestUtils;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Builder
@Getter
public class PublicKey {
    private BigInteger p;
    private BigInteger q;
    private BigInteger g;
    private BigInteger y;

    public String encoded(String msg) {
        byte[] bytes = hash(msg).getBytes();
        byte[] res = new byte[bytes.length];
        for (int i = 0; i < bytes.length; i++) {
            BigInteger b = BigInteger.valueOf(bytes[i]);
            res[i] = (byte) calc(b).intValue();
        }
        return new String(bytes, StandardCharsets.UTF_8);
    }

    private String hash(String msg) {
        return DigestUtils.sha256Hex(msg);
    }

    public BigInteger calc(BigInteger r) {
        return g.modPow(r, p);
    }
}
