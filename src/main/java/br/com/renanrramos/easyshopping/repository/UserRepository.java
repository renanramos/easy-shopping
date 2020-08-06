/**------------------------------------------------------------
 * Project: easy-shopping
 * 
 * Creator: renan.ramos - 04/08/2020
 * ------------------------------------------------------------
 */
package br.com.renanrramos.easyshopping.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.renanrramos.easyshopping.model.User;

/**
 * @author renan.ramos
 *
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long>{
	
	List<User> getUserByEmail(String email);
}