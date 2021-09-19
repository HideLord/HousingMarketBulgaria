package housing.logic.parsing;

import java.util.List;

import housing.logic.House;

public interface HouseParser {
	public List<House> parse(String _url);
}
