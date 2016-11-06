package keystore;

import java.io.Console;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.print.attribute.standard.Media;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import org.bouncycastle.util.encoders.Base64;

import javazoom.jl.player.advanced.AdvancedPlayer;
import keygen.Key;
import mp3cp.mp3cipher;

public class Mp3 {
	public static byte[] SHAsum(byte[] convertme) {
		try {
			MessageDigest wiadomosc = MessageDigest.getInstance("SHA-256");
			return wiadomosc.digest(convertme);
		} catch (Exception e) {
			e.printStackTrace();
		}
		;
		return null;
	}

	public static void main(String[] args) {
		Klucze klucze;
		File f = new File("cfg");
		char[] pin = { '1', '2', '3', '4', '5', '6' };
		char[] haslo = { '1', '2', '3', '4', '5', '6', '9', '0' };
		byte[] piv = { '7', '2', '4', '4', '4', '6', '7', '3', '6', '1', '4', '0', '5', '3', '0', '1' };

		Konfig cfg;
		Console console = System.console();
		if (console == null) {
			System.out.println("Blad polaczenia");
			System.exit(0);
		}

		if (f.exists() && !f.isDirectory()) {

			pin = console.readPassword("Wprowadz pin: ");
			AesKrypt acrpt = new AesKrypt();
			AesKlucz pinklucz = new AesKlucz();
			pinklucz.haslo = SHAsum(new String(pin).getBytes());
			pinklucz.iv = piv;
			acrpt.set_cipher_cbc(pinklucz);
			try {
				FileInputStream fin = new FileInputStream("cfg");
				ObjectInputStream ooi = new ObjectInputStream(fin);
				cfg = (Konfig) ooi.readObject();
				cfg.aklucz.haslo = acrpt.decrypt(cfg.aklucz.haslo);
				cfg.aklucz.iv = acrpt.decrypt(cfg.aklucz.iv);
				cfg.keystore = new String(acrpt.decrypt(Base64.decode(cfg.keystore)));
				klucze = new Klucze(false, cfg.keystore, haslo);
				fin.close();
				ooi.close();

				AesKlucz aklucz = klucze.get_aes_key(Base64.toBase64String(cfg.aklucz.iv),
						Base64.toBase64String(cfg.aklucz.haslo).toCharArray());
				AesKryptp aesf = new AesKryptp();
				System.out.println("Wybierz opcje: ");
				System.out.println("1 - koduj plik");
				System.out.println("2 - otworz plik");
				String str = null;
				while (true) {
					str = console.readLine();
					if (str.compareTo("1") == 0) {

						System.out.println("Podaj nazwe pliku:");
						str = console.readLine();
						aesf.set_cipher_gcm(aklucz);
						aesf.fencrypt(str, str + ".smp3");
					}
					if (str.compareTo("2") == 0) {
						System.out.println("Wpisz siezke pliku:");
						str = console.readLine();
						aesf.set_cipher_gcm(aklucz);
						aesf.mp3_play(str);
					}
					if (str.compareTo("exit") == 0)
						break;
				}
			} catch (Exception e) {
				System.out.println("Blad w konfiguracjii");
				// e.printStackTrace();
			}

		} else {
			SecretKey cfgkey = Key.genbyte(256);
			SecretKey cfgiv = Key.genbyte(128);
			String scierzka = "keystore";
			pin = console.readPassword("Podaj pin: ");
			System.out.println("Podaj nazwe keystora:");
			scierzka = console.readLine();

			AesKrypt acrpt = new AesKrypt();
			AesKlucz pinkey = new AesKlucz();
			pinkey.haslo = SHAsum(new String(pin).getBytes());
			pinkey.iv = piv;
			acrpt.set_cipher_cbc(pinkey);

			cfg = new Konfig(cfgkey.getEncoded(), cfgiv.getEncoded(), scierzka);
			try {
				Konfig cfg2 = new Konfig(cfg);
				FileOutputStream fout = new FileOutputStream("cfg");
				ObjectOutputStream oos = new ObjectOutputStream(fout);
				cfg.aklucz.haslo = acrpt.encrypt(cfg.aklucz.haslo);
				cfg.aklucz.iv = acrpt.encrypt(cfg.aklucz.iv);
				cfg.keystore = Base64.toBase64String(acrpt.encrypt(scierzka.getBytes()));
				oos.writeObject(cfg);
				oos.close();
				fout.close();
				System.out.println("Utworzyæ nowy keystore? T/N");

				String tak = "";
				while (!(tak.compareTo("T") == 0 || tak.compareTo("N") == 0 || tak.compareTo("t") == 0
						|| tak.compareTo("n") == 0))
					tak = console.readLine();
				if (tak.compareTo("T") == 0 || tak.compareTo("t") == 0) {
					klucze = new Klucze(true, scierzka, haslo);
					klucze.close();
					klucze = new Klucze(false, scierzka, haslo);
					klucze.make_aes_key(Base64.toBase64String(cfg2.aklucz.iv),
							Base64.toBase64String(cfg2.aklucz.haslo).toCharArray());
					klucze.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			;
		}
		;
	}

}
