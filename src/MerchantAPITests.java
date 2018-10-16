import java.util.Base64;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.github.cliftonlabs.json_simple.JsonObject;

import io.restassured.RestAssured;
import io.restassured.http.Header;
import io.restassured.http.Headers;
import io.restassured.http.Method;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.response.ResponseBody;
import io.restassured.specification.RequestSpecification;

public class MerchantAPITests {

	public static String baseURI = "https://api.omise.co";
	private static String secret_key = "skey_test_qqq5dlu5w8vnwo1m5ck6jyqqq:";


	private static final Map<String, String> AccountResponseHeaderMap;
    static {
        Map<String, String> ExpectedAccountResponseHeader = new HashMap<String, String>();
        ExpectedAccountResponseHeader.put("Content-Type", "application/json; charset=utf-8");
        ExpectedAccountResponseHeader.put("Status", "200 OK");
        ExpectedAccountResponseHeader.put("Server", "nginx");
        ExpectedAccountResponseHeader.put("Content-Encoding", "gzip");
        AccountResponseHeaderMap = Collections.unmodifiableMap(ExpectedAccountResponseHeader);
    } //More Header Details can be added so that they can be validated against response header payload.

	private static final Map<String, Object> AccountResponseBodyMap;
    static {
        Map<String, Object> ExpectedAccountResponseHeader = new HashMap<String, Object>();
        ExpectedAccountResponseHeader.put("email", "digitiqman@gmail.com");
        ExpectedAccountResponseHeader.put("id", "acct_5djxx4gqsu0u5m8hb0e");
        ExpectedAccountResponseHeader.put("object", "account");
        ExpectedAccountResponseHeader.put("currency", "thb");
        ExpectedAccountResponseHeader.put("livemode", false);
        AccountResponseBodyMap = Collections.unmodifiableMap(ExpectedAccountResponseHeader);
    }//More Body Details can be added so that they can be validated against response payload.


    private static final Map<String, String> NewCustRespHeaderMap;
    static {
        Map<String, String> ExpNewCustRespHeader = new HashMap<String, String>();
        ExpNewCustRespHeader.put("Content-Type", "application/json; charset=utf-8");
        ExpNewCustRespHeader.put("Status", "200 OK");
        ExpNewCustRespHeader.put("Server", "nginx");
        ExpNewCustRespHeader.put("Content-Encoding", "gzip");
        NewCustRespHeaderMap = Collections.unmodifiableMap(ExpNewCustRespHeader);
    } //More Header Details can be added so that they can be validated against response header payload.

	private static final Map<String, Object> NewCustRespBodyMap;
    static {
        Map<String, Object> ExpNewCustBodyContent = new HashMap<String, Object>();
        ExpNewCustBodyContent.put("cards.object", "list");
        ExpNewCustBodyContent.put("default_card", null);
        ExpNewCustBodyContent.put("object", "customer");
        ExpNewCustBodyContent.put("email", "john.doe@example.com");
        ExpNewCustBodyContent.put("livemode", false);
        NewCustRespBodyMap = Collections.unmodifiableMap(ExpNewCustBodyContent);
    }//More Body Details can be added so that they can be validated against response payload.


	@Test
	//Positive and Negative Test for Account Authentication
	public void AccountVerificationTest() {

		//Specify the RESTful Base Uri
		RestAssured.baseURI = MerchantAPITests.baseURI;

		//Extract Request Specification Object
		RequestSpecification requestspec = RestAssured.given();

		//Initialize and Set the Headers
		String enc = Base64.getEncoder().encodeToString(secret_key.getBytes());
		Header authheader = new Header("Authorization", "Basic " + enc);
		Header contypeheader = new Header("Content-Type", "application/json; charset=utf-8");

		Headers requestheader = Headers.headers(authheader, contypeheader);

		//Set the header for the request to be sent to the Server
		requestspec.headers(requestheader);

		//Send the request to server
		Response response = requestspec.request(Method.GET, "/account");

		//Get the status code from the response and Assert
		int statusCode = response.getStatusCode();
		Assert.assertEquals(statusCode /*actual value*/, 200 /*expected value*/, "Correct status code returned");

		//Validate the received response header against the expected response header
		String responseheadervalue;
		for (String key : AccountResponseHeaderMap.keySet()){
			responseheadervalue = response.header(key);
			Assert.assertEquals(responseheadervalue /* actual value */, AccountResponseHeaderMap.get(key) /* expected value */, "Wrong " + key.toUpperCase() + " Header Value returned || ");
	    }

		//Validate the response body using JSON Path Evaluator
		JsonPath responsejsonpath = response.jsonPath();
		for (String key : AccountResponseBodyMap.keySet()){
			responseheadervalue = response.header(key);
			Assert.assertEquals(responsejsonpath.get(key) /* actual value */, AccountResponseBodyMap.get(key) /* expected value */, "Wrong " + key.toUpperCase() + " Response Value returned || ");
	    }

	}

	@Test
	public void NewCustomerTest() {

		//Specify the RESTful Base Uri
		RestAssured.baseURI = MerchantAPITests.baseURI;

		//Extract Request Specification Object
		RequestSpecification requestspec = RestAssured.given();

		//Initialize and Set the Headers
		String enc = Base64.getEncoder().encodeToString(secret_key.getBytes());
		Header authheader = new Header("Authorization", "Basic " + enc);
		Header contypeheader = new Header("Content-Type", "application/json; charset=utf-8");

		Headers requestheader = Headers.headers(authheader, contypeheader);

		//Set the header for the request to be sent to the Server
		requestspec.headers(requestheader);

		JsonObject requestParams = new JsonObject();
		requestParams.put("description", "John Doe (id: 30)");
		requestParams.put("email", "john.doe@example.com");

		// Add the Json to the body of the request
		requestspec.body(requestParams.toJson());

		// Post the request
		Response response = requestspec.request(Method.POST, "/customers");

		//Validate Response Status Code
		int statusCode = response.getStatusCode();
		Assert.assertEquals(statusCode /*actual value*/, 200 /*expected value*/, "Correct status code NOT returned");

		//Validate the received response header against the expected response header
		String responseheadervalue;
		for (String key : NewCustRespHeaderMap.keySet()){
			responseheadervalue = response.header(key);
			Assert.assertEquals(responseheadervalue /* actual value */, NewCustRespHeaderMap.get(key) /* expected value */, "Wrong " + key.toUpperCase() + " Header Value returned || ");
	    }

		//Validate the response body using JSON Path Evaluator
		JsonPath responsejsonpath = response.jsonPath();
		for (String key : NewCustRespBodyMap.keySet()){
			responseheadervalue = response.header(key);
			Assert.assertEquals(responsejsonpath.get(key) /* actual value */, NewCustRespBodyMap.get(key) /* expected value */, "Wrong " + key.toUpperCase() + " Response Value returned || ");
	    }

	}
}
