import java.util.ArrayList;

/**
 * This is a utility class that encrypts and decrypts a phrase using three
 * different approaches. 
 * 
 * The first approach is called the Vigenere Cipher.Vigenere encryption 
 * is a method of encrypting alphabetic text based on the letters of a keyword.
 * 
 * The second approach is Playfair Cipher. It encrypts two letters (a digraph) 
 * at a time instead of just one.
 * 
 * The third approach is Caesar Cipher. It is a simple replacement cypher. 
 * 
 * @author Huseyin Aygun
 * @version 8/3/2025
 */
/*
 * Class: CMSC203 CRN21410
 * Instructor: Huseyin Aygun
 * Description: (CryptoManager is a class that encrypts and decrypts text.)
 * Due: 10/11/2026
 * Platform/compiler: Eclipse IDE
 * I pledge that I have completed the programming  assignment independently. 
*  I have not copied the code from a student or any source. 
*  I have not given my code to any student.
*  Print your Name here: Alexander Zlodorev
*/

public class CryptoManager { 
	private static final char LOWER_RANGE = ' ';
    private static final char UPPER_RANGE = '_';
    private static final int RANGE = UPPER_RANGE - LOWER_RANGE + 1;
    // Use 64-character matrix (8X8) for Playfair cipher  
    private static final String ALPHABET64 = " ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!\"#$%&'()*+,-./:;<=>?@[\\]^_";
    //The following fields are added to track whether the padding in playfair cipher was added or not and where they were added
    static boolean paddingAdded = false, middlePaddingAdded =false;
	private static int trailingPadsAdded=0, middlePadsAdded=0;
    public static boolean isStringInBounds(String plainText) {
        for (int i = 0; i < plainText.length(); i++) {
            if (!(plainText.charAt(i) >= LOWER_RANGE && plainText.charAt(i) <= UPPER_RANGE)) {
                return false;
            }
        }
        return true;
    }
	/**
	 * Vigenere Cipher is a method of encrypting alphabetic text 
	 * based on the letters of a keyword. It works as below:
	 * 		Choose a keyword (e.g., KEY).
	 * 		Repeat the keyword to match the length of the plaintext.
	 * 		Each letter in the plaintext is shifted by the position of the 
	 * 		corresponding letter in the keyword (A = 0, B = 1, ..., Z = 25).
	 */   
    public static String vigenereEncryption(String plaintext, String key) {
        if(!isStringInBounds(plaintext)) {
            return "The selected string is not in bounds, Try again.";
        }
        key = key.replace(" ", "");
        String encryptedText = "";
        while (plaintext.length() > key.length()) {
            key += key;
        }
        
        for(int plaintextIndex = 0, keyIndex = 0; plaintextIndex < plaintext.length(); plaintextIndex++) {
            char plainChar = plaintext.charAt(plaintextIndex);
            
            if(plainChar != ' ') {
            	encryptedText += ALPHABET64.charAt((ALPHABET64.indexOf(plainChar) + ALPHABET64.indexOf(key.charAt(keyIndex))) % 64);
                keyIndex++;
            } else {
                encryptedText += " ";
            }
        }
        return encryptedText;
    }
	//Vigenere Decryption
    public static String vigenereDecryption(String encryptedText, String key) {
        String decryptedText = "";
        key = key.replace(" ", "");
        while(key.length() < encryptedText.length()) {
            key += key;
        }
        
        for(int encryptedTIndex = 0, keyIndex = 0; encryptedTIndex < encryptedText.length(); encryptedTIndex++) {
            char cipherChar = encryptedText.charAt(encryptedTIndex);
            
            if(cipherChar != ' ') {
                decryptedText += ALPHABET64.charAt(((ALPHABET64.indexOf(cipherChar) - ALPHABET64.indexOf(key.charAt(keyIndex))) % 64 + 64) % 64);
                keyIndex++;
            } else {
                decryptedText += " ";
            }
        }
        return decryptedText;
    }
	/**
	 * Playfair Cipher encrypts two letters at a time instead of just one.
	 * It works as follows:
	 * A matrix (8X8 in our case) is built using a keyword
	 * Plaintext is split into letter pairs (e.g., ME ET YO UR).
	 * Encryption rules depend on the positions of the letters in the matrix:
	 *     Same row: replace each letter with the one to its right.
	 *     Same column: replace each with the one below.
	 *     Rectangle: replace each letter with the one in its own row but in the column of the other letter in the pair.
	 */    
	public static String playfairEncryption(String plaintext, String key) {
		if(!isStringInBounds(plaintext)) {
	    	return "The selected string is not in bounds, Try again.";
	    }
		key = key.replace(" ", "");
		String encryptedText = "", revisedAlphabet = "";
		//Revise the alphabet so that it has no duplicates, and the first characters should be from the key.
		for (int i = 0; i < ALPHABET64.length(); i++) {
		    char ch = ALPHABET64.charAt(i);
		    if (!key.contains(String.valueOf(ch))) {
		    	revisedAlphabet += ch;
		    }
		}
		//Revise the key.
		String keyWithoutDuplicates = "";
		for (int i = 0; i < key.length(); i++) {
			char ch = key.charAt(i);
		    if (keyWithoutDuplicates.indexOf(ch) == -1) {
		    	keyWithoutDuplicates += ch;
		    }
		}
		//Create the matrix of encrypttion and the dygraphs.
		char[][] matrix = generateMatrix(revisedAlphabet, keyWithoutDuplicates);
		ArrayList<String> digraphsList = new ArrayList<>();
		//Clean up the flag variables after the previous ciphers
		middlePaddingAdded = false;
		middlePadsAdded = 0;
		paddingAdded = false;
		trailingPadsAdded = 0;
		int plainTIndex = 0;
		//The following while loop steps through the plaintext and acts according to the scheme:
		//It reads a character, determines whether there are more characters to read.
		// If there are characters to read, it reads the second one and compares it to the first.
		// They are the same? If the first char is 'X' add the first char and 'Q' to the dygraph. Otherwise, add the first char and 'X' to the dygraph.
		//The first and second chars are not the same? add both to dygraphs.
		//If there are no more chars to read after the first one, the simply add it and 'X' to the dygraph.
		while (plainTIndex < plaintext.length()) {
		    char first = plaintext.charAt(plainTIndex);
		    char second;
		    if (plainTIndex + 1 < plaintext.length()) {
		    	second = plaintext.charAt(plainTIndex + 1);
		        if (first == second) {
		        	char filler = (first == 'X') ? 'Q' : 'X';
		            digraphsList.add("" + first + filler);
		            middlePadsAdded++;
		            middlePaddingAdded = true;
		            plainTIndex += 1;
		        }
		        else {
		            digraphsList.add("" + first + second);
		            plainTIndex += 2;
		        }
		    }
		    else {
		    	digraphsList.add("" + first + 'X');
		        trailingPadsAdded++;
		        paddingAdded = true;
		        plainTIndex += 1;
		    }
		}
		//This converts ArrayList to a String[] that will be processed by modifyText() and encrypted.
		String[] plaintextDyphr = digraphsList.toArray(new String[0]);

		encryptedText = modifyText(plaintextDyphr, matrix, +1);
		return encryptedText;
	}
	//Playfair decryption
	public static String playfairDecryption(String encryptedText, String key) {
		String decryptedText = "", revisedAlphabet = "";
		//This eliminates the spaces in the key.
		key = key.replace(" ", "");
		int index;
		//The following loops revise the alphabet and the key so that both would not contain repetitive letters or symbols.
		for (index = 0; index < ALPHABET64.length(); index++) {
		    char ch = ALPHABET64.charAt(index);
		    if (!key.contains(String.valueOf(ch))) {
		    	revisedAlphabet += ch;
		    }
		}
		String keyWithoutDuplicates = "";

		for (index = 0; index < key.length(); index++) {
			char ch = key.charAt(index);
			if (keyWithoutDuplicates.indexOf(ch) == -1) {
				keyWithoutDuplicates += ch;
		    }
		}
		//This creates the decryption matrix.
		char[][] matrix = generateMatrix(revisedAlphabet, keyWithoutDuplicates);
		//The following statement and loop create the dyphragms of the encrypted text.
		String[] cyphertextDyphr = new String[encryptedText.length() / 2];
		for (index = 0; index < encryptedText.length(); index += 2) {
			cyphertextDyphr[index / 2] = encryptedText.substring(index, index + 2);
		}
		//This helps determine whether padding was added or not.
		paddingAdded = (encryptedText.length() % 2 != 0 || cyphertextDyphr.length * 2 > encryptedText.length());
		//The following two statements decipher and strip the deciphered message of its padding.
		decryptedText = modifyText(cyphertextDyphr, matrix, -1);
		return removePlayfairPadding(decryptedText);
	}
    /**
     * Caesar Cipher is a simple substitution cipher that replaces each letter in a message 
     * with a letter some fixed number of positions down the alphabet. 
     * For example, with a shift of 3, 'A' would become 'D', 'B' would become 'E', and so on.
     */    
 
