package keystore;

import java.security.KeyStore;
import java.security.KeyStore.SecretKeyEntry;
import java.security.KeyStoreException;
import java.security.SecureRandom;
import java.security.spec.KeySpec;

import javax.crypto.*;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

public class ObslugaKeyStore {
	private KeyStore keystore = null;
	
	public void load(String path,char [] haslo)
	{
		java.io.FileInputStream fis = null;
		try{
		fis = new java.io.FileInputStream(path);
		keystore = KeyStore.getInstance("JCEKS");
		keystore.load(fis,haslo);
	  
	    fis.close();
		} catch (Exception e){e.printStackTrace();}
	}
	public void make(String path,char [] haslo)
	{
		try{
		keystore = KeyStore.getInstance("JCEKS");
		keystore.load(null,haslo);
		java.io.FileOutputStream fos =
	               new java.io.FileOutputStream(path);
	          keystore.store(fos,haslo);
	          fos.close();
		} catch (Exception e){e.printStackTrace();}
	}
	public void store(String path,char [] haslo)
	{
		try{java.io.FileOutputStream fos =
		               new java.io.FileOutputStream(path);
		          keystore.store(fos,haslo);
		          fos.close();
		} catch (Exception e){e.printStackTrace();}
	}
	public SecretKey get_key(String alias,char[] haslo)
	{
		SecretKey myPrivateKey =null;
		try{
			 KeyStore.ProtectionParameter  protParam =
			            new KeyStore.PasswordProtection(haslo);
			SecretKeyEntry ent=(SecretKeyEntry) keystore.getEntry(alias,protParam);
			myPrivateKey=ent.getSecretKey();
		} catch (Exception e){e.printStackTrace();}
		return myPrivateKey;
	}
	public void make_iv(String alias,char[] haslo)
	{
		try{
			KeyGenerator keyGen = KeyGenerator.getInstance("AES");   
	        SecureRandom random = SecureRandom.getInstance("SHA1PRNG", "SUN");            
	        keyGen.init(128,random);       
	        SecretKey secretKey = keyGen.generateKey();
	        KeyStore.SecretKeyEntry skEntry = new KeyStore.SecretKeyEntry(secretKey);
	        keystore.setEntry(alias, skEntry, new KeyStore.PasswordProtection(haslo));
		} catch (Exception e){e.printStackTrace();}
	}
	public void make_key(String alias,char[] haslo)
	{
		try{
			KeyGenerator keyGen = KeyGenerator.getInstance("AES");   
	        SecureRandom random = SecureRandom.getInstance("SHA1PRNG", "SUN");            
	        keyGen.init(256,random);       
	        SecretKey secretKey = keyGen.generateKey();
	        KeyStore.SecretKeyEntry skEntry = new KeyStore.SecretKeyEntry(secretKey);
	        keystore.setEntry(alias, skEntry, new KeyStore.PasswordProtection(haslo));
		} catch (Exception e){e.printStackTrace();}
	}
	public boolean is_key(String alias)
	{
		try {
			return keystore.containsAlias(alias);
		} catch (KeyStoreException e) {
			e.printStackTrace();
		}
		return false;
	}
	public void test(byte [] key,String alg)
	{
		
	}
}
