build:
	./compile.sh

rundefault:
	./run.sh InputObjects.Graph InputObjects.MaxFlowMinCut 

buildrun:
	Build
	Run

clean:
	rm -f ./InputObjects/*.class
	rm -f ./RandComp/*.class
