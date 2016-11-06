package keystore;

import java.security.Security;
import java.security.spec.AlgorithmParameterSpec;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.bouncycastle.crypto.BufferedBlockCipher;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.engines.AESEngine;
import org.bouncycastle.crypto.modes.CBCBlockCipher;
import org.bouncycastle.crypto.paddings.BlockCipherPadding;
import org.bouncycastle.crypto.paddings.PKCS7Padding;
import org.bouncycastle.crypto.paddings.PaddedBufferedBlockCipher;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.crypto.params.ParametersWithIV;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

public class AesKrypt {
	public static int blockSize = 16;
    Cipher encryptCipher = null;
    Cipher decryptCipher = null;
    
	public AesKrypt()
	{
	    	Security.addProvider(new BouncyCastleProvider());
	}
	public void set_cipher_cbc(AesKlucz klucz)
	{
		try{
		  encryptCipher = Cipher.getInstance("AES/CBC/PKCS5Padding", "BC");
	      SecretKey keyValue = new SecretKeySpec(klucz.haslo,"AES");
	      AlgorithmParameterSpec IVspec = new IvParameterSpec(klucz.iv);
	      encryptCipher.init(Cipher.ENCRYPT_MODE, keyValue, IVspec);
	      decryptCipher =
	               Cipher.getInstance("AES/CBC/PKCS5Padding", "BC");
	       decryptCipher.init(Cipher.DECRYPT_MODE, keyValue, IVspec);
		}
		catch(Exception e){e.printStackTrace();};
	}
	public byte [] encrypt(byte [] data)
	{
		try{
		byte[] encrypted= encryptCipher.doFinal(data);
		return encrypted;
		}
		catch(Exception e){e.printStackTrace();};
		return null;
	}
	public byte [] decrypt(byte [] data)
	{
		try{
			byte[] decrypted = decryptCipher.doFinal(data);
			return decrypted;
		}
			catch(Exception e){e.printStackTrace();};
			return null;	
	}
}
