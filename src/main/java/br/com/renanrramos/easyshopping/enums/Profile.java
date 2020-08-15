/**------------------------------------------------------------
 * Project: easy-shopping
 * 
 * Creator: renan.ramos - 24/06/2020
 * ------------------------------------------------------------
 */
package br.com.renanrramos.easyshopping.enums;

import lombok.Getter;

@Getter
public enum Profile {

	CUSTOMER("CUSTOMER"),
	COMPANY("COMPANY"),
	ADMINISTRATOR("ADMINISTRATOR");

	private String role;
	
	Profile(String profile) {
		this.role = "ROLE_" + profile.toUpperCase();
	}

}
