/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package consequences;

import fuzzy.Consequence;
import fuzzy.membership.PIFunction;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author jasper
 */
public class SpeedConsequences {
    Map<String,Consequence> map;
    
    public SpeedConsequences(){
        fillMap();
    }
    
    private void fillMap(){
        this.map = new HashMap(){{
        
        put("accelBase",new Consequence("acceleration",
                new PIFunction.TrapezoidPIFunction(480, 660, 840, 1120), 0, 1600));   
        put("accelLow",new Consequence("acceleration",
                new PIFunction.TrapezoidPIFunction(0, 0, 300, 600), 0, 1600));
        put("accelMed",new Consequence("acceleration",
                new PIFunction.TrapezoidPIFunction(500, 600, 800, 1100), 0, 1600));
        put("accelHigh",new Consequence("acceleration",
                new PIFunction.TrapezoidPIFunction(800, 1100, 1300, 1400), 0, 1600));
        put("accelNitro",new Consequence("acceleration",
                new PIFunction.TrapezoidPIFunction(1300, 1400, 1600, 1600), 0, 1600));
        put("accelNone",new Consequence("acceleration",
                new PIFunction.TrapezoidPIFunction(0, 0, 0, 0), 0, 1600));

            
        put("brakeNone",new Consequence("brake",
                new PIFunction.TrapezoidPIFunction(0, 0, 0, 0), 0, 40));
        put("brakeLow",new Consequence("brake",
                new PIFunction.TrapezoidPIFunction(0, 0, 10, 20), 0, 40));
        put("brakeMed",new Consequence("brake",
                new PIFunction.TrapezoidPIFunction(15, 20, 30, 35), 0, 40));
        put("brakeHigh",new Consequence("brake",
                new PIFunction.TrapezoidPIFunction(30, 35, 40, 40), 0, 40));
        put("brakeExtreme",new Consequence("brake",
                new PIFunction.TrapezoidPIFunction(30, 38, 40, 40), 0, 40));

        put("steerLeft",new Consequence("steering",
                new PIFunction.TriangularPIFunction(-1, -1, -0.5), -1, 1));
        put("steerRight",new Consequence("steering",
                new PIFunction.TriangularPIFunction(0.5, 1, 1), -1, 1));
        put("steerGentleLeft",new Consequence("steering",
                new PIFunction.TriangularPIFunction(-0.4, -0.05, 0), -1, 1));
        put("steerGentleRight",new Consequence("steering",
                new PIFunction.TriangularPIFunction(0, 0.05, 0.4), -1, 1));

        put("driftLeft",new Consequence("steering",
                new PIFunction.TriangularPIFunction(-1, -1, -0.5), -1, 1));
        put("driftRight",new Consequence("steering",
                new PIFunction.TriangularPIFunction(0.5, 1, 1), -1, 1));
           
        }};
    }
    
    public Consequence get(String key){
        return map.get(key);
    }
}
