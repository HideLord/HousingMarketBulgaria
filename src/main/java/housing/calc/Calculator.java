package housing.calc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Calculator {
	public void averagePricePerFloor(List<House> houses) {
		List<Double> avg = new ArrayList<>();
		List<Integer> num = new ArrayList<>();
		for(House house : houses) {
			while(avg.size() <= house.floor) {
				avg.add(0.0);
				num.add(0);
			}
			avg.set(house.floor, avg.get(house.floor) + house.getPricePerSqM());
			num.set(house.floor, num.get(house.floor) + 1);
		}

		System.out.println("Average price per sq.m per floor:");
		
		for(int i = 1; i < avg.size(); ++i) {
			if(num.get(i) > 0)
				System.out.println(" >> Floor " + i + ": " + (avg.get(i)/num.get(i)) + " euro");
		}
	}
	
	public void averagePrice(List<House> houses) {
		double avg = 0.0;
		for(House house : houses)
			avg += house.getPricePerSqM();
		System.out.println("Average price per sq.m:\n >> " + avg/houses.size() + " euro");
	}
	
	public void averagePricePerNeighborhood(List<House> houses) {
		Map<String, List<House>> map = new HashMap<>();
		for(House house : houses) {
			if(!map.containsKey(house.neighbourhood)) {
				map.put(house.neighbourhood, new ArrayList<House>());
			}
			map.get(house.neighbourhood).add(house);
		}
		
		for(Map.Entry<String, List<House>> entry: map.entrySet()) {
			System.out.println("------------------------------------------\n вартал " + entry.getKey() + ":");
			averagePrice(entry.getValue());
			averagePricePerFloor(entry.getValue());
		}
	}
}
