package com.example.reservationservice;

import lombok.Data;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Collection;
import java.util.stream.Stream;

@EnableDiscoveryClient
@SpringBootApplication
public class ReservationServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ReservationServiceApplication.class, args);
	}
	@Bean
	CommandLineRunner loadData(ReservationRepository repository) {
		return args-> {
			Stream.of("Scott","Derek","Debra","Carly")
					.forEach( n -> repository.save(new Reservation(n)));
		};
	}
}

@RefreshScope
@RestController
class MessageController {
    @org.springframework.beans.factory.annotation.Value("${message}")
    private String message;

    @GetMapping("/message")
    String getMessage() {
       return  this.message;
    }
}

@Data
@Entity
class Reservation {
	@Id
    @GeneratedValue
    long id;

    public Reservation() {
    }

    public Reservation(String reservationName) {
        this.reservationName = reservationName;
    }

    String reservationName;
}

@RepositoryRestResource
interface ReservationRepository extends JpaRepository<Reservation,Long>{
    @RestResource(path="by-name")
    Collection<Reservation> findByReservationName(@Param("rn") String rn);

}
