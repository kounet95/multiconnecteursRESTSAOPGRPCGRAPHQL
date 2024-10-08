package kounettechnologie.kounet.accountservices;


import kounettechnologie.kounet.multiconnexions.web.CustomerSoapService;
import kounettechnologie.kounet.multiconnexions.web.CustomerWS;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableFeignClients
public class AccountServicesApplication {

    public static void main(String[] args) {
        SpringApplication.run(AccountServicesApplication.class, args);
    }

    @Bean
    RestTemplate restTemplate(){
        return new RestTemplate();
    }
    @Bean
    CustomerSoapService customerSoapService(){
        return new CustomerWS().getCustomerSoapServicePort();
    }
}
