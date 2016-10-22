/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mujavass;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Luke
 */
public class LoadSave {
    
    /*
        Load the save state from previous run. Takes in a path to the
        file which contains a list of names. These names represent the
        previous tests which are still in the directory.
    */
    public List<String> loadSave(String path) throws FileNotFoundException, IOException {
        FileReader load = new FileReader(path);
        BufferedReader br = new BufferedReader(load);
        String str;
        
        List<String> list = new ArrayList<>();
        while((str = br.readLine()) != null) {
            list.add(str);
        }
        
        return list;
    }
    
    
    /*
        loadTest() will load and create a test object from a specified name.
        Creates a testCase, JavaFile and result objects and inserts all
        into a test object which is passed back to the caller.
        
        VendingMachine
            |
            ->TestCase
                |
                -> testVendingMachine
            ->JavaFile
                |
                -> VendingMachine    
            ->Result
                |
                -> VendingMachineResult
                -> Mutants
                    |
                    -> 1
                    -> 2
                    -> ...
                    -> n
        
        VendingMachine is the name of the test.
        Each test containts 3 directories:
            1. TestCase which holds the JUnit test harness for the test (test test)
            2. JavaFile which holds the original java file for the test
            3. Result which holds the score for the test and the Mutated java files
                in a directory called Mutants
    */
    public Test loadTest(String name) throws FileNotFoundException, IOException {
        Test test;

        TestCase testCase = loadTestCase(name);
        JavaFile javaFile = loadJavaFile(name);
        Result result = loadResult(name);
        loadMutants(result, name, javaFile);
        
        test = new Test(name, testCase, javaFile, result);
        
        return test;        
    }
    
    public TestCase loadTestCase(String name) throws FileNotFoundException, IOException {
        String path = "tests/" + name + "/TestCase";
        File folder = new File(path);
        
        File[] listOfFiles = folder.listFiles();
        ArrayList<String> content = new ArrayList<>();
        String testName = "";
        
        for (int i=0; i<listOfFiles.length; i++) {
            File file = listOfFiles[i];
            if(file.isFile() && file.getName().endsWith(".java")) {
                content = processLine(file);
                testName = file.getName();
            }
        }
        
        TestCase testCase = new TestCase(getName(testName), content);
        
        return testCase;
    }
    
    /*
        So much repeated code, clean this up.
    */
    public JavaFile loadJavaFile(String name) throws IOException {
        String path = "tests/" + name + "/JavaFile";
        File folder = new File(path);
        
        File[] listOfFiles = folder.listFiles();
        ArrayList<String> content = new ArrayList<>();
        String testName = "";
        
        
        for (int i=0; i<listOfFiles.length; i++) {
            File file = listOfFiles[i];
            if(file.isFile() && file.getName().endsWith(".java")) {
                content = processLine(file);
                testName = file.getName();
            }
        }
        
        JavaFile javaFile = new JavaFile(getName(testName), content);
        
        return javaFile;       
    }
    
    
    public ArrayList<String> processLine(File file) throws FileNotFoundException, IOException {
        ArrayList<String> content = new ArrayList<>();
        int count = 1;
        BufferedReader br = new BufferedReader(new FileReader(file));
        for(String line; (line = br.readLine()) != null; ) {
            //remove comments when reading
            if(!line.startsWith("//")) {
                content.add(count + " " + line);
                count++;
            }
        }
        return content;
    }
    
    public Result loadResult(String name) throws FileNotFoundException, IOException {
        String path = "tests/" + name + "/Result";
        File folder = new File(path);
        
        File[] listOfFiles = folder.listFiles();
        String resultName = "";
        String score = "";
        String live = "";
        String killed = "";
        String total = "";
        
        for (int i=0; i<listOfFiles.length; i++) {
            File file = listOfFiles[i];
            if(file.isFile() && file.getName().endsWith(".txt")) {
                resultName = file.getName();
                
                BufferedReader br = new BufferedReader(new FileReader(file));
                score = br.readLine();
                live = br.readLine();
                killed = br.readLine();
                total = br.readLine();                
            }
        }
        
        Result result = new Result(getName(resultName), score, live, killed, total);
        
        return result;        
    }
            
    public Result loadMutants(Result result, String name, JavaFile javafile) throws FileNotFoundException, IOException {
        String path = "tests/" + name + "/Result/Mutants";
        File folder = new File(path);

        File[] listOfFiles = folder.listFiles();
                
        for (int i=0; i<listOfFiles.length; i++) {
            File file = listOfFiles[i];
            if(file.isFile() && file.getName().endsWith(".txt")) {
                String mutantName = file.getName();
                Mutant mutant = processMutant(file, javafile);
                result.addMutant(getName(mutantName), mutant);
            }
        }
        
        return result;        
    }
    
    public Mutant processMutant(File file, JavaFile javafile) throws FileNotFoundException, IOException {
            BufferedReader br = new BufferedReader(new FileReader(file));
            
            int line = Integer.parseInt(br.readLine());
            
            String before = "";
            int j;
            for (j=1; j<line-1; j++) {
                before += "\n"+ j + br.readLine();
            }
            
            String changed = "\n" + j + br.readLine() + "\n";
            String after = "";
            String outOfNames;
            String oChanged = "\n" + javafile.getCode().get(j-1) + "\n";
            
            while((outOfNames = br.readLine()) != null) {
                j++;
                after += j + outOfNames + "\n";
            }
            br.close();
            
            Mutant mutant = new Mutant(file.getName());
            mutant.setChanged(before, changed, after);
            

            mutant.setOriginal(before, oChanged, after);
            
            
            return mutant;
    }
    
    public String getName(String name) {
        //turns out the dot has a special meaning. Needed \\ to be able to split it properly
        String[] tokens = name.split("\\.");
        return tokens[0];
    }
    
}
