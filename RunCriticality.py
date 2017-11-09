import os
import boto3
from botocore.client import Config
from subprocess import call
import matplotlib.pyplot as plt
import argparse
import time
import glob
from multiprocessing import Pool

IS_DEBUG = False #True

def pool_work(arg_dict):
    try:
        RunCriticality().run_single_criticality_error_point( 
                        arg_dict['input_object'], 
                        arg_dict['experiment_object'], 
                        error_point = arg_dict['error_point'],
                        fallible_method = arg_dict['fallible_method'],
                        fallible_method_num = arg_dict['fallible_method_num'],
                        experiment_size = arg_dict['experiment_size'], 
                        run_time = arg_dict['run_time'],
                        error_points = arg_dict['error_points'],
                        fallible_methods_list = arg_dict['fallible_methods_list'])
    except:
        print("bad step on fm: " + str(arg_dict['fallible_method_num']) +\
                " ep: " + str(arg_dict['error_point']))

class RunCriticality(object):
    def __init__(self):
        config_dict = {'region_name': 'us-west-2', 'connect_timeout': 5, 'read_timeout': 300}
        config = Config(**config_dict)
        self.client = boto3.client('lambda', config=config)
        self.memory_string = "aws lambda update-function-configuration --function-name RunTimeHandler --region us-west-2 --memory-size "
        self.run_time_test_command =  "runTime com.criticalityworkbench.inputobjects.InputMatrices com.criticalityworkbench.inputobjects.NaiveMatrixMultiply com.criticalityworkbench.randcomphandler.CriticalityExperimenter 1"
        self.rand_comp_base_string = 'com.criticalityworkbench.randcomphandler.'
        self.input_objects_base_string = 'com.criticalityworkbench.inputobjects.'

    def set_timeouts(self):
        self.set_timeout('RunTimeHandler', 300)
        self.set_timeout('RandCompHandler', 300)

    def create_run_time_command(self, 
            input_object = "InputMatrices", 
            experiment_object = "NaiveMatrixMultiply", 
            experiment_size = "1"):
        experimenter_name = "CriticalityExperimenter"
        out = "runTime " +\
                self.input_objects_base_string + input_object + " " +\
                self.input_objects_base_string + experiment_object + " " +\
                self.rand_comp_base_string + experimenter_name + " " +\
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

    def get_raw_criticality_file_name(self, 
            input_object,
            experiment_object,
            experiment_size, 
            fallible_method,
            error_point):

            return str(input_object) + "_" +\
                str(experiment_object) + "_" +\
                str(experiment_size) + "_" +\
                str(fallible_method) + "_" +\
                str(error_point)

    def get_criticality_file_name(self, 
            input_object,
            experiment_object,
            experiment_size):

            return str(input_object) + "_" +\
                str(experiment_object) + "_" +\
                "CriticalityExperimenter" + "_" +\
                str(experiment_size) +\
                ".csv"

    def run_single_criticality_error_point( 
                    self, 
                    input_object, 
                    experiment_object, 
                    error_point,
                    fallible_method,
                    fallible_method_num,
                    experiment_size, 
                    run_time,
                    error_points,
                    fallible_methods_list):

        fallible_methods_str = self.stringify_fallible_methods(fallible_methods_list)
        print("fallible method " + str(fallible_method_num) +\
                " of " + str(len(fallible_methods_list)) +\
                " error point " + str(error_point) +\
                " of " + str(error_points))
        directory_name = "./raw_lambda_results/"
        file_name = self.get_raw_criticality_file_name(
                input_object,
                experiment_object,
                experiment_size,
                fallible_method,
                error_point)
        total_name = os.path.join(directory_name, file_name)
        f = open(total_name, 'a')
        
        criticality_command = self.create_criticality_command( 
            input_object, 
            experiment_object, 
            error_point = error_point,
            fallible_method = fallible_method_num,
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


    def get_max_eps_per_step(self):
        if IS_DEBUG:
            return 2
        else:
            return 950

    def get_max_eps(self, eps):
        if IS_DEBUG:
            return 2
        else:
            return eps

    def build_arg_dict(self,
                    input_object, 
                    experiment_object, 
                    error_point,
                    fallible_method,
                    fallible_method_num,
                    experiment_size, 
                    run_time,
                    error_points,
                    fallible_methods_list
                    ):
        out = {}
        out['input_object'] = input_object 
        out['experiment_object'] = experiment_object
        out['error_point'] = error_point
        out['fallible_method'] = fallible_method
        out['fallible_method_num'] = fallible_method_num
        out['experiment_size'] = experiment_size 
        out['run_time'] = run_time
        out['error_points'] = error_points
        out['fallible_methods_list'] = fallible_methods_list
        return out


    def run_criticality(self, 
            input_object,
            experiment_object,
            experiment_size):

        run_time_command = self.create_run_time_command( 
                input_object, 
                experiment_object, 
                experiment_size)
        print(run_time_command)
       

        run_time, error_points, fallible_methods_list = \
            self.get_run_time_error_points_and_fallible_methods(run_time_command)
        dpapx = float(2*60*1000)/float(run_time)
        print("fallible methods: " + str(fallible_methods_list))
        print("RunTime of: " + str(run_time) +\
                " data points collected of (approx): " + str(dpapx) +\
                " errorpoints of: " + str(error_points))
      
        print(self.create_criticality_command( 
            input_object, 
            experiment_object, 
            "0",
            "0",
            "4", 
            run_time,
            error_points,
            fallible_methods_list[0]))
        if not self.dry_run: 
            max_eps_per_step = self.get_max_eps_per_step()
            pool = Pool(max_eps_per_step)
            print("max eps per step: ", max_eps_per_step)
            for fm, fallible_method in enumerate(fallible_methods_list):
                for start in range(0, self.get_max_eps(error_points), max_eps_per_step):
                    try:
                        eps = range(start, min(error_points, start + max_eps_per_step))
                        eps = [self.build_arg_dict( 
                            input_object, 
                            experiment_object, 
                            ep,
                            fallible_method,
                            fm,
                            experiment_size, 
                            run_time,
                            error_points,
                            fallible_methods_list) for ep in eps]
                        pool.map(pool_work, eps)
                    except KeyboardInterrupt:
                        print("Keyboard interrupt, control c")
                        pool.close()
                        pool.join()
                        sys.exit(1)
            pool.close()
            pool.join()

        results_directory = "./results/"
        crit_file = self.get_criticality_file_name(
                    input_object,
                    experiment_object,
                    experiment_size, 
                    )
        crit_file = os.path.join(results_directory, crit_file)
        out_file = open(crit_file, 'w')
        for fm, fallible_method in enumerate(fallible_methods_list):
            for ep in range(0, self.get_max_eps(error_points)):
                directory_name = "./raw_lambda_results/"
                file_name = self.get_raw_criticality_file_name(
                        input_object,
                        experiment_object,
                        experiment_size,
                        fallible_method,
                        ep)
                total_name = os.path.join(directory_name, file_name)
                try:
                    f = open(total_name, 'r')
                    for i, line in enumerate(f.readlines()):
                        if i == 0:
                            out_file.write("#,fallmeth: " +\
                                    str(fm) +\
                                    ",errorPoint: " +\
                                    str(ep) +\
                                    "\n")
                        else:
                            if not line == "\n" and not '#' in line:
                                out_file.write(line)
                    f.close()
                except:
                    pass
        out_file.close()


    def criticality_graph_run(self, 
        input_object,
        experiment_object,
        experiment_size,
        criticality_graph_directory,
        processed_data_directory):
        
        call(['java', 
            '-cp', 
            './target/CriticalityWorkbench-1.0-SNAPSHOT.jar', 
            self.rand_comp_base_string + 'GraphBuilder',
            self.input_objects_base_string + str(input_object),
            self.input_objects_base_string + str(experiment_object),
            self.rand_comp_base_string + 'CriticalityExperimenter',
            criticality_graph_directory,
            './results/',
            experiment_size,
            processed_data_directory,
            ])
 


def create_argparser():
    parser = argparse.ArgumentParser( description='LSTM')
    parser.add_argument('--input_object', type=str,
                        help='the input object', default='InputMatrices')
    parser.add_argument('--experiment_object', type=str,
                        help='the experiment object', default='NaiveMatrixMultiply')
    parser.add_argument('--experiment_size', type=str,
                        help='the experiment size', default='8')
    parser.add_argument('--criticality_graph_directory', type=str,
                        help='the directory where we dump graphs', default='')
    parser.add_argument('--processed_data_directory', type=str,
                        help='the directory where we dump graphs', default='./processed_data/')

    parser.add_argument('--clear_raw_data', type=str, default ='n',
            help='clear the raw lambda data for this experiment')

    parser.add_argument('--limited_command', type=str, default = '')
    parser.add_argument('--dry_run', type=str, default='y')
    
    return parser



if __name__ == "__main__":
    args = create_argparser().parse_args()
    input_object = args.input_object
    experiment_object = args.experiment_object
    experiment_size = args.experiment_size
    criticality_graph_directory = args.criticality_graph_directory
    processed_data_directory = args.processed_data_directory


    crit = RunCriticality()
    crit.dry_run = args.dry_run == 'y'

    if args.clear_raw_data == 'y':
        file_name = input_object + "_" + experiment_object + "_" + experiment_size +\
                "*" + args.limited_command + "*"
        file_names = "./raw_lambda_results/" + file_name
        print(file_names)
        files = glob.glob(file_names)
        for f in files:
            os.remove(f)
    else:
        crit.set_timeouts()
        crit.run_criticality(input_object, experiment_object, experiment_size)
        


        if criticality_graph_directory != '':
            crit.criticality_graph_run(input_object, 
                    experiment_object, 
                    experiment_size, 
                    criticality_graph_directory,
                    processed_data_directory
                    )

