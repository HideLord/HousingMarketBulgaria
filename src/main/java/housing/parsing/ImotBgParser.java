package housing.parsing;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import housing.calc.House;

public class ImotBgParser implements HouseParser {
	
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
		
		Elements elements = page.getElementsByClass("Price");
		
		System.out.println("INFO: parsing page from url: " + page.location() + "\n\tFound " + elements.size() + " potential candidates.");
		
		for(Element element : elements) { 
			House newHouse = parsePriceClass(element);
			if(newHouse != null)
				_houses.add(newHouse);
		}
		
		return _houses;
	}
	
	private static final Pattern LOCATION_PATTERN = Pattern.compile("град\\s*(.*),\\s*(.*)");
	private static final Pattern FLOOR_PATTERN = Pattern.compile("(?:\\s|^)(\\d+)[\\s-]*(?:ви|ри|ти|ми)\\s*ет");
	private static final Pattern SQM_PATTERN = Pattern.compile("(?:\s|^)(\\d+)\\s*кв\\.м");
	
	private House parsePriceClass(Element _priceElement) {
		try {
			House thisHouse = new House();
			thisHouse.priceEuro = Integer.valueOf(_priceElement.text().replaceAll("[^0-9]", ""));
			
			for(Element el : _priceElement.parent().children()) {
				Matcher m = LOCATION_PATTERN.matcher(el.text());
				if(m.find()) {
					thisHouse.city = m.group(1);
					thisHouse.neighbourhood = m.group(2);
					thisHouse.fullListingUrl = el.absUrl("href");
					break;
				}
			}
			
			String description = _priceElement.parent().parent().nextElementSibling().child(0).text();
			
			Matcher m = FLOOR_PATTERN.matcher(description);
			if(m.find()) {
				thisHouse.floor = Integer.valueOf(m.group(1));
			}
			
			m = SQM_PATTERN.matcher(description);
			if(m.find()) {
				thisHouse.sqM = Integer.valueOf(m.group(1));
			}
			
			return thisHouse;
		} catch(Exception ex) {
			System.out.println("WARNING: Could not parse house from this element: " + ex.getMessage());
			return null;
		}
	}

}
