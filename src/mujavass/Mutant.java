/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mujavass;

/**
 *
 * @author Luke
 */
public class Mutant {
    private String name;
    private ThreeWay textOriginal;
    private ThreeWay textChanged;
    
    public Mutant(String name) {
        this.name = name;
        textChanged = new ThreeWay();
        textOriginal = new ThreeWay();
    }
    
    public String getName() {
        return this.name;
    }
    
    public void setOriginal(String b, String c, String a) {
        textOriginal.setBefore(b);
        textOriginal.setChanged(c);
        textOriginal.setAfter(a);
    }
    
    public void setChanged(String b, String c, String a) {
        textChanged.setBefore(b);
        textChanged.setChanged(c);
        textChanged.setAfter(a);
    }
    
    public String[] getOriginal() {
        String[] original = new String[3];
        
        original[0] = textOriginal.getBefore();
        original[1] = textOriginal.getChanged();
        original[2] = textOriginal.getAfter();
        
        return original;
    }
    
    public String[] getChanged() {
        String[] changed = new String[3];
        
        changed[0] = textChanged.getBefore();
        changed[1] = textChanged.getChanged();
        changed[2] = textChanged.getAfter();
        
        return changed;
    }  
}
