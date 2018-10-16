import java.util.Base64;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.Test;

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
	private static String secret_key = "skey_test_5dlu5wqq8vnwo1m5ck6jyqq:";

	private static final Map<String, String> AccountResponseHeaderMap;
    static {
        Map<String, String> ExpectedAccountResponseHeader = new HashMap<String, String>();
        ExpectedAccountResponseHeader.put("Content-Type", "application/json; charset=utf-8");
        ExpectedAccountResponseHeader.put("Status", "200 OK");
        ExpectedAccountResponseHeader.put("Server", "nginx");
        AccountResponseHeaderMap = Collections.unmodifiableMap(ExpectedAccountResponseHeader);
    }

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

		//Get the status code from the response
		int statusCode = response.getStatusCode();

		//Extract all the response header.
		Headers allHeaders = response.headers();
		allHeaders.hasHeaderWithName("asa");
		allHeaders.get("qwqwe");

		//Validate the received response header against the expected response header
		String responseheadervalue;
		for (String key : AccountResponseHeaderMap.keySet()){
			responseheadervalue = response.header(key);
			Assert.assertEquals(responseheadervalue /* actual value */, AccountResponseHeaderMap.get(key) /* expected value */);
	    }

		//Iterate over all the Headers and check if the
		//Header values are present as expected
		/*for(Header header : allHeaders)
		{

			System.out.println("Key: " + header.getName() + " Value: " + header.getValue());
		}*/

	}

}
