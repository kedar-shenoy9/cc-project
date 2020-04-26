package customclasses;

import java.awt.Color;
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import javax.swing.*;

import org.cloudbus.cloudsim.Log;
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

	public Charts( String applicationTitle , double[] values, double[][] completionTime, double minminThroughput, double maxminThroughput) {
//      super( applicationTitle );
      for(int i=0; i<values.length; i++) {
    	  this.values[i]  = values[i];
      }
      for(int i=0; i<completionTime.length; i++) {
    	  for(int j=0; j<completionTime[0].length; j++) {
    		  this.completionTime[i][j] = completionTime[i][j];
    	  }
      }
      this.minminThroughput = minminThroughput;
      this.maxminThroughput = maxminThroughput;
      Log.printLine(minminThroughput);
      Log.printLine(maxminThroughput);
      
      //barchart 1 comparing the average completion time
      JFreeChart barChart = ChartFactory.createBarChart(
         "Average Completion Time",           
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
    	         "VM ID" ,
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
      
      //final chart
      //test
      JFrame frame1 = new JFrame("Chart");
      frame1.getContentPane().add(chartPanel1, BorderLayout.WEST);
      frame1.getContentPane().add(chartPanel2, BorderLayout.EAST);
      frame1.pack();
      frame1.setVisible(true);
      
      JFrame frame2 = new JFrame("Chart");
      frame2.getContentPane().add(chartPanel3);
      frame2.pack();
      frame2.setVisible(true);
   }
   
   private CategoryDataset createDataset1( ) {
      final String minmin = "Min-Min";        
      final String maxmin = "Max-Min";        
      final String fcfs = "FCFS";        
      final String time = "Time";
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
	      final String time = "Time";
	      final DefaultCategoryDataset dataset = 
	      new DefaultCategoryDataset( );
	      dataset.addValue( minminThroughput , minmin , time );        
	      dataset.addValue( maxminThroughput , maxmin , time);   

	      return dataset; 
	   }
}
