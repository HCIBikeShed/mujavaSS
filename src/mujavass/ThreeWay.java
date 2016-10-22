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
public class ThreeWay {
    private String before;
    private String changed;
    private String after;
    
    public ThreeWay() {
        this.before = "";
        this.changed = "";
        this.after = "";
    }
    
    public void setBefore(String before) {
        this.before = before;
    }
    
    public void setChanged(String changed) {
        this.changed = changed;
    }
    
    public void setAfter(String after) {
        this.after = after;
    }
    
    public String getBefore(){
        return this.before;
    }
    
    public String getChanged() {
        return this.changed;
    }
    
    public String getAfter() {
        return this.after;
    }
}
