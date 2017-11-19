#!/bin/bash

python -u RunCriticality.py --input_object InputIntegers --experiment_object NaiveMultiply --experiment_size 100 --dry_run y --use_decompose n --proxy_method_name NaiveMultiply.multiply --create_proxy_data_files y --graphs_only y
python -u RunCriticality.py --input_object InputIntegers --experiment_object KaratsubaMultiply --experiment_size 100 --dry_run y --use_decompose n --proxy_method_name KaratsubaMultiply.multiply --create_proxy_data_files y --graphs_only y

python -u RunCriticality.py --input_object InputMatrices --experiment_object NaiveMatrixMultiply --experiment_size 4 --dry_run y --use_decompose n --proxy_method_name NaiveMultiply.multiply --create_proxy_data_files y --graphs_only y

python -u RunCriticality.py --input_object InputMatrices --experiment_object DACMatrixMultiply --experiment_size 4 --dry_run y --use_decompose n --proxy_method_name NaiveMultiply.multiply --create_proxy_data_files y --graphs_only y
