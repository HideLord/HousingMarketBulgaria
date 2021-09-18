package housing.parsing;

import java.util.List;

import housing.calc.House;

public interface HouseParser {
	public List<House> parse(String _url);
}
