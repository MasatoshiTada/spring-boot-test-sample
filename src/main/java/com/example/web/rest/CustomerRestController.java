package com.example.web.rest;

import com.example.persistence.entity.Customer;
import com.example.service.CustomerService;
import com.example.web.request.CustomerRequest;
import com.example.web.response.CustomerResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/customers")
public class CustomerRestController {

    private final CustomerService customerService;

    public CustomerRestController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping
    public List<CustomerResponse> findAll() {
        List<Customer> customerList = customerService.findAll();
        List<CustomerResponse> customerResponseList = customerList.stream()
                .map(customer -> new CustomerResponse(customer))
                .collect(Collectors.toList());
        return customerResponseList;
    }

    @PostMapping
    public ResponseEntity<?> save(@RequestBody CustomerRequest customerRequest) {
        Customer customer = customerRequest.convertToEntity();
        customerService.save(customer);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .pathSegment(customer.getId().toString())
                .build().encode().toUri();
        return ResponseEntity.created(location).build();
    }
}
