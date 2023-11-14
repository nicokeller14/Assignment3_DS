Assignment 3
------------

# Team Members

>Nico Keller and Teo Field-Marsham

# GitHub link to your repository (if submitting through GitHub)

https://github.com/nicokeller14/Assignment3_DS

# Task 2

> We ran into large problems here when it came to running the word counter on both our laptop and Raspberry Pi. We could not
> get the worker of the Raspberry Pi to work, and so we could only run it on our localhost. The SSH connections worked however
> for some unknown reason we could not run the program on anything other than the localhost. The functions still worked but we
> could not leverage being able to run on multiple machines/workers due to these issues and were only able to run Task 2 on
> our localhost. 

   Answer to the question:
> When comparing the performance difference of the word count algorithm in local and cluster modes, local mode is suitable for smaller amounts of, 
> data, because it is limited to the resources of a single machine. In contrast, cluster mode can handle large datasets, leveraging the combined 
> resources of multiple nodes for better scalability and tasks can run in parallel. While local mode is simpler and more efficient for quick 
> iterations, cluster mode offers better performance for larger computations, with better fault tolerance and data handling capacity, however the 
> system is more complex. The choice between the two depends on the amount of data and computing power of the devices being used.

# Task 3

1. How does Spark optimize its file access compared to the file access in MapReduce?
> Ans: In the first assignment we saw that in MapReduce, we read and write the data to the disk after every step, so this takes a lot of time.
> On the other hand, Spark tries to optimize the file access through in-memory processing. This means, that Spark caches the intermediate necessary 
> data in memory. This way, we don't have to access the disk that many time to read and write.
> This approach used by Spark speeds up the data processing by a lot because it is way faster to access data from RAM
> than from the disk.
> Source: https://inoxoft.com/blog/key-differences-between-mapreduce-and-spark/#:~:text=Spark%20vs%20MapReduce%3A%20Performance,-The%20first%20thing&text=Hadoop%20MapReduce%20persists%20data%20back,to%20100x%20faster%20than%20MapReduce.

2. In your implementation of WordCount (task1), did you use ReduceByKey or groupByKey method? 
   What does your preferred method do in your implementation? 
   What are the differences between the two methods in Spark?
> Ans: ReduceByKey is a transformation operation on key values. This method groups the values corresponding to 
> each one of the keys in the RDD and then reduces the function to the values of each group. Finally, 
> it return a new RDD to where the key was associated with only a isingle reduced value.
> We used the ReduceByKey method because it merges the values for eah key locally to every partition, so before 
> sending all the data over to the network. Meanwhile the groupByKey approach shuffles all the key-value pairs over the network.
> So for performance reasons we decided to use ReduceByKey because less data is being shuffled aroung and it lead to better 
> efficiency when processing the data pipeline.
> Source: https://sparkbyexamples.com/spark/spark-groupbykey-vs-reducebykey/

3. Explain what Resilient Distributed Dataset (RDD) is and the benefits it brings to the classic MapReduce model.
> Ans: An RDD is a fundamental data structure in Spark, is is an immutable distributed collection of elements of our data,
> partitioned across nodes that can be processed in parallel. It has several benefits for the classic MapReduce model such as:
> --> an RDD allows the data to be cached in memory, thus resulting in faster access and processing. Compared to disk-based storage, this
> is a huge benefit.
> --> RDD's are fault tolerant. So if for example a node fails, then data can be recomputed from the data that was already replicated by other nodes.
> --> Another benefit is its flexibility. RDD's can be created from many differente data sources. Including local file systems, databases and HDFS.
> --> Furthermore, RDDs have an immutable nature, meaning that one they are created, they can't be changed. So we can ensure consistency and deterministic results.
Source: https://www.sciencedirect.com/topics/computer-science/resilient-distributed-dataset#:~:text=An%20RDD%20is%20an%20immutable%20distributed%20collection%20of%20datasets%20partitioned,stored%20in%20external%20storage%20systems.
https://www.databricks.com/glossary/what-is-rdd

4. Imagine that you have a large dataset that needs to be processed in parallel. 
   How would you partition the dataset efficiently and keep control over the number of outputs created at the end of the execution?
> Ans: In order to efficiently partition datasets there are a couple of things to have in mind.  Firstly, it is important
> to use hash partitioning. Spark utilizes hash partitioning by default, so it distributes records according to the hash value of the key.
> With this we guarantee that the data is distributed evenly throughout all the patitions. Another important thing to keep in mid is
> range partitioning. We can used this to ensure that a continuous range of data values goes to the same partition.
> Furthermore, custom partitioning is important. If some specific domain knowwledge is known about some datast, then a custom partitioner
> could be implemented to control and monitor all the data dsitribution. Also, when performing operations like for example repatition or 
> coalesce, the number of partitions can be specifically set to control the output while repartitioning or coalescing.
Source: https://towardsdatascience.com/how-to-efficiently-re-partition-spark-dataframes-c036e8261418
> https://www.projectpro.io/article/how-data-partitioning-in-spark-helps-achieve-more-parallelism/297

5. If a task is stuck on the Spark cluster due to a network issue that the cluster had during execution, 
  which methods can be used to retry or restart the task execution on a node?
> Ans: So if  a task is stuck on the Spark cluster, Spark has fault tolerance methods that can automatically retry taasks if they fail
> for some reason, including network issues in this case. Spark retries a failed task up to 4 times on different nodes by default.
> The job is terminated only if none of the retries succeed. Also, node failures can be detected by cluster managerss like YARN MESOS or other, 
> and can reschedule tasks to healthy nodes. Ultimately, administrators of the jobs also have the ability to manually restart the Spark
> program or specific stage of the application.
