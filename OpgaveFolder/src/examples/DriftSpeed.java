/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package examples;

import fuzzy.Consequence;
import fuzzy.FuzzySystem;
import fuzzy.Rule;
import fuzzy.expression.Conjunction;
import fuzzy.expression.Disjunction;
import fuzzy.expression.Expression;
import fuzzy.expression.GreaterThanEqual;
import fuzzy.expression.LessThanEqual;
import fuzzy.expression.Not;
import fuzzy.expression.Premise;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.json.simple.parser.ParseException;
import premises.ConsequenceReader;
import premises.PremiseReader;

/**
 *
 * @author jasper
 */
public class DriftSpeed {

    public static void main(String[] args) throws IOException, FileNotFoundException, ParseException {
        
        FuzzySystem system = new FuzzySystem();
        
        Map<String, Premise> premises = PremiseReader.read("RallyPremises");
        Map<String, Consequence> consequences = ConsequenceReader.read("RallyConsequences");
        
        Premise speedBackwards = premises.get("speedBackwards");
        Premise speedVeryLow = premises.get("speedVeryLow");
        Premise speedLow = premises.get("speedLow");
        Premise speedMed = premises.get("speedMed");
        Premise speedDrift = premises.get("speedDrift");
        Premise speedHigh = premises.get("speedHigh");
        Premise speedVeryHigh = premises.get("speedHigh");
        Premise speedNitro = premises.get("speedNitro");

        Premise distanceVeryLow = premises.get("distanceVeryLow");
        Premise distanceLow = premises.get("distanceLow");
        Premise distanceMed = premises.get("distanceMed");
        Premise distanceHigh = premises.get("distanceHigh");
        Premise distanceVeryHigh = premises.get("distanceVeryHigh");
        Premise distanceEndless = premises.get("distanceEndless");
        
        Premise ratioLow = premises.get("ratioLow");
        Premise ratioHigh = premises.get("ratioHigh");
        Premise ratioLowSpeedy = premises.get("ratioLowSpeedy");
        Premise ratioHighSpeedy = premises.get("ratioHighSpeedy");
        
        Premise ratioLowDrift = premises.get("ratioLowDrift");
        Premise ratioHighDrift = premises.get("ratioHighDrift");
        
        Premise ratioLowBeforeDrift = premises.get("ratioLowBeforeDrift");
        Premise ratioHighBeforeDrift = premises.get("ratioHighBeforeDrift");
        
        Premise lateralVelocityLow = premises.get("lateralVelocityLow");
        Premise notLateralVelocityHigh = premises.get("notLateralVelocityHigh");
        Premise notDriftingLateral = premises.get("notDriftingLateral");
        
        Premise noFrontLeftFriction = premises.get("noFrontLeftFriction");
        Premise noBackLeftFriction = premises.get("noBackLeftFriction");
        Premise noFrontRightFriction = premises.get("noFrontRightFriction");
        Premise noBackRightFriction = premises.get("noBackRightFriction");
        
        /////////////////////////////////////
        
        Consequence accelBase = consequences.get("accelBase");
        Consequence accelLow = consequences.get("accelLow");
        Consequence accelMed = consequences.get("accelMed");
        Consequence accelHigh = consequences.get("accelHigh");
        Consequence accelNitro = consequences.get("accelNitro");
        Consequence accelDriftHigh = consequences.get("accelDriftHigh");
        Consequence accelDriftVeryHigh = consequences.get("accelDriftVeryHigh");
        
        Consequence brakeNone = consequences.get("brakeNone");
        Consequence brakeLow = consequences.get("brakeLow");
        Consequence brakeMed = consequences.get("brakeMed");
        Consequence brakeHigh = consequences.get("brakeHigh");
        Consequence brakeExtreme = consequences.get("brakeExtreme");
        Consequence brakeEpic = consequences.get("brakeEpic");
        Consequence brakeDrift = consequences.get("brakeDrift");

        Consequence steerLeft = consequences.get("steerLeft");
        Consequence steerRight = consequences.get("steerRight");
        
        Consequence steerGentleLeft = consequences.get("steerGentleLeft");
        Consequence steerGentleRight = consequences.get("steerGentleRight");
 
        Consequence driftLeft = consequences.get("driftLeft");
        Consequence driftRight = consequences.get("driftRight");
        
        Consequence steerIntoDriftLeft = consequences.get("steerIntoDriftLeft");
        Consequence steerIntoDriftRight = consequences.get("steerIntoDriftRight");
        
        Expression driftingNoFriction = new Disjunction(new Disjunction(noFrontLeftFriction,noFrontRightFriction),new Disjunction(noBackLeftFriction,noBackRightFriction));
        Expression driftingLateral = new Not(notDriftingLateral);
        Expression drifting = new Conjunction(driftingNoFriction,driftingLateral);

/* ACCEL */      
        system.addRule(new Rule(speedBackwards, accelBase));

        /**
         * EVALUATION
         */
        
        List<Input> input = new ArrayList<Input>(){{
            add(new Input(new HashMap<String,Double>(){{
                    put("speed",(double) 0);
                    put("frontDistance",(double) 20);
                    put("leftRightRatio",(double) 56);
                    put("lateralVelocity",(double) 4.5);
                    
                    
            }}));
            //START POSITION
            add(new Input(new HashMap<String,Double>(){{
                    put("speed",(double) -2);
                    put("frontDistance",(double) 256);
                    put("leftRightRatio",(double) 10);
                    put("lateralVelocity",(double) 0.5);
                    
                    
            }}));
            
            
            
        }};
        
        for(Input i: input) {
            for(String s: i.inputs.keySet()){
                system.addInput(s, i.inputs.get(s));
            }
            System.out.println(i + " => " + system.evaluate());
            
        }
    }
}
