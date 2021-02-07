package org.example.bean;

import lombok.Builder;
import lombok.Getter;

import java.math.BigInteger;

@Builder
@Getter
public class PrivateKey {
    private BigInteger w;
}
