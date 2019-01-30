package chain;

import java.security.PublicKey;

import crypto.CryptoUtils;
import merkle.Bytes;
import merkle.Hash;

public class TransactionOutput {
	public String id;
	public PublicKey receiver; // Will get the money baby
	public float value; // But how many money ?
	public String parentTransactionId; // Ref to the transaction ID that created me
	
	public TransactionOutput(PublicKey reciepient, float value, String parentTransactionId) {
		this.receiver = reciepient;
		this.value = value;
		this.parentTransactionId = parentTransactionId;
		this.id = Bytes.toHex(Hash.digestString(CryptoUtils.getStringFromKey(reciepient)+Float.toString(value)+parentTransactionId)).toLowerCase();
	}
	
	// IS IT FOR ME ?!
	public boolean isMine(PublicKey publicKey) {
		return (publicKey == receiver);
}
}
