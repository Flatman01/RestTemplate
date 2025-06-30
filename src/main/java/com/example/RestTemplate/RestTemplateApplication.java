package com.example.RestTemplate;

import com.example.RestTemplate.model.User;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@SpringBootApplication
public class RestTemplateApplication {

    public static void main(String[] args) {
        SpringApplication.run(RestTemplateApplication.class, args);

        RestTemplate restTemplate = new RestTemplate();
        String url = "http://94.198.50.185:7081/api/users";


        ResponseEntity<List<User>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<User>>() {
                }
        );
        List<String> cookies = response.getHeaders().get(HttpHeaders.SET_COOKIE);
        String sessionId = null;
        if (cookies != null) {
            for (String cookie : cookies) {
                if (cookie.startsWith("JSESSIONID=")) {
                    sessionId = cookie.split("=")[1];
                    break;
                }
            }
        }
        System.out.println("Номер сессии: " + sessionId);
        System.out.println("Cookie: " + cookies);
        List<User> users = response.getBody();
        if (users != null) {
            for (User user : users) {
                System.out.println(user);
            }
        }

        if (sessionId != null) {
            User newUser = new User();
            newUser.setId(3L);
            newUser.setName("James");
            newUser.setLastName("Brown");
            newUser.setAge((byte) 24);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.add("Cookie", "JSESSIONID=" + sessionId);
            HttpEntity<User> entity = new HttpEntity<>(newUser, headers);
            ResponseEntity<String> postResponse = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    entity,
                    String.class
            );
            System.out.println(postResponse.getBody());
        }

        User updateUser = new User();
        updateUser.setId(3L);
        updateUser.setName("Thomas");
        updateUser.setLastName("Shelby");
        updateUser.setAge((byte) 24);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Cookie", "JSESSIONID=" + sessionId);
        HttpEntity<User> entity = new HttpEntity<>(updateUser, headers);
        ResponseEntity<String> postResponse = restTemplate.exchange(
                url,
                HttpMethod.PUT,
                entity,
                String.class
        );
        System.out.println(postResponse.getBody());

        HttpHeaders deleteUser = new HttpHeaders();
        deleteUser.setContentType(MediaType.APPLICATION_JSON);
        deleteUser.add("Cookie", "JSESSIONID=" + sessionId);
        HttpEntity<String> deleteEntity = new HttpEntity<>(deleteUser);
        ResponseEntity<String> deleteResponse = restTemplate.exchange(
                url + "/3",
                HttpMethod.DELETE,
                deleteEntity,
                String.class
        );
        System.out.println(deleteResponse.getBody());
    }

}
