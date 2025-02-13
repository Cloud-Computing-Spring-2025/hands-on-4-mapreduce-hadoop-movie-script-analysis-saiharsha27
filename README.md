# 🎬 Hadoop Movie Script Analysis

## 📌 Project Overview  
This project implements a **Hadoop MapReduce** pipeline to analyze movie scripts. The analysis includes:  
- **Character Word Frequency**: Identifies the most frequently spoken words by characters.  
- **Dialogue Length Analysis**: Computes the total dialogue length per character.  
- **Unique Word Usage**: Extracts unique words spoken by each character.  

The project runs inside a **Docker-based Hadoop cluster**, making it easy to deploy and execute distributed computations.

## 🔍 Approach and Implementation

### **1️⃣ Mapper Logic**  
Each line in the script is processed to extract **character names and their dialogues**. The Mapper emits key-value pairs where:  
- **Key** → Character Name  
- **Value** → Word Count, Unique Words, or Dialogue Length  

Example Mapper Output:  
```
John    5
Alice   3
John    7
Alice   2
```

### **2️⃣ Reducer Logic**  
The Reducer aggregates the values per character to compute:  
- **Total word count per character**  
- **Total unique words spoken**  
- **Total dialogue length**  

Example Reducer Output:  
```
John    12
Alice   5
```

## 🚀 Execution Steps  

### **Step 1: Start the Hadoop Cluster**  
```sh
docker compose up -d
```

### **Step 2: Build the Java Project**  
```sh
mvn install
```
Move the generated JAR file to the `input/` directory:  
```sh
mv target/hands-on2-movie-script-analysis-1.0-SNAPSHOT.jar input/
```

### **Step 3: Copy Files to Hadoop Container**  
```sh
docker cp input/hands-on2-movie-script-analysis-1.0-SNAPSHOT.jar resourcemanager:/opt/hadoop-3.2.1/share/hadoop/mapreduce/
docker cp input/movie_dialogues.txt resourcemanager:/opt/hadoop-3.2.1/share/hadoop/mapreduce/
```

### **Step 4: Connect to the Hadoop Container**  
```sh
docker exec -it resourcemanager /bin/bash
cd /opt/hadoop-3.2.1/share/hadoop/mapreduce/
```

### **Step 5: Setup HDFS**  
```sh
hadoop fs -mkdir -p /input/dataset
hadoop fs -put ./movie_dialogues.txt /input/dataset
```

### **Step 6: Run the MapReduce Job**  
```sh
hadoop jar /opt/hadoop-3.2.1/share/hadoop/mapreduce/hands-on2-movie-script-analysis-1.0-SNAPSHOT.jar com.example.controller.Controller /input/dataset/movie_dialogues.txt /output
```

### **Step 7: View the Output**  
```sh
hadoop fs -cat /output/*
hadoop fs -cat /output/task1/*  # Character Word Frequency
hadoop fs -cat /output/task2/*  # Dialogue Length Analysis
hadoop fs -cat /output/task3/*  # Unique Word Usage
```

### **Step 8: Retrieve Results to Local Machine**  
```sh
hdfs dfs -get /output /opt/hadoop-3.2.1/share/hadoop/mapreduce/
exit
docker cp resourcemanager:/opt/hadoop-3.2.1/share/hadoop/mapreduce/output/ output/
```

## ⚠️ Challenges Faced & Solutions  

### **Challenge 1: Input Path Does Not Exist**
**Error:**  
```
org.apache.hadoop.mapreduce.lib.input.InvalidInputException: Input path does not exist: hdfs://namenode:9000/user/root/com.movie.script.analysis.MovieScriptAnalysis
```
**Solution:**  
Ensure that the input dataset is correctly uploaded to HDFS before running the job:  
```sh
hadoop fs -mkdir -p /input/dataset
hadoop fs -put ./movie_dialogues.txt /input/dataset
```

### **Challenge 2: Output Directory Already Exists**
**Error:**  
```
org.apache.hadoop.mapred.FileAlreadyExistsException: Output directory /output already exists
```
**Solution:**  
Before re-running the job, remove the existing output directory:  
```sh
hadoop fs -rm -r /output
```

## 📄 Sample Input & Output  

### **Sample Input (`movie_dialogues.txt`)**
```
John: Hey Alice, how are you?
Alice: I'm good, John! How about you?
John: I'm doing great, thanks for asking.
```

### **Expected Output (`task1` - Word Frequency)**
```
John    10
Alice   7
```

### **Expected Output (`task2` - Dialogue Length)**
```
John    36
Alice   24
```

### **Expected Output (`task3` - Unique Words)**
```
John    Hey, how, are, you, doing, great, thanks, for, asking
Alice   I'm, good, how, about, you
```

## 🎯 Conclusion  
This project provides a **distributed** and **scalable** approach to analyzing movie scripts using **Hadoop MapReduce**. It can be expanded to process **large-scale** dialogue datasets and extract deeper insights into character speech patterns. 🚀  

