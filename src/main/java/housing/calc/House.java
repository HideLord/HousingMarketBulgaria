package housing.calc;

import java.io.Serializable;

public class House implements Serializable{
	private static final long serialVersionUID = 1L;
	
	public int priceEuro;
	public int sqM;
	public int floor;
	public String neighbourhood;
	public String city;
	public String fullListingUrl;
	
	double getPricePerSqM() {
		return (double)priceEuro / (double)sqM;
	}
}
