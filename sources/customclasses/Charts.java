package customclasses;

import org.jfree.ui.ApplicationFrame;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel; 
import org.jfree.chart.JFreeChart; 
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.CategoryDataset; 
import org.jfree.data.category.DefaultCategoryDataset;
//import org.jfree.ui.RefineryUtilities; 

public class Charts extends ApplicationFrame {
	
	private double[] values = new double[3];

	public Charts( String applicationTitle , String chartTitle, double[] values ) {
      super( applicationTitle );
      for(int i=0; i<values.length; i++) {
    	  this.values[i]  = values[i];
      }
      JFreeChart barChart = ChartFactory.createBarChart(
         chartTitle,           
         "Algorithm",            
         "Time Taked",            
         createDataset(),          
         PlotOrientation.VERTICAL,           
         true, true, false);
         
      ChartPanel chartPanel = new ChartPanel( barChart );        
      chartPanel.setPreferredSize(new java.awt.Dimension( 560 , 367 ) );        
      setContentPane( chartPanel ); 
   }
   
   private CategoryDataset createDataset( ) {
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
}
