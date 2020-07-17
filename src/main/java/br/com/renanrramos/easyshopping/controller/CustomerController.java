/**------------------------------------------------------------
 * Project: easy-shopping
 * 
 * Creator: renan.ramos - 25/06/2020
 * ------------------------------------------------------------
 */
package br.com.renanrramos.easyshopping.controller;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.renanrramos.easyshopping.constants.messages.ExceptionMessagesConstants;
import br.com.renanrramos.easyshopping.model.Customer;
import br.com.renanrramos.easyshopping.model.dto.CustomerDTO;
import br.com.renanrramos.easyshopping.model.form.CustomerForm;
import br.com.renanrramos.easyshopping.service.impl.CustomerService;

/**
 * @author renan.ramos
 *
 */
@RestController
@RequestMapping(path = "customers", produces = "application/json")
public class CustomerController {
	
	@Autowired
	private CustomerService customerService;
	
	private URI uri;
	
	@ResponseBody
	@Transactional
	@PostMapping
	public ResponseEntity<CustomerDTO> saveCustomer(@Valid @RequestBody CustomerForm customerForm, UriComponentsBuilder uriBuilder) {
		Customer customer = CustomerForm.converterCustomerFormToCustomer(customerForm);
		Customer customerCreated = customerService.save(customer);
		if (customerCreated.getId() != null) {
			uri = uriBuilder.path("/customers/{id}").buildAndExpand(customerCreated.getId()).encode().toUri();
			return ResponseEntity.created(uri).body(CustomerDTO.converterToCustomerDTO(customer));
		}
		return ResponseEntity.noContent().build();
	}
	
	@ResponseBody
	@GetMapping
	public ResponseEntity<List<CustomerDTO>> getCustomers() {		
		List<Customer> customers = customerService.findAll();
		if (customers.isEmpty()) {
			return ResponseEntity.noContent().build();
		}		
		return ResponseEntity.ok(CustomerDTO.converterCustomerListToCustomerDTOList(customers));
	}
	
	@ResponseBody
	@GetMapping(path = "/{id}")
	public ResponseEntity<CustomerDTO> getCustomerById(@PathVariable("id") Long customerId) {
		Optional<Customer> customer = customerService.findById(customerId);
		if (customer.isPresent()) {
			return ResponseEntity.ok(CustomerDTO.converterToCustomerDTO(customer.get()));
		}
		return ResponseEntity.notFound().build();
	}

	@ResponseBody
	@PutMapping(path = "/{id}")
	@Transactional
	public ResponseEntity<CustomerDTO> updateCustomer(@PathVariable("id") Long customerId, @RequestBody CustomerForm customerForm, UriComponentsBuilder uriBuilder) {
		Optional<Customer> customerToUpdate = customerService.findById(customerId);
		if(customerToUpdate.isPresent()) {			
			Customer customer = CustomerForm.converterCustomerFormToCustomer(customerForm);
			customer.setId(customerId);
			CustomerDTO customerUpdatedDTO = CustomerDTO.converterToCustomerDTO(customerService.save(customer));
			uri = uriBuilder.path("/customers/{id}").buildAndExpand(customerId).encode().toUri();
			return ResponseEntity.accepted().location(uri).body(customerUpdatedDTO);
		} else {
			throw new EntityNotFoundException(ExceptionMessagesConstants.ACCOUNT_NOT_FOUND);
		}
	}
	
	
	@ResponseBody
	@DeleteMapping("/{id}")
	@Transactional
	public ResponseEntity<?> removeCustomer(@PathVariable("id") Long customerId) {
		Optional<Customer> customerToRemove = customerService.findById(customerId);
		if (customerToRemove.isPresent()) {
			customerService.remove(customerId);
			return ResponseEntity.ok().build();
		} else {
			throw new EntityNotFoundException(ExceptionMessagesConstants.ACCOUNT_NOT_FOUND);
		}
	}	
}
