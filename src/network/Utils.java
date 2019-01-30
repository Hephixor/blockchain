package network;


import org.json.JSONObject;

public class Utils {



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
			jsonPayloadLimit.put("min", payload.getLimitMin());
			jsonPayloadLimit.put("max", payload.getLimitMax());

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

	public static JSONObject makeJsonBloc(String pkey, String hashPrev, String rootHash) {
		JSONObject jsonBloc = new JSONObject();

		try {
			jsonBloc.put("pub_key", pkey);
			jsonBloc.put("hash_prev_bloc", hashPrev);
			jsonBloc.put("root_hash", rootHash);
		} catch (Exception e) {
			System.err.println("Error creatin JSON bloc");
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



	/* 
	  public static void sendToClient(HttpServletResponse resp, JSONObject json) throws JSONException {
		resp.setContentType("text/plain");
		resp.setCharacterEncoding("UTF-8");

		PrintWriter out = null;
		try {
			out = resp.getWriter();
		} catch (IOException e) {
			e.printStackTrace();
		}
		out.println(json.toString(3));
		out.flush();
	} 

		public static String getFieldValue(HttpServletRequest req, String fieldname) {
		String value = req.getParameter(fieldname);

		if (value == null || value.trim().length() == 0) {
			return null;
		} else {
			return value.trim();
		}
	}

	 */



}