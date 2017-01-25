package RandComp;

import java.util.*;
import java.lang.*;


public class EnsTriple{
	double avg;
	double stdErr;
	double location;
	int count;
	ArrayList<Double> dataPoints = new ArrayList<>();

	public EnsTriple build(){
		return new EnsTriple();
	}

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
		count = dataPoints.size();
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

