import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.scene.shape.*;
import javafx.scene.text.Text;
import javafx.scene.text.Font;
import javafx.scene.paint.Color;

public class TestApp extends Application {
    public void start(Stage stage) {
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
        Text tableText = new Text(520, 50, "Summary\nIshan");
        tableText.setFill(Color.WHITE);
        font = new Font(14);   
        tableText.setFont(font);


        // add elements
        Group root = new Group();
        root.getChildren().add(graphBackground);
        root.getChildren().add(backBackground);
        root.getChildren().add(grapGrids);
        root.getChildren().add(gridPath);
        root.getChildren().add(heading1);
        root.getChildren().add(heading2);
        root.getChildren().add(tableText);
        Scene scene = new Scene(root, 750, 300);
        scene.setFill(Color.rgb(2,3,6));


        stage.setTitle("Server-side Stock value viewer");
        stage.setScene(scene);
        stage.sizeToScene();
        stage.show();
    }
}