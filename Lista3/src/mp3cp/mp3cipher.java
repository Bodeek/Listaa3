package mp3cp;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.RandomAccessFile;

import javax.crypto.Cipher;
import javax.crypto.CipherOutputStream;

import org.bouncycastle.crypto.BufferedBlockCipher;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.engines.AESEngine;
import org.bouncycastle.crypto.io.CipherInputStream;
import org.bouncycastle.crypto.modes.SICBlockCipher;
import org.bouncycastle.crypto.paddings.PaddedBufferedBlockCipher;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.crypto.params.ParametersWithIV;

import keystore.AesKlucz;

public class mp3cipher {
	BufferedBlockCipher bc=null;
	byte [] buffin;
	byte [] buffout;
	int ll;
	 public void init(AesKlucz klucz)
	  {
		 bc = new BufferedBlockCipher(new SICBlockCipher(new AESEngine())); 
		 bc.init(false, new ParametersWithIV(new KeyParameter(klucz.haslo), klucz.iv)); 
		 ll=1024;
		 buffin=new byte [ll];
		 buffout=new byte [ll];
	  }
	 public void play(String str,int i)
	 {
		 //mp3cp.
	 }
}
