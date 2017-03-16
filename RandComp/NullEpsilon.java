package RandComp;


public class NullEpsilon implements EpsilonProbability{
	double probability = 0.0;
	
	
	public double getProbability(String scoreName, String methodName, Location location){
		return probability;
	}

	public void setProbability(double probability){
		this.probability = probability;
	}

	public double getLocation(){
		return this.probability;
	}

	public String getName(){
		return "NullEpsilon";
	}

	public void setInputSize(int inputSize){}

	public void printName(){
		System.out.println("NullEpsilon");

	}

}
