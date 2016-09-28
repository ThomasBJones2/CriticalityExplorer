public class Score{
	String name;
	double score;

	Score(double score, String name){
		this.name = name;
		this.score = score;
	}		

	double getScore(){
		return score;
	}

	String getName(){
		return name;
	}

	public void print(){
		System.out.println(name + " " + score);
	}
}	
