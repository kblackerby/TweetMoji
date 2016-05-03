/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PiechartUI;

import displaytweets.FilteredResultsDisplay;
import displaytweets.SearchGUI;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.File;
import java.util.ArrayList;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.JFXPanel;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javax.swing.JApplet;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

/**
 *
 * @author DanielMedina
 */
public class PieChartUI extends JApplet {
    //Set panel dimensions
    private static final int JFXPANEL_WIDTH_INT = 500;
    private static final int JFXPANEL_HEIGHT_INT = 520;
    private static JFXPanel fxContainer;
    private ArrayList<File> negList;
    private ArrayList<File> neutList;
    private ArrayList<File> posList;
    
    public FilteredResultsDisplay negFrame;
    public FilteredResultsDisplay neutFrame;
    public FilteredResultsDisplay posFrame;
    
    /**
     * @param args the command line arguments
     */
/*
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            
            @Override
            public void run() {
                try {
                    UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
                } catch (Exception e) {
                }
                
                JFrame frame = new JFrame("Tweet Pie Chart");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                
                JApplet applet = new PieChartUI();
                applet.init();
                
                frame.setContentPane(applet.getContentPane());
                
                frame.pack();
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
                
                applet.start();
                
            }
        });
    }
*/    
    
    
    public void init(ArrayList<File> ngList,ArrayList<File> ntList,ArrayList<File> pList) {
        negList = ngList;
        neutList = ntList;
        posList = pList;
        negFrame = null;
        neutFrame = null;
        posFrame = null;
        
        fxContainer = new JFXPanel();
        fxContainer.setPreferredSize(new Dimension(JFXPANEL_WIDTH_INT, JFXPANEL_HEIGHT_INT));
        add(fxContainer, BorderLayout.CENTER);
        // create JavaFX scene
        Platform.runLater(new Runnable() {
            
            @Override
            public void run() {
                createScene();
                
            }
        });
    }
    
    
    // --- Pie Chart---
    private void createScene() {
        
        StackPane root = new StackPane();
        int totalTweets = negList.size() + neutList.size() + posList.size();
        ObservableList<PieChart.Data> pieChartData =
                FXCollections.observableArrayList(
                new PieChart.Data("Negative: " + (double) negList.size()/totalTweets*100 + "%", negList.size()),
                new PieChart.Data("Neutral: " + (double) neutList.size()/totalTweets*100 + "%", neutList.size()),
                new PieChart.Data("Positive: " + (double) posList.size()/totalTweets*100 + "%", posList.size()));
        final PieChart chart = new PieChart(pieChartData);
        chart.setTitle("Sentiment Analysis");        
        root.getChildren().add(chart);
        
        //Panel
        fxContainer.setScene(new Scene(root));
        
        
        for (final PieChart.Data data : chart.getData()) {
            data.getNode().addEventHandler(MouseEvent.MOUSE_PRESSED, (MouseEvent e) -> {
                if (data.getName().equals("Negative: " + (double) negList.size()/totalTweets*100 + "%")) {
                    System.out.print("Negative: \n"+String.valueOf(data.getPieValue() + "\n"/* divide by total tweets ) + "%"*/));
                    // ((SearchGUI) getRootPane().getParent()).setFilteredList(new ArrayList<File>());
                    if(negFrame == null) {
                        negFrame = new FilteredResultsDisplay(negList, this);
                        negFrame.setTitle("Negative Tweets");
                        negFrame.setVisible(true);
                    }
                } else if (data.getName().equals("Neutral: " + (double) neutList.size()/totalTweets*100 + "%")) {
                    System.out.print("Neutral: \n"+String.valueOf(data.getPieValue() + "\n"/* divide by total tweets ) + "%"*/));
                    if(neutFrame == null) {
                        neutFrame = new FilteredResultsDisplay(neutList, this);
                        neutFrame.setTitle("Neutral Tweets");
                        neutFrame.setVisible(true);
                    }
                } else if (data.getName().equals("Positive: " + (double) posList.size()/totalTweets*100 + "%")) {
                    System.out.print("Positive: \n"+String.valueOf(data.getPieValue() + "\n"/* divide by total tweets ) + "%"*/));
                    if(posFrame == null) {
                        posFrame = new FilteredResultsDisplay(posList, this);
                        posFrame.setTitle("Positive Tweets");
                        posFrame.setVisible(true);
                    }
                }
            });
        }
    }
    
}
