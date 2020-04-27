package capstone;

import java.util.Arrays;
import java.util.List;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;

public class SparkDemo {
	public static void main(String[] args) {
		SparkConf conf = new SparkConf();
		conf.setAppName("Spark Demo Job");
		conf.setMaster("local");
		
		JavaSparkContext sc = new JavaSparkContext(conf);
		JavaRDD<String> lines = sc.textFile("input.txt");
		JavaRDD<String> filteredLines = lines.filter(new Function<String, Boolean>() {
			
			@Override
			public Boolean call(String line) throws Exception {
				// TODO Auto-generated method stub
				return line.contains("Spark");
			}
		});
		
		long linesCount = filteredLines.count();
		String firstLine = filteredLines.first();
		System.out.println("Total Lines containing Spark: " + linesCount);
		System.out.println("First Line containing Spark: " + firstLine);	
		
		sc.close();
	}
}
