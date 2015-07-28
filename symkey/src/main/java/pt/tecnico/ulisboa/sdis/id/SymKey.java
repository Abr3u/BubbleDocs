package pt.tecnico.ulisboa.sdis.id;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;


/**
 * 	Program to read and write symmetric key file
 */
public class SymKey {

	private static String _password;

	public SymKey(String pass){
		this._password=pass;
	}
	
	public static byte[] iV = code("random".getBytes());
	

	public static void write(String keyPath, byte[] pass) throws Exception {
		// get a private key
		File f = new File(keyPath); 
		if(!f.exists()){
			f.createNewFile();
		}
		FileOutputStream fos = new FileOutputStream(keyPath);
		fos.write(code(pass));
		fos.close();
	}

	public static Key read(String keyPath) throws Exception {
		
		File f = new File(keyPath); 
		if(!f.exists()){
			System.out.println("No such File!!");
		}
		
		FileInputStream fis = new FileInputStream(keyPath);
		byte[] encoded = new byte[fis.available()];
		fis.read(encoded);
		fis.close();	 
		SecretKeySpec spec = new SecretKeySpec(encoded, "AES");
		return spec;
	}
	
	public static byte[] code(byte[] encoded){
		 	MessageDigest md;
		 	byte byteData[] = null;
			try {
				md = MessageDigest.getInstance("SHA-256");
			md.update(encoded);
			byteData = md.digest();
			byteData = Arrays.copyOf(byteData, 16);
			} catch (NoSuchAlgorithmException e) {
				System.out.println("Impossible error");
			}
			return byteData;
	}
	
	public static byte[] encrypt(byte[] toEncrypt, Key keyToUse) throws NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException, InvalidAlgorithmParameterException{
		 // get a AES cipher object and print the provider
		IvParameterSpec ivSpec = new IvParameterSpec(iV);
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");

        // encrypt using the key and the plaintext
       
        cipher.init(Cipher.ENCRYPT_MODE, keyToUse, ivSpec);
        byte[] cipherBytes = cipher.doFinal(toEncrypt);
        return cipherBytes;
	}
	
	public static byte[] decrypt(byte[] toDecrypt, Key keyToUse) throws IllegalBlockSizeException, BadPaddingException, InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidAlgorithmParameterException{

		// get a AES cipher object and print the provider
		IvParameterSpec ivSpec = new IvParameterSpec(iV);
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		// decrypt the ciphertext using the same key
       cipher.init(Cipher.DECRYPT_MODE, keyToUse, ivSpec);
       byte[] newPlainBytes = cipher.doFinal(toDecrypt);
        
        return newPlainBytes;
	}
}
