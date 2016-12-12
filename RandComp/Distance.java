package RandComp;

import java.util.*;

public class Distance{
	int runName;
	long threadId;
	int timeCount = 0;
	Score[] scores;
	ArrayList<DefinedDistance> dDistances = new ArrayList<>();
	String failedMethod;


	Distance burnIn;
	
	Distance(int runName, long threadId){
		this.runName = runName;
		this.threadId = threadId;
	}	

	Distance(Distance in){
		this.runName = in.runName;
		this.threadId = in.threadId;
		this.timeCount = in.timeCount;
		this.dDistances = new ArrayList<>();
		for(DefinedDistance d : in.dDistances){
			this.dDistances.add(new DefinedDistance(d));
		}
		this.failedMethod = in.failedMethod;
	}

	public void clearPertinence(){
		for(int i = 0; i < dDistances.size(); i ++)
			dDistances.get(i).pertinent = false;
	}

	public Score[] get_scores(){
		return scores;
	}
	
	void burnIn(String failedMethod){
		burnIn = new Distance(this);
		burnIn.failedMethod = failedMethod;
	}

	public Distance getBurnIn(){
		return burnIn;
	}

	@Override
	public boolean equals(Object in){
		if(in == null) return false;
		if(!(in instanceof Distance)) return false;
		return threadId == ((Distance) in).threadId; 
	}

	public ArrayList<DefinedDistance> getDefinedDistances(){
		return dDistances;
	}

	public DefinedDistance getFailedDistance(){
		return getDefinedDistanceFromName(failedMethod);
	}

	public DefinedDistance getDefinedDistanceFromName(String name){
		for(DefinedDistance d : dDistances){
			if (d.name.equals(name)) return d;
		}
		return null;
	}

	public void print(){
		if(burnIn != null) {
			burnIn.print();
		} else {
			System.out.print(timeCount + " ");
			for(DefinedDistance d : dDistances){
				d.print();
			}
			System.out.println();
		}
	}

	public void addBurnInScores(Score[] in){
		if(burnIn == null)
			System.out.println("Null Burn In");
		else
			this.burnIn.addScores(in);
	}

	public void addScores(Score[] in){
		this.scores = in;
	}
}
