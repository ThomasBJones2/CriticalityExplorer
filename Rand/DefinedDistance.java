import java.util.*;

public class DefinedDistance {
	String name;
	double distance;

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
	}

	@Override
	public boolean equals(Object in){
		if(this == in) return true;
		if(in == null) return false;
		if(!(in instanceof DefinedDistance)) return false;
		DefinedDistance inMTC = (DefinedDistance)in;
		return name.equals(inMTC.name);
	}

	void increment(){
		distance ++;
	}

	public void print(){
		System.out.print(name + " " + distance + " ");
	}
}
