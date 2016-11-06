package keystore;

import java.io.FileNotFoundException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.io.Console;

public class Zad1 {
	public static void wyswietl(byte[] arr) {
		for (byte x : arr)
			System.out.println(Integer.toHexString(x));
	}

	public static byte[] bity(char[] chars) {
		CharBuffer charBuffer = CharBuffer.wrap(chars);
		ByteBuffer byteBuffer = Charset.forName("UTF-8").encode(charBuffer);
		byte[] bytes = Arrays.copyOfRange(byteBuffer.array(), byteBuffer.position(), byteBuffer.limit());
		Arrays.fill(charBuffer.array(), '\u0000');
		Arrays.fill(byteBuffer.array(), (byte) 0);
		return bytes;
	}

	public static void main(String[] args) {

		Console console = System.console();
		if (console == null) {
			System.out.println("Blad polaczenia");
			System.exit(0);
		}
		char[] haslo = { '1', '2', '3', '4', '5', '6', '7', '8','9' };
		ObslugaKeyStore k = new ObslugaKeyStore();
		if (args.length < 3) {
			Klucze klucz;
			if (args.length > 1 && args[0].compareTo("makestore") == 0) {
				klucz = new Klucze(true, args[1], haslo);
				klucz.close();
				char[] haslo2 = console.readPassword("Podaj haslo: ");
				AesKryptp aes = new AesKryptp();
				klucz = new Klucze(false, args[1], haslo);
				klucz.make_aes_key("key", haslo2);
				klucz.close();
			}
		} else {
			if (args.length > 4) {

				char[] haslo2 = console.readPassword("Podaj haslo: ");
				AesKryptp aes = new AesKryptp();
				Klucze Klucze = new Klucze(false, args[4], haslo);
				AesKlucz akey = Klucze.get_aes_key("key", haslo2);
				Klucze.close();
				if (args[1].compareTo("cbc") == 0)
					aes.set_cipher_cbc(akey);
				if (args[1].compareTo("ctr") == 0)
					aes.set_cipher_ctr(akey);
				if (args[1].compareTo("gcm") == 0)
					aes.set_cipher_gcm(akey);
				if (args[0].compareTo("e") == 0)
					try {
						aes.fencrypt(args[2], args[3]);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				else
					aes.fdecrypt(args[2], args[3]);
			}
		}
	}

}
