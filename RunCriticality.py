import os
import boto3

class RunCriticality(object):
    def __init__(self):
        self.client = boto3.client('lambda')

    def run(self, command):
        out = self.client.invoke(
                FunctionName='RunTimeHandler',
                InvocationType='RequestResponse',
                LogType='None',
                Payload=b'"' + command + b'"',
                )
        return out['Payload'].read()


if __name__ == "__main__":
    crit = RunCriticality()
    val = crit.run("runTime com.criticalityworkbench.inputobjects.InputMatrices com.criticalityworkbench.inputobjects.NaiveMatrixMultiply com.criticalityworkbench.randcomphandler.CriticalityExperimenter 1")
    print(val)
