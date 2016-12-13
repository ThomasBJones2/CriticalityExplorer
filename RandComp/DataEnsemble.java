package RandComp;

import java.util.*;
import java.lang.*;

public class DataEnsemble{

	public class EnsScore{
		String name;
		ArrayList<EnsLocation> locations = new ArrayList<>();
	
		EnsScore(Score score){
			name = score.name;
		}

		EnsLocation getLocation(DefinedLocation locLocation){
			for(EnsLocation location : locations){
				if(location.name.equals(locLocation.name))
					return location;
			}
			return null;
		}
	
	}

	ArrayList<EnsScore> scores = new ArrayList<>();

	public class EnsTriple{
		double avg;
		double stdErr;
		double location;
		ArrayList<Double> dataPoints = new ArrayList<>();

		void addScore(Score score){
			dataPoints.add(score.score);
		}

		double average(){
			double out = 0;
			for(double dp : dataPoints){
				out += dp;
			}
			out /= dataPoints.size();
			return out;
		}

		public void print(){
			System.out.println("Ens Triple: ");
			System.out.println(location);
			System.out.println(avg);
			System.out.println(stdErr);
		}

		double standardErr(){
			double out = 0;
			for(double dp : dataPoints){
				out += (dp - avg)*(dp - avg);
			}
			out /= dataPoints.size();
			return Math.sqrt(out/dataPoints.size());
		}

		void resolve(){
			avg = average();
			stdErr = standardErr();
		}

		boolean nearEqual(double a, double b){
			return Math.abs(a - b) < 0.0000000001;
		}

		@Override
		public boolean equals(Object in){
			if(in == null) return false;
			if(!(in instanceof DefinedLocation)) return false;
			return nearEqual(location, ((DefinedLocation) in).location);
		}
	}

	public class EnsLocation {
		String name;
		ArrayList<EnsTriple> triples = new ArrayList<>();

		EnsLocation(DefinedLocation location){
			name = location.name;
		}

		EnsTriple getTriple(DefinedLocation dLocation){
			for(EnsTriple triple : triples){
				if(triple.equals(dLocation))
					return triple;
			}
			return null;
		}
	}

	EnsScore getScore(Score locScore){
		for(EnsScore score: scores){
			if (score.name.equals(locScore.name))
				return score;
		}
		return null;
	}

	public void addScores(ArrayList<Location> locationsWithScores){
		int count = 0;
		for(Location location : locationsWithScores){
			//System.out.println("on location " + count);
			count ++;
			for(Score score : Arrays.asList(location.scores)){

				DefinedLocation dLocation = location.getFailedLocation();
			//	for(DefinedLocation dLocation : location.dLocations){
				
				EnsScore locScore = getScore(score);
				if(locScore == null){
					locScore = new EnsScore(score);
					scores.add(locScore);
				}

				EnsLocation locLocation = locScore.getLocation(dLocation);
				if(locLocation == null){
					locLocation = new EnsLocation(dLocation);
					locScore.locations.add(locLocation);
				}

				EnsTriple locTriple = locLocation.getTriple(dLocation);
				if(locTriple == null){
					locTriple = new EnsTriple();
					locTriple.location = dLocation.location;
					locLocation.triples.add(locTriple);
				}
				locTriple.addScore(score);
				//}
			}
		}
	}

	void resolveScores(){
		for(EnsScore score : scores)
			for(EnsLocation location: score.locations)
				for(EnsTriple triple: location.triples)
					triple.resolve();
	}

	DataEnsemble(ArrayList<Location> locationsWithScores){
		System.out.println("Adding scores to data ensemble for processed output");
		addScores(locationsWithScores);
		System.out.println("Processing data ensemble");
		resolveScores();
	}

}	
