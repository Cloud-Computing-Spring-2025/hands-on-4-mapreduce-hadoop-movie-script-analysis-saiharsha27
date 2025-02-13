package com.movie.script.analysis;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Counter;
import org.apache.hadoop.mapreduce.Counters;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class MovieScriptAnalysis {

    public static void main(String[] args) throws Exception {
        Configuration conf = new Configuration();

        // Task 1: Most Frequent Words by Character
        Job job1 = Job.getInstance(conf, "Most Frequent Words by Character");
        job1.setJarByClass(MovieScriptAnalysis.class);
        job1.setMapperClass(CharacterWordMapper.class);
        job1.setReducerClass(CharacterWordReducer.class);
        job1.setOutputKeyClass(Text.class);
        job1.setOutputValueClass(IntWritable.class);
        FileInputFormat.addInputPath(job1, new Path(args[1]));
        FileOutputFormat.setOutputPath(job1, new Path(args[2] + "/task1"));
        job1.waitForCompletion(true);

        // Fetch Counters for Words, Lines, and Characters
        Counters counters1 = job1.getCounters();
        long totalLinesProcessed = counters1.findCounter(CharacterWordMapper.Counter.TOTAL_LINES_PROCESSED).getValue();
        long totalWordsProcessed = counters1.findCounter(CharacterWordMapper.Counter.TOTAL_WORDS_PROCESSED).getValue();
        long totalCharactersProcessed = counters1.findCounter(CharacterWordMapper.Counter.TOTAL_CHARACTERS_PROCESSED).getValue();

        // Task 2: Dialogue Length Analysis
        Job job2 = Job.getInstance(conf, "Dialogue Length Analysis");
        job2.setJarByClass(MovieScriptAnalysis.class);
        job2.setMapperClass(DialogueLengthMapper.class);
        job2.setReducerClass(DialogueLengthReducer.class);
        job2.setOutputKeyClass(Text.class);
        job2.setOutputValueClass(IntWritable.class);
        FileInputFormat.addInputPath(job2, new Path(args[1]));
        FileOutputFormat.setOutputPath(job2, new Path(args[2] + "/task2"));
        job2.waitForCompletion(true);

        // Task 3: Unique Words by Character
        Job job3 = Job.getInstance(conf, "Unique Words by Character");
        job3.setJarByClass(MovieScriptAnalysis.class);
        job3.setMapperClass(UniqueWordsMapper.class);
        job3.setReducerClass(UniqueWordsReducer.class);
        job3.setOutputKeyClass(Text.class);
        job3.setOutputValueClass(Text.class);
        FileInputFormat.addInputPath(job3, new Path(args[1]));
        FileOutputFormat.setOutputPath(job3, new Path(args[2] + "/task3"));
        job3.waitForCompletion(true);

        // Fetch Counters for Unique Words and Characters Speaking
        Counters counters3 = job3.getCounters();
        long totalUniqueWordsIdentified = counters3.findCounter(UniqueWordsMapper.Counter.TOTAL_UNIQUE_WORDS_IDENTIFIED).getValue();
        long numberOfCharactersSpeaking = counters3.findCounter(UniqueWordsMapper.Counter.NUMBER_OF_CHARACTERS_SPEAKING).getValue();

        // Print Final Counter Results
        System.out.println("\nHadoop Counter Output");
        System.out.println("Total Lines Processed: " + totalLinesProcessed);
        System.out.println("Total Words Processed: " + totalWordsProcessed);
        System.out.println("Total Characters Processed: " + totalCharactersProcessed);
        System.out.println("Total Unique Words Identified: " + totalUniqueWordsIdentified);
        System.out.println("Number of Characters Speaking: " + numberOfCharactersSpeaking);

        System.exit(0);
    }
}