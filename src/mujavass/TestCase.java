/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mujavass;

import java.util.ArrayList;

/**
 *
 * @author Luke
 */
public class TestCase {
    private String name;
    private ArrayList<String> testCase;
    
    public TestCase(String name, ArrayList<String> code) {    
        testCase = new ArrayList<>();
        this.name = name;
         for (String text : code) {
            this.testCase.add(text);
        }
    }
    
    public String getName() {
        return this.name;
    }
    
    public ArrayList<String> getCode() {
        return this.testCase;
    }
}
