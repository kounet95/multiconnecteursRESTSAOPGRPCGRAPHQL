package kounettechnologie.kounet.multiconnexions;

import kounettechnologie.kounet.multiconnexions.entity.Customer;
import kounettechnologie.kounet.multiconnexions.repository.CustomerRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.UUID;

@SpringBootApplication
public class MulticonnexionsApplication {


    public static void main(String[] args) {
        SpringApplication.run(MulticonnexionsApplication.class, args);
    }
@Bean
    CommandLineRunner start(CustomerRepository customerRepository){
        return args -> {
               customerRepository.save(Customer.builder().name("kounet").email("kounet@gmail.com").build());
            customerRepository.save(Customer.builder().name("oumar").email("oumar@gmail.com").build());
            customerRepository.save(Customer.builder().name("diallo").email("diallo@gmail.com").build());
        };

}

}
