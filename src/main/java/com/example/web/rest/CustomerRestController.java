package com.example.web.rest;

import com.example.persistence.entity.Customer;
import com.example.service.CustomerService;
import com.example.web.request.CustomerRequest;
import com.example.web.response.CustomerResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 顧客情報に関するRESTコントローラークラスです。
 */
@RestController
@RequestMapping("/api/customers")
public class CustomerRestController {

    private final CustomerService customerService;

    public CustomerRestController(CustomerService customerService) {
        this.customerService = customerService;
    }

    /**
     * 顧客情報を全件取得するRESTコントローラーメソッドです。
     * @return 顧客情報リスト + 200 OK
     */
    @GetMapping
    public List<CustomerResponse> findAll() {
        List<Customer> customerList = customerService.findAll();
        List<CustomerResponse> customerResponseList = customerList.stream()
                .map(customer -> new CustomerResponse(customer))
                .collect(Collectors.toList());
        return customerResponseList;
    }

    /**
     * 顧客情報を登録するRESTコントローラーメソッドです。
     * @param customerRequest
     * @return 201 Created
     */
    @PostMapping
    public ResponseEntity<?> save(@Validated @RequestBody CustomerRequest customerRequest) {
        Customer customer = customerRequest.toEntity();
        customerService.save(customer);
        URI location = URI.create("/api/customers/" + customer.id());
        return ResponseEntity.created(location).build();
    }
}
