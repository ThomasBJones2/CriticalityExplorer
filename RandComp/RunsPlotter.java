package RandComp;

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



public class RunsPlotter extends Plotter{
	public RunsPlotter(String inputFileName, String outputFileName,
							String scoreName, String distanceName, 
							double[][] plottable){
		super(inputFileName, outputFileName, scoreName, distanceName, plottable);

	}

	@Override
	JavaPlot createPlots(JavaPlot p, double[][] theData){
		p = setUpPlot(p, plottable, Style.POINTS, NamedPlotColor.BLACK);
		return p;
	}

	@Override
	double[][] cleanData(double[][] inData){
		double[][] outData = new double[inData.length][2];
		for(int i = 0; i < inData.length; i ++)
			for(int j = 0; j < 2; j ++)
				outData[i][j] = inData[i][j*3]; //(0 gives us 0, 1 gives us 3)
		return outData;
	}

}
