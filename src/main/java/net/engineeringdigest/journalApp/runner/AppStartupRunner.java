package net.engineeringdigest.journalApp.runner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Component
public class AppStartupRunner implements CommandLineRunner {

    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    public void run(String... args) {
        try {
            String initUrl = "https://bfhldevapigw.healthrx.co.in/hiring/generateWebhook/JAVA";

            HttpHeaders initHeaders = new HttpHeaders();
            initHeaders.setContentType(MediaType.APPLICATION_JSON);

            Map<String, String> payload = new HashMap<>();
            payload.put("name", "Radhika Karma");
            payload.put("regNo", "0827CI221111");
            payload.put("email", "radhikakarma220428@acropolis.in");
            HttpEntity<Map<String, String>> request = new HttpEntity<>(payload, initHeaders);

            ResponseEntity<Map> initResponse = restTemplate.postForEntity(initUrl, request, Map.class);

            String webhookUrl = (String) initResponse.getBody().get("webhook");
            String accessToken = (String) initResponse.getBody().get("accessToken");

            System.out.println("Webhook URL: " + webhookUrl);
            System.out.println("Access Token: " + accessToken);


            String finalQuery = "SELECT department, COUNT(*) AS total_employees FROM employees GROUP BY department";  // Replace with actual

            // Step 3: Submit final query
            HttpHeaders postHeaders = new HttpHeaders();
            postHeaders.setContentType(MediaType.APPLICATION_JSON);
            postHeaders.setBearerAuth(accessToken);

            Map<String, String> answerPayload = new HashMap<>();
            answerPayload.put("finalQuery", finalQuery);

            HttpEntity<Map<String, String>> answerRequest = new HttpEntity<>(answerPayload, postHeaders);

            ResponseEntity<String> response = restTemplate.postForEntity(webhookUrl, answerRequest, String.class);


            System.out.println("Submission Response: " + response.getBody());

        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}
