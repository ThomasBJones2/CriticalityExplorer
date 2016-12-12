build:
	./compile.sh

run:
	./run.sh params

buildrun:
	Build
	Run

clean:
	rm -f ./InputObjects/*.class
	rm -f ./RandComp/*.class
