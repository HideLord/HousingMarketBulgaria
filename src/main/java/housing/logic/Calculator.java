package housing.logic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Calculator {
	public static void averagePricePerFloor(List<House> _houses) {
		List<Double> avg = new ArrayList<>();
		List<Integer> num = new ArrayList<>();
		for(House house : _houses) {
			if(house.floor == null)
				continue;
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
	
	public static void averagePrice(List<House> _houses) {
		double avg = 0.0;
		for(House house : _houses)
			avg += house.getPricePerSqM();
		System.out.println("Average price per sq.m:\n >> " + avg/_houses.size() + " euro");
	}
	
	public static void averagePricePerNeighborhood(List<House> _houses) {
		Map<String, List<House>> map = new HashMap<>();
		for(House house : _houses) {
			if(!map.containsKey(house.neighborhood)) {
				map.put(house.neighborhood, new ArrayList<House>());
			}
			map.get(house.neighborhood).add(house);
		}
		
		for(Map.Entry<String, List<House>> entry: map.entrySet()) {
			System.out.println("------------------------------------------\n вартал " + entry.getKey() + ":");
			averagePrice(entry.getValue());
			averagePricePerFloor(entry.getValue());
		}
	}
	
	private static class ParetoItem implements Comparable<ParetoItem> {
		public int index;
		public int price;
		public int area;
		public int layer = -1;
		
		@Override
		public int compareTo(ParetoItem other) {
			return this.price - other.price;
		}
	}
	
	private static List<ParetoItem> paretoLayer(ParetoItem[] _items, int _layer) {
		int maxi = -1;
		List<ParetoItem> layer = new ArrayList<>();
		
		for(int i = 0; i < _items.length; ++i) {
			if(_items[i].layer >= 0 || maxi > _items[i].area) // bigger price and smaller area
				continue;
			_items[i].layer = _layer;
			layer.add(_items[i]);
			maxi = _items[i].area;
		}
		
		return layer;
	}
	
	private static ParetoItem[] getParetoItems(List<House> _houses) {
		ParetoItem[] items = new ParetoItem[_houses.size()];
		
		for(int i = 0; i < items.length; ++i) {
			items[i] = new ParetoItem();
			items[i].area = _houses.get(i).sqM;
			items[i].price = _houses.get(i).priceEuro;
			items[i].index = i;
		}
		
		Arrays.sort(items);
		
		return items;
	}
	
	public static boolean[] getParetoFrontMask(List<House> _houses) {
		boolean[] isOptimal = new boolean[_houses.size()];
		
		ParetoItem[] items = getParetoItems(_houses);
		List<ParetoItem> layer = paretoLayer(items, 0);
		
		for(int i = 0; i < layer.size(); ++i)
			isOptimal[layer.get(i).index] = true;
		
		return isOptimal;
	}
	
	public static int[] getParetoLayers(List<House> _houses) {
		int[] paretoLayer = new int[_houses.size()];
		ParetoItem[] items = getParetoItems(_houses);
		
		for(int layerIndex = 0; true; ++layerIndex) {
			List<ParetoItem> layer = paretoLayer(items, layerIndex);
			
			if(layer.isEmpty())
				break;
			
			for(ParetoItem item:layer) {
				paretoLayer[item.index] = layerIndex;
			}
		}
		
		return paretoLayer;
	}
}
