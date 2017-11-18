package com.criticalityworkbench.randcomphandler;

import au.com.bytecode.opencsv.CSVReader;

import java.util.*;
import java.io.*;

//This class is used to build graphs from raw experiment data files

public class GraphBuilder extends DataExtractor {
  boolean use_decompose = false;


	public static void main(String[] args){
		if(args[0].equals("h") || args[0].equals("H")){
			System.out.println("Welcome to the GraphBuilding Service");
			System.out.println("There are two ways to use this service: \n" +
					"(1) java.jar <name> Input_Object Main_Object Experiment_Type  OR \n"+
				  "(2) java.jar <name> Input_Object Main_Object Experiment_Type Images_Directory "
 					+ "Data_output_directory <input_size> <processed_data_directory>");
		} else {


			GraphBuilder theBuilder = new GraphBuilder(args[0], args[1], args[2]);

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

			System.out.println("List of input arguments in build graph: ");
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

	GraphBuilder(String inputClassName, String experimentClassName, String experimentTypeName){
			super(inputClassName, experimentClassName, experimentTypeName);
	}

	private static void clearOutputOnInputSize (String directoryName, 
			int input_size, 
			String fileTerminal){
		File directory = new File(directoryName);
		for(File f: directory.listFiles())
	    if(f.getName().endsWith(input_size + fileTerminal))
				f.delete();
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

	private void printOutput(String scoreName, 
			double score, 
			Double stdErr,
			Integer count,
			String locationName, 
			double location, 
			String rootDirectoryName,
			int inputSize) {
		try(FileWriter fw = 
					new FileWriter(createFile(rootDirectoryName, 
						scoreName,
						locationName,
						inputSize) + ".csv", true);

				BufferedWriter bw = 
					new BufferedWriter(fw);
				PrintWriter out = 
					new PrintWriter(bw))
		{
			if(stdErr == null)
				out.println(location + ", " + score);	
			else
				out.println(location + ", " + score + ", " + stdErr + ", " + count);
		} catch (IOException E){
			System.out.print(E);
		}

	}

	private static String cleanString(String in){
		return  in.replaceAll("\\$","\\\\\\$").replaceAll("\\ ", "\\\\ ");
	}
	
	private void printNoSDCGraph(String scoreName, String locationName,
																	String outputDirectory,
																	int inputSize,
																	double[][] plottable){

		String outputName = createFile(outputDirectory,
																		scoreName,
																		locationName,
																		inputSize) +  ".png";

		System.out.println("creating no SDC Count pdfs for " + outputName);
		NoSDCPlotter plotter = new NoSDCPlotter(outputName, 
										scoreName,
										locationName,
										plottable);
		plotter.plot();		
	}

	private void printGraph(String scoreName, String locationName,
																	String inputDirectory, 
																	String outputDirectory,
																	int inputSize,
																	double[][] plottable){

		String outputName = createFile(outputDirectory,
																		scoreName,
																		locationName,
																		inputSize) +  ".png";
		String numRunsOutputName = createFile(outputDirectory,
																		scoreName,
																		locationName,
																		inputSize) + "_Num_Runs" + ".png";


		System.out.println("creating pdfs for " + outputName);
		Plotter plotter = new Plotter(outputName, 
										scoreName,
										locationName,
										plottable);
		plotter.plot();		
		
		RunsPlotter plotter2 = new RunsPlotter(numRunsOutputName, 
										scoreName,
										locationName,
										plottable);
		plotter2.plot();		
		
	}
	private void printAllNoSDCData(DataEnsemble<NoSDCEnsTriple> dataEnsemble, 
			int inputSize
			){

		for(int i = 0; i < dataEnsemble.scores.size(); i ++) {
			DataEnsemble<NoSDCEnsTriple>.EnsScore score = dataEnsemble.scores.get(i);
			for(int j = 0; j < score.locations.size(); j ++){
				DataEnsemble<NoSDCEnsTriple>.EnsLocation location = score.locations.get(j);
				double[][] theData = new double[location.triples.size()][2];
				for(int q = 0; q < location.triples.size(); q ++) {
					NoSDCEnsTriple triple = location.triples.get(q);
					theData[q][0] = triple.location;
					theData[q][1] = triple.count;
				}
		
				//clearOutputOnInputSize(imageRootDirectory, inputSize, ".png");
				printNoSDCGraph(score.name, 
						location.name,  
						imageRootDirectory,
						inputSize,
						theData);
			}
		}

	}

	private void printAllProcessedData(DataEnsemble<EnsTriple> dataEnsemble, 
			int inputSize
			){

		System.out.println(dataEnsemble.scores.size());

		//clearOutputOnInputSize(processedRootDirectory, inputSize, ".csv");
		for(int i = 0; i < dataEnsemble.scores.size(); i ++) {
			DataEnsemble<EnsTriple>.EnsScore score = dataEnsemble.scores.get(i);
			for(int j = 0; j < score.locations.size(); j ++){
				DataEnsemble<EnsTriple>.EnsLocation location = score.locations.get(j);
				double[][] theData = new double[location.triples.size()][4];
				for(int q = 0; q < location.triples.size(); q ++) {
					EnsTriple triple = location.triples.get(q);
					printOutput(score.name,
						triple.avg,
						triple.stdErr,
						triple.count,
						location.name,
						triple.location,
						processedRootDirectory,
						inputSize);
					theData[q][0] = triple.location;
					theData[q][1] = triple.avg;
					theData[q][2] = triple.stdErr;
					theData[q][3] = triple.count;
				}
		
				//clearOutputOnInputSize(imageRootDirectory, inputSize, ".png");
				printGraph(score.name, 
						location.name, 
						processedRootDirectory, 
						imageRootDirectory,
						inputSize,
						theData);
			}
		}

	}
}
