package RandComp;

import java.util.Random;

public class RunId{
	int runName;
	boolean experiment;
	boolean errorful;
	int errorPoint;
	long threadId;
	String methodName;
	String scoreName;

	Random rand;

	RunId(int runName, 
			boolean experiment, 
			boolean errorful, 
			int errorPoint, 
			long threadId, 
			Random rand,
			String fallibleMethodName,
			String scoreName){
		this.runName = runName;
		this.errorful = errorful;
		this.threadId = threadId;
		this.errorPoint = errorPoint;
		this.experiment = experiment;
		this.rand = rand;
		this.methodName = fallibleMethodName;
		this.scoreName = scoreName;
	}

	RunId(long threadId){
		this.threadId = threadId;
	}

	public int getRunName(){
		return runName;
	}

	public boolean getErrorful(){
		return errorful;
	}

	public boolean getExperiment(){
		return experiment;
	}

	public int getErrorPoint(){
		return errorPoint;
	}

	public long getThreadId(){
		return threadId;
	}

	public Random getRand(){
		return rand;
	}

	public void setErrorful(boolean isErrorful){
		this.errorful = isErrorful;
	}

	public void setExperiment(boolean isExperiment){
		this.experiment = isExperiment;
	}

	public void print(){
		System.out.println();
		System.out.println("runName: " + runName);
		System.out.println("errorful: " + errorful);
		System.out.println("errorPoint: " + errorPoint);
		System.out.println("threadId: " + threadId);
		System.out.println("scoreName: " + scoreName);	
		System.out.println();
	}

	@Override
	public boolean equals(Object in){
		if(in == null) return false;
		if(!(in instanceof RunId)) return false;
		return threadId == ((RunId) in).threadId; 
	}
}
