package main;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;

import housing.gui.MainFrame;
import housing.logic.Calculator;
import housing.logic.House;
import housing.logic.parsing.HouseParser;
import housing.logic.parsing.ImotiComParser;

public class Main {
	static final String VARNA = "https://www.imot.bg/pcgi/imot.cgi?act=3&slink=74cqub&f1=";
	static final String VARNA_NEW = "https://www.imoti.com/prodazhbi/grad-varna/dvustaini/page-";
	static final String SOFIA = "https://www.imot.bg/pcgi/imot.cgi?act=3&slink=74c7m8&f1=";
	static final String SOFIA_NEW = "https://www.imoti.com/prodazhbi/grad-sofiya/dvustaini/page-";
	static final String PLOVDIV = "https://www.imot.bg/pcgi/imot.cgi?act=3&slink=74ctpr&f1=";
	static final String POLVDIV_NEW = "https://www.imoti.com/prodazhbi/grad-plovdiv/dvustaini/page-";
	
	private static List<House> parsePages(String _url, HouseParser _parser, int _numPages) throws IOException {
		List<House> houses = new ArrayList<House>();
		for(int i = 1; i <= _numPages; ++i)
			houses.addAll(_parser.parse(_url + i));
		return houses;
	}
	
	private static void saveTo(String _filePath, List<House> _houses) throws IOException {
		FileOutputStream fout = new FileOutputStream(_filePath);
		ObjectOutputStream oos = new ObjectOutputStream(fout);
		oos.writeObject(_houses);
		oos.close();
	}
	
	private static List<House> loadFrom(String _filePath) throws ClassNotFoundException, IOException {
		FileInputStream fin = new FileInputStream(_filePath);
	    ObjectInputStream ois = new ObjectInputStream(fin);
	    @SuppressWarnings("unchecked")
		List<House> houses = (ArrayList<House>) ois.readObject();
	    ois.close();
	    return houses;
	}
	
	static void run() {
		try {
			List<House> houses = parsePages(SOFIA_NEW, new ImotiComParser(), 100);
			saveTo("houses.data", houses);
			
			Calculator calc = new Calculator();
			
			System.out.println("Parsed " + houses.size() + " houses");
			
			calc.averagePricePerFloor(houses);
			calc.averagePrice(houses);
			System.out.println("\n\n");
			calc.averagePricePerNeighborhood(houses);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	static void runGui() throws ClassNotFoundException, IOException {
		MainFrame frame = new MainFrame();
		frame.reset(loadFrom("houses.data"));
	}
	
	public static void main(String[] args) throws Exception {
		//run();
		runGui();
	}
}
