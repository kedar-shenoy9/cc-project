package customclasses;

import java.util.HashMap;

import java.awt.Color;
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import javax.swing.*;

//import org.cloudbus.cloudsim.Log;
//import org.jfree.ui.ApplicationFrame;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel; 
import org.jfree.chart.JFreeChart; 
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.CategoryDataset; 
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.chart.plot.XYPlot; 
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
//import org.jfree.ui.RefineryUtilities; 

public class Charts{
	
	private double[] values = new double[3];
	private double[][] completionTime = new double[100][3];
	private double minminThroughput;
	private double maxminThroughput;
	private double fcfsThroughput;
	private long[] lengths = new long[100];

	public Charts( String applicationTitle , double[] values, double[][] completionTime, double minminThroughput, double maxminThroughput, long[] cloudletSizes) {
//      super( applicationTitle );
      for(int i=0; i<values.length; i++) {
    	  this.values[i]  = values[i];
      }
      for(int i=0; i<completionTime.length; i++) {
    	  for(int j=0; j<completionTime[0].length; j++) {
    		  this.completionTime[i][j] = completionTime[i][j];
    	  }
      }
      for(int i=0; i<cloudletSizes.length; i++) {
    	  this.lengths[i] = cloudletSizes[i];
      }
      this.minminThroughput = minminThroughput;
      this.maxminThroughput = maxminThroughput;
//      Log.printLine(minminThroughput);
//      Log.printLine(maxminThroughput);
      
      //barchart 1 comparing the average completion time
      JFreeChart barChart = ChartFactory.createBarChart(
         "Total Completion Time",           
         "Algorithm",            
         "Time Taken",            
         createDataset1(),          
         PlotOrientation.VERTICAL,           
         true, true, false);
      
    //not test
      ChartPanel chartPanel1 = new ChartPanel( barChart );        
//      chartPanel.setPreferredSize(new java.awt.Dimension( 560 , 367 ) );        
//      setContentPane( chartPanel ); 
      
      //line chart for the completion time all the cloudlets
      //test
      JFreeChart xylineChart = ChartFactory.createXYLineChart(
         "Completion Times" ,
         "Cloudlet ID" ,
         "Completion Time" ,
         createDataset2() ,
         PlotOrientation.VERTICAL ,
         true , true , false);
         
      //test
      final XYPlot plot = xylineChart.getXYPlot( );
      
      XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer( );
      renderer.setSeriesPaint( 0 , Color.RED );
      renderer.setSeriesPaint( 1 , Color.BLUE );
      renderer.setSeriesPaint( 2 , Color.GREEN );
      renderer.setSeriesStroke( 0 , new BasicStroke( 1.0f ) );
      renderer.setSeriesStroke( 1 , new BasicStroke( 1.0f ) );
      renderer.setSeriesStroke( 2 , new BasicStroke( 1.0f ) );
      plot.setRenderer( renderer );
      
      ChartPanel chartPanel2 = new ChartPanel( xylineChart );
      
      //barchart 2 for throughput
      JFreeChart barChart1 = ChartFactory.createBarChart(
         "Average Throughput",           
         "Algorithm",            
         "Throughput",            
         createDataset3(),          
         PlotOrientation.VERTICAL,           
         true, true, false);
      
      ChartPanel chartPanel3 = new ChartPanel( barChart1 ); 
      
      //barchart 3 for lengths of cloudlets
      JFreeChart barChart2 = ChartFactory.createBarChart(
         "Cloudlet Siezes",           
         "Cloudlet ID",            
         "Length",            
         createDataset4(),          
         PlotOrientation.VERTICAL,           
         true, true, false);
      ChartPanel chartPanel4 = new ChartPanel( barChart2 );
      
      //final chart
      //test
      JFrame frame1 = new JFrame("Chart");
      frame1.getContentPane().add(chartPanel1, BorderLayout.WEST);
      frame1.getContentPane().add(chartPanel4, BorderLayout.EAST);
      frame1.pack();
      frame1.setVisible(true);
      
      JFrame frame2 = new JFrame("Chart");
      frame2.getContentPane().add(chartPanel2, BorderLayout.WEST);
      frame2.getContentPane().add(chartPanel3, BorderLayout.EAST);
      frame2.pack();
      frame2.setVisible(true);
   }
   
