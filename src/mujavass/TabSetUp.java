/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mujavass;

import java.util.ArrayList;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Orientation;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SplitPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

/**
 *
 * @author Luke
 */
public class TabSetUp {
    @FXML private TextFlow t1;
    @FXML private TextFlow t2;
    @FXML private TextArea textArea;
    @FXML private ScrollPane sp1;
    @FXML private ScrollPane sp2;
        
    public Tab createTextTab(String name, ArrayList<String> textArray) {
        String text = "";
        for (String string : textArray) {
            text += string + "\n";
        }
        
        textArea = new TextArea(text);
 
        Tab tab = new Tab(name);
        tab.setContent(textArea);
        
        return tab;
    }
    
    public Tab createResultsTab(String key, Test test) {
        t2 = new TextFlow();
        t1 = new TextFlow();
        //results tab needs a border pane to hold all components
        BorderPane borderpane = new BorderPane();
        BorderPane rightSection = new BorderPane();
        
        //The left section of the border pane will hold the listview for mutants
        ListView list = setupList(key, test); //inserts mutants from the test into the list
        borderpane.setLeft(list);
        
        //Right section of the tabpane. Split to hold the 2 areas of code
        SplitPane split = setupSplit();
        //TextField to hold the test results(score, killed, live total)
        TextField score = setupScore(test);
        score.setMaxHeight(1.0); //restrict the height of text area.
        
        rightSection.setCenter(split);
        rightSection.setTop(score); 
        
        borderpane.setCenter(rightSection);
        
        
        Tab tab = new Tab(key);
        tab.setContent(borderpane);
        
        return tab;
    }

    public TextField setupScore(Test test) {
        TextField score = new TextField();
        Result result = test.getResult();
        
        String text = ("Score: " + result.getScore() + "%" +
                        " Live Mutants #: " + result.getLive() + 
                        " Killed Mutants #: " + result.getKilled() +
                        " Total Mutants #: " + result.getTotal());
        
        score.setText(text);
        
        return score;
    }
          
    public SplitPane setupSplit() {
        SplitPane split = new SplitPane();
        split.setOrientation(Orientation.VERTICAL);
        
        sp1 = new ScrollPane(t1);
        sp2 = new ScrollPane(t2);
        
        sp2.setFitToWidth(true);
        sp2.setPrefWidth(400);
        sp2.setVvalue(100);
        
        sp1.setFitToWidth(true);
        sp1.setPrefWidth(400);
        sp1.setVvalue(100);

        split.getItems().addAll(sp1, sp2);
              
        return split;
    }
 
//Create the listview from the test mutants
    public ListView setupList(String key, Test test) {
        ListView list = new ListView();

        ObservableList<String> items =FXCollections.observableArrayList();
        for (String mutant : test.getMutants().keySet()) {
            items.add(mutant);
        }
        
        list.setItems(items); 
        //Set up the listener. So when mutant is clicked. Fills the 
        list.getSelectionModel().selectedItemProperty().addListener(
            new ChangeListener<String>() {
                public void changed(ObservableValue<? extends String> ov, 
                    String old_val, String new_val) {
                        //need to clear so each time a new mutant is clicked, old text is removed
                        t2.getChildren().clear();
                        t1.getChildren().clear();
                    
                        Result result = test.getResult();
                        Mutant mutant = result.getMutant(new_val);
                        
                        t1.getChildren().addAll(createFlow(mutant.getOriginal()));
                        t2.getChildren().addAll(createFlow(mutant.getChanged()));
                    }
                });
        return list;
    
    }
    
    public TextFlow createFlow(String[] text) {
        TextFlow tf = new TextFlow();
        
        Text before = new Text(text[0]);
        Text change = new Text(text[1]);
        change.setFill(Color.RED);
        Text after = new Text(text[2]);
        
        tf.getChildren().addAll(before, change, after);
        return tf;
    }
    
    public void setUpListH(Test test) {
        
    }
}
