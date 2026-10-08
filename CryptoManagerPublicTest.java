//package BobsCircus;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class CryptoManagerPublicTest {

    @Test
    public void testIsStringInBoundsTrue() {
        assertTrue(CryptoManager.isStringInBounds("HELLO WORLD"));
    }

    @Test
    public void testIsStringInBoundsFalse() {
        assertFalse(CryptoManager.isStringInBounds("hello"));
    }

    @Test
    public void testCaesarEncryptionDecryption() {
        String text = "HELLO";
        int key = 3;
        String encrypted = CryptoManager.caesarEncryption(text, key);
        String decrypted = CryptoManager.caesarDecryption(encrypted, key);
        assertEquals(text, decrypted);
    }

    @Test
    public void testVigenereEncryptionDecryption() {
        String text = "TEST MESSAGE";
        String key = "KEY";
        String encrypted = CryptoManager.vigenereEncryption(text, key);
        String decrypted = CryptoManager.vigenereDecryption(encrypted, key);
        assertEquals(text, decrypted);
    }

    @Test
    public void testPlayfairEncryptionDecryption_NoDuplicates() {
        String text = "JAVA FUN";
        String key = "CIPHER";

        assertEquals(text.toUpperCase(),
            CryptoManager.playfairDecryption(
                CryptoManager.playfairEncryption(text, key), key));
    }
}
