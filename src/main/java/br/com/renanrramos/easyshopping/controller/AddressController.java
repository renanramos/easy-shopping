/**------------------------------------------------------------
 * Project: easy-shopping
 * 
 * Creator: renan.ramos - 12/07/2020
 * ------------------------------------------------------------
 */
package br.com.renanrramos.easyshopping.controller;

import java.net.URI;
import java.util.List;
import java.util.Optional;

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

import br.com.renanrramos.easyshopping.model.Address;
import br.com.renanrramos.easyshopping.model.Customer;
import br.com.renanrramos.easyshopping.model.dto.AddressDTO;
import br.com.renanrramos.easyshopping.model.form.AddressForm;
import br.com.renanrramos.easyshopping.service.impl.AddressService;
import br.com.renanrramos.easyshopping.service.impl.CustomerService;

/**
 * @author renan.ramos
 *
 */
@RestController
@RequestMapping(path = "addresses", produces = "application/json")
public class AddressController {

	@Autowired
	private AddressService addressService;
	
	@Autowired
	private CustomerService customerService;
	
	private URI uri;
	
	@ResponseBody
	@PostMapping
	@Transactional
	public ResponseEntity<AddressDTO> saveAddress(@Valid @RequestBody AddressForm addressForm, UriComponentsBuilder uriBuilder) {
		Optional<Customer> customerOptional = customerService.findById(addressForm.getCustomerId());
		
		if (customerOptional.isPresent()) {
			
			Customer customer = customerOptional.get();
			Address address = AddressForm.convertAddressFormToAddress(addressForm);
			address.setCustomer(customer);
			address = addressService.save(address);
			uri = uriBuilder.path("/addresses/{id}").buildAndExpand(address.getId()).encode().toUri();
			
			return ResponseEntity.created(uri).body(AddressDTO.convertAddressToAddressDTO(address));
			
		} else {
			
			// TODO: throws an exception here
		}
		return ResponseEntity.badRequest().build();
	}
	
	@ResponseBody
	@GetMapping
	public ResponseEntity<List<AddressDTO>> getAddresses() {
		List<Address> addresses = addressService.findAll();
		return ResponseEntity.ok(AddressDTO.convertAddressListToAddressDTOList(addresses));	
	}
	
	@ResponseBody
	@GetMapping(path = "/{id}")
	public ResponseEntity<AddressDTO> getAddressById(@PathVariable("id") Long addressId) {
		Optional<Address> addressOptional = addressService.findById(addressId);
		
		if(addressOptional.isPresent()) {
			Address address = addressOptional.get();
			return ResponseEntity.ok(AddressDTO.convertAddressToAddressDTO(address));
		}
		
		return ResponseEntity.notFound().build();
	}
	
	
	@ResponseBody
	@PutMapping(path = "/{id}")
	@Transactional
	public ResponseEntity<?> updateAddress(@PathVariable("id") Long addressId, @RequestBody AddressForm addressForm, UriComponentsBuilder uriBuilder) {
		
		Optional<Customer> customerOptional = customerService.findById(addressForm.getCustomerId());
		Customer customer;
		if (customerOptional.isPresent()) {
			customer = customerOptional.get();
		} else {
			
			// TODO: throws an exception here
			
			return null;
		}

		Optional<Address> addressOptional = addressService.findById(addressId);
		
		if (addressOptional.isPresent()) {
			
			Address address = AddressForm.convertAddressFormToAddress(addressForm);
			address.setId(addressId);
			address.setCustomer(customer);
			address = addressService.save(address);
			uri = uriBuilder.path("/addresses/{id}").buildAndExpand(address.getId()).encode().toUri();
			return ResponseEntity.created(uri).body(AddressDTO.convertAddressToAddressDTO(address));
		}
		return ResponseEntity.badRequest().build();
	}
	
	@ResponseBody
	@DeleteMapping(path = "/{id}")
	@Transactional
	public ResponseEntity<?> removeAddress(@PathVariable("id") Long addressId) {
		Optional<Address> addressOptional = addressService.findById(addressId);
		
		if (addressOptional.isPresent()) {
			addressService.remove(addressId);
		} else {
			// TODO throws an exception here
		}
		return ResponseEntity.badRequest().build();
	}

}