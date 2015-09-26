import java.util.*;

// JavaFX application imports
import javafx.stage.WindowEvent;
import javafx.event.EventHandler;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.scene.shape.*;
import javafx.scene.text.Text;
import javafx.scene.text.Font;
import javafx.scene.paint.Color;
import javafx.concurrent.Task;
import javafx.animation.Transition.*;
import javafx.animation.TranslateTransition;
import javafx.util.Duration;
import javafx.scene.control.Label;
import javafx.beans.property.SimpleStringProperty;

public class Visual_StockStatDisplay extends Application implements StockStatDisplay{
	private StockDatabase stockStats;
	private Map currentStats;
	private String outputString;
    private SimpleStringProperty outputStringBind;
    private static int GRAPH_RESOLUTION = 50;
	private static float graphPoints[][] = new float[GRAPH_RESOLUTION][8];
	private Circle graphNodes_circles[][] = new Circle[GRAPH_RESOLUTION][8];
    private Path connectedPath[];
	private Color colorArray[] = {
								Color.rgb(232, 65, 65),
								Color.rgb(234, 163, 26),
								Color.rgb(123, 251, 60),
								Color.rgb(43, 251, 170),
								Color.rgb(40, 141, 244),
								Color.rgb(130, 55, 254),
								Color.rgb(247, 39, 205),
								Color.rgb(255, 41, 101)
							};
	private Group root;


	public Visual_StockStatDisplay(){
		stockStats = new StockDatabase("availableStocks.csv","Symbol", "Price");
		// streamData();
	}
	public void streamData(){
            getData();
            outputData();
	}
	public void getData(){
		currentStats = stockStats.getStockStats();
        // System.out.println(currentStats.toString());
	}
	public void outputData(){
		Set keys = currentStats.keySet();
		int index = 0;
	    StringBuilder sb = new StringBuilder();
		for (Iterator i = keys.iterator(); i.hasNext();){
	    	sb.append("\r\n");
			String key = (String) i.next();
			sb.append(key);
			if(key.length() < 5) 
				for(int t_i=0; t_i <= 5-key.length() + 1; t_i++)
					sb.append(" ");
			sb.append("\t");
			sb.append(currentStats.get(key).toString());
			graphPoints[GRAPH_RESOLUTION-1][index++] = (Float)currentStats.get(key);
			outputString = sb.toString();
        }
    }

