package keystore;

import java.io.Serializable;

public class Konfig implements Serializable{
 public AesKlucz aklucz;
 public String keystore;
 public Konfig(byte [] haslo,byte [] iv,String sciezka)
 {
	 aklucz=new AesKlucz();
	 aklucz.iv=iv;
	 aklucz.haslo=haslo;
	 this.keystore=sciezka;
 }
public Konfig(Konfig cfg) {
	aklucz=new AesKlucz();
	this.aklucz.iv=cfg.aklucz.iv.clone();
	this.aklucz.haslo=cfg.aklucz.haslo.clone();
	this.keystore=new String(cfg.keystore);
}
}
