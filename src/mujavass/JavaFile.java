/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mujavass;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Luke
 */
public class JavaFile {
    private String name;
    private ArrayList<String> code;
    
    public JavaFile(String name, ArrayList<String> codes) {
        code = new ArrayList<>();
        this.name = name;
         for (String text : codes) {
            this.code.add(text);
        }
    }
    
    public String getName() {
        return this.name;
    }
    
    public ArrayList<String> getCode() {
        return this.code;
    }
}
