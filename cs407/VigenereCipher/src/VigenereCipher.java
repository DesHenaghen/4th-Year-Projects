import java.io.IOException;

public class VigenereCipher {

    public static void main(String[] args) throws IOException {
        if (args.length == 3) {
            VigenereCipher cipher = new VigenereCipher();

            // Extract Arguments
            Boolean encrypt = Boolean.valueOf(args[0]);
            String text = args[1].toLowerCase();
            String key = args[2].toLowerCase();

            // Repeat key to the same length as the plain text
            StringBuilder builder = new StringBuilder(text.length() + key.length() - 1);
            while (builder.length() < text.length()) {
                builder.append(key);
            }
            builder.setLength(text.length());
            String paddedKey = builder.toString();

            if (encrypt) {
                // Encrypt plain text
                cipher.encryptPlainText(text, paddedKey);
            } else {
                //Decrypt cipher text
                cipher.decryptCipherText(text, paddedKey);
            }

        } else {
            System.out.println("Please provide true for encrypt, false for decrypt then the text and then the key for the cipher.");
        }
    }

    private void decryptCipherText(String cipherText, String paddedKey) {
        System.out.println("Cipher Text: " + cipherText);
        System.out.println("Key: " + paddedKey);

        StringBuilder result = new StringBuilder(cipherText.length());

        // Perform Vigenere Cipher
        for (int i = 0; i < cipherText.length(); i++){
            // Extract characters at index
            char p = cipherText.charAt(i);
            char k = paddedKey.charAt(i);

            // Convert key character to integer
            Integer kValue = Character.getNumericValue(k) - 10;
            // Increment plain text char by that integer
            p -= kValue;

            // If plain text char is no longer a letter, subtract 26 to loop back through the alphabet
            if (Character.getNumericValue(p) == -1 || Character.isUpperCase(p)) {
                p += 26;
            }

            result.append(p);
        }

        System.out.println("Plain Text: " + result);
    }

    private void encryptPlainText(String plainText, String paddedKey) {
        System.out.println("Plain Text: " + plainText);
        System.out.println("Key: " + paddedKey);

        StringBuilder result = new StringBuilder(plainText.length());

        // Perform Vigenere Cipher
        for (int i = 0; i < plainText.length(); i++){
            // Extract characters at index
            char p = plainText.charAt(i);
            char k = paddedKey.charAt(i);

            // Convert key character to integer
            Integer kValue = Character.getNumericValue(k) - 10;
            // Increment plain text char by that integer
            p += kValue;

            // If plain text char is no longer a letter, subtract 26 to loop back through the alphabet
            if (Character.getNumericValue(p) == -1) {
                p -= 26;
            }

            result.append(p);
        }

        System.out.println("Cipher Text: " + result);
    }
}