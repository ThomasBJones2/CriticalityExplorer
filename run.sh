#!/bin/bash
#add /root/aspectj1.6/bin to PATH, explained in /root/aspectj1.6/README-AspectJ.html


aj -cp  /home/thomas/aspectj1.6/lib/*:./:./JavaPlot.jar Experimenter Graph MaxFlowMinCut 
