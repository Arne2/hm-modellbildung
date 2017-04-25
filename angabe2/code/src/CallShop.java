import java.io.FileWriter;
import java.io.IOException;

public class CallShop {

	public static void main(String[] args) {
		System.out.println("CallShop Application");
		
		
		
        
        
		
		// Create CSV file, maybe useful for Mathematica Calculations and Tests
		try {
			String csvFile = "NormalRand.csv";
	        FileWriter writer = new FileWriter(csvFile);
	        
	        // Object to create Random Numbers with exponential distribution
	        RandomExp myRand = new RandomExp();
	        
	        
	        // create one random number:
	        myRand.getRand(0.001);
	        
	        // create multiple (here: 100) random numbers
	        myRand.getRandList(0.001, 100);
	        
	        // create multiple (here: 100) random Numbers as String
	        myRand.getRandListString(0.001, 100);
	        
	        // create 1000 Random Values and write them in file, separated by ';'
	        // Warning: this code creates all Values in one Row
	        // To create multiple Lines -> call function writeLine() multiple times
	        CSVUtils.writeLine(writer, myRand.getRandListString(0.001, 1000), ';');
			writer.flush();
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
