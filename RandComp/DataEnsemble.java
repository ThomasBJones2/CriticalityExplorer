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
	
	}

	ArrayList<EnsScore> scores = new ArrayList<>();


	public class EnsLocation {
		String name;
		ArrayList<T> triples = new ArrayList<>();

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
			for(Score score : location.scores){

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

	void resolveScores(){
		for(EnsScore score : scores)
			for(EnsLocation location: score.locations)
				for(T triple: location.triples)
					triple.resolve();
	}

	DataEnsemble(ArrayList<Location> locationsWithScores, Supplier<? extends T> ctor){
		this.ctor = Objects.requireNonNull(ctor);
		System.out.println("Adding scores to data ensemble for processed output");
		addScores(locationsWithScores);
		System.out.println("Processing data ensemble");
		resolveScores();
	}

}	
