/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package premises;

import fuzzy.expression.Premise;
import fuzzy.membership.PIFunction;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author jasper
 */
public class SpeedPremises {      
    Map<String,Premise> map;
    
    public SpeedPremises(){
        fillMap();
    }
    
    private void fillMap(){
        this.map = new HashMap(){{
            


            put("speedLow",new Premise("speed",
                    new PIFunction.TrapezoidPIFunction(0, 0, 60, 90)));
            put("speedMed",new Premise("speed",
                    new PIFunction.TrapezoidPIFunction(80, 90, 100, 110)));
            put("speedHigh",new Premise("speed",
                    new PIFunction.TrapezoidPIFunction(100, 150, 190, 210)));
            put("speedVeryHigh",new Premise("speed",
                    new PIFunction.TrapezoidPIFunction(200, 210, 250, 260)));
            put("speedNitro",new Premise("speed",
                    new PIFunction.TrapezoidPIFunction(250, 260, Integer.MAX_VALUE, Integer.MAX_VALUE)));
            //put("speedInsane",new Premise("speed",
            //        new PIFunction.TrapezoidPIFunction(270, 290, 310, 400)));
            

            

            put("distanceLow",new Premise("frontSensorDistance",
                    new PIFunction.TrapezoidPIFunction(0, 0, 35, 50)));
            put("distanceMed",new Premise("frontSensorDistance",
                    new PIFunction.TrapezoidPIFunction(45, 75, 100, 120)));
            put("distanceHigh",new Premise("frontSensorDistance",
                    new PIFunction.TrapezoidPIFunction(110, 150, 180, 200)));
            put("distanceEndless",new Premise("frontSensorDistance",
                    new PIFunction.TrapezoidPIFunction(190, 210, Integer.MAX_VALUE, Integer.MAX_VALUE)));
        
            
            put("ratioLow",new Premise("leftRightDistanceRatio",
                new PIFunction.TrapezoidPIFunction(
                        0,
                        0,
                        0.20, 0.35)));
            put("ratioHigh",new Premise("leftRightDistanceRatio",
                new PIFunction.TrapezoidPIFunction(
                        0.65, 0.8,
                        1,
                        1)));
            
            put("ratioMiddle",new Premise("leftRightDistanceRatio",
                new PIFunction.TrapezoidPIFunction(
                        0.35,
                        0.40,
                        0.60, 0.65)));
            
            //Note: ratioLow/HighSpeedy should end whe ratioMiddle does because the consequences are connected.
            put("ratioLowSpeedy",new Premise("leftRightDistanceRatio",
                    new PIFunction.TrapezoidPIFunction(
                        0.20,
                        0.40,
                        0.40, 0.45)));
            put("ratioHighSpeedy",new Premise("leftRightDistanceRatio",
                    new PIFunction.TrapezoidPIFunction(
                        0.55,
                        0.6,
                        0.6,0.8)));
            
            put("ratioLowDrift",new Premise("leftRightDistanceRatio",
                    new PIFunction.TrapezoidPIFunction(
                        0,
                        0,
                        0.35, 0.40)));
            put("ratioHighDrift",new Premise("leftRightDistanceRatio",
                    new PIFunction.TrapezoidPIFunction(
                        0.6,
                        0.65,
                        1,1)));
            
            put("lateralVelocityLow",new Premise("lateralVelocity",
                    new PIFunction.TriangularPIFunction(
                        -3, 0,
                        3)));
            put("notDrifting",new Premise("lateralVelocity",
                    new PIFunction.TriangularPIFunction(
                        -0.2, 0,
                        0.2)));
            put("frontLeftFriction",new Premise("frontLeftFriction",
                    new PIFunction.TrapezoidPIFunction(
                        0,
                        0.1,
                        Double.MAX_VALUE, Double.MAX_VALUE)));
            put("backLeftFriction",new Premise("frontLeftFriction",
                    new PIFunction.TrapezoidPIFunction(
                        0,
                        0.1,
                        Double.MAX_VALUE, Double.MAX_VALUE)));
            put("frontRightFriction",new Premise("frontLeftFriction",
                    new PIFunction.TrapezoidPIFunction(
                        0,
                        0.1,
                        Double.MAX_VALUE, Double.MAX_VALUE)));
            put("backRightFriction",new Premise("frontLeftFriction",
                    new PIFunction.TrapezoidPIFunction(
                        0,
                        0.1,
                        Double.MAX_VALUE, Double.MAX_VALUE)));
            put("noFrontLeftFriction",new Premise("frontLeftFriction",
                    new PIFunction.TrapezoidPIFunction(
                        0,
                        0,
                        0, 0.1)));
            put("noBackLeftFriction",new Premise("frontLeftFriction",
                    new PIFunction.TrapezoidPIFunction(
                        0,
                        0,
                        0, 0.1)));
            put("noFrontRightFriction",new Premise("frontLeftFriction",
                    new PIFunction.TrapezoidPIFunction(
                        0,
                        0,
                        0, 0.1)));
            put("noBackRightFriction",new Premise("frontLeftFriction",
                    new PIFunction.TrapezoidPIFunction(
                        0,
                        0,
                        0, 0.1)));
        }};
    }
    
    public Premise get(String key){
        return map.get(key);
    } 
}
