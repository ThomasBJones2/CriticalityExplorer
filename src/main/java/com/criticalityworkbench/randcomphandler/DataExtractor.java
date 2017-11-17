package com.criticalityworkbench.randcomphandler;


import au.com.bytecode.opencsv.CSVReader;

import java.util.*;
import java.io.*;

//This class is used to build graphs from raw experiment data files

public class DataExtractor {

	static ArrayList<Location> readInLocations = null; //new ArrayList<>();

	String imageRootDirectory = "./output_images/";
	String rawDataOutputDirectory = "./output/";
	String processedRootDirectory = "./output_processed/";
	String inputClassName, experimentClassName, experimentTypeName;
  String proxyMethodName = "";

	public static void main(String[] args){
		if(args[0].equals("h") || args[0].equals("H")){
			System.out.println("Welcome to the DataExtraction Service");
			System.out.println("There are two ways to use this service: \n" +
					"(1) java.jar <name> Input_Object Main_Object Experiment_Type  OR \n"+
				  "(2) java.jar <name> Input_Object Main_Object Experiment_Type Images_Directory "
 					+ "Data_output_directory");
		} else {

			DataExtractor theExtractor = new DataExtractor(args[0], args[1], args[2]);

			if(args.length == 5){
				theExtractor.imageRootDirectory = args[3];
				theExtractor.rawDataOutputDirectory = args[4];
			}

			for(int inputSize : Experimenter.inputSizes){
				theExtractor.readDataIn(inputSize);
			}

		}
	}

	DataExtractor(String inputClassName, 
			String experimentClassName, 
			String experimentTypeName){

			this.inputClassName = inputClassName; 
			this.experimentClassName = experimentClassName;	
			this.experimentTypeName = experimentTypeName;
			((Experimenter)Experimenter.getNewObject(experimentTypeName)).dropZeros();
	}

	DataExtractor(String inputClassName, 
			String experimentClassName, 
			String experimentTypeName,
			String rawDataOutputDirectory,
			String imageRootDirectory){
      this(inputClassName, experimentClassName, experimentTypeName);
			this.rawDataOutputDirectory = rawDataOutputDirectory;
			this.imageRootDirectory = imageRootDirectory;
	}

	DataExtractor(String inputClassName, 
			String experimentClassName, 
			String experimentTypeName,
			String rawDataOutputDirectory,
			String imageRootDirectory,
			String proxyMethodName){
      this(inputClassName, 
					experimentClassName, 
					experimentTypeName,
					rawDataOutputDirectory,
					imageRootDirectory);
			this.proxyMethodName = proxyMethodName;
	}


	public static void printReadInData(){
			for(Location location : readInLocations){
				location.print();
			}
	}


  public String createReadFile(int inputSize){
		String ic = inputClassName.split("[.]")[inputClassName.split("[.]").length - 1];
		String ec = experimentClassName.split("[.]")[experimentClassName.split("[.]").length - 1];
		String et = experimentTypeName.split("[.]")[experimentTypeName.split("[.]").length - 1];
    String pmn = "";
		if(proxyMethodName.split("[.]").length >= 2) 
	      pmn = proxyMethodName.split("[.]")[proxyMethodName.split("[.]").length - 2] + "." + 
					proxyMethodName.split("[.]")[proxyMethodName.split("[.]").length - 1] + "_";
 
		return ic + "_" +
			ec + "_" +
			pmn + 
			et + "_" + 
			inputSize;
	}


	public void readDataIn(int inputSize){
    String inputFile = rawDataOutputDirectory + createReadFile(inputSize) + ".csv";


	  if(readInLocations == null)	
		    readInLocations = new ArrayList<>();

    System.out.println("readInLocations length: " + readInLocations.size());

		try{
			CSVReader inputReader = new CSVReader(new FileReader(new File(inputFile)));

			//List<String[]> allLocations = inputReader.readAll();
			
			String[] location = inputReader.readNext();
	
			System.out.println("Reading in data at readDataIn() from file " + inputFile);
			while(location != null){
				if (!location[0].equals("#")){
					Location nextLocation = Location.buildFromStringArray(location);
					if(nextLocation != null){
						readInLocations.add(nextLocation);
					}
				}
				location = inputReader.readNext();
			}
			inputReader.close();

		} catch (IOException e){
			System.out.println("The file doesn't exist ---" + 
				" you've probably not run the experiment" + e); 
		}
	}
}
