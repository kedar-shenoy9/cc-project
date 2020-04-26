package customclasses;

import java.awt.Color; 
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import javax.swing.*;

import org.jfree.ui.ApplicationFrame;
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

	public Charts( String applicationTitle , double[] values, double[][] completionTime) {
//      super( applicationTitle );
      for(int i=0; i<values.length; i++) {
    	  this.values[i]  = values[i];
      }
      for(int i=0; i<completionTime.length; i++) {
    	  for(int j=0; j<completionTime[0].length; j++) {
    		  this.completionTime[i][j] = completionTime[i][j];
    	  }
      }
      JFreeChart barChart = ChartFactory.createBarChart(
         "Average Completion Time",           
         "Algorithm",            
         "Time Taken",            
         createDataset1(),          
         PlotOrientation.VERTICAL,           
         true, true, false);
      
      //test
      JFreeChart xylineChart = ChartFactory.createXYLineChart(
    	         "Completion Times" ,
    	         "VM ID" ,
    	         "Completion Time" ,
    	         createDataset2() ,
    	         PlotOrientation.VERTICAL ,
    	         true , true , false);
         
      //not test
      ChartPanel chartPanel1 = new ChartPanel( barChart );        
//      chartPanel.setPreferredSize(new java.awt.Dimension( 560 , 367 ) );        
//      setContentPane( chartPanel ); 
      
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
      
      //test
      JFrame frame = new JFrame("Chart");
      frame.getContentPane().add(chartPanel1, BorderLayout.WEST);
      frame.getContentPane().add(chartPanel2, BorderLayout.EAST);
      frame.pack();
      frame.setVisible(true);
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
	   
//	   	final XYSeries firefox = new XYSeries( "Firefox" );          
//	      firefox.add( 1.0 , 1.0 );          
//	      firefox.add( 2.0 , 4.0 );          
//	      firefox.add( 3.0 , 3.0 );          
//	      
//	      final XYSeries chrome = new XYSeries( "Chrome" );          
//	      chrome.add( 1.0 , 4.0 );          
//	      chrome.add( 2.0 , 5.0 );          
//	      chrome.add( 3.0 , 6.0 );          
//	      
//	      final XYSeries iexplorer = new XYSeries( "InternetExplorer" );          
//	      iexplorer.add( 3.0 , 4.0 );          
//	      iexplorer.add( 4.0 , 5.0 );          
//	      iexplorer.add( 5.0 , 4.0 );          
//	      
//	      final XYSeriesCollection dataset = new XYSeriesCollection( );          
//	      dataset.addSeries( firefox );          
//	      dataset.addSeries( chrome );          
//	      dataset.addSeries( iexplorer );
//	      return dataset;
	   }
}
