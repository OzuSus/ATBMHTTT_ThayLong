package controller;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;

public class Key {
	private KeyPairGenerator keyPairGene;
	private KeyPair keyPair;
	private PublicKey pub;
	private PrivateKey pri;

	public Key() {
		try {
			this.keyPairGene = KeyPairGenerator.getInstance("RSA");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
	}

	public void generatorKey(int sizeKey) {
		this.keyPairGene.initialize(sizeKey);
		this.keyPair = keyPairGene.generateKeyPair();
		this.pub = this.keyPair.getPublic();
		this.pri = this.keyPair.getPrivate();
	}

	public PublicKey getPub() {
		return pub;
	}

	public PrivateKey getPri() {
		return pri;
	}
}
