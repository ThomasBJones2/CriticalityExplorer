import java.util.*;

public class DefinedDistance {
	String name;
	double distance;
	public boolean pertinent = false;



	DefinedDistance(String mName){
		name = mName;
		distance = 0;
	}

	DefinedDistance(String mName, int tCount){
		name = mName;
		distance = (double)tCount;
	}

	DefinedDistance(DefinedDistance in){
		this.name = in.name;
		this.distance = in.distance;
		this.pertinent = in.pertinent;
	}

	@Override
	public boolean equals(Object in){
		if(this == in) return true;
		if(in == null) return false;
		if(!(in instanceof DefinedDistance)) return false;
		DefinedDistance inMTC = (DefinedDistance)in;
		return name.equals(inMTC.name);
	}

	public void increment(){
		distance ++;
	}

	public double getDistance(){
		return distance;
	}

	public String getName(){
		return name;
	}

	public void setDistance(double distance){
		this.distance = distance;
	}

	public void print(){
		System.out.print(name + " " + distance + " ");
	}
}
