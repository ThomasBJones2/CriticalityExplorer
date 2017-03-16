package RandComp;

import java.util.*;
import java.lang.*;
import java.util.function.*;

public class DataEnsemble<T extends EnsTriple>{

	private final Supplier<? extends T> ctor;

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

		EnsLocation getLocation(String locationName){
			for(EnsLocation location : locations){
				if(location.name.equals(locationName))
					return location;
			}
			return null;
		}
	
	}

	ArrayList<EnsScore> scores = new ArrayList<>();


	public class EnsLocation {
		String name;
		double median = Double.MAX_VALUE;
		ArrayList<T> triples = new ArrayList<>();


		public int size(){
			return triples.size();
		}
	
		void calculateMedian(){
			double[] averages = new double[triples.size()];
			for(int i = 0; i < triples.size(); i ++){
				averages[i] = triples.get(i).avg;
			}
			Arrays.sort(averages);
			if (averages.length % 2 == 0){
				median = (averages[averages.length/2] + averages[averages.length/2 - 1])/2.0;
			} else {
				median = averages[averages.length/2];
			}

		}

		double getMedian(){
			return median;
		}

		EnsLocation(DefinedLocation location){
			name = location.name;
		}

		T getTriple(DefinedLocation dLocation){
			for(T triple : triples){
				if(triple.equals(dLocation))
					return triple;
			}
			return null;
		}

		T getTriple(int locationCount){
			if(triples.size() > locationCount){
				return triples.get(locationCount);
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

	EnsScore getScore(String scoreName){
		for(EnsScore score: scores){
			if (score.name.equals(scoreName))
				return score;
		}
		return null;
	}

	public void addScores(ArrayList<Location> locationsWithScores){
		int count = 0;
		for(Location location : locationsWithScores){
			//System.out.println("on location " + count);
			count ++;
			for(Score score : location.scores){
				//System.out.println("looking at score: " + score.name);

				for(DefinedLocation dLocation : location.dLocations){
					if(dLocation.pertinent){
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

						T locTriple = locLocation.getTriple(dLocation);
						if(locTriple == null){
							locTriple = ctor.get();
							locTriple.location = dLocation.location;
							locLocation.triples.add(locTriple);
						}
						locTriple.addScore(score);
					}
				}
			}
		}
	}

	double getCriticality(String scoreName, String methodName, Location location){
		EnsScore score = getScore(scoreName);
		if(score != null) {
			EnsLocation ensLocation = score.getLocation(methodName);

			int locationCount = (int)location.getDefinedLocationFromName(methodName).location;

			T theTriple = ensLocation.getTriple(locationCount);
			if(theTriple != null) {
				return theTriple.avg;
			} else {
				return 0.0;
			}
		}

		return 0.0;
	}


	public boolean isValidLocation(String scoreName, String methodName, Location location){
		EnsScore score = getScore(scoreName);
		if(score != null) {
			EnsLocation ensLocation = score.getLocation(methodName);

			int locationCount = (int)location.getDefinedLocationFromName(methodName).location;

			return ensLocation.size() < locationCount;
		}

		return false;
	}

	double getMedian(String scoreName, String methodName){
		EnsScore score = getScore(scoreName);
		if(score != null) {
			EnsLocation ensLocation = score.getLocation(methodName);
			if(ensLocation != null) {
				return ensLocation.getMedian();
			}
		}

		return Double.MAX_VALUE;

	}

	void resolveScores(){
		for(EnsScore score : scores) {
			for(EnsLocation location: score.locations) {
				for(T triple: location.triples) {
					triple.resolve();
				}
				location.calculateMedian();
			}
		}
	}

	DataEnsemble(ArrayList<Location> locationsWithScores, Supplier<? extends T> ctor){
		this.ctor = Objects.requireNonNull(ctor);
		System.out.println("Adding scores to data ensemble for processed output");
		addScores(locationsWithScores);
		System.out.println("Processing data ensemble");
		resolveScores();
	}

}	
