package com.assignment3.spark;

import java.util.Arrays;
import java.util.Iterator;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.*;
import org.apache.spark.api.java.function.*;
import org.apache.spark.SparkFiles;
import java.util.List;
import java.util.ArrayList;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;

import scala.Tuple2;

import org.apache.spark.sql.Dataset;

public class WordCount {

    static class Filter implements FlatMapFunction<String, String>
    {
        @Override
        public Iterator<String> call(String s) {
            /*
             * add your code to filter words
             */
            String[] subStrings = s.split("\\s+");
            return Arrays.asList(subStrings).iterator();
        }

    }

    public static void main(String[] args) {
        String textFilePath = "input/pigs.txt";
        SparkConf conf = new SparkConf().setAppName("WordCountWithSpark").setMaster("local[*]");
        JavaSparkContext sparkContext =  new JavaSparkContext(conf);
        JavaRDD<String> textFile = sparkContext.textFile(textFilePath);
        JavaRDD<String> words = textFile.flatMap(new Filter());

        // Mapping words to key-value pairs
        JavaPairRDD<String, Integer> wordCounts = words.mapToPair(
                new PairFunction<String, String, Integer>() {
                    public Tuple2<String, Integer> call(String s) {
                        return new Tuple2<String, Integer>(s, 1);
                    }
                }
        );

        // Reducing counts for each word
        JavaPairRDD<String, Integer> reducedCounts = wordCounts.reduceByKey(
                new Function2<Integer, Integer, Integer>() {
                    public Integer call(Integer a, Integer b) {
                        return a + b;
                    }
                }
        );

        // Saving the output
        reducedCounts
                .coalesce(1)
                .saveAsTextFile("output");

        // Optional: Printing word counts on the Spark Master terminal
        List<Tuple2<String, Integer>> output = reducedCounts.collect();
        for (Tuple2<?,?> tuple : output) {
            System.out.println("(" + tuple._1() + "," + tuple._2() + ")");
        }

        sparkContext.stop();
        sparkContext.close();
    }
}

