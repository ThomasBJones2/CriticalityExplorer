package com.criticalityworkbench.randcomphandler;

public class RunTimeTriple<E>{
	String name;
	E runTime;
	E errorPoints;

  public String toString(){
    return "name: " + name + " runTime: " 
				+ runTime + " errorPoints: " + errorPoints;
	}

	public void print(){
		System.out.println(this.toString());
	}

	public RunTimeTriple(E runTime, E errorPoints){
		this.runTime = runTime;
		this.errorPoints = errorPoints;
	}
	
	public RunTimeTriple(RunTimeTriple<E> in){
		name = in.name;
		runTime = in.runTime;
		errorPoints = in.errorPoints;
	}

	public void copy(RunTimeTriple<E> in){
		name = in.name;
		runTime = in.runTime;
		errorPoints = in.errorPoints;
	}
}
