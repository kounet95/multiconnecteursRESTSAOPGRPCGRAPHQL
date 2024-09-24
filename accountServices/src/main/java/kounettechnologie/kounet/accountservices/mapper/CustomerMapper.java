package kounettechnologie.kounet.accountservices.mapper;

import kounettechnologie.kounet.accountservices.model.Customer;
import kounettechnologie.kounet.multiconnexions.stub.CustomerServiceOuterClass;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class CustomerMapper {
    private ModelMapper modelMapper=new ModelMapper();
    public Customer fromSoapCustomer(kounettechnologie.kounet.multiconnexions.web.Customer soapCustomer){
        return modelMapper.map(soapCustomer,Customer.class);
    }
  public Customer fromGrpcCustomer(CustomerServiceOuterClass.Customer grpcCustomer){
        return modelMapper.map(grpcCustomer, Customer.class);
    }
}
