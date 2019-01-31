package tests;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import network.*;


class JsonTest {

	final String jsonPayloadExpected = "{\n" + 
			"   \"type_transact\": \"creation\",\n" + 
			"   \"payload\": {\n" + 
			"      \"date\": {\n" + 
			"         \"end\": \"dateEndTest\",\n" + 
			"         \"begin\": \"dateBeginTest\",\n" + 
			"         \"end_subscription\": \"dateEndSubscriptionTest\"\n" + 
			"      },\n" + 
			"      \"name\": \"nameTest\",\n" + 
			"      \"description\": \"descriptionTest\",\n" + 
			"      \"location\": \"locationTest\",\n" + 
			"      \"limits\": {\n" + 
			"         \"min\": 0,\n" + 
			"         \"max\": 10\n" + 
			"      }\n" + 
			"   },\n" + 
			"   \"pub_key\": \"pub_key\"\n" + 
			"}";

	final String jsonRegisterExpected =  "{\n" + 
			"   \"type_transact\": \"register\",\n" + 
			"   \"payload\": {\"event_hash\": \"eventHashTest\"},\n" + 
			"   \"pub_key\": \"pub_key\"\n" + 
			"}";

	PayloadCreation payload;
	JSONObject json;

	@BeforeEach
	void beforeEach() {
		payload = new PayloadCreation("nameTest", "descriptionTest", "dateBeginTest", "dateEndTest", "dateEndSubscriptionTest", "locationTest", 0, 10);

	}

	@AfterEach 
	void afterEach() {
		payload = null;
		json = null;

	}

	@Test
	void testJsonCreation() {
		json = JsonUtils.makeJson("pub_key", payload, "");
		String acutalJson = "" ;
		try {acutalJson = json.toString(3);} catch (JSONException e) {e.printStackTrace();}

		System.out.println("\n/*---------- Json with Payload (creation mode) ----------*/");
		System.out.println("expected: \n"+ jsonPayloadExpected +"\n");
		System.out.println("actual: \n"+ acutalJson +"\n");

		Assert.assertEquals(acutalJson, jsonPayloadExpected);
	}

	@Test
	void testJsonRegister() {
		json = JsonUtils.makeJson("pub_key", null, "eventHashTest");
		String acutalJson= "" ;
		try {acutalJson = json.toString(3);} catch (JSONException e) {e.printStackTrace();}

		System.out.println("\n/*---------- Json without Payload (register mode) ----------*/");
		System.out.println("expected: \n"+ jsonRegisterExpected +"\n");
		System.out.println("actual: \n"+ acutalJson +"\n");

		Assert.assertEquals(acutalJson, jsonRegisterExpected);
	}

}

