package com.criticalityworkbench.randcomphandler;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;
import java.util.*;
import java.io.*;

//This class is used to build graphs from raw experiment data files

public class NumBuilder extends DataExtractor {


  boolean use_decompose = false;


	public static void main(String[] args){
		if(args[0].equals("h") || args[0].equals("H")){
			System.out.println("Welcome to the NumberBuilding Service");
			System.out.println("There are two ways to use this service: \n" +
					"(1) java.jar <name> Input_Object Main_Object Experiment_Type  OR \n"+
				  "(2) java.jar <name> Input_Object Main_Object Experiment_Type "
 					+ "Data_output_directory");
		} else {


			NumBuilder theBuilder = new NumBuilder(args[0], args[1], args[2]);


			if(args.length >= 5){
				theBuilder.imageRootDirectory = args[3];
				theBuilder.rawDataOutputDirectory = args[4];
			}

			if(args.length >= 6){
				Experimenter.inputSizes = new int[1];
				Experimenter.inputSizes[0] = Integer.parseInt(args[5]);
			}

			if(args.length >= 7){
				theBuilder.processedRootDirectory = args[6];
			}

			if(args.length >= 8){
				if(!args[7].equals("None"))
          theBuilder.proxyMethodName = args[7];
			}

      if(args.length >= 9){
  			if(args[8].equals("Decompose")){
            theBuilder.use_decompose = true;
				} else {
					  theBuilder.use_decompose = false;
				}
			}

			System.out.println("List of input arguments in build nums: ");
			int v = 0;
			for(String arg : args) {
				System.out.println("argument " + v + ": " +arg);
				v ++;
			}
			System.out.println();
  


			for(int inputSize : Experimenter.inputSizes){
				theBuilder.readDataIn(inputSize);
				//Here is where we actually print stuff out!
				theBuilder.printAllProcessedData(
					new DataEnsemble<EnsTriple>(readInLocations,EnsTriple::new ), inputSize);
			}

		}
	}

	NumBuilder(String inputClassName, String experimentClassName, String experimentTypeName){
			super(inputClassName, experimentClassName, experimentTypeName);
	}


	private static String strip(String in){
		String[] outStrings = in.split("[.]");
		if(outStrings.length > 0)
			return outStrings[outStrings.length - 1];
		else
			return "";
	}


	private String createFile(String directory,
			String scoreName,
			String locationName,
			int inputSize){
		  
		  String decompose_name = "BaseRandom";
		  if(use_decompose)
				decompose_name = "Decompose";
			String new_location;
			if(locationName.split("[.]").length >= 2)
          new_location = locationName.split("[.]")[locationName.split("[.]").length - 2] 
					 	+ "." + locationName.split("[.]")[locationName.split("[.]").length - 1];
			else
				  new_location = locationName;
			return directory +
			      inputClassName.split("[.]")[inputClassName.split("[.]").length - 1] + "_" +
					  experimentClassName.split("[.]")[experimentClassName.split("[.]").length - 1] + "_" +
            scoreName.split("[.]")[scoreName.split("[.]").length - 1] + "_on_" +
						new_location + "_" +
						decompose_name + "_" + 
						inputSize;
	}



	private void printAllProcessedData(DataEnsemble<EnsTriple> dataEnsemble, 
			int inputSize
			){

		System.out.println(dataEnsemble.scores.size());

		//String[][] theData = new double[dataEnsembe.scores.maxLocationSize()][1 + dataEnsemble.scores.size()*2 + 2 + 2];
		//clearOutputOnInputSize(processedRootDirectory, inputSize, ".csv");
		for(int i = 0; i < dataEnsemble.scores.size(); i ++) {
			DataEnsemble<EnsTriple>.EnsScore score = dataEnsemble.scores.get(i);
			for(int j = 0; j < score.locations.size(); j ++){
				DataEnsemble<EnsTriple>.EnsLocation location = score.locations.get(j);

				String[][] theData = new String[location.triples.size() + 1][8];
				String[] headers = {"#location value,", 
					"score average at location,", 
					"score std error at location,",
					"number of data points,",
					"score median at location,",
					"median of all scores,",
					"average timeCount (number of fallible operations executed),",
					"average failCount (number of fallible operations that actually failed),"
				};

				theData[0] = headers;
				
				for(int q = 0; q < location.triples.size(); q ++) {
					EnsTriple triple = location.triples.get(q);
					theData[q+1][0] = "" + triple.location;
					theData[q+1][1] = "" + triple.avg;
					theData[q+1][2] = "" + triple.stdErr;
					theData[q+1][3] = "" + triple.count;
					theData[q+1][4] = "" + triple.median;
					theData[q+1][5] = "" + location.median;
					theData[q+1][6] = "" + triple.avgTimeCount;
					theData[q+1][7] = "" + triple.avgFailCount;
				}
		

				String fileName = createFile(processedRootDirectory, 
					location.name, 
					score.name,
					inputSize) + ".csv";

				System.out.println("Printing num build processed results to: " +
						fileName);
				try{
          
					CSVWriter outputWriter = new CSVWriter(new FileWriter(new File(fileName)), 
						' ',
						CSVWriter.NO_QUOTE_CHARACTER);
					for(String[] line : theData){
						outputWriter.writeNext(line);
					}
					outputWriter.close();
				}	catch (IOException e){

					System.out.println("Couldn't write output to file " + e);
				}	
			}
		}

	}
}
