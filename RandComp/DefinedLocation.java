package RandComp;

import java.util.*;

public class DefinedLocation {
	String name;
	double location;
	public boolean pertinent = false;



	DefinedLocation(String mName){
		name = mName;
		location = 0;
	}

	DefinedLocation(String mName, int tCount){
		name = mName;
		location = (double)tCount;
	}

	DefinedLocation(String mName, double tCount){
		name = mName;
		location = tCount;
	}


	DefinedLocation(DefinedLocation in){
		this.name = in.name;
		this.location = in.location;
		this.pertinent = in.pertinent;
	}

	@Override
	public boolean equals(Object in){
		if(this == in) return true;
		if(in == null) return false;
		if(!(in instanceof DefinedLocation)) return false;
		DefinedLocation inMTC = (DefinedLocation)in;
		return name.equals(inMTC.name);
	}

	public void increment(){
		location ++;
	}

	public double getLocation(){
		return location;
	}

	public String getName(){
		return name;
	}

	public void setLocation(double location){
		this.location = location;
	}

	public void print(){
		System.out.print(this.toString() + " ");
	}

	@Override
	public String toString(){
		return name + " " + location;
	}
}
