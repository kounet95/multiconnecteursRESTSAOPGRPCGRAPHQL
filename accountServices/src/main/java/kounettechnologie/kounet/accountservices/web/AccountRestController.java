package kounettechnologie.kounet.accountservices.web;


import kounettechnologie.kounet.accountservices.feign.CustomerRestClient;
import kounettechnologie.kounet.accountservices.mapper.CustomerMapper;
import kounettechnologie.kounet.accountservices.model.Customer;
import kounettechnologie.kounet.multiconnexions.stub.CustomerServiceGrpc;
import kounettechnologie.kounet.multiconnexions.stub.CustomerServiceOuterClass;
import kounettechnologie.kounet.multiconnexions.web.CustomerSoapService;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.client.HttpGraphQlClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/account-service")
public class AccountRestController {
   @Autowired
   private RestTemplate restTemplate;
    @Autowired
    private CustomerMapper customerMapper;
    @Autowired
    private CustomerRestClient customerRest;
    @Autowired
    private CustomerSoapService customerSoapService;
    @GrpcClient(value = "customerService")
    private CustomerServiceGrpc.CustomerServiceBlockingStub customerServiceBlockingStub;
//par la methode bloquante
    @GetMapping("/customers")
    public List<Customer> listCustomers(){

        //RestTemplate restTemplate=new RestTemplate();
        Customer[] customers = restTemplate.getForObject(
                "http://localhost:8084/customer", Customer[].class);

       return List.of(customers);
    }

    //par la methode non-bloquante
    @GetMapping("/customers/v2")
    public Flux<Customer> customerLists(){
            //RestTemplate restTemplate=new RestTemplate();
            WebClient webClient= WebClient.builder()
                    .baseUrl("http://localhost:8084")
                    .build();
            Flux<Customer> customerFlux = webClient.get()
                    .uri("/customer")
                    .retrieve().bodyToFlux(Customer.class);
            return customerFlux;
        }


    @GetMapping("/customers/{id}")
    public Customer customerById(@PathVariable Long id){
        Customer customer = restTemplate.getForObject("http://localhost:8084/customer/" + id, Customer.class);
        return customer;
    }


    @GetMapping("/customers/v2/{id}")
    public Mono<Customer> customerByIdV2(@PathVariable Long id){
        WebClient webClient= WebClient.builder()
                .baseUrl("http://localhost:8084")
                .build();
        Mono<Customer> customerMono = webClient.get()
                .uri("/customers/{id}",id)
                .retrieve().bodyToMono(Customer.class);
        return customerMono;
    }
    @GetMapping("/customers/v3")
    public List<Customer> listCustomersV3(){
        return customerRest.getCustomers();
    }

    @GetMapping("/customers/v3/{id}")
    public Customer customerByIdV3(@PathVariable Long id){
        return customerRest.getCustomerById(id);
    }


    @GetMapping("/gql/customers/{id}")
    public Mono<Customer> customerByIdGql(@PathVariable Long id){
        HttpGraphQlClient graphQlClient=HttpGraphQlClient.builder()
                .url("http://localhost:8084/graphql")
                .build();
        var httpRequestDocument= """
                 query($id:Int) {
                    customerById(id:$id){
                      id, name, email
                    }
                  }
                """;
        Mono<Customer> customerById = graphQlClient.document(httpRequestDocument)
                .variable("id",id)
                .retrieve("customerById")
                .toEntity(Customer.class);
        return customerById;
    }

    @GetMapping("/gql/customers")
    public Mono<List<Customer>> customerListGql(){
        HttpGraphQlClient graphQlClient=HttpGraphQlClient.builder()
                .url("http://localhost:8084/graphql")
                .build();
        var httpRequestDocument= """
                 query {
                     allCustomers{
                       name,email, id
                     }
                   }
                """;
        Mono<List<Customer>> customers = graphQlClient.document(httpRequestDocument)
                .retrieve("allCustomers")
                .toEntityList(Customer.class);
        return customers;
    }



    @GetMapping("/soap/customers")
    public List<Customer> soapCustomers(){
        List<kounettechnologie.kounet.multiconnexions.web.Customer> soapCustomers = customerSoapService.customerList();
        return soapCustomers.stream().map(customerMapper::fromSoapCustomer).collect(Collectors.toList());
    }
    @GetMapping("/soap/customerById/{id}")
    public Customer customerByIdSoap(@PathVariable Long id){
       kounettechnologie.kounet.multiconnexions.web.Customer soapCustomer = customerSoapService.customerById(id);
        return customerMapper.fromSoapCustomer(soapCustomer);
    }


    @GetMapping("/grpc/customers")
    public List<Customer> grpcCustomers(){
        CustomerServiceOuterClass.GetAllCustomersRequest request =
                CustomerServiceOuterClass.GetAllCustomersRequest.newBuilder().build();
        CustomerServiceOuterClass.GetCustomersResponse response =
                customerServiceBlockingStub.getAllCustomers(request);
        List<CustomerServiceOuterClass.Customer> customersList = response.getCustomersList();
        List<Customer> customerList =
                customersList.stream().map(customerMapper::fromGrpcCustomer).collect(Collectors.toList());
        return customerList;
    }
    @GetMapping("/grpc/customers/{id}")
    public Customer grpcCustomerById(@PathVariable Long id){
        CustomerServiceOuterClass.GetCustomerByIdRequest request =
                CustomerServiceOuterClass.GetCustomerByIdRequest.newBuilder()
                        .setCustomerId(id)
                        .build();
        CustomerServiceOuterClass.GetCustomerByIdResponse response =
                customerServiceBlockingStub.getCustomerById(request);
        return customerMapper.fromGrpcCustomer(response.getCustomer());
    }

}
