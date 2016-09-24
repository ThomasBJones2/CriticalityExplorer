import java.util.Random;

public class RunID{
	int runName;
	boolean errorful;
	int errorPoint;
	long threadID;
	Random rand;

	RunID(int runName, boolean errorful, int errorPoint, long threadID, Random rand){
		this.runName = runName;
		this.errorful = errorful;
		this.threadID = threadID;
		this.errorPoint = errorPoint;
		this.rand = rand;
	}

	public int getRunName(){
		return runName;
	}

	public boolean getErrorful(){
		return errorful;
	}

	public int getErrorPoint(){
		return errorPoint;
	}

	public long getThreadID(){
		return threadID;
	}

	public void setErrorful(boolean isErrorful){
		this.errorful = isErrorful;
	}

	@Override
	public boolean equals(Object in){
		if(in == null) return false;
		if(!(in instanceof RunID)) return false;
		return threadID == ((RunID) in).threadID; 
	}
}
