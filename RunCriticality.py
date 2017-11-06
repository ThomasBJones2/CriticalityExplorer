import os
import boto3
from botocore.client import Config
from subprocess import call
import matplotlib.pyplot as plt
import argparse
import time


class RunCriticality(object):
    def __init__(self):
        config_dict = {'region_name': 'us-west-2', 'connect_timeout': 5, 'read_timeout': 300}
        config = Config(**config_dict)
        self.client = boto3.client('lambda', config=config)

        self.set_timeout('RunTimeHandler', 300)
        self.set_timeout('RandCompHandler', 300)

        self.memory_string = "aws lambda update-function-configuration --function-name RunTimeHandler --region us-west-2 --memory-size "
        self.run_time_test_command =  "runTime com.criticalityworkbench.inputobjects.InputMatrices com.criticalityworkbench.inputobjects.NaiveMatrixMultiply com.criticalityworkbench.randcomphandler.CriticalityExperimenter 1"


    def create_run_time_command(self, 
            input_object = "InputMatrices", 
            experiment_object = "NaiveMatrixMultiply", 
            experiment_size = "1"):
        input_base_string = "com.criticalityworkbench.inputobjects." 
        experimenter_name = "com.criticalityworkbench.randcomphandler.CriticalityExperimenter"
        out = "runTime " +\
                input_base_string + input_object + " " +\
                input_base_string + experiment_object + " " +\
                experimenter_name + " " +\
                experiment_size
        return out


    def create_criticality_command(self, 
            input_object = "InputMatrices", 
            run_object = "NaiveMatrixMultiply", 
            error_point = "0",
            fallible_method = "0",
            experiment_size = "1", 
            num_run_time = "21",
            num_error_points = "79",
            fallible_methods_list = None):
        if fallible_methods_list is None:
            fallible_methods_list = "com.criticalityworkbench.inputobjects.NaiveMultiply.check"\
                    + "/com.criticalityworkbench.inputobjects.NaiveMultiply.add"
        input_base_string = "com.criticalityworkbench.inputobjects." 
        experimenter_name = "com.criticalityworkbench.randcomphandler.CriticalityExperimenter"
        out = str(input_base_string) + str(input_object) + " " +\
                str(input_base_string) + str(run_object) + " " +\
                str(error_point) + " " +\
                str(fallible_method) + " " +\
                str(experimenter_name) + " " +\
                str(experiment_size) + " " +\
                str(num_run_time) + " " +\
                str(num_error_points) + " " +\
                str(fallible_methods_list)
        return out

    def set_mem_size(self, func_name, mem_size):
        call(['aws', 
            'lambda', 
            'update-function-configuration', 
            '--function-name',
            func_name,
            '--region',
            'us-west-2',
            '--memory-size',
            str(mem_size)])
    
    def set_timeout(self, func_name, timeout):
        call(['aws', 
            'lambda', 
            'update-function-configuration', 
            '--function-name',
            func_name,
            '--region',
            'us-west-2',
            '--timeout',
            str(timeout)])
         

    def get_memory_graph(self):
        mem_sizes = [128*i for i in range(1,12)]
        run_times = []
        for mem_size in mem_sizes:
            print("On mem_size: " + str(mem_size))
            self.set_mem_size('RunTimeHandler', mem_size)
            run_times.append(self.get_run_time_error_points_and_fallible_methods()[0])
        run_times = [float(a)*float(b)/1024.0 for a,b in zip(run_times, mem_sizes)] 
        plt.plot(mem_sizes, run_times) 
        plt.show()

    def get_rtepafm_helper(self, result_string):
        names = []
        for rs  in result_string.split('\\n'):
            if len(rs) > 1:
                _, name, rt, ep = rs.split(':')
                names.append(name.split(" ")[1])
                rt = rt.split(" ")[1]
                ep = ep.split(" ")[1]
        return int(rt), int(ep), names
         

    def get_run_time_error_points_and_fallible_methods(self, command = None):
        if command is None:
            command = self.run_time_test_command
        result = self.invoke_rt(command)
        return self.get_rtepafm_helper(result)
 
    def invoke_rt(self, command):
        out = self.client.invoke(
                FunctionName='RunTimeHandler',
                InvocationType='RequestResponse',
                LogType='None',
                Payload=b'"' + command + b'"',
                )
        return out['Payload'].read()

    def invoke_criticality(self, command = None):
        if command is None:
            command = self.create_criticality_command
        out = self.client.invoke(
                FunctionName='RandCompHandler',
                InvocationType='RequestResponse',
                LogType='None',
                Payload=b'"' + command + b'"',
                )
        return out['Payload'].read()


    def stringify_fallible_methods(self, fallible_method_list):
        out = ""
        for i, fallible_method in enumerate(fallible_method_list):
            out += fallible_method 
            if i < len(fallible_method_list) - 1:
                out += "/"
        return out

    def run_criticality(self, 
            input_object,
            experiment_object,
            experiment_size):

        run_time_command = self.create_run_time_command( 
                input_object, 
                experiment_object, 
                experiment_size)

        
        run_time, error_points, fallible_method_list = \
            self.get_run_time_error_points_and_fallible_methods(run_time_command)
        fallible_methods_str = self.stringify_fallible_methods(fallible_method_list)

        for fm, fallible_method in enumerate(fallible_method_list):
            for error_point in range(0, error_points):
                print("fallible method " + str(fm) + " error point " + str(error_point))

                directory_name = "./raw_lambda_results/"
                file_name = str(input_object) + "_" +\
                    str(experiment_object) + "_" +\
                    str(experiment_size) + "_" +\
                    str(fallible_method) + "_" +\
                    str(error_point)
                total_name = os.path.join(directory_name, file_name)
                f = open(total_name, 'a')
                
                criticality_command = self.create_criticality_command( 
                    input_object, 
                    experiment_object, 
                    error_point = error_point,
                    fallible_method = fm,
                    experiment_size = experiment_size, 
                    num_run_time = run_time,
                    num_error_points = error_points,
                    fallible_methods_list = fallible_methods_str)
                val = crit.invoke_criticality(criticality_command)
                vals = val.split('\\n')
                for val in vals:
                    val = val.replace('\\"', '')
                    val = val.replace('\"', '')
                    f.write(val + "\n")
                f.close()

                print("vals", len(vals))

def create_argparser():
    parser = argparse.ArgumentParser( description='LSTM')
    parser.add_argument('--input_object', type=str,
                        help='the input object', default='InputMatrices')
    parser.add_argument('--experiment_object', type=str,
                        help='the experiment object', default='NaiveMatrixMultiply')
    parser.add_argument('--experiment_size', type=str,
                        help='the experiment size', default='8')

    return parser



if __name__ == "__main__":
    args = create_argparser().parse_args()
    input_object = args.input_object
    experiment_object = args.experiment_object
    experiment_size = args.experiment_size

    crit = RunCriticality()
    crit.run_criticality(input_object, experiment_object, experiment_size)


