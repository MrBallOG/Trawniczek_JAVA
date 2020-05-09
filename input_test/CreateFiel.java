public class CreateFiel {

	public static void main(String[] args) {

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
	}

}
