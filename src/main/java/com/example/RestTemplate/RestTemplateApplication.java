package com.example.RestTemplate;

import com.example.RestTemplate.model.User;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.GsonBuilderUtils;
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
            users.forEach(System.out::println);
        } else {
            System.out.println("Пользователи не найдены ");
        }
    }
}