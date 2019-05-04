# bigdata-Project-1
Top K Words on Single Node
Goal
There are multiple factors that affect the performance of an application. The purpose of this assignment is to analyze such factors -- input size (or load), algorithms efficiency, data structures, system resource utilization (e.g., CPU, Memory), and so on. The assignment will give you an opportunity to understand the impact of the above factors on the performance. And also we’ll get to know how the size of input dataset impacts performance and what are the techniques we should use to alleviate the problems introduced by increased input dataset size (crux of Big Data).

Input Dataset
We have provided three datasets of different sizes. The dataset is available here.
The dataset is a zip file that consists of three files with only English words.
1. data_1GB.txt – A text file of size 1GB.
2. data_8GB.txt – A text file of size 8GB.
3. data_32GB.txt – A text file of size 32GB.


The Problem Statement
Design and Implement an efficient java code (or any language you prefer) to determine 100 most frequent/repeated words in a given dataset. The objective here is to obtain the result with the least possible execution Time (or with the best performance on your computer).

Execute your code on each of the three input data files separately. It is a good practice to execute your code initially on 1 GB dataset and then repeat on larger datasets.

Analyze the performance through different metrics such as running time, speedup, CPU utilization, memory usage, etc.

Present detailed analysis for why you use a particular algorithm or a particular data structure to solve this problem.

Note:

The result should preserve case sensitivity i.e. words “Title” and “title” are considered as two different words.
The input dataset contains only english alphabets and white spaces i.e. “a-z”,”A-Z”,”\s”.
