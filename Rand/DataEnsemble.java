import java.util.*;

public class DataEnsemble{
	public class Score{
		String name;
		ArrayList<Distance> distances;
	}

	ArrayList<Score> scores;

	public class Triple{
		double avg;
		double stdDev;
		double distance;
	}

	public class Distance {
		String name;
		ArrayList<Triple> triples;
	}

}	
