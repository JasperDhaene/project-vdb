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
public class RallyPremises {
    
    Map<String,Premise> map;
    
    public RallyPremises(){
        fillMap();
    }
    
    private void fillMap(){
        this.map = new HashMap(){{
            put("speedLow",new Premise("speed",
                    new PIFunction.TrapezoidPIFunction(0, 0, 30, 50)));
            put("speedMed",new Premise("speed",
                    new PIFunction.TrapezoidPIFunction(40, 60, 80, 100)));
            put("speedHigh",new Premise("speed",
                    new PIFunction.TrapezoidPIFunction(90, 120, 150, 170)));
            put("speedNitro",new Premise("speed",
                    new PIFunction.TrapezoidPIFunction(160, 210, 230, 280)));
            put("speedInsane",new Premise("speed",
                    new PIFunction.TrapezoidPIFunction(270, 290, 310, 400)));    
            
            put("distanceVeryLow",new Premise("frontSensorDistance",
                    new PIFunction.TrapezoidPIFunction(0, 0, 10, 15)));
            put("distanceLow",new Premise("frontSensorDistance",
                    new PIFunction.TrapezoidPIFunction(0, 0, 30, 45)));
            put("distanceMed",new Premise("frontSensorDistance",
                    new PIFunction.TrapezoidPIFunction(45, 60, 80, 90)));
            put("distanceHigh",new Premise("frontSensorDistance",
                    new PIFunction.TrapezoidPIFunction(80, 110, 130, 150)));
            put("distanceEndless",new Premise("frontSensorDistance",
                    new PIFunction.TrapezoidPIFunction(140, 160, Integer.MAX_VALUE, Integer.MAX_VALUE)));
        
            put("ratioLow",new Premise("leftRightDistanceRatio",
                    new PIFunction.TrapezoidPIFunction(
                        -Double.MAX_VALUE,
                        -Double.MAX_VALUE,
                        -30, -10)));
            put("ratioHigh",new Premise("leftRightDistanceRatio",
                    new PIFunction.TrapezoidPIFunction(
                        10, 30,
                        Double.MAX_VALUE,
                        Double.MAX_VALUE)));
            //Note: ratioLow/HighSpeedy should end whe ratioMiddle does because the consequences are connected.
            put("ratioLowSpeedy",new Premise("leftRightDistanceRatio",
                    new PIFunction.TrapezoidPIFunction(
                        -15,
                        -15,
                        -10, -5)));
            put("ratioHighSpeedy",new Premise("leftRightDistanceRatio",
                    new PIFunction.TrapezoidPIFunction(
                        5, 10,
                        15,
                        15)));
            put("ratioMiddle",new Premise("leftRightDistanceRatio",
                    new PIFunction.TrapezoidPIFunction(
                        -15,
                        -10,
                        10, 15)));
            put("ratioLowDrift",new Premise("leftRightDistanceRatio",
                    new PIFunction.TrapezoidPIFunction(
                        -Double.MAX_VALUE,
                        -Double.MAX_VALUE,
                        -20, -1)));
            put("ratioHighDrift",new Premise("leftRightDistanceRatio",
                    new PIFunction.TrapezoidPIFunction(
                        1,
                        20,
                        Double.MAX_VALUE, Double.MAX_VALUE)));
            put("lateralVelocityLow",new Premise("lateralVelocity",
                    new PIFunction.TriangularPIFunction(
                        -3, 0,
                        3)));
            put("notDrifting",new Premise("lateralVelocity",
                    new PIFunction.TriangularPIFunction(
                        -0.2, 0,
                        0.2)));
            put("noFrontLeftFriction",new Premise("frontLeftFriction",
                    new PIFunction.TrapezoidPIFunction(
                        0,
                        0.1,
                        Double.MAX_VALUE, Double.MAX_VALUE)));
            put("noBackLeftFriction",new Premise("frontLeftFriction",
                    new PIFunction.TrapezoidPIFunction(
                        0,
                        0.1,
                        Double.MAX_VALUE, Double.MAX_VALUE)));
            put("noFrontRightFriction",new Premise("frontLeftFriction",
                    new PIFunction.TrapezoidPIFunction(
                        0,
                        0.1,
                        Double.MAX_VALUE, Double.MAX_VALUE)));
            put("noBackRightFriction",new Premise("frontLeftFriction",
                    new PIFunction.TrapezoidPIFunction(
                        0,
                        0.1,
                        Double.MAX_VALUE, Double.MAX_VALUE)));
            put("frontLeftFriction",new Premise("frontLeftFriction",
                    new PIFunction.TrapezoidPIFunction(
                        0,
                        0,
                        0, 0.1)));
            put("backLeftFriction",new Premise("frontLeftFriction",
                    new PIFunction.TrapezoidPIFunction(
                        0,
                        0,
                        0, 0.1)));
            put("frontRightFriction",new Premise("frontLeftFriction",
                    new PIFunction.TrapezoidPIFunction(
                        0,
                        0,
                        0, 0.1)));
            put("backRightFriction",new Premise("frontLeftFriction",
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
