/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PiechartUI;

import java.awt.BorderLayout;
import java.awt.Dimension;
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

    /**
     * @param args the command line arguments
     */
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
    
    
    
    @Override
    public void init() {
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
 
        ObservableList<PieChart.Data> pieChartData =
                FXCollections.observableArrayList(
                new PieChart.Data("Negative: "/* + number of neg. tweets/total tweets */, 13),
                new PieChart.Data("Neutral: "/* + number of neutral tweets/total tweets */, 25),
                new PieChart.Data("Positive: "/* + number of pos. tweets/total tweets */, 10));
        final PieChart chart = new PieChart(pieChartData);
        chart.setTitle("Sentiment Analysis");        
        root.getChildren().add(chart);
        
        //Panel
        fxContainer.setScene(new Scene(root));
        
        for (final PieChart.Data data : chart.getData()) {
            data.getNode().addEventHandler(MouseEvent.MOUSE_PRESSED,
            new EventHandler<MouseEvent>() {
                @Override public void handle(MouseEvent e) {
            
                    switch(data.getName()){
                        case "Negative: " /* + number of neg. tweets/total tweets */: 
                            System.out.print("Negative: \n"+String.valueOf(data.getPieValue()/* divide by total tweets ) + "%"*/)); 
                            break;
                            
                        case "Neutral: " /* + number of neutral tweets/total tweets */: 
                            System.out.print("Neutral: \n"+String.valueOf(data.getPieValue()/* divide by total tweets ) + "%"*/));
                            break;
                            
                        case "Positive: " /* + number of neg. tweets/total tweets */: 
                            System.out.print("Positive: \n"+String.valueOf(data.getPieValue()/* divide by total tweets ) + "%"*/));
                            break;
                    }
                 }
            });
        }
    }
    
}
