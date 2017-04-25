import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class RandomExp {
	
	public RandomExp() {
		// TODO Auto-generated constructor stub
	}
	
	
	public double getRand(double lambda){
		return  Math.log(1-ThreadLocalRandom.current().nextDouble())/(-lambda);
	}
	
	public ArrayList<Double> getRandList(double lambda, long num){
		ArrayList<Double> listOfValues = new ArrayList<Double>();
		
		while(num > 0 ){
			listOfValues.add(getRand(lambda));
			num--;
		}
		return listOfValues;
		
	}
	
	public ArrayList<String> getRandListString(double lambda, long num){
		ArrayList<String> listOfValues = new ArrayList<String>();
		for(Double val : getRandList(lambda, num)){
			listOfValues.add(val.toString());
		}
		return listOfValues;
		
	}

}
