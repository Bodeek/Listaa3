package keystore;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.security.Security;
import java.security.spec.AlgorithmParameterSpec;

import org.bouncycastle.crypto.*;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javazoom.jl.player.advanced.AdvancedPlayer;

public class AesKryptp {
	public static int blockSize = 16;

	Cipher encryptCipher = null;
	Cipher decryptCipher = null;

	public AesKryptp() {
		Security.addProvider(new BouncyCastleProvider());
	}

	public void set_cipher_cbc(AesKlucz klucz) {
		try {
			encryptCipher = Cipher.getInstance("AES/CBC/PKCS5Padding", "BC");
			SecretKey skey = new SecretKeySpec(klucz.haslo, "AES");
			AlgorithmParameterSpec IVspec = new IvParameterSpec(klucz.iv);
			encryptCipher.init(Cipher.ENCRYPT_MODE, skey, IVspec);
			decryptCipher = Cipher.getInstance("AES/CBC/PKCS5Padding", "BC");
			decryptCipher.init(Cipher.DECRYPT_MODE, skey, IVspec);
		} catch (Exception e) {
			e.printStackTrace();
		}
		;
	}

	public void set_cipher_ctr(AesKlucz klucz) {
		try {
			encryptCipher = Cipher.getInstance("AES/CTR/PKCS5Padding", "BC");
			SecretKey skey = new SecretKeySpec(klucz.haslo, "AES");
			AlgorithmParameterSpec IVspec = new IvParameterSpec(klucz.iv);
			encryptCipher.init(Cipher.ENCRYPT_MODE, skey, IVspec);
			decryptCipher = Cipher.getInstance("AES/CTR/PKCS5Padding", "BC");
			decryptCipher.init(Cipher.DECRYPT_MODE, skey, IVspec);
		} catch (Exception e) {
			e.printStackTrace();
		}
		;
	}

	public void set_cipher_gcm(AesKlucz klucz) {
		try {
			encryptCipher = Cipher.getInstance("AES/GCM/NoPadding", "BC");
			SecretKey skey = new SecretKeySpec(klucz.haslo, "AES");
			AlgorithmParameterSpec IVspec = new IvParameterSpec(klucz.iv);
			encryptCipher.init(Cipher.ENCRYPT_MODE, skey, IVspec);
			decryptCipher = Cipher.getInstance("AES/GCM/NoPadding", "BC");
			decryptCipher.init(Cipher.DECRYPT_MODE, skey, IVspec);
		} catch (Exception e) {
			e.printStackTrace();
		}
		;
	}

	public void fdecrypt(String inp, String outp) {
		try {
			int i;
			byte[] block = new byte[1048576];
			InputStream is = new FileInputStream(inp);
			OutputStream os = new FileOutputStream(outp);
			os = new CipherOutputStream(os, decryptCipher);
			while ((i = is.read(block)) != -1) {
				os.write(block, 0, i);
			}
			is.close();
			os.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void mp3_play(String inp, int st, int kn) {
		try {

			PipedInputStream in = new PipedInputStream();
			final PipedOutputStream out = new PipedOutputStream(in);
			new Thread(new Runnable() {
				public void run() {
					try {
						int i;
						int y = 0;
						byte[] block = new byte[1048576];
						InputStream is = new FileInputStream(inp);

						ByteArrayOutputStream os = new ByteArrayOutputStream();
						CipherOutputStream cos = new CipherOutputStream(os, decryptCipher);

						while ((i = is.read(block)) != -1) {
							cos.write(block, 0, i);
							os.writeTo(out);
							os.reset();
						}
						cos.close();
						is.close();
						os.close();
						out.close();
					} catch (Exception e) {
					}
				}
			}).start();
			int frame = 1024;
			AdvancedPlayer mp3play = new AdvancedPlayer(in);
			mp3play.play(st, kn);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void mp3_play(String inp) {
		try {

			PipedInputStream in = new PipedInputStream();
			final PipedOutputStream out = new PipedOutputStream(in);
			new Thread(new Runnable() {
				public void run() {
					try {
						int i;
						int y = 0;

						byte[] block = new byte[1048576];
						InputStream is = new FileInputStream(inp);

						ByteArrayOutputStream os = new ByteArrayOutputStream();
						CipherOutputStream cos = new CipherOutputStream(os, decryptCipher);

						while ((i = is.read(block)) != -1) {
							cos.write(block, 0, i);
							os.writeTo(out);
							os.reset();
						}
						cos.close();
						is.close();
						os.close();
						out.close();
					} catch (Exception e) {
					}
				}
			}).start();
			int frame = 1024;
			AdvancedPlayer mp3play = new AdvancedPlayer(in);
			mp3play.play();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void fencrypt(String inp, String outp) throws Exception {
		java.io.FileInputStream fis = new java.io.FileInputStream(inp);
		java.io.FileOutputStream fos = new java.io.FileOutputStream(outp);
		byte[] buffer = new byte[blockSize];
		int noBytes = 0;
		byte[] cipherBlock = new byte[encryptCipher.getOutputSize(buffer.length)];
		int cipherBytes;
		while ((noBytes = fis.read(buffer)) != -1) {
			cipherBytes = encryptCipher.update(buffer, 0, noBytes, cipherBlock);
			fos.write(cipherBlock, 0, cipherBytes);
		}

		cipherBytes = encryptCipher.doFinal(cipherBlock, 0);
		fos.write(cipherBlock, 0, cipherBytes);

		fos.close();
		fis.close();
	}
}
