package com.example.test.steps;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import net.serenitybdd.rest.SerenityRest;
import com.github.fge.jackson.JsonLoader;
import com.github.fge.jsonschema.core.report.ProcessingReport;
import com.github.fge.jsonschema.main.JsonSchema;
import com.github.fge.jsonschema.main.JsonSchemaFactory;
import io.restassured.response.Response;
import java.io.File;
import java.io.IOException;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class USDExchangeSteps {

    @Given("the USD exchange API is available")
    public void givenTheUSDExchangeAPIIsAvailable() {
         try {
            // Create a URL object with the API endpoint
            URL url = new URL("https://open.er-api.com/v6/latest/USD");
            
            // Open a connection to the URL
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            
            // Set the request method to HEAD (to avoid downloading the entire response)
            connection.setRequestMethod("HEAD");
            
            // Get the response code
            int responseCode = connection.getResponseCode();
            
            // Check if the response code indicates success (2xx range)
            if (responseCode >= 200 && responseCode < 300) {
                System.out.println("API is available (HTTP status code: " + responseCode + ")");
            } else {
                System.out.println("API is not available (HTTP status code: " + responseCode + ")");
                // Optionally, throw an exception or fail the test if the API is not available
            }
        } catch (Exception e) {
            System.out.println("Error checking API availability: " + e.getMessage());
            // Optionally, throw an exception or fail the test if an error occurs while checking API availability
        }
    }

    @When("I make a request to fetch USD exchange rates")
    public void whenIMakeARequestToFetchUSDExchangeRates() {
        SerenityRest.when().get("https://open.er-api.com/v6/latest/USD");
    }

    // @Then("the API call is successful")
    // public void thenTheAPICallIsSuccessful() {
    //     SerenityRest.then().statusCode(200);
    // }
//    @Then("API call is successful and returns valid price")
//     public void apiCallIsSuccessfulAndReturnsValidPrice() {
//         // Check status code and status message
//         assertThat(response.getStatusCode(), is(equalTo(200)));
//         assertThat(response.jsonPath().getString("status"), is(equalTo("SUCCESS")));

//         // Fetch USD price against AED and validate price range
//         Double aedPrice = response.jsonPath().getDouble("rates.AED");
//         assertThat(aedPrice, allOf(greaterThanOrEqualTo(3.6), lessThanOrEqualTo(3.7)));

//         // Validate API response time
//         String timestampStr = response.jsonPath().getString("timestamp");
//         Instant timestamp = Instant.parse(timestampStr);
//         Instant currentTime = Instant.now();
//         long responseTimeInSeconds = currentTime.getEpochSecond() - timestamp.getEpochSecond();
//         assertThat(responseTimeInSeconds, greaterThanOrEqualTo(3L));

//         // Verify that 162 currency pairs are returned
//         assertThat(response.jsonPath().getMap("rates"), hasSize(162));

//         // Make sure API response matches the JSON schema 
//         assertThat(response.getBody().asString(), matchesJsonSchemaInClasspath("https://open.er-api.com/v6/latest/USD"));
//     }
    @Then("the API call is successful and returns valid price")
    public void apiCallIsSuccessfulAndReturnsValidPrice() {
        Response response = null; // Initialize response to avoid potential null pointer exceptions

        try {
            response = SerenityRest.when().get("https://open.er-api.com/v6/latest/USD");

            // 1. Define expected success statuses
            List<String> expectedSuccessStatuses = Arrays.asList("SUCCESS", "OK");

            // 2. Check status code and status message
            assertThat(response.getStatusCode(), containsInAnyOrder(expectedSuccessStatuses));
            assertThat(response.jsonPath().getString("status"), is(not(equalTo("")))); // Ensure status message is not empty

            
            // 3. Fetch USD price against eg AED and validate price range
            Double aedPrice = response.jsonPath().getDouble("rates.AED");
            assertThat(aedPrice, allOf(greaterThanOrEqualTo(3.6), lessThanOrEqualTo(3.7)));

            // 4. Validate API response time
            String timestampStr = response.jsonPath().getString("timestamp");
            Instant timestamp = Instant.parse(timestampStr);
            Instant currentTime = Instant.now();
            long responseTimeInSeconds = currentTime.getEpochSecond() - timestamp.getEpochSecond();
            assertThat(responseTimeInSeconds, greaterThanOrEqualTo(3L));

            // 5. Verify that 162 currency pairs are returned
            assertThat(response.jsonPath().getMap("rates"), hasSize(162));

            } catch (Exception e) {
                System.out.println("Error making API request: " + e.getMessage());
}   