	// javaFX window
	public void start(Stage stage) {

        outputStringBind = new SimpleStringProperty();
        root = new Group();
        // Graph background
        Rectangle backBackground = new Rectangle(500,0,250,300);
        backBackground.setFill(Color.rgb(8,9,12));
        Rectangle graphBackground = new Rectangle(0,0,500,300);
        graphBackground.setFill(Color.rgb(14,15,19));

        // Graph grids 
        int x,y,width, height;
        x = 20;
        y = 50;
        width = 460;
        height = 230;
        Rectangle grapGrids = new Rectangle(x,y,width,height);
        grapGrids.setStroke(Color.rgb(32,35,42));
        grapGrids.setFill(Color.rgb(14,15,19));

        Path gridPath = new Path();

        for(int i=(int)Math.floor(width/4); i<width; i+=(int)Math.floor(width/4)){
            gridPath.getElements().add(new MoveTo(x+i,y));
            gridPath.getElements().add(new LineTo(x+i, y+height));
        }
        for(int i=(int)Math.floor(height/3); i<height-10; i+=(int)Math.floor(height/3)){
            gridPath.getElements().add(new MoveTo(x,y+i));
            gridPath.getElements().add(new LineTo(x+width,y+i));
        }

        gridPath.setStroke(Color.rgb(32,35,42));
        gridPath.setStrokeWidth(1);

        // Heading 1
        Text heading1 = new Text(20, 30, "Stock Values");
        heading1.setFill(Color.WHITE);
        Font font = new Font(20);   
        heading1.setFont(font);

        // Heading 2
        Text heading2 = new Text(520, 30, "Summary");
        heading2.setFill(Color.WHITE);
        font = new Font(16);   
        heading2.setFont(font);

        // Table

        Text tableHeader = new Text(570, 70, "Symbol\tPrice");
        tableHeader.setFill(Color.WHITE);
        font = new Font(12);   
        tableHeader.setFont(font);

        
        Text tableText = new Text(570, 80, "\nwaiting");
        tableText.setFill(Color.WHITE);
        font = new Font(14);  
        tableText.setFont(font);

        // Heading 2
        Text timeDisp = new Text(520, 280, (new java.util.Date()).toString());
        timeDisp.setFill(Color.WHITE);
        font = new Font(14);   
        timeDisp.setFont(font);


        // add elements
        root.getChildren().add(graphBackground);
        root.getChildren().add(backBackground);
        root.getChildren().add(grapGrids);
        root.getChildren().add(gridPath);
        root.getChildren().add(heading1);
        root.getChildren().add(heading2);
        root.getChildren().add(tableHeader);
        root.getChildren().add(tableText);
        root.getChildren().add(timeDisp);
        Scene scene = new Scene(root, 750, 300);
        scene.setFill(Color.rgb(2,3,6));

        drawGraph(root);

        Task task = new Task<Void>() {
            @Override public Void call() {
                while(true) {
                    try{
                        leftShiftGraph();
                        getData();
                        outputData();
                        // tableText.setText("ASDF");
                        // outputStringBind.setValue("ISHAN");
                        // tableText.setText(outputString)
                        timeDisp.setText((new java.util.Date()).toString());
                        tableText.setText(outputString);
                        // System.out.println(outputString);
                        updateGraph(root);
                        Thread.sleep(displayInterval);
                    } catch (InterruptedException e){
                        System.out.println(e);
                    } catch (Exception e){
                        System.out.println(e);
                    }
                    
                }
            }
        };
        Platform.runLater(new Runnable() {
            public void run() {
                new Thread(task).start();
            }
        });
        // add Legend symbols
        for(int i=0; i<8; i++){
        	Circle legendNode = nodePoint(540, 96+18*i);
        	legendNode.setFill(colorArray[i]);
        	root.getChildren().add(legendNode);
        }

        stage.setTitle("Server-side Stock value viewer");
        stage.setScene(scene);
        stage.sizeToScene();
        stage.setResizable(false);
        stage.show();
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent t) {
                System.exit(0);
            }
        });

    }
    private Circle nodePoint(int x, int y) {
        return CircleBuilder.create()
           .radius (6)
           .centerX(x)
           .centerY(y)
           .build();
    }
    private synchronized void leftShiftGraph(){
    	for(int i=0; i<GRAPH_RESOLUTION-1; i++){
    		for(int j=0; j<8; j++){
    			graphPoints[i][j] = graphPoints[i+1][j];
    		}
    	}
    }
    private void drawGraph(Group root){
        float graphYmin = 0f;
        float graphYmax = 100f;
        graphYmin = graphPoints[3][2];
        for(int i=0; i<GRAPH_RESOLUTION; i++){
            for(int j=0; j<8; j++){
              if(graphPoints[i][j] < graphYmin)
                 graphYmin = graphPoints[i][j];
            }
        }
        for(int i=0; i<GRAPH_RESOLUTION; i++){
            for(int j=0; j<8; j++){
              if(graphPoints[i][j] > graphYmax)
                 graphYmax = graphPoints[i][j];
            }
        }
        for(int i=0; i<GRAPH_RESOLUTION; i++){
            for(int j=0; j<8; j++){
                graphNodes_circles[i][j] = CircleBuilder.create()
                .radius (2+30/GRAPH_RESOLUTION)
                .centerX(23 + i*(460f/(GRAPH_RESOLUTION-1)))
                .centerY(280 - graphPoints[i][j]/(graphYmax - graphYmin)*230)
                .build();
                graphNodes_circles[i][j].setFill(colorArray[j]);
                root.getChildren().add(graphNodes_circles[i][j]);
            }
        }

        Path connectedPath[] = new Path[8];
        for(int j=0; j<8; j++){
            for(int i=0; i<GRAPH_RESOLUTION-1; i++){
                Line line = new Line();

                line.startXProperty().bind(graphNodes_circles[i][j].centerXProperty().add(graphNodes_circles[i][j].translateXProperty()));
                line.startYProperty().bind(graphNodes_circles[i][j].centerYProperty().add(graphNodes_circles[i][j].translateYProperty()));
                line.endXProperty().bind(graphNodes_circles[i+1][j].centerXProperty().add(graphNodes_circles[i+1][j].translateXProperty()));
                line.endYProperty().bind(graphNodes_circles[i+1][j].centerYProperty().add(graphNodes_circles[i+1][j].translateYProperty()));
                line.setStroke(colorArray[j]);
                line.setStrokeWidth(2);
                root.getChildren().add(line);
            }
        }        
    }
    private void updateGraph(Group root){
        float graphYmin = 0f;
        float graphYmax = 100f;
        graphYmin = graphPoints[0][0];
        for(int i=0; i<GRAPH_RESOLUTION; i++){
            for(int j=0; j<8; j++){
              if(graphPoints[i][j] < graphYmin)
                 graphYmin = graphPoints[i][j];
            }
        }
        graphYmax = graphPoints[0][0];
        for(int i=0; i<GRAPH_RESOLUTION; i++){
            for(int j=0; j<8; j++){
              if(graphPoints[i][j] > graphYmax)
                 graphYmax = graphPoints[i][j];
            }
        }
        for(int i=0; i<GRAPH_RESOLUTION; i++){
            for(int j=0; j<8; j++){
                Double transitionValue = (280 - graphPoints[i][j]/(graphYmax - graphYmin)*230)-graphNodes_circles[i][j].getCenterY();
                if(transitionValue>-0.5) continue;
                TranslateTransition tt = new TranslateTransition(Duration.millis(displayInterval/4), graphNodes_circles[i][j]);
                tt.setToY(transitionValue);
                tt.play();
            }
        } 
    }
}