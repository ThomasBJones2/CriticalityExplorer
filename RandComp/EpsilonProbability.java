package RandComp;

public interface EpsilonProbability {
	public double getProbability(String ScoreName, String methodName, Location location);

	public String getName();

	public void setProbability(double probability);

	public double getLocation();

	public void setInputSize(int inputSize);

	public void printName(); 
}
