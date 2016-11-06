package keystore;

import javax.crypto.SecretKey;

public class Klucze {
	private ObslugaKeyStore k = new ObslugaKeyStore();
	private char[] haslo;
	private String sciezka;

	public Klucze(boolean make, String sciezka, char[] haslo) {
		if (make)
			k.make(sciezka, haslo);
		else
			k.load(sciezka, haslo);
		this.sciezka = sciezka;
		this.haslo = haslo;
	}

	public void make_aes_key(String alias, char[] haslo) {
		if (!k.is_key(alias)) {
			k.make_key(alias, haslo);
			k.make_iv("iv-" + alias, haslo);
		}
		;
	}

	public AesKlucz get_aes_key(String alias, char[] haslo) {
		AesKlucz as = new AesKlucz();
		SecretKey klucz = k.get_key(alias, haslo);
		as.haslo = klucz.getEncoded();
		as.iv = k.get_key("iv-" + alias, haslo).getEncoded();
		return as;
	}

	public void close() {
		k.store(sciezka, haslo);
	}
}
