package com.criticalityworkbench.randcomphandler;

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

		void printName(){
			System.out.println("EnsScore name: " + name);
		}

    public String toString(){
			String out = "";
			out += "name: " + name + " ";
			for(EnsLocation el : locations)
				out += el.toString();
      return out; 
		}

	  void print(){
        System.out.println(this.toString());
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

    public String toString(){
			String out = "";
			out += "name: " + name + " ";
			out += "median: " + median;
      return out; 
		}

	  void print(){
        System.out.println(this.toString());
		}

		int upCount = 0, downCount = 0;

		int getDownCount(){
			return downCount;
		}

		int getUpCount(){
			return upCount;
		}

		void setDownCount(int val){
			downCount = val;
		}

		void setUpCount(int val){
			upCount = val;
		}


		EnsLocation(){}

		public int size(){
			return triples.size();
		}

		
		void replicate(double[] averages, int i, double value, int num_vals, int maxErrorPoints){
			for(int j = i; j < i + num_vals; j ++){
				averages[j] = value;
			}
			for(int j = i + num_vals; j < i + maxErrorPoints; j ++){
				averages[j] = 0;
			}
		}

		double[] clearZeros(double[] averages){
			int numZeros = 0;			
			for(int i = 0; i < averages.length; i ++){
				if(averages[i] == 0)
					numZeros ++;
			}

			double[] out = new double[averages.length - numZeros];
			int curIndex = 0;			
			for(int i = 0; i < averages.length; i ++){
				if(averages[i] != 0){
					out[curIndex] = averages[i];
					curIndex ++;
				}
			}
			return out;
		}

		int getMaxErrorPoints(){
			int out = 0;
			for(T triple : triples){
				if(out < triple.count)
					out = triple.count;
			}
			return out;
		}
	
		void calculateMedian(){

			int maxErrorPoints = getMaxErrorPoints();

			double[] averages = new double[triples.size()*maxErrorPoints];
			for(int i = 0; i < triples.size(); i ++){
				replicate(averages, 
					i*maxErrorPoints, 
					triples.get(i).avg, 
					triples.get(i).count,
					maxErrorPoints);
			}
			//System.out.println("averages.length " + averages.length);
			//averages = clearZeros(averages);
			//System.out.println("averages.length " + averages.length);


			Arrays.sort(averages);
			if (averages.length % 2 == 0 && averages.length >= 2){
				median = (averages[averages.length/2] + averages[averages.length/2 - 1])/2.0;
			} else if(averages.length % 2 == 1) {
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
			for(T triple : triples){
				if((int)(triple.location + 0.000000001) == locationCount)
					return triple;
			}
			return null;
		}

		void printName(){
			System.out.println("EnsLocation name: " + name);
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
		while(locationsWithScores.size() > 0){
			Location location = locationsWithScores.remove(locationsWithScores.size() - 1);

		//for(Location location : locationsWithScores){
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
						locTriple.addTimeCount(location.timeCount);
						locTriple.addFailCount(location.failCount);
					}
				}
			}
		}
	}

	static int getCountFromLocation(Location location, String methodName){
		DefinedLocation dLoc = location.getDefinedLocationFromName(methodName); 
		if(dLoc != null){
			return (int) (dLoc.location + 0.0000000000001);
		}
		return -1;

	}

	double getCriticality(String scoreName, String methodName, Location location){
		String newScoreName = scoreName; //"Absolute_Logarithm_Value";
		EnsScore score = getScore(newScoreName);
		if(score != null) {
			EnsLocation ensLocation = score.getLocation(methodName);
			if(ensLocation != null){
				int locationCount = getCountFromLocation(location, methodName);
				T theTriple = ensLocation.getTriple(locationCount);
				if(theTriple != null) {
					return theTriple.avg;
				} else {
					return 0.0;
				}
			}
		}

		return 0.0;
	}


	public boolean isValidLocation(String scoreName, String methodName, Location location){
		EnsScore score = getScore(scoreName);
		if(score != null) {
			EnsLocation ensLocation = score.getLocation(methodName);
			if(ensLocation != null){
				int locationCount = getCountFromLocation(location, methodName);
				return locationCount < ensLocation.size();
			}
		}

		return false;
	}

	double getMedian(String scoreName, String methodName){
		String newScoreName = scoreName; //"Absolute_Logarithm_Value";		

		EnsScore score = getScore(newScoreName);
		if(score != null) {
			EnsLocation ensLocation = score.getLocation(methodName);
			if(ensLocation != null) {
				return ensLocation.getMedian();
			}
		}

		return Double.MAX_VALUE;

	}

	public void printCounts(){
		for(EnsScore score : scores){
			for(EnsLocation location : score.locations){
				System.out.println(score.name + " " 
					+ location.name + " " 
					+ location.getUpCount() + " " 
					+ location.getDownCount());
			}
			
		}
	}


	int getUpCountSum(){
		int out = 0;
		for(EnsScore score : scores){
			for(EnsLocation location : score.locations){
				out += location.getUpCount();
			}

		}
		return out;
	}

	int getDownCountSum(){
		int out = 0;
		for(EnsScore score : scores){
			for(EnsLocation location : score.locations){
				out += location.getDownCount();
			}

		}
		return out;
	}

	void setDownCount(String scoreName, String methodName, int val){
		EnsScore score = getScore(scoreName);
		if(score != null) {
			EnsLocation ensLocation = score.getLocation(methodName);
			if(ensLocation != null) {
				ensLocation.setDownCount(val);
			}
		}
	}

	void setUpCount(String scoreName, String methodName, int val){
		EnsScore score = getScore(scoreName);
		if(score != null) {
			EnsLocation ensLocation = score.getLocation(methodName);
			if(ensLocation != null) {
				ensLocation.setUpCount(val);
			}
		}
	}


	int getDownCount(String scoreName, String methodName){
		EnsScore score = getScore(scoreName);
		if(score != null) {
			EnsLocation ensLocation = score.getLocation(methodName);
			if(ensLocation != null) {
				return ensLocation.getDownCount();
			}
		}

		return 0;

	}

	int getUpCount(String scoreName, String methodName){
		EnsScore score = getScore(scoreName);
		if(score != null) {
			EnsLocation ensLocation = score.getLocation(methodName);
			if(ensLocation != null) {
				return ensLocation.getUpCount();
			}
		}

		return 0;

	}

	void resolveScores(){
		for(EnsScore score : scores) {
			//score.printName();
			for(EnsLocation location: score.locations) {
				//location.printName();
				for(T triple: location.triples) {
					triple.resolve();
					//triple.print();
				}
				location.calculateMedian();
			}
		}
	}

	void clearData(){
		for(EnsScore score : scores) {
			for(EnsLocation location: score.locations) {
				for(T triple: location.triples) {
					triple.clearData();
				}
			}
		}
	}

	DataEnsemble(){
		ctor = null;	
	}

	DataEnsemble(ArrayList<Location> locationsWithScores, Supplier<? extends T> ctor){
		this.ctor = Objects.requireNonNull(ctor);
		System.out.println("Adding scores to data ensemble for processed output");
		addScores(locationsWithScores);
		System.out.println("Processing data ensemble");
		resolveScores();
	}

}	
