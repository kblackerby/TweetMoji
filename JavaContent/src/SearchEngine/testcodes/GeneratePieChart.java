package testcodes;

import javax.swing.JPanel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

public class GeneratePieChart extends ApplicationFrame {
	
	
	public GeneratePieChart( String title, int happyCount, int angryCount, int neutralCount ) 
	   {
	      super( title ); 
	      setContentPane(createDemoPanel(title, happyCount, angryCount, neutralCount ));
	   }
	
	   private static PieDataset createDataset(int happyCount, int angryCount, int neutralCount ) 
	   {
	      DefaultPieDataset dataset = new DefaultPieDataset( );
	      dataset.setValue( "Positive Sentiment" , new Double( happyCount ) );  
	      dataset.setValue( "Neutral Sentiment" , new Double( neutralCount ) );   
	      dataset.setValue( "Negative Sentiment" , new Double( angryCount ) );    
	      return dataset;         
	   }
	   
	   
	   private static JFreeChart createChart( String title, PieDataset dataset )
	   {
	      JFreeChart chart = ChartFactory.createPieChart(      
	         title,  // chart title 
	         dataset,        // data    
	         true,           // include legend   
	         true, 
	         false);

	      return chart;
	   }
	   
	   public static JPanel createDemoPanel(String title, int happyCount, int angryCount, int neutralCount )
	   {
	      JFreeChart chart = createChart(title, createDataset( happyCount, angryCount, neutralCount) );  
	      return new ChartPanel( chart ); 
	   }
	   
	   
	
	
	
	
	
	
	
	
	
	

}// end of class
