import os
import boto3
from subprocess import call
import matplotlib.pyplot as plt

class RunCriticality(object):
    def __init__(self):
        self.client = boto3.client('lambda')
        self.memory_string = "aws lambda update-function-configuration --function-name RunTimeHandler --region us-west-2 --memory-size "
        self.test_command =  "runTime com.criticalityworkbench.inputobjects.InputMatrices com.criticalityworkbench.inputobjects.NaiveMatrixMultiply com.criticalityworkbench.randcomphandler.CriticalityExperimenter 1"

    def set_mem_size(self, mem_size):
        call(['aws', 
            'lambda', 
            'update-function-configuration', 
            '--function-name',
            'RunTimeHandler',
            '--region',
            'us-west-2',
            '--memory-size',
            str(mem_size)])
         

    def get_memory_graph(self):
        mem_sizes = [128*i for i in range(1,12)]
        run_times = []
        for mem_size in mem_sizes:
            print("On mem_size: " + str(mem_size))
            self.set_mem_size(mem_size)
            run_times.append(self.get_run_time_and_error_points()[0])
        run_times = [a*float(b)/1024.0 for a,b in zip(run_times, mem_sizes)] 
        plt.plot(mem_sizes, run_times) 
        plt.show()

    def get_rtaep_helper(self, result_string):
        _, name, rt, ep = result_string.split('\\n')[0].split(':')
        rt = rt.split(" ")[1]
        ep = ep.split(" ")[1]
        return float(rt)*1000.0, float(ep)
         

    def get_run_time_and_error_points(self, command = None):
        if command is None:
            command = self.test_command
        result = self.invoke_rt(command)
        return self.get_rtaep_helper(result)
 
    def invoke_rt(self, command):
        out = self.client.invoke(
                FunctionName='RunTimeHandler',
                InvocationType='RequestResponse',
                LogType='None',
                Payload=b'"' + command + b'"',
                )
        return out['Payload'].read()


if __name__ == "__main__":
    crit = RunCriticality()
    val = crit.invoke_rt(crit.test_command)
    print(val)
    crit.get_memory_graph()
