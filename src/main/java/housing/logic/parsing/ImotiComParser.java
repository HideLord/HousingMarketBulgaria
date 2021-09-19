package housing.logic.parsing;

import java.io.IOException;
import java.rmi.UnexpectedException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import housing.logic.House;

public class ImotiComParser implements HouseParser {

	@Override
	public List<House> parse(String _url) {	
		Document page;
		try {
			page = Jsoup.connect(_url).get();
		} catch (IOException e) {
			System.out.println("ERROR: could not connect to: " + _url);
			return null;
		}
		List<House> _houses = new ArrayList<House>();
		
		Elements elements = page.getElementsByClass("info");
		
		System.out.println("INFO: parsing page from url: " + page.location() + "\n\tFound " + elements.size() + " potential candidates.");
		
		for(Element element : elements) { 
			House newHouse = parseInfoClass(element);
			if(newHouse != null)
				_houses.add(newHouse);
		}
		
		return _houses;
	}
	
	private static final Pattern LOCATION_SQM_PATTERN = Pattern.compile("град\\s+(.*),\\s*(.*)\\s+(\\d+)\\s*кв\\.м");
	private static final Pattern FLOOR_PATTERN = Pattern.compile("(?:\\s|^)(\\d+)[\\s-]*(?:ви|ри|ти|ми)\\s*ет");
	
	/**
	 * Parses an info element and returns the house inside it.
	 */
	private House parseInfoClass(Element _info) {
		try {
			House thisHouse = new House();
			
			thisHouse.fullListingUrl = _info.parent().child(0).absUrl("href");
			
			Elements priceElements = _info.getElementsByClass("price");
			if(priceElements.size() == 0) {
				priceElements = _info.parent().getElementsByClass("price"); // for normal non-top offers, the price is in the parent.
				if(priceElements.size() == 0)
					throw new UnexpectedException("No price elements");
			}
			
			thisHouse.priceEuro = Integer.valueOf(priceElements.get(0).text().replaceAll("[^0-9]", ""));
			
			Elements locElements = _info.getElementsByClass("location");
			if(priceElements.size() == 0)
				throw new UnexpectedException("No location elements");
			
			String location = locElements.get(0).text();
			
			Matcher m = LOCATION_SQM_PATTERN.matcher(location);
			if(m.find()) {
				thisHouse.city = m.group(1);
				thisHouse.neighborhood = m.group(2);
				thisHouse.sqM = Integer.valueOf(m.group(3));
			}
			
			m = FLOOR_PATTERN.matcher(_info.text());
			if(m.find()) {
				thisHouse.floor = Integer.valueOf(m.group(1));
			}
			
			return thisHouse;
		} catch(Exception ex) {
			System.out.println("WARNING: Could not parse house from this element: " + ex.getMessage());
			return null;
		}
	}
}
