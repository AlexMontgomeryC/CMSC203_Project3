import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
public class CryptoManagerTestStudent {

	@Test
	public void testIsStringInBoundsTrue() {
        assertTrue(CryptoManager.isStringInBounds("JAVA IS FUN"));
    }

    @Test
    public void testIsStringInBoundsFalse() {
        assertFalse(CryptoManager.isStringInBounds("going forward|"));
    }

    @Test
    public void testCaesarEncryptionDecryption() {
        String text = "ZLODOREV";
        int key = 7;
        String encrypted = CryptoManager.caesarEncryption(text, key);
        String decrypted = CryptoManager.caesarDecryption(encrypted, key);
        assertEquals(text, decrypted);
    }

    @Test
    public void testVigenereEncryptionDecryption() {
        String text = "TESTING YOU";
        String key = "OKAY?";
        String encrypted = CryptoManager.vigenereEncryption(text, key);
        String decrypted = CryptoManager.vigenereDecryption(encrypted, key);
        assertEquals(text, decrypted);
    }

    @Test
    public void testPlayfairEncryptionDecryption_NoDuplicates() {
        String text = "MEET YOU TOMORROW";
        String key = "GENERATE";

        assertEquals(text.toUpperCase(),
            CryptoManager.playfairDecryption(
                CryptoManager.playfairEncryption(text, key), key));
    }
}
