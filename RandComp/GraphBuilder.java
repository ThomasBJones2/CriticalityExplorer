package RandComp;

import au.com.bytecode.opencsv.CSVReader;

import java.util.*;
import java.io.*;

//This class is used to build graphs from raw experiment data files

public class GraphBuilder{

	static ArrayList<Location> readInLocations;

	static String imageRootDirectory = "./output_images/";
	static String rawDataOutputDirectory = "./output/";
	static String processedRootDirectory = "./output_processed/";
	static String inputClassName, experimentClassName, experimentTypeName;

	public static void main(String[] args){
		if(args[0].equals("h") || args[0].equals("H")){
			System.out.println("Welcome to the GraphBuilding Service");
			System.out.println("There are two ways to use this service: \n" +
					"(1) java.jar <name> Input_Object Main_Object Experiment_Type  OR \n"+
				  "(2) java.jar <name> Input_Object Main_Object Experiment_Type Images_Directory "
 					+ "Data_output_directory");
		} else {

			inputClassName = args[0]; 
			experimentClassName = args[1];	
			experimentTypeName = args[2];

			if(args.length == 5){
				imageRootDirectory = args[3];
				rawDataOutputDirectory = args[4];
			}

			for(int inputSize = 10; inputSize <= 1000; inputSize *= 10){
				readDataIn(inputSize);
				printAllProcessedData(
					new DataEnsemble<EnsTriple>(readInLocations,EnsTriple::new ), inputSize);
			}

		}
		

	}

	public static void printReadInData(){
			for(Location location : readInLocations){
				location.print();
			}
	}


	public static void readDataIn(int inputSize){
		String inputFile = rawDataOutputDirectory + 
			inputClassName + 
			experimentClassName + 
			experimentTypeName + 
			inputSize + ".csv";
		
		readInLocations = new ArrayList<>();

		try{
			CSVReader inputReader = new CSVReader(new FileReader(new File(inputFile)));

			List<String[]> allLocations = inputReader.readAll();

			for(String[] location : allLocations){
				readInLocations.add(Location.buildFromStringArray(location));
			}

		} catch (IOException e){
			System.out.println("The file doesn't exist ---" + 
				" you've probably not run the experiment" + e); 

		}
	}

	private static void clearOutputOnInputSize (String directoryName, 
			int input_size, 
			String fileTerminal){
		File directory = new File(directoryName);
		for(File f: directory.listFiles())
	    if(f.getName().endsWith(input_size + fileTerminal))
				f.delete();
	}


	private static String createFile(String directory,
			String scoreName,
			String locationName,
			int inputSize){
			return directory + 
						scoreName + "_on_" +
						locationName + "_" +
						inputSize;
	}

	private static void printOutput(String scoreName, 
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
	
	private static void printNoSDCGraph(String scoreName, String locationName,
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

	private static void printGraph(String scoreName, String locationName,
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
	private static void printAllNoSDCData(DataEnsemble<NoSDCEnsTriple> dataEnsemble, 
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

	private static void printAllProcessedData(DataEnsemble<EnsTriple> dataEnsemble, 
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
