# RandComp

Welcome to the Random Computation criticality service. This computation can be used to perform a criticality analysis (https://pdfs.semanticscholar.org/e9c3/113b2b8465756026fdf2cd659d554a9cc075.pdf) on algorithms written and designed by the user. 

In order to use :

(1) Download this git to your computer. 

(2) Place a copy of the AspectJ jar files, google guava files, reflections files, and JavaPlot in the RandComp sub directory of this folder. 

Two files are needed for the aspect j runtime. 

The runtime can be found at: http://central.maven.org/maven2/org/aspectj/aspectjrt/1.6.12/aspectjrt-1.6.12.jar. 

The weaver can be found at: https://mvnrepository.com/artifact/org.aspectj/aspectjweaver/1.6.2

A jar file for JavaPlot can be found at: http://downloads.sourceforge.net/project/gnujavaplot/javaplot/0.5.0/JavaPlot-0.5.0.tar.bz2?r=http%3A%2F%2Fjavaplot.panayotis.com%2F&ts=1481526958&use_mirror=pilotfiber.

The jar file for reflections can be found at http://dl.bintray.com/ronmamo/reflections/org/reflections/reflections/0.9.10/:reflections-0.9.10-sources.jar --- this jar is used to find methods marked with the 'randomize' tag

The google common files can be found at http://central.maven.org/maven2/com/google/guava/guava/16.0.1/guava-16.0.1.jar.

Javaassist files can be found at: http://central.maven.org/maven2/javassist/javassist/3.12.1.GA/javassist-3.12.1.GA.jar

Some of the input objects require junit a jar for junit can be found at https://mvnrepository.com/artifact/junit/junit/4.12

TODO: all of this should be managed through maven or something. I just don't know how to do that right at the moment.

(3) Be sure to install aj and ajc, the aspectj compilers. These will be used to weave the aspects into your user code so that the code can be integrated into the analyzer in a fairly seemless manner. 

(4) Run make build with your properly modified code in the InputObjects file (example code in Graph.java, MaxFlowMinCut.java, InputArray.java, and SumExperiment.java can be found in the InputObjects file). --- mostly this means ensuring that the code implements the correct interfaces, declares itself part of the InputObjects package, and has at least one function of name <function-name> that has a @Randomize tag along with a <function-name>Rand version of that function that accepts a Random object along with the inputs accepted by the original function (Also! the function must used boxed types --- no double, only Double). 

(5) Run the code by calling ./run.sh InputObjects.<input-class> InputObjects.<experiment-class> where the input-class is where fallible components reside and experiment-class uses the input-class to run an experiment. 

Some Caveats:
(1) Some times the first time you run a particular combinaton of input and experiment object, the first time you will get a lot of null objects. System runs fine on second run
