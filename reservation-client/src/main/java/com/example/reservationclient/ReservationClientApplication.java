package com.example.reservationclient;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Bean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.Resources;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@EnableZuulProxy
@EnableDiscoveryClient
@SpringBootApplication
@EnableFeignClients
public class ReservationClientApplication {

    public static void main(String[] args) {
        SpringApplication.run(ReservationClientApplication.class, args);
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

}

@RefreshScope
@RestController
@RequestMapping("/reservations")
class ReservationApiGetwayController {


    @Autowired
    EmployeeProxy proxy;

    @GetMapping("/names")
    public List<String> getReservationNames() throws IOException {
        String str = proxy.findAll();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(str);
        JsonNode rs = rootNode.path("_embedded").path("reservations");

        return Arrays.asList(objectMapper.treeToValue(rs, Reservation[].class)).stream()
                .map( Reservation::getReservationName).collect(Collectors.toList());
    }
    @GetMapping(path = {"/",""})
    public List<Reservation> getReservations() throws IOException {
        String str = proxy.findAll();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(str);
        JsonNode rs = rootNode.path("_embedded").path("reservations");

        return Arrays.asList(objectMapper.treeToValue(rs, Reservation[].class));
    }

}


@FeignClient(name = "reservation-service")
interface EmployeeProxy {
    @RequestMapping("/reservations")
    public String findAll();

}

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
class Reservation {
    String reservationName;
}


