package com.cam.springboothelloworld;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.patriques.AlphaVantageConnector;
import org.patriques.TimeSeries;
import org.patriques.input.timeseries.Interval;
import org.patriques.input.timeseries.OutputSize;
import org.patriques.output.AlphaVantageException;
import org.patriques.output.timeseries.IntraDay;
import org.patriques.output.timeseries.data.StockData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;


@RestController
@RequestMapping(value = "/cam-kafka/")
public class ApacheKakfaWebController {

	@Autowired
	KafkaSender kafkaSender;

	@GetMapping(value = "/producer")
	public ModelAndView producer(@RequestParam("message") String message) {
		kafkaSender.send("test_topic",message);

//		return "Message sent to the Kafka Topic cam Successfully";
		return new ModelAndView("message");
	}
	
	@GetMapping(value = "/startTxt")
	public ModelAndView startTxt() throws IOException, InterruptedException {
		FileReader fr=new FileReader("file.txt");  
		int i; 
		String message ="";
        while((i=fr.read())!=-1)    
        {
        	message += ((char)i);
        }
        fr.close();  
        String[] lines = message.split("\n");
        
        for(String line : lines)
		{
        	Thread.sleep(200);
        	kafkaSender.send("test_topic",line);
		}

		return new ModelAndView("message");
	}
	
	
	
	@GetMapping(value = "/startTwitter")
	public ModelAndView startTwitter() throws TwitterException, InterruptedException {
		//Configuration builder to authenticate with twitter api
		  ConfigurationBuilder cb = new ConfigurationBuilder();
	      cb.setDebugEnabled(true)
	      			.setOAuthConsumerKey("n644ZsMWr5FzIng2YuCfAA1fT")
	      			.setOAuthConsumerSecret("A6bD7yNNLvgJdtLxeYHN7FdKg6gIauKcyRhqDyFuiglodwHpcG")
	      			.setOAuthAccessToken("945229166059782144-zrJNruAmpil6TqYaMFbj0RBa4lQoHX7")
	      			.setOAuthAccessTokenSecret("2QP5STUKoTDukPaex4acrEKuYitq3wlyArTGBY0n8fzto");
		  
	      TwitterFactory tf = new TwitterFactory(cb.build());
	      twitter4j.Twitter twitter = tf.getInstance();
//	      List<Status> status = twitter.getHomeTimeline();
	      String value = "";
//	      for(Status st : status) {
//	    	 Thread.sleep(200);
//	    	 value = st.getUser().getName()+": -------- "+st.getText()+"\n";
//	    	 kafkaSender.send(value);
//	      }
	      Query query = new Query("india");
	        query.setCount(100);
	        QueryResult result = twitter.search(query);
	        for (Status status : result.getTweets()) {
	        	 Thread.sleep(200);
		    	 value = status.getText()+"\n";
		    	 kafkaSender.send("test_topic",value);
	        }

		return new ModelAndView("message");
	}
	
	@GetMapping(value = "/startXlsx")
	public ModelAndView startXlsx() throws IOException, InterruptedException {
		try
        {
            FileInputStream file = new FileInputStream(new File("NSE1.xlsx"));
 
            //Create Workbook instance holding reference to .xlsx file
            XSSFWorkbook workbook = new XSSFWorkbook(file);
 
            //Get first/desired sheet from the workbook
            XSSFSheet sheet = workbook.getSheetAt(0);
 
            //Iterate through each rows one by one
            Iterator<Row> rowIterator = sheet.iterator();
            
            String message = "0.0";
            while (rowIterator.hasNext())
            {	
                Row row = rowIterator.next();
                //For each row, iterate through all the columns		
                Iterator<Cell> cellIterator = row.cellIterator();
                Thread.sleep(1000);
                while (cellIterator.hasNext())
                {
                    Cell cell = cellIterator.next();
                    //Check the cell type and format accordingly

                    message = String.valueOf(cell.getNumericCellValue());
                }
                kafkaSender.send("test1",message);
            }
            file.close();
//            MA spark = new MA();
//            spark.main(null);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
		return new ModelAndView("message");
	}
	
	
	@GetMapping(value = "/startAlphaVantage")
	public ModelAndView startAlphaVantage(){
		String apiKey = "JSNST4OVP2BWTLF1";
	    int timeout = 10000;
	    AlphaVantageConnector apiConnector = new AlphaVantageConnector(apiKey, timeout);
	    TimeSeries stockTimeSeries = new TimeSeries(apiConnector);
	    
	    try {
	      IntraDay response = stockTimeSeries.intraDay("MSFT", Interval.ONE_MIN, OutputSize.FULL);
	      List<StockData> stockData = response.getStockData();
	      stockData.forEach(stock -> {
	    	  try {
				Thread.sleep(200);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		    kafkaSender.send("test_topic",String.valueOf(stock.getClose()));
	      });
	    } catch (AlphaVantageException e) {
	      System.out.println("something went wrong");
	      System.out.println(e.getMessage());
	    }
		return new ModelAndView("message");
	}

}