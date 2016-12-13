package RandComp;

import java.util.*;

public class Location{
	int runName;
	long threadId;
	int timeCount = 0;
	Score[] scores;
	ArrayList<DefinedLocation> dLocations = new ArrayList<>();
	String failedMethod;


	Location burnIn;
	
	Location(int runName, long threadId){
		this.runName = runName;
		this.threadId = threadId;
	}	

	Location(Location in){
		this.runName = in.runName;
		this.threadId = in.threadId;
		this.timeCount = in.timeCount;
		this.dLocations = new ArrayList<>();
		for(DefinedLocation d : in.dLocations){
			this.dLocations.add(new DefinedLocation(d));
		}
		this.failedMethod = in.failedMethod;
	}

	public void clearPertinence(){
		for(int i = 0; i < dLocations.size(); i ++)
			dLocations.get(i).pertinent = false;
	}

	public Score[] get_scores(){
		return scores;
	}
	
	void burnIn(String failedMethod){
		burnIn = new Location(this);
		burnIn.failedMethod = failedMethod;
	}

	public Location getBurnIn(){
		return burnIn;
	}

	@Override
	public boolean equals(Object in){
		if(in == null) return false;
		if(!(in instanceof Location)) return false;
		return threadId == ((Location) in).threadId; 
	}

	public ArrayList<DefinedLocation> getDefinedLocations(){
		return dLocations;
	}

	public DefinedLocation getFailedLocation(){
		return getDefinedLocationFromName(failedMethod);
	}

	public DefinedLocation getDefinedLocationFromName(String name){
		for(DefinedLocation d : dLocations){
			if (d.name.equals(name)) return d;
		}
		return null;
	}

	public void print(){
		if(burnIn != null) {
			burnIn.print();
		} else {
			System.out.print(timeCount + " ");
			for(DefinedLocation d : dLocations){
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
