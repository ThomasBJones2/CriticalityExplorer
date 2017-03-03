package RandComp;


public class NullEpsilon implements EpsilonProbability{
	public double getProbability(String methodName, Location location){
		return 0.0;
	}

}
