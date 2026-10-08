package BobsCircus;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class CryptoManagerGFATest {

    /* =======================
       isStringInBounds Tests
       ======================= */

    @Test
    public void testIsStringInBoundsLowerBoundary() {
        assertTrue(CryptoManager.isStringInBounds(" "));
    }

    @Test
    public void testIsStringInBoundsUpperBoundary() {
        assertTrue(CryptoManager.isStringInBounds("_"));
    }

    @Test
    public void testIsStringInBoundsBelowLowerBoundary() {
        assertFalse(CryptoManager.isStringInBounds("\n"));
    }

    @Test
    public void testIsStringInBoundsAboveUpperBoundary() {
        assertFalse(CryptoManager.isStringInBounds("`"));
    }

    /* =======================
       Caesar Cipher Tests
       ======================= */

    @Test
    public void testCaesarEncryptionDecryption() {
        String text = "ABC XYZ";
        int key = 4;
        assertEquals(text,
                CryptoManager.caesarDecryption(
                        CryptoManager.caesarEncryption(text, key), key));
    }

    @Test
    public void testCaesarUpperBoundaryWrap() {
        assertEquals(" ",
                CryptoManager.caesarEncryption("_", 1));
    }

    @Test
    public void testCaesarLowerBoundaryWrap() {
        assertEquals("_",
                CryptoManager.caesarDecryption(" ", 1));
    }

    @Test
    public void testCaesarZeroKey() {
        String text = "BOUNDARY";
        assertEquals(text, CryptoManager.caesarEncryption(text, 0));
    }

    /* =======================
       Vigenere Cipher Tests
       ======================= */

    @Test
    public void testVigenereEncryptionDecryption() {
        String text = "SECURE DATA";
        String key = "KEY";
        assertEquals(text,
                CryptoManager.vigenereDecryption(
                        CryptoManager.vigenereEncryption(text, key), key));
    }

    @Test
    public void testVigenereLowerBoundaryChar() {
        String text = " ";
        String key = "A";
        assertEquals(text,
                CryptoManager.vigenereDecryption(
                        CryptoManager.vigenereEncryption(text, key), key));
    }

    @Test
    public void testVigenereUpperBoundaryChar() {
        String text = "_";
        String key = "Z";
        assertEquals(text,
                CryptoManager.vigenereDecryption(
                        CryptoManager.vigenereEncryption(text, key), key));
    }

    @Test
    public void testVigenereSingleCharacter() {
        String text = "A";
        String key = "K";
        assertEquals(text,
                CryptoManager.vigenereDecryption(
                        CryptoManager.vigenereEncryption(text, key), key));
    }

    /* =======================
       Playfair Cipher Tests
       ======================= */

    @Test
    public void testPlayfairEncryptionDecryption() {
        String text = "ENCRYPT THIS";
        String key = "PLAYFAIR";
        assertEquals(text.toUpperCase(),
                CryptoManager.playfairDecryption(
                        CryptoManager.playfairEncryption(text, key), key));
    }

    @Test
    public void testPlayfairDuplicateLetters() {
        String text = "BALLOON";
        String key = "KEYWORD";
        assertEquals(text,
                CryptoManager.playfairDecryption(
                        CryptoManager.playfairEncryption(text, key), key));
    }

    @Test
    public void testPlayfairOddLength() {
        String text = "CAT";
        String key = "SECRET";
        assertEquals(text,
                CryptoManager.playfairDecryption(
                        CryptoManager.playfairEncryption(text, key), key));
    }

    @Test
    public void testPlayfairSingleCharacter() {
        String text = "A";
        String key = "KEY";
        assertEquals(text,
                CryptoManager.playfairDecryption(
                        CryptoManager.playfairEncryption(text, key), key));
    }

    @Test
    public void testPlayfairSymbolsMixed() {
        String text = "!@#";
        String key = "KEY";

        String encrypted = CryptoManager.playfairEncryption(text, key);
        String decrypted = CryptoManager.playfairDecryption(encrypted, key);

        assertEquals(text, decrypted);
    }

    @Test
    public void testPlayfairMinimalValidDecryption() {
        String text = "AB";
        String key = "KEY";

        assertEquals(text,
            CryptoManager.playfairDecryption(
                CryptoManager.playfairEncryption(text, key), key));
    }
}
