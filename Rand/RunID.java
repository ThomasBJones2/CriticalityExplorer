public class RunID{
	int runName;
	boolean errorful;
	int errorPoint;
	long threadID;

	RunID(int runName, boolean errorful, int errorPoint, long threadID){
		this.runName = runName;
		this.errorful = errorful;
		this.threadID = threadID;
		this.errorPoint = errorPoint;
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
}
