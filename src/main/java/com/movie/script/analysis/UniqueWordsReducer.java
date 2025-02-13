package com.movie.script.analysis;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;
import java.util.HashSet;

public class UniqueWordsReducer extends Reducer<Text, Text, Text, Text> {
    @Override
    public void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
        HashSet<String> uniqueWordsSet = new HashSet<>();
        for (Text value : values) {
            uniqueWordsSet.add(value.toString());
        }
        context.write(key, new Text(uniqueWordsSet.toString()));
    }
}