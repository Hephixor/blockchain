package network;


import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PublicKey;
import java.security.Security;
import java.security.spec.InvalidKeySpecException;
import org.bouncycastle.jce.ECNamedCurveTable;
import org.bouncycastle.jce.spec.ECParameterSpec;
import org.bouncycastle.jce.spec.ECPublicKeySpec;
import org.bouncycastle.math.ec.ECCurve;
import org.bouncycastle.util.encoders.Hex;
import org.json.JSONException;
import org.json.JSONObject;

import chain.Transaction;
import chain.TransactionTypeEnum;
import crypto.CryptoUtils;

public class JsonUtils {


	public static Payload payloadFromJson(String json) {

		try {
			JSONObject payload = new JSONObject(json);

			//C'est un register
			if(payload.has("event_hash")) {
				PayloadRegister payloadR = new PayloadRegister(payload.get("event_hash").toString());
				return payloadR;
			}

			//C'est un create
			else {
			    JSONObject date = payload.getJSONObject("date");
			    JSONObject limits = payload.getJSONObject("limits");
				PayloadCreation payloadC = new PayloadCreation(payload.getString("name"),payload.getString("description"),payload.getString("location"),date.getString("begin"),date.getString("end_subscription"),date.getString("end"),limits.getInt("min"),limits.getInt("max"));
				return payloadC;
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return null;
	}

	public static Transaction transactionFromJson(String json) {
		System.out.println(json);
		if(json != null) {
			try {
				JSONObject jsonT =  new JSONObject(json);
				if(jsonT.getString("type_transact").equals("creation")) {
					Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
			        KeyFactory fact = KeyFactory.getInstance("ECDSA", "BC");
			        ECParameterSpec spec = ECNamedCurveTable.getParameterSpec("secp256r1");
			        ECCurve curve = spec.getCurve();
			        ECPublicKeySpec pubKey = new ECPublicKeySpec(curve.decodePoint(Hex.decode("029e15edf9abdbf2bbcbedad647c881ca6d0a068552f8dc459c4fef3439254103e")),spec);
			        PublicKey vKey = fact.generatePublic(pubKey);
			        
			        Transaction transaction = new Transaction(vKey,payloadFromJson(jsonT.getJSONObject("payload").toString()), 0, TransactionTypeEnum.CREATION);
					return transaction;
				}
				// REGISTER
				else {
					Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
			        KeyFactory fact = KeyFactory.getInstance("ECDSA", "BC");
			        ECParameterSpec spec = ECNamedCurveTable.getParameterSpec("secp256r1");
			        ECCurve curve = spec.getCurve();
			        ECPublicKeySpec pubKey = new ECPublicKeySpec(curve.decodePoint(Hex.decode("029e15edf9abdbf2bbcbedad647c881ca6d0a068552f8dc459c4fef3439254103e")),spec);
			        PublicKey vKey = fact.generatePublic(pubKey);
			        
			        Transaction transaction = new Transaction(vKey,payloadFromJson(jsonT.get("payload").toString()), 0, TransactionTypeEnum.REGISTER);
					return transaction;
				}
			} catch (NoSuchAlgorithmException | NoSuchProviderException | InvalidKeySpecException | JSONException e) {

				e.printStackTrace();
			}

		}
		return null;
	}



	public static JSONObject makeJsonHead(JSONObject json, PublicKey pkey, Boolean isRegister)  {
		try {
			json.put("pub_key", CryptoUtils.getStringFromKey(pkey));

			if(isRegister) 
				json.put("type_transact", "register");

			else 
				json.put("type_transact", "creation");
		}catch(Exception e) {
			System.err.println("Error creating Json Head");
		}


		return json;
	}

	public static JSONObject makeJsonPayload(PayloadCreation payload)  {
		JSONObject jsonPayload = new JSONObject();
		JSONObject jsonPayloadDate = new JSONObject();
		JSONObject jsonPayloadLimit = new JSONObject();

		try {

			//JSON date
			jsonPayloadDate.put("begin", payload.getDateBegin());
			jsonPayloadDate.put("end", payload.getDateEnd());
			jsonPayloadDate.put("end_subscription", payload.getDateEndSubscription());

			//JSON limit
			if(payload.getLimitMin() != -1) jsonPayloadLimit.put("min", payload.getLimitMin());
			if(payload.getLimitMax() != -1) jsonPayloadLimit.put("max", payload.getLimitMax());

			//JSON payload
			jsonPayload.put("name", payload.getName());
			jsonPayload.put("description", payload.getDescription());
			jsonPayload.put("date", jsonPayloadDate);
			jsonPayload.put("location", payload.getLocation());
			jsonPayload.put("limits", jsonPayloadLimit);

		} catch(Exception e) {
			System.err.println("Error creating JSON payload");
			e.printStackTrace();
		}

		return jsonPayload;
	}

	public static JSONObject makeJsonBloc(PublicKey pkey, String hashPrev, String rootHash, int level, int time) {
		JSONObject jsonBloc = new JSONObject();

		try {
			jsonBloc.put("pub_key", CryptoUtils.getStringFromKey(pkey));
			jsonBloc.put("hash_prev_bloc", hashPrev);
			jsonBloc.put("root_hash", rootHash);
			jsonBloc.put("time", time);
			jsonBloc.put("level", level);
		} catch (Exception e) {
			System.err.println("Error creating JSON bloc");
			e.printStackTrace();
		}

		return jsonBloc;
	}

	public static JSONObject makeJson(PublicKey pkey, Payload payload, String eventHash) {
		JSONObject json = new JSONObject();

		try {
			if(pkey != null) {	
				
				if(payload!=null && eventHash.equals("")) {
					makeJsonHead(json, pkey, false);
					json.put("payload", makeJsonPayload((PayloadCreation) payload));
				}

				else {
					System.out.println("THIS IS A REGISTER");
					if(eventHash!= null && eventHash != "") {
						makeJsonHead(json, pkey, true);
						JSONObject payloadRegister = new JSONObject();
						payloadRegister.put("event_hash", eventHash);
						json.put("payload", payloadRegister);
					}
					else {
						System.err.println("Error making json please provide at least payload or eventHash");
					}
				}
				
			}

			else {
				System.err.println("Error making json please provide valid pub_key");
			}

		}catch(Exception e) {
			System.err.println("Error creating Json");
			e.printStackTrace();

		}

		return json;
	}

}
