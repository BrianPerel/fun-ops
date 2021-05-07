package data.encrypt.decrypt;

import java.io.IOException;

public interface IEncryptDecrypt {
	public void encrypt() throws IOException;
	public String decrypt() throws IOException;
}
