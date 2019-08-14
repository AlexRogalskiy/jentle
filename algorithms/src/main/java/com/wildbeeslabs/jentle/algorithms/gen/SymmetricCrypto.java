package com.wildbeeslabs.jentle.algorithms.gen;

import java.security.GeneralSecurityException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Random;
import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;

import static javax.crypto.Cipher.*;

final class SymmetricCrypto {
    private static final int KEY_LENGTH = 128;

    private final Cipher aes;
    private final Random random = new SecureRandom();

    public SymmetricCrypto() {
        try {
            aes = Cipher.getInstance("AES/CBC/PKCS5Padding");
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            throw new AssertionError(e);
        }
    }

    EncryptionResult encrypt(final byte[] plaintext, final Key key)
        throws GeneralSecurityException {

        byte[] iv = initializationVector();
        aes.init(ENCRYPT_MODE, key, new IvParameterSpec(iv));

        byte[] ciphertext = aes.doFinal(plaintext);

        return new EncryptionResult(ciphertext, iv);
    }

    byte[] decrypt(final EncryptionResult enciphered, final Key key)
        throws GeneralSecurityException {

        aes.init(DECRYPT_MODE, key, new IvParameterSpec(enciphered.iv));
        return aes.doFinal(enciphered.ciphertext);
    }

    private byte[] initializationVector() {
        byte[] iv = new byte[KEY_LENGTH / 8];
        random.nextBytes(iv);
        return iv;
    }

    static class EncryptionResult {
        final byte[] ciphertext;
        final byte[] iv;

        EncryptionResult(byte[] ciphertext, byte[] iv) {
            this.ciphertext = ciphertext;
            this.iv = iv;
        }
    }
}
