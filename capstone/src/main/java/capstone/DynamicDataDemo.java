package capstone;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Properties;

import javax.swing.JButton;
import javax.swing.JPanel;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.time.Millisecond;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

public class DynamicDataDemo extends ApplicationFrame{

    /** The time series data. */
    private static TimeSeries series1, series2;

    /** The most recent value added. */
    private static double lastValue1 = 0.0, lastValue2 = 0.0;
    

    public DynamicDataDemo(final String title) {

        super(title);
        this.series1 = new TimeSeries("Closing Value", Millisecond.class);
        this.series2 = new TimeSeries("Moving Avg", Millisecond.class);
        final TimeSeriesCollection dataset = new TimeSeriesCollection();
        dataset.addSeries(series1);
        dataset.addSeries(series2);
        final JFreeChart chart = createChart(dataset);
        

        final ChartPanel chartPanel = new ChartPanel(chart);
//        final JButton button = new JButton("Add New Data Item");
//        button.setActionCommand("ADD_DATA");
//        button.addActionListener(this);

        final JPanel content = new JPanel(new BorderLayout());
        content.add(chartPanel);
//        content.add(button, BorderLayout.SOUTH);
        chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));
        setContentPane(content);

    }

    
    private JFreeChart createChart(final XYDataset dataset) {
        final JFreeChart result = ChartFactory.createTimeSeriesChart(
            "Stock Market Data of NSE", 
            "Time", 
            "Value ($)",
            dataset, 
            true, 
            true, 
            false
        );
        final XYPlot plot = result.getXYPlot();
        ValueAxis axis = plot.getDomainAxis();
        axis.setAutoRange(true);
        axis.setFixedAutoRange(60000.0);  // 60 seconds
        axis = plot.getRangeAxis();
        axis.setAutoRange(true);
//        if(comp == "msft")
//        	axis.setRange(90, 100.0);
//        else
//        		axis.setRange(5200, 12000.0); 
        return result;
    }
    
    
    
  

    
    public static void main(final String[] args) throws UnknownHostException, IOException, InterruptedException {

        final DynamicDataDemo demo = new DynamicDataDemo("Stock market data");
        demo.pack();
        RefineryUtilities.centerFrameOnScreen(demo);
        demo.setVisible(true);
        
        String topicName = "test1";
        Properties props = new Properties();
        
        props.put("bootstrap.servers", "localhost:9092");
        props.put("group.id", "test1");
        props.put("key.deserializer", 
           "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", 
           "org.apache.kafka.common.serialization.StringDeserializer");
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest");
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "true");
        KafkaConsumer<String, String> consumer = new KafkaConsumer
           <String, String>(props);
        
        consumer.subscribe(Arrays.asList(topicName));
        
        System.out.println("Subscribed to topic " + topicName);
        
        String topicName2 = "spark";
        Properties props2 = new Properties();
        
        props2.put("bootstrap.servers", "localhost:9092");
        props2.put("group.id", "test2");
        props2.put("key.deserializer", 
           "org.apache.kafka.common.serialization.StringDeserializer");
        props2.put("value.deserializer", 
           "org.apache.kafka.common.serialization.StringDeserializer");
        props2.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest");
        props2.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "true");
        
        KafkaConsumer<String, String> consumer2 = new KafkaConsumer
           <String, String>(props2);
        
        consumer2.subscribe(Arrays.asList(topicName2));
        
        System.out.println("Subscribed to topic " + topicName2);
        

        while (true) {
        	System.out.println("---------");
        	ConsumerRecords<String, String> records = consumer.poll(100);
           for (ConsumerRecord<String, String> record : records)
        	   lastValue1 = Double.parseDouble(record.value()); 

           final Millisecond now = new Millisecond();
           System.out.println("Now = " + now.toString() + "last value1: "+ lastValue1);
           series1.add(new Millisecond(), lastValue1);
           
           System.out.println("----------------------------------------");
           ConsumerRecords<String, String> records2 = consumer2.poll(100);
           
           for (ConsumerRecord<String, String> record : records2)
        	   lastValue2 = Double.parseDouble(record.value()); 
         
           System.out.println("Now = " + now.toString() + "last value2: "+ lastValue2);
           series2.add(new Millisecond(), lastValue2);
        }
        
    }

}

