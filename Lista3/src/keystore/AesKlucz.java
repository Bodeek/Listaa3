package keystore;

import java.io.Serializable;

import javax.crypto.SecretKey;

public class AesKlucz implements Serializable {
	public byte[] iv;
	public byte[] haslo;
	public SecretKey klucz;
}
