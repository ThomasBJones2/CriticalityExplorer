package com.criticalityworkbench.randcomphandler;

import com.panayotis.gnuplot.GNUPlotParameters;
import com.panayotis.gnuplot.JavaPlot;
import com.panayotis.gnuplot.dataset.FileDataSet;
import com.panayotis.gnuplot.layout.StripeLayout;
import com.panayotis.gnuplot.plot.AbstractPlot;
import com.panayotis.gnuplot.plot.DataSetPlot;
import com.panayotis.gnuplot.style.NamedPlotColor;
import com.panayotis.gnuplot.style.PlotStyle;
import com.panayotis.gnuplot.style.Style;
import com.panayotis.gnuplot.swing.JPlot;
import com.panayotis.gnuplot.terminal.ImageTerminal;
import com.panayotis.gnuplot.utils.Debug;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import javax.swing.JFrame;
import javax.imageio.ImageIO;


public class Plotter{
	String outputFileName;
	String scoreName;
	String distanceName;
	double[][] plottable;

	public Plotter(String outputFileName,
							String scoreName, String distanceName, 
							double[][] plottable){
		this.outputFileName = outputFileName;
		this.scoreName = scoreName;
		this.distanceName = distanceName;
		this.plottable = plottable;		
	}

	double[][] cleanData(double[][] inData){
		double[][] outData = new double[inData.length][3];
		for(int i = 0; i < inData.length; i ++)
			for(int j = 0; j < 3; j ++)
				outData[i][j] = inData[i][j];
		return outData;
	}

	JavaPlot createPlots(JavaPlot p, double[][] theData){
		p = setUpPlot(p, plottable, Style.POINTS, NamedPlotColor.BLACK);
		p = setUpPlot(p, plottable, Style.ERRORBARS, NamedPlotColor.BLACK);
		return p;
	}

	JavaPlot setUpPlot(JavaPlot p, 
										double[][] theData,
										Style style, 
										NamedPlotColor npc){

		
		

		DataSetPlot s = new DataSetPlot(cleanData(theData));
		p.addPlot(s);

		System.out.println("the number of plots is: " + p.getPlots().size());
		PlotStyle stl = ((AbstractPlot) 
									p.getPlots().get(p.getPlots().size() - 1)).getPlotStyle();
		stl.setStyle(style);
		stl.setLineType(npc);
		//stl.setPointType(5);
		//stl.setPointSize(8);
	
		return p;
	}

	public String translate(String in){
		return in.replace("_", " ").replace("InputObjects.", "");
	}

	public void plot(){
		JavaPlot p = new JavaPlot();
		
		ImageTerminal imgTerm = new ImageTerminal();
		p.setTerminal(imgTerm);
	
		p.setTitle(translate(scoreName) + " vs " + translate(distanceName));
		p.getAxis("x").setLabel(translate(distanceName), "Arial", 20);
		//p.getAxis("x").setBoundaries(50, 150);
		p.getAxis("y").setLabel(translate(scoreName), "Arial", 20);
		p.setKey(JavaPlot.Key.OFF);

		p = createPlots(p, plottable);

		p.plot();

		File file = new File(outputFileName);
		try {
			ImageIO.write(imgTerm.getImage(), "png", file);
		} catch (IOException ex) {
			System.err.print(ex);
		}
	}
}
