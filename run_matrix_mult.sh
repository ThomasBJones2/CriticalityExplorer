#!/bin/bash
#add /root/aspectj1.6/bin to PATH, explained in /root/aspectj1.6/README-AspectJ.html

aj -cp ./RandComp/*:./:./RandComp/JavaPlot.jar:./InputObjects/* -Xmx10g "$@"
#@ = InputObjects.input_object and InputObjects.Algorithm_or_experiment
#InputObjects.Graph InputObjects.MaxFlowMinCut 
