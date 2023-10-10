package no.hvl.dat152.rest.ws.main.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import no.hvl.dat152.rest.ws.exceptions.UserNotFoundException;
import no.hvl.dat152.rest.ws.security.payload.AuthRequest;
import no.hvl.dat152.rest.ws.security.payload.SignupRequest;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class TestSignupLogin {
	
	private String API_ROOT = "http://localhost:8090/elibrary/api/v1/auth";
	
	@DisplayName("JUnit test for @PostMapping(/signup) endpoint")
	@Test
	public void AregisterUser_thenOK() {
		
		SignupRequest user1 = new SignupRequest();
		user1.setEmail("test1@email.com");
		user1.setFirstname("Test1");
		user1.setLastname("User1");
		user1.setPassword("test1_pwd");		
		
		Response response = RestAssured.given()
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.body(user1)
				.post(API_ROOT+"/signup");
	    
	    assertEquals(HttpStatus.CREATED.value(), response.getStatusCode());
	    assertEquals("Test1", response.jsonPath().get("firstname"));

	}

	@DisplayName("JUnit test for @PostMapping(/login) endpoint")
	@Test
	public void BloginUser_thenOK() throws UserNotFoundException {
		
		AuthRequest authReq = new AuthRequest();
		authReq.setEmail("berit@email.com");
		authReq.setPassword("berit_pwd");
		
		AuthRequest authReq2 = new AuthRequest();
		authReq2.setEmail("robert@email.com");
		authReq2.setPassword("robert_pwd");
		
		AuthRequest authReq3 = new AuthRequest();
		authReq3.setEmail("kristin@email.com");
		authReq3.setPassword("kristin_pwd");

		Response response = RestAssured.given()
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.body(authReq)
				.post(API_ROOT+"/login");
		
		System.out.println("access_token(SUPER_ADMIN) = "+ response.jsonPath().get("accessToken").toString());
		assertEquals(HttpStatus.OK.value(), response.getStatusCode());
		assertTrue(response.jsonPath().get("accessToken").toString().contains("eyJhbGciOiJSUzI1NiJ9"));
		
		Response response2 = RestAssured.given()
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.body(authReq2)
				.post(API_ROOT+"/login");
		
		System.out.println("access_token(USER) = "+ response2.jsonPath().get("accessToken").toString());

		Response response3 = RestAssured.given()
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.body(authReq3)
				.post(API_ROOT+"/login");
		
		System.out.println("access_token(ADMIN) = "+ response3.jsonPath().get("accessToken").toString());
		

	}

}
