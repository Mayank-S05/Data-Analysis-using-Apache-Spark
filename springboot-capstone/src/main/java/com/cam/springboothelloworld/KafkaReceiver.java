package com.cam.springboothelloworld;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.corundumstudio.socketio.SocketIOServer;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

@Component
@Controller
public class KafkaReceiver {

  String message1 = "", message2 = "0.0";
  static private Socket socket;
  static final int PORT = 8080;
//  static SocketIOServer server;

  @KafkaListener(topics = "test_topic" , groupId="group-id")
  public void kafkaListener1(String message) {
	  	this.message1 += message + "<br/>";
	  	
	  	//	  	new SparkAnalysis();
	  	 try {
	         FileWriter writer =  new FileWriter(new File("/home/gulshan/Downloads/springboot-capstone/src/main/webapp/liveData"));
	         writer.write(this.message1);
//	         writer.flush();
	         writer.close();
	         writer =  new FileWriter(new File("/home/gulshan/Downloads/springboot-capstone/src/main/webapp/WEB-INF/jsp/liveData"));
	         writer.write(this.message1);
//	         writer.flush();
	         writer.close();
	      }
	      catch (IOException e) {
	         System.out.println("exception occoured"+ e);
	      }
//		System.out.println("Message recieved from Producer to Consumer");
//		System.out.println(message);
	  	 
  }
  
  @KafkaListener(topics = "test1" , groupId="group-id")
  public void kafkaListener2(String message) throws URISyntaxException {
	  	this.message2 = message + "<br/>";	 
	  	 
  }
   
  @RequestMapping("/reciever")
  public ModelAndView reciever() throws NumberFormatException, UnknownHostException, IOException, InterruptedException {
//	  return new ModelAndView("reciever","msg",message);
	  return new ModelAndView("reciever");
  }
  
  @RequestMapping("/index")
  public ModelAndView index() throws URISyntaxException {
//	  return new ModelAndView("reciever","msg",message);
	 
	  return new ModelAndView("index");
  }
  
  @RequestMapping("/analyse")
  public ModelAndView analyse() {
	  return new ModelAndView("index");
  }
  
  @RequestMapping("/test")
  public ModelAndView test() {
	  return new ModelAndView("test");
  }
}