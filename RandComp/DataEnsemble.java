package RandComp;

import java.util.*;
import java.lang.*;

public class DataEnsemble{

	public class EnsScore{
		String name;
		ArrayList<EnsDistance> distances = new ArrayList<>();
	
		EnsScore(Score score){
			name = score.name;
		}

		EnsDistance getDistance(DefinedDistance locDistance){
			for(EnsDistance distance : distances){
				if(distance.name.equals(locDistance.name))
					return distance;
			}
			return null;
		}
	
	}

	ArrayList<EnsScore> scores = new ArrayList<>();

	public class EnsTriple{
		double avg;
		double stdErr;
		double distance;
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
			System.out.println(distance);
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
			if(!(in instanceof DefinedDistance)) return false;
			return nearEqual(distance, ((DefinedDistance) in).distance);
		}
	}

	public class EnsDistance {
		String name;
		ArrayList<EnsTriple> triples = new ArrayList<>();

		EnsDistance(DefinedDistance distance){
			name = distance.name;
		}

		EnsTriple getTriple(DefinedDistance dDistance){
			for(EnsTriple triple : triples){
				if(triple.equals(dDistance))
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

	public void addScores(ArrayList<Distance> distancesWithScores){
		int count = 0;
		for(Distance distance : distancesWithScores){
			//System.out.println("on distance " + count);
			count ++;
			for(Score score : Arrays.asList(distance.scores)){

				DefinedDistance dDistance = distance.getFailedDistance();
			//	for(DefinedDistance dDistance : distance.dDistances){
				
				EnsScore locScore = getScore(score);
				if(locScore == null){
					locScore = new EnsScore(score);
					scores.add(locScore);
				}

				EnsDistance locDistance = locScore.getDistance(dDistance);
				if(locDistance == null){
					locDistance = new EnsDistance(dDistance);
					locScore.distances.add(locDistance);
				}

				EnsTriple locTriple = locDistance.getTriple(dDistance);
				if(locTriple == null){
					locTriple = new EnsTriple();
					locTriple.distance = dDistance.distance;
					locDistance.triples.add(locTriple);
				}
				locTriple.addScore(score);
				//}
			}
		}
	}

	void resolveScores(){
		for(EnsScore score : scores)
			for(EnsDistance distance: score.distances)
				for(EnsTriple triple: distance.triples)
					triple.resolve();
	}

	DataEnsemble(ArrayList<Distance> distancesWithScores){
		System.out.println("Adding scores to data ensemble for processed output");
		addScores(distancesWithScores);
		System.out.println("Processing data ensemble");
		resolveScores();
	}

}	
