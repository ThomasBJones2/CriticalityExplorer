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
	String inputFileName;
	String outputFileName;
	String scoreName;
	String distanceName;
	double[][] plottable;

	public Plotter(String inputFileName, String outputFileName,
							String scoreName, String distanceName, 
							double[][] plottable){
		this.inputFileName = inputFileName;
		this.outputFileName = outputFileName;
		this.scoreName = scoreName;
		this.distanceName = distanceName;
		this.plottable = plottable;		
	}

	private JavaPlot setUpPlot(JavaPlot p, 
										double[][] theData,
										Style style, 
										NamedPlotColor npc){

		
		DataSetPlot s = new DataSetPlot(theData);
		p.addPlot(s);

		PlotStyle stl = ((AbstractPlot) 
									p.getPlots().get(p.getPlots().size() - 1)).getPlotStyle();
		stl.setStyle(style);
		stl.setLineType(npc);
		//stl.setPointType(5);
		//stl.setPointSize(8);
	
		return p;
	}

	public void plot(){
		JavaPlot p = new JavaPlot();
		

		ImageTerminal imgTerm = new ImageTerminal();
		p.setTerminal(imgTerm);
	
		p.setTitle(scoreName + " vs " + distanceName);
		p.getAxis("x").setLabel(distanceName, "Arial", 20);
		p.getAxis("y").setLabel(scoreName, "Arial", 20);
		p.setKey(JavaPlot.Key.OFF);

		p = setUpPlot(p, plottable, Style.POINTS, NamedPlotColor.BLACK);
		p = setUpPlot(p, plottable, Style.ERRORBARS, NamedPlotColor.BLACK);

//		DataSetPlot s = new DataSetPlot(plottable);
//		p.addPlot(s);

//		PlotStyle stl = ((AbstractPlot) p.getPlots().get(0)).getPlotStyle();
//		stl.setStyle(Style.ERRORBARS);
//		stl.setLineType(NamedPlotColor.GOLDENROD);
//		stl.setPointType(5);
//		stl.setPointSize(8);


//		try{
// 			p.addPlot(new FileDataSet(new File("lala")));
//		} catch (IOException E){
//			System.out.println(inputFileName);
//		}		
		p.plot();

		File file = new File(outputFileName);
		try {
			ImageIO.write(imgTerm.getImage(), "png", file);
		} catch (IOException ex) {
			System.err.print(ex);
		}
	}
}
