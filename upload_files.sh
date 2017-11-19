#!/bin/bash

mvn clean
mvn package

aws lambda update-function-code --function-name RandCompHandler --zip-file ./target/CriticalityWorkbench-1.0-SNAPSHOT.jar 
aws lambda update-function-code --function-name RunTimeHandler --zip-file ./target/CriticalityWorkbench-1.0-SNAPSHOT.jar 
