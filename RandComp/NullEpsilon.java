package RandComp;


public class NullEpsilon implements EpsilonProbability{
	public double getProbability(String methodName, Location location){
		return 0.0;
	}

	public void printName(){
		System.out.println("NullEpsilon");

	}

}
