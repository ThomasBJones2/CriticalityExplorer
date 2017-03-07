package RandComp;

public interface EpsilonProbability {
	public double getProbability(String methodName, Location location);

	public String getName();

	public void setProbability(double probability);

	public double getLocation();

	public void printName(); 
}
