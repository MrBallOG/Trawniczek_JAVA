package trawniczek;

import java.util.*;

public class Application {

	public static void main(String[] args) {
		
		/*
		Input in = new Input("D:\\pobrane\\lawn.txt");
		int i = in.readFromFile();
		System.out.println(i);
		
		if(i == 0) {
		char [][] arr = in.getLawn();
		
		for(int j = 0; j<arr.length; j++) {
			for(char ii : arr[j]) {
				System.out.print(ii);
			}
			System.out.println();
			}
		}
		System.out.println(in.getXSize() + " " + in.getYSize());
*/
		List <Sprinkler> s = new ArrayList<Sprinkler>();
		for(int j = 0; j<10; j++) {
			switch(j%4) {
			case 0:
				s.add(new Sprinkler(90, 52-j, j+100, 1));
				break;
			case 1:
				s.add(new Sprinkler(180, 58-j, j+10, 12));
				break;
			case 2:
				s.add(new Sprinkler(270, j+5, j, 3));
				break;
			case 3:
				s.add(new Sprinkler(360, 18-j, j+35, 1));
				break;
			}
		}
		
		Output out = new Output(s);
		out.printToFile();
		
	}

}
