package network;


import java.security.PublicKey;

import org.json.JSONObject;

import crypto.CryptoUtils;

public class JsonUtils {



	public static JSONObject makeJsonHead(JSONObject json, String pkey, Boolean isRegister)  {
		try {
			json.put("pub_key", pkey);

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

	public static JSONObject makeJson(String pkey, PayloadCreation payload, String eventHash) {
		JSONObject json = new JSONObject();

		try {
			if(pkey!=null && pkey != "") {				
				if(payload!=null) {
					makeJsonHead(json, pkey, false);
					json.put("payload", makeJsonPayload(payload));
				}

				else {
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