   private CategoryDataset createDataset1( ) {
      final String minmin = "Min-Min";        
      final String maxmin = "Max-Min";        
      final String fcfs = "FCFS";        
      final String time = "";
      final DefaultCategoryDataset dataset = 
      new DefaultCategoryDataset( );
      dataset.addValue( values[0] , minmin , time );        
      dataset.addValue( values[1] , maxmin , time);        
      dataset.addValue( values[2] , fcfs , time);           

      return dataset; 
   }
   
   //test
   private XYDataset createDataset2( ) {
      final XYSeries minmin = new XYSeries( "Min-Min" );          
      for(int i=0; i<completionTime.length; i++) {
    	  minmin.add(i, completionTime[i][0]);
      }
      
      final XYSeries maxmin = new XYSeries( "Max-Min" );          
      for(int i=0; i<completionTime.length; i++) {
    	  maxmin.add(i, completionTime[i][1]);
      }    
      
      final XYSeries fcfs = new XYSeries( "FCFS" );          
      for(int i=0; i<completionTime.length; i++) {
    	  fcfs.add(i, completionTime[i][2]);
      }          
      
      final XYSeriesCollection dataset = new XYSeriesCollection( );          
      dataset.addSeries( minmin );          
      dataset.addSeries( maxmin );          
      dataset.addSeries( fcfs );
      return dataset;
   }
   
   private CategoryDataset createDataset3( ) {
      final String minmin = "Min-Min";        
      final String maxmin = "Max-Min";     
      final String fcfs = "FCFS";
      final String time = "";
      final DefaultCategoryDataset dataset = 
      new DefaultCategoryDataset( );
      minminThroughput = 100.0/values[0];
      maxminThroughput = 100.0/values[1];
      fcfsThroughput = 100.0/values[2];
      dataset.addValue( minminThroughput , minmin , time );        
      dataset.addValue( maxminThroughput , maxmin , time);
      dataset.addValue(fcfsThroughput, fcfs, time);

      return dataset; 
   }
   
   private CategoryDataset createDataset4( ) {
//      final String minmin = "Min-Min";        
//      final String maxmin = "Max-Min";     
      final String time = "";
      final DefaultCategoryDataset dataset = 
      new DefaultCategoryDataset( );
      HashMap<Integer, String> hm = new HashMap<>();
      hm.put(0, "0k-100k");
      hm.put(1, "100k-200k");
      hm.put(2, "200k-300k");
      hm.put(3, "300k-400k");
      hm.put(4, "400k-500k");
      hm.put(5, "500k-600k");
      hm.put(6, "600k-700k");
      hm.put(7, "700k-800k");
      hm.put(8, "800k-900k");
      long[] ranges = {0,0,0,0,0,0,0,0,0};
      for(int i=0; i<lengths.length; i++) {
    	  if(lengths[i] > 0 && lengths[i] <= 100000)
    		  ranges[0] ++;
    	  else if(lengths[i] > 100000 && lengths[i] <= 200000)
    		  ranges[1] ++;
    	  else if(lengths[i] > 200000 && lengths[i] <= 300000)
    		  ranges[2] ++;
    	  else if(lengths[i] > 300000 && lengths[i] <= 400000)
    		  ranges[3] ++;
    	  else if(lengths[i] > 400000 && lengths[i] <= 500000)
    		  ranges[4] ++;
    	  else if(lengths[i] > 500000 && lengths[i] <= 600000)
    		  ranges[5] ++;
    	  else if(lengths[i] > 600000 && lengths[i] <= 700000)
    		  ranges[6] ++;
    	  else if(lengths[i] > 700000 && lengths[i] <= 800000)
    		  ranges[7] ++;
    	  else if(lengths[i] > 800000 && lengths[i] <= 900000)
    		  ranges[8] ++;
      }
      for(int i=0; i<ranges.length; i++) {
    	  dataset.addValue(ranges[i], hm.get(i), time);
      }

      return dataset; 
   }
   
}
