/**------------------------------------------------------------
 * Project: easy-shopping
 *
 * Creator: renan.ramos - 10/11/2020
 * ------------------------------------------------------------
 */
package br.com.renanrramos.easyshopping.model.dto;

import java.util.List;
import java.util.stream.Collectors;

import br.com.renanrramos.easyshopping.model.Stock;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * @author renan.ramos
 *
 */
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class StockDTO {

	private String name;

	private Long storeId;

	private String storeName;

	public StockDTO() {
		// Intentionally empty
	}

	public StockDTO(Stock stock) {
		this.name = stock.getName();
		this.storeId = stock.getStore().getId();
		this.storeName = stock.getStore().getName();
	}

	public static List<StockDTO> converterStockListToStockDTOList(List<Stock> stocks) {
		return stocks.stream().map(StockDTO::new).collect(Collectors.toList());

	}

	public static StockDTO converterStockToStockDTO(Stock stock) {
		return new StockDTO(stock);
	}
}