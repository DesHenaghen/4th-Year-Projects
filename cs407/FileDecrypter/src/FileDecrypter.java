import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.*;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;

public class FileDecrypter {
    public static void main (String[] args) {
        FileDecrypter fd = new FileDecrypter();

        // Get Public Key
        byte[] pk_bytes = fd.getPkBytes();
        PublicKey pk = null;
        try {
            pk = KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(pk_bytes));
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Get encrypted file
        byte[] file_bytes = fd.getEncryptedFileBytes();

        // Create cipher and decrypt file
        try {
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE, pk);
            System.out.println(new String(cipher.doFinal(file_bytes)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public byte[] getPkBytes() {
        return getFileBytes("public.key");
    }

    public byte[] getEncryptedFileBytes() {
        return getFileBytes("encrypted.txt");
    }

    private byte[] getFileBytes(String filename) {
        File file = new File(filename);
        FileInputStream fis =  null;
        try {
            fis = new FileInputStream(file);

            byte file_bytes[] = new byte[(int)file.length()];
            fis.read(file_bytes);

            return file_bytes;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (fis != null)
                    fis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
