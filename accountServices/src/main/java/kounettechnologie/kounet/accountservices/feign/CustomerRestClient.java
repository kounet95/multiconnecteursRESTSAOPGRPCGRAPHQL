package kounettechnologie.kounet.accountservices.feign;


import kounettechnologie.kounet.accountservices.model.Customer;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "customer-rest-client", url = "http://localhost:8084")
public interface CustomerRestClient {
    @GetMapping("/customer")
    List<Customer> getCustomers();
    @GetMapping("/customer/{id}")
    Customer getCustomerById( @PathVariable(name = "id") Long id);

}
