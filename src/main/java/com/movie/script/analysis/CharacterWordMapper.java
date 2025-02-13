package com.movie.script.analysis;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;
import java.util.StringTokenizer;

public class CharacterWordMapper extends Mapper<Object, Text, Text, IntWritable> {

    private final static IntWritable one = new IntWritable(1);
    private Text word = new Text();

    public enum Counter {
        TOTAL_LINES_PROCESSED,
        TOTAL_WORDS_PROCESSED,
        TOTAL_CHARACTERS_PROCESSED
    }

    @Override
    public void map(Object key, Text value, Context context) throws IOException, InterruptedException {
        String line = value.toString();
        context.getCounter(Counter.TOTAL_LINES_PROCESSED).increment(1);

        String[] parts = line.split(":", 2);
        if (parts.length < 2) return;

        String character = parts[0].trim();
        String dialogue = parts[1].trim();
        
        context.getCounter(Counter.TOTAL_CHARACTERS_PROCESSED).increment(dialogue.length());

        StringTokenizer tokenizer = new StringTokenizer(dialogue);
        while (tokenizer.hasMoreTokens()) {
            word.set(tokenizer.nextToken().toLowerCase().replaceAll("[^a-zA-Z]", ""));
            if (!word.toString().isEmpty()) {
                context.write(word, one);
                context.getCounter(Counter.TOTAL_WORDS_PROCESSED).increment(1);
            }
        }
    }
}