/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package examples;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author jasper
 */
public class Input {
    
    public Map<String,Double> inputs;

    public Input(HashMap<String,Double> inputs) {
        this.inputs = inputs;
    }

    @Override
    public String toString() {
        String output = "{";
        int i=0;
        
        for (String key : inputs.keySet()) {
            output+= key + "=" + inputs.get(key) + "|" ;
        }
        
        output+= "}";
        return output;
    }
    
}
