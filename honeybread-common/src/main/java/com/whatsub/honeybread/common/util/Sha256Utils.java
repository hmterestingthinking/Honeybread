package com.whatsub.honeybread.common.util;

import com.whatsub.honeybread.core.infra.errors.ErrorCode;
import com.whatsub.honeybread.core.infra.exception.HoneyBreadException;
import lombok.experimental.UtilityClass;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;

@UtilityClass
public final class Sha256Utils {

    public String generate(String salt) {
        byte[] raw = new byte[16];
        ThreadLocalRandom.current().nextBytes(raw);

        MessageDigest md;
        try {
            md = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            throw new HoneyBreadException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
        md.update(raw);
        md.update(salt.getBytes());
        return String.format("%064x", new BigInteger(1, md.digest()));
    }

    public String generate(Object... saltItem) {
        StringBuilder builder = new StringBuilder();
        Arrays.stream(saltItem).forEach(item -> builder.append("_").append(item));
        return generate(builder.toString());
    }

}
