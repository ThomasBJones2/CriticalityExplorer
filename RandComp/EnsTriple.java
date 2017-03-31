package RandComp;

import java.util.*;
import java.lang.*;


public class EnsTriple{
	double avg;
	double stdErr;
	double location;
	double median;
	double avgTimeCount;
	double avgFailCount;
	int count;
	ArrayList<Double> dataPoints = new ArrayList<>();
	ArrayList<Double> timeCounts = new ArrayList<>();
	ArrayList<Double> failCounts = new ArrayList<>();

	public void clearData(){
		dataPoints = null;
		failCounts = null;
		timeCounts = null;
	}

	double median(){
			double out = Double.MAX_VALUE;
			if(dataPoints != null){
				double[] values = new double[dataPoints.size()];
				for(int i = 0; i < dataPoints.size(); i ++){
					values[i] = dataPoints.get(i);
				}
				Arrays.sort(values);
				if (values.length % 2 == 0){
					out = (values[values.length/2] + values[values.length/2 - 1])/2.0;
				} else {
					out = values[values.length/2];
				}
			} 
			return out;
	}

	public EnsTriple build(){
		return new EnsTriple();
	}

	void addScore(Score score){
		dataPoints.add(score.score);
	}

	void addTimeCount(double timeCount){
		timeCounts.add(timeCount);
	}

	void addFailCount(double failCount){
		failCounts.add(failCount);
	}

	double average(ArrayList<Double> theDataPoints){
		double out = 0;
		for(double dp : theDataPoints){
			//if(dp == Double.POSITIVE_INFINITY)
				//System.out.println("Found one: " + dp + dataPoints.size());
			//System.out.println(dp);
			out += dp;
		}
		//System.out.println("out1: "  + out);
		if(Double.isFinite(out)){
			out /= theDataPoints.size();			
		}
		else {
			out = Double.MAX_VALUE;
		}
		//System.out.println("out2: " + out);
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
		if(Double.isFinite(out)){
			out /= dataPoints.size();
			return Math.sqrt(out/dataPoints.size());
		} else {
			return Double.MAX_VALUE;
		}
	}

	void resolve(){
		avg = average(dataPoints);
		avgFailCount = average(failCounts);
		avgTimeCount = average(timeCounts);
		stdErr = standardErr();
		count = dataPoints.size();
		median = median();
		clearData();
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