	public static String caesarEncryption(String plaintext, int key) {
		if(!isStringInBounds(plaintext)) {
    		return "The selected string is not in bounds, Try again.";
    	}

	    String encryptedText = "";
	    for(int index = 0; index < plaintext.length(); index++) {
	        int plainIndex = ALPHABET64.indexOf(plaintext.charAt(index));
	        encryptedText += ALPHABET64.charAt(((plainIndex + key)%64));
	    }
	    
	    return encryptedText;
	}
	    // Caesar Decryption
   public static String caesarDecryption(String encryptedText, int key) {
	    
	    String decryptedText = "";
	    for(int index = 0; index < encryptedText.length(); index++) {
	        int encryptedIndex = ALPHABET64.indexOf(encryptedText.charAt(index));
	        decryptedText += ALPHABET64.charAt(((encryptedIndex - key) % 64 +64)%64);
	    }
	    
	    return decryptedText;
	} 
	/**
	 * This method was designed by the student and helps break up the playfair encryption and decryption logic.
	 * @param textDyphr Dyphragms of an encrypted or plain text
	 * @param matrix The encryption and decryption matrix
	 * @param changeNum Determines whether to encrypt or decrypt the text
	 * @return encrypted or decrypted text, depending on the value of changeNum
	 */
	public static String modifyText(String[] textDyphr, char[][] matrix, int changeNum) {
		String modifiedText = "";
		for (int index = 0; index < textDyphr.length; index++) {
			if (textDyphr[index] == null) {
				continue; 
			}
			if(textDyphr[index].isEmpty()) {
			    continue;
			}
			//The following statements determine the characters that should be searched for and define the variables that will hold their positions in the matrix.
			char seek1 = textDyphr[index].charAt(0);
			char seek2 = textDyphr[index].charAt(1);
			    		   
			int row1 = -1, col1 = -1;
			int row2 = -1, col2 = -1;
			//Thw following nested loops go over the matrix and seek for char1 and char2. Once found, these loops record the respective character's position.
			for (int row = 0; row < matrix.length; row++) {
			    for (int column = 0; column < matrix[row].length; column++) {
			    	if (matrix[row][column] == seek1) {
			    		row1 = row;
			    		col1 = column;
			    	}
			    	if (matrix[row][column] == seek2) {
			    		row2 = row;
			    		col2 = column;
			    	}
			    }
			}
			//The following if-elseif-else statements encipher and decipher text according to the scheme:
			//If both chars are 'X', add them both to modifiedText
			//If rows are equal, apply the rows rule.
			//if columns are equal, apply the columns rule.
			//if nothing works, then apply the rectangle rule.
			if(seek1 == 'X' && seek2 == 'X') {
				modifiedText += ""+seek1 + seek2;
			}
			else if (row1 == row2) {
			   modifiedText += "" + matrix[row1][(col1 +changeNum+matrix.length) % matrix.length] + 
					   matrix[row2][(col2 +changeNum+matrix.length) % matrix.length];
			} 
			else if (col1 == col2) {
			    modifiedText += "" + matrix[(row1 +changeNum+matrix.length) % matrix.length][col1] +
			    	matrix[(row2 +changeNum+matrix.length) % matrix.length][col2];
			}
			else {
			    modifiedText += "" +matrix[row1][col2]+ matrix[row2][col1];
			}
		}
		return modifiedText;
	}
	/**
	 * This method was designed by the student and its purpose is to generate the matrix used to encode and decode text.
	 * @param alphabet The list of characters of which the matrix will be made of
	 * @param key The key which is used to generate matrix and avoid repetitive characters.
	 * @return generated matrix
	 */
	public static char[][] generateMatrix(String alphabet, String key){
		char[][] matrix = new char[8][8]; // Defines the matrix and necessary count variables.
		int keyIndex =0, alphaIndex =0;
		//The following loop creates the matrix.
		for (int row = 0; row < 8; row++) {
			for (int col = 0; col < 8; col++) {
				if (keyIndex < key.length()) {
					matrix[row][col] = key.charAt(keyIndex);
				} 
				else if (alphaIndex < alphabet.length()) {
					matrix[row][col] = alphabet.charAt(alphaIndex);
					alphaIndex++;
				}
				else {
					matrix[row][col] = '\0';
				}
				keyIndex++;
			}
		}
		return matrix;
	}
	/**
	 * This method was designed by the student. Its purpose is to strip the provided decrypted of its padding letters.
	 * @param decryptedText
	 * @return decrypted text stripped of padded letters
	 */
	public static String removePlayfairPadding(String decryptedText) {
		if (decryptedText == null || decryptedText.isEmpty()) {
			return decryptedText;
		}

		String cleanedText = "";
		int index = 0;
		//The following loop strips the provided text of its padding.
		while (index < decryptedText.length()) {
			if (index + 1 >= decryptedText.length()) {
				cleanedText += decryptedText.charAt(index);
				break;
			}

			char char1 = decryptedText.charAt(index);
			char char2 = decryptedText.charAt(index + 1);
				        
			if (middlePaddingAdded && middlePadsAdded > 0) {
				if ((char2 == 'X' || char2 == 'Q') && index + 2 < decryptedText.length() && char1 == decryptedText.charAt(index + 2)) {
				     	cleanedText += char1;
				        middlePadsAdded--;
				        index += 2;
				        continue;
				}
				else if ((char1 == 'X' || char1 == 'Q') && index > 0 && decryptedText.charAt(index - 1) == char2) {
					cleanedText += char2;
				    middlePadsAdded--;
				    index += 2;
				    continue;
				    }
			}

			cleanedText += "" + char1 + char2;
			index += 2;
		}
		String result = cleanedText;
		if (trailingPadsAdded > 0) {
			if (result.length() >= trailingPadsAdded) {
				result = result.substring(0, result.length() - trailingPadsAdded);
			}
		}
		trailingPadsAdded = 0;
		middlePadsAdded =0;
		middlePaddingAdded =false;

		return result;
	}
    
    
  
}

