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

        // here we map the words to key-value pairs
        JavaPairRDD<String, Integer> wordCounts = words.mapToPair(
                new PairFunction<String, String, Integer>() {
                    // define the pupel with a word and 1
                    public Tuple2<String, Integer> call(String s) {
                        return new Tuple2<String, Integer>(s, 1);
                    }
                }
        );

        // here we reduce the counts for each word
        // we use the reduceByKey method
        JavaPairRDD<String, Integer> reducedCounts = wordCounts.reduceByKey(
                new Function2<Integer, Integer, Integer>() {
                    public Integer call(Integer a, Integer b) {
                        return a + b;
                    }
                }
        );

        // save the output into a file
        reducedCounts
                // we use .coalesce(1) because this way yhe output get saved onto only one file, otherwise
                // they arent in just one file but in many others
                .coalesce(1)
                .saveAsTextFile("output");

        // to make it faster to correct we also print the output on to the terminal
        List<Tuple2<String, Integer>> output = reducedCounts.collect();
        for (Tuple2<?,?> tuple : output) {
            System.out.println("(" + tuple._1() + "," + tuple._2() + ")");
        }

        sparkContext.stop();
        sparkContext.close();
    }
}

