package housing.logic;

import java.io.Serializable;

public class House implements Serializable{
	private static final long serialVersionUID = 1L;
	
	public int priceEuro;
	public int sqM;
	public Integer floor; // can be null if there is no floor in the listing
	public String neighborhood;
	public String city;
	public String fullListingUrl;
	
	public double getPricePerSqM() {
		return (double)priceEuro / (double)sqM;
	}
}
