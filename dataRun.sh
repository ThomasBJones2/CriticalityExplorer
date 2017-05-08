#!/bin/bash
#add /root/aspectj1.6/bin to PATH, explained in /root/aspectj1.6/README-AspectJ.html

aj -cp  ./RandComp/*:./:./RandComp/JavaPlot.jar:./InputObjects/* -Xmx15g RandComp.NumBuilder "$@"

#InputObjects.Graph InputObjects.MaxFlowMinCut 
