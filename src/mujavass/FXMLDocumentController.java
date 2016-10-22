/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mujavass;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 *
 * @author Luke
 */
public class FXMLDocumentController implements Initializable {
    
    @FXML private Label label;
    @FXML private TreeView tree;
    @FXML private ListView listV;
    @FXML private TabPane tabpane;
    @FXML private MenuItem newProject;
    @FXML private MenuItem importFile;
    private TreeItem<String> root;
    private List<Test> tests;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        tests = new LinkedList<>();

        //Load the saved state from previous run. Add all tests to the test list
        loadState();
        
        //Create the treeview from the test list
        createTree();
        
        createMenu();
        
        printChildren(root);
        
        importFile();

    }
    
    /********************************************************************
     *  File Loader
     ********************************/
    public void importFile() {
        importFile.setOnAction(new EventHandler<ActionEvent>() {
        public void handle(ActionEvent e) {
            final Stage dialog = new Stage();
            dialog.setTitle("File Import");
            //set up pop out window
            BorderPane bp = new BorderPane();
            Text instruction = new Text();
            ListView list = new ListView();
            
            ObservableList<String> items =FXCollections.observableArrayList();
            for(TreeItem<String> child: root.getChildren()){
                items.add(child.getValue());
            }
            list.setItems(items); 
            
            Button importJava = new Button("Java File");
            Button importTest = new Button("JUnit Test");
                    
            importJava.setOnAction(new EventHandler<ActionEvent>() {
                public void handle(ActionEvent e) {
                    FileChooser fileChooser = new FileChooser();
                    fileChooser.setTitle("Load File");
                    Stage open = new Stage();
                    File file = fileChooser.showOpenDialog(open);
                    
                    System.out.println(list.getSelectionModel().getSelectedItem()); //use this for directory
                    File dest = new File("" + file.getName());

                    open.show();
                    
                    //file copy stuffs
                    try{
                        FileInputStream instream = new FileInputStream(file);
                        FileOutputStream outstream = new FileOutputStream(dest);

                        byte[] buffer = new byte[1024];

                        int length;
                        /*copying the contents from input stream to
                         * output stream using read and write methods
                         */
                        while ((length = instream.read(buffer)) > 0){
                            outstream.write(buffer, 0, length);
                        }

                        //Closing the input/output file streams
                        instream.close();
                        outstream.close();

                    }catch(IOException ioe){
                            ioe.printStackTrace();
                    }

                    String name = file.getName();
                    int pos = name.lastIndexOf(".");
                    if (pos > 0) {
                        name = name.substring(0, pos);
                    }

                    for(TreeItem<String> child: root.getChildren()){
                        if(child.getValue().equals(list.getSelectionModel().getSelectedItem())) {
                            for(TreeItem<String> child2: child.getChildren()) {
                                if(child2.getValue().equals("Original files")) {
                                    TreeItem<String> item = new TreeItem<>(name);
                                    child2.getChildren().addAll(item);
                                }
                            }
                        }
                    }    
                }
            });
            
            
            instruction.setText("Select project.");
            
            
            VBox vbox = new VBox();
            HBox hbox = new HBox();
            hbox.getChildren().addAll(importJava, importTest);
            hbox.setAlignment(Pos.CENTER);
            vbox.getChildren().addAll(instruction, list, hbox);
            bp.setCenter(vbox);
 
            Scene scene = new Scene(bp, 200, 350);
            

            
            dialog.setResizable(false);
            dialog.setScene(scene);
            dialog.show();
            
            }
        });      
    }
    
    public void printChildren(TreeItem<String> root) {
        for(TreeItem<String> child: root.getChildren()){
                System.out.println(child.getValue());
        }    
    }
    
    public void createMenu() {
        //newProject = new MenuItem();
        newProject.setOnAction(new EventHandler<ActionEvent>() {
        public void handle(ActionEvent e) {
            final Stage dialog = new Stage();
            dialog.setTitle("New Project");
            //set up pop out window
            BorderPane bp = new BorderPane();
            Text instruction = new Text();
            TextField userInput = new TextField();
            Button generate = new Button("Generate");
            userInput.setPrefWidth(270);
            
            instruction.setText("Enter the new projects name.");
            
            bp.setTop(instruction);
            
            HBox hbox = new HBox();
            
            hbox.getChildren().addAll(userInput, generate);
            bp.setCenter(hbox);
 
            Scene scene = new Scene(bp, 350, 40);
            
            //add listener to button. When pressed, create new project with name
            generate.setOnAction(new EventHandler<ActionEvent>() {
                public void handle(ActionEvent e) {
                    createProject(userInput.getText());
                }
            });
            
            dialog.setResizable(false);
            dialog.setScene(scene);
            dialog.show();
            
            }
        });
    }
    
    //creates an empty project with user supplied name
    public void createProject(String name) {
        TreeItem<String> project, testCases, originalFiles, results;
        
        project = makeBranch(name, root);   
        
        testCases = makeBranch("Test cases", project);
        originalFiles = makeBranch("Original files", project);
        results = makeBranch("Results", project);
    }
    
    public void loadState() {
        try {
            LoadSave ls = new LoadSave();
            List<String> list = ls.loadSave("saved/Save.txt"); //hardcoded for now.

            for (String name : list) {
                Test test = ls.loadTest(name); 
                tests.add(test);
            }
        } catch (IOException ex) {
            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        }        
    }

    /*********************************************************************/
    
    /*
        Three methods required for setting up and adding to the treeview
        createTree sets up the initial tree. Adds all tests in the tests
        list to the tree. These will come from the load file.
    */
    public void createTree() {
        root = new TreeItem("root");
        root.setExpanded(true);
        tree.setRoot(root);
        tree.setShowRoot(false);
        
        for (Test test : tests) {
            addToTree(test);
        }
    }
    
    /*
        Add passed in test object to the tree. Can come from loading the previous
        state, or by creating a new test.
    */
    public void addToTree(Test test) {
        TreeItem<String> testName, testCases, originalFiles, results;
       
        testName = makeBranch(test.getName(), root);        
        testCases = makeBranch("Test cases", testName);
        originalFiles = makeBranch("Original files", testName);
        results = makeBranch("Results", testName);
        
        TreeItem<String> testCase, javaFile, result;
        
        testCase = makeBranch(test.getTestCase().getName(), testCases);
        javaFile = makeBranch(test.getJavaFile().getName(), originalFiles);
        result = makeBranch(test.getResult().getName(), results);
        
        idk();
    }
   
    public TreeItem<String> makeBranch(String title, TreeItem<String> parent){
        TreeItem<String> item = new TreeItem<>(title);
        item.setExpanded(true);
        parent.getChildren().add(item);
        
        return item;
    }
    
    public void idk() {
        tree.getSelectionModel().selectedItemProperty().addListener( new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                TreeItem<String> selectedItem = (TreeItem<String>) newValue;
                //work around so that only children will create a new tab. Needs more work...
                createTab(selectedItem);                  
            }
        });
    }

    /*********************************************************************/
    
    public void createTab(TreeItem<String> item) {
        Tab tab = new Tab(item.getValue());
        TabSetUp tsu = new TabSetUp();
        
        if(item.getParent().getValue().equals("Test cases")){
            Test test = findTest(item);
            tab = tsu.createTextTab(item.getValue(), test.getTestCase().getCode());
            tabpane.getTabs().add(tab); 
        }
        else if(item.getParent().getValue().equals("Original files")){
            Test test = findTest(item);
            tab = tsu.createTextTab(item.getValue(), test.getJavaFile().getCode());
            tabpane.getTabs().add(tab); 
        }
        else if(item.getParent().getValue().equals("Results")){
            Test test = findTest(item); 
            tab = tsu.createResultsTab(item.getValue(), test); 
            tabpane.getTabs().add(tab);        
        }
    }
    
    public Test findTest(TreeItem<String> item) {
            String testName = item.getParent().getParent().getValue();
            Test test = null;
            for(Test tests : tests) {
                if (tests.getName().equals(testName)) {
                    test = new Test(tests);
                }
            }        
            return test;
    }
}
