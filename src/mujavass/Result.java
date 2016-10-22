/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mujavass;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Luke
 */
public class Result {
    private String name;
    private String score;
    private String live;
    private String killed;
    private String total;
    private Map<String, Mutant> mutants;
    
    public Result(String name, String score, String live, String killed, String total) {
        this.name = name;
        this.score = score;
        this.live = live;
        this.killed = killed;
        this.total = total;
        mutants = new HashMap<String,Mutant>();
    }
    
    public String getScore() {
        return this.score;
    }
    
    public String getLive() {
        return this.live;
    }
    
    public String getKilled() {
        return this.killed;
    }
    
    public Map<String, Mutant> getMutants() {
        return this.mutants;
    }
    
    public String getTotal() {
        return this.total;
    }
    
    public String getName() {
        return this.name;
    }
    
    public void addMutant(String name, Mutant mutant) {
        mutants.put(name, mutant);
    }
    
    public Mutant getMutant (String key) {
        return mutants.get(key);
    }
    
    public String[] getOriginal(String key) {
        return mutants.get(key).getOriginal();
    }
    
    public String[] getChanged(String key) {
        return mutants.get(key).getChanged();
    }
}
