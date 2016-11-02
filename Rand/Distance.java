import java.util.*;

public class Distance{
	int runName;
	long threadId;
	int timeCount = 0;
	Score[] scores;
	ArrayList<DefinedDistance> dDistances = new ArrayList<>();

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
	}

	public Score[] get_scores(){
		return scores;
	}
	
	void burnIn(){
		burnIn = new Distance(this);
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
