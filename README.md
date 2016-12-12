# RandComp

Welcome to the Random Computation criticality service. This computation can be used to perform a criticality analysis (https://pdfs.semanticscholar.org/e9c3/113b2b8465756026fdf2cd659d554a9cc075.pdf) on algorithms written and designed by the user. 

In order to use :

(1) Download this git to your computer. 

(2) Place a copy of AspectJ and JavaPlot in the RandComp sub directory of this folder. A jar file for AspectJ can be found at: http://central.maven.org/maven2/org/aspectj/aspectjrt/1.6.12/aspectjrt-1.6.12.jar. A jar file for JavaPlot can be found at: http://downloads.sourceforge.net/project/gnujavaplot/javaplot/0.5.0/JavaPlot-0.5.0.tar.bz2?r=http%3A%2F%2Fjavaplot.panayotis.com%2F&ts=1481526958&use_mirror=pilotfiber.

(3) Be sure to install aj and ajc, the aspectj compilers. These will be used to weave the aspects into your user code so that the code can be integrated into the analyzer in a fairly seemless manner. 

(4) Run make build with your properly modified code in the InputObjects file (example code in Graph.java, MaxFlowMinCut.java, InputArray.java, and SumExperiment.java can be found in the InputObjects file). --- mostly this means ensuring that the code implements the correct interfaces, declasres itself part of the InputObjects package, and has at least one function of name <function-name> that has a @Randomize tag along with a <function-name>Rand version of that function that accepts and Random object along with the inputs accepted by the original function (Also! the function must used boxed types --- no double, only Double). 

(5) Run the code by calling ./run.sh InputObjects.<input-class> InputObjects.<experiment-class> where the input-class is where fallible components reside and experiment-class uses the input-class to run an experiment. 
