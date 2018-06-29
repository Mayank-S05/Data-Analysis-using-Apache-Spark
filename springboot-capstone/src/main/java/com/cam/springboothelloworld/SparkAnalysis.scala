package com.cam.springboothelloworld
import kafka.serializer.StringDecoder

import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.spark.SparkConf
import org.apache.spark.streaming.kafka010.{ConsumerStrategies, KafkaUtils, LocationStrategies}
import org.apache.spark.streaming.{Durations, StreamingContext}

import scala.collection.mutable
import org.apache.spark.mllib.rdd.RDDFunctions._
import org.apache.spark.mllib.rdd.RDDFunctions._

import java.net._
import java.io._
import scala.io._
import org.apache.hadoop.fs.shell.Display
import org.apache.hadoop.fs.shell.Display
import java.util.{Date, Properties}

import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord}

import scala.util.Random

class MA {
  

  def main(argv: Array[String]): Unit = {

   val kafkaParam = new mutable.HashMap[String, String]()
      kafkaParam.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092")
      kafkaParam.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
        "org.apache.kafka.common.serialization.StringDeserializer")
      kafkaParam.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
        "org.apache.kafka.common.serialization.StringDeserializer")
      kafkaParam.put(ConsumerConfig.GROUP_ID_CONFIG, "test")
      kafkaParam.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest")
      kafkaParam.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "true")
       
      val conf = new SparkConf().setMaster("local[2]").setAppName("Kafka10")
  
      //Read messages in batch of 30 seconds
      val sparkStreamingContext = new StreamingContext(conf, Durations.seconds(30))
      val sc = sparkStreamingContext.sparkContext
      //Configure Spark to listen messages in topic test
      val topicList = List("test1")
      
     
      //------------ kafka producer ----------------------
      
      
  val props = new Properties()
  props.put("bootstrap.servers", "localhost:9092")
  props.put("group.id", "test2")
  props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer")
  props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer")
      
  val producer = new KafkaProducer[String, String](props)
  val topic = "spark"      
  
  // Read value of each message from Kafka and return it
      val messageStream = KafkaUtils.createDirectStream(sparkStreamingContext,
          LocationStrategies.PreferConsistent,
          ConsumerStrategies.Subscribe[String, String](topicList, kafkaParam))
       
     val lines = messageStream.map(consumerRecord => consumerRecord.value())
     val parsedData = lines.map(_.toDouble)
//     lines.foreachRDD(myRdd => myRdd.collect().foreach(println))
      
//       val avg = parsedData.foreachRDD(myRDD => myRDD.sliding(3).map(curSlice => (curSlice.sum / curSlice.size)).collect()) 
       
    
       val avg = parsedData.foreachRDD(myRDD => myRDD.sliding(2).map(curSlice => (curSlice.sum / curSlice.size)).collect().foreach({a => 
         println(a)
         val x = a.toString()
          val record = new ProducerRecord[String, String](topic,x)
    	 			
        Thread.sleep(1000);
	  	  producer.send(record);
       }))
      
      sparkStreamingContext.start()
      sparkStreamingContext.awaitTermination()

  }
}