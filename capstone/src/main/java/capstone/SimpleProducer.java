package capstone;
import java.util.*;
import org.apache.kafka.clients.producer.*;
import org.patriques.AlphaVantageConnector;
import org.patriques.TimeSeries;
import org.patriques.input.timeseries.Interval;
import org.patriques.input.timeseries.OutputSize;
import org.patriques.output.AlphaVantageException;
import org.patriques.output.timeseries.IntraDay;
import org.patriques.output.timeseries.data.StockData;

import twitter4j.Status;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;
public class SimpleProducer {
  
   public static void main(String[] args) throws Exception{
      
//	  Configuration builder to authenticate with twitter api
//	  ConfigurationBuilder cb = new ConfigurationBuilder();
//      cb.setDebugEnabled(true)
//      			.setOAuthConsumerKey("n644ZsMWr5FzIng2YuCfAA1fT")
//      			.setOAuthConsumerSecret("A6bD7yNNLvgJdtLxeYHN7FdKg6gIauKcyRhqDyFuiglodwHpcG")
//      			.setOAuthAccessToken("945229166059782144-zrJNruAmpil6TqYaMFbj0RBa4lQoHX7")
//      			.setOAuthAccessTokenSecret("2QP5STUKoTDukPaex4acrEKuYitq3wlyArTGBY0n8fzto");
//	  
//      TwitterFactory tf = new TwitterFactory(cb.build());
//      twitter4j.Twitter twitter = tf.getInstance();
      
	   
	   String apiKey = "JSNST4OVP2BWTLF1";
	    int timeout = 10000;
	    AlphaVantageConnector apiConnector = new AlphaVantageConnector(apiKey, timeout);
	    TimeSeries stockTimeSeries = new TimeSeries(apiConnector);
	    
	    //setting properties
	      Properties props = new Properties();
	      props.put("bootstrap.servers", "localhost:9092,localhost:9093"); //list of kafka brokers
	      props.put("group.id", "test");
	      props.put("key.serializer","org.apache.kafka.common.serialization.StringSerializer");          
	      props.put("value.serializer", "org.apache.kafka.common.serialization.DoubleSerializer");
		        
	      Producer<String, Double> producer = new KafkaProducer <>(props);
	      
		  String topicName = "test1";
//
//	    
	    try {
	      IntraDay response = stockTimeSeries.intraDay("MSFT", Interval.ONE_MIN, OutputSize.FULL);
	      List<StockData> stockData = response.getStockData();
	      stockData.forEach(stock -> {
	    	 
    	  ProducerRecord<String, Double> record = new ProducerRecord<>(topicName,stock.getClose());
    	  try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	  producer.send(record);
	      });
	    } catch (AlphaVantageException e) {
	      System.out.println("something went wrong");
	      System.out.println(e.getMessage());
	    }
      
//	  String value="";
//      List<Status> status = twitter.getHomeTimeline();
//      for(Status st : status) {
//    	
//    	 value = st.getUser().getName()+": -------- "+st.getText()+"\n";
//    	 ProducerRecord<String, String> record = new ProducerRecord<>(topicName,value);
//    	 producer.send(record);
//      }
      
      producer.close();
	  
	  System.out.println("SimpleProducer Completed.");
   }
}