/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package examples;

import consequences.RallyConsequences;
import fuzzy.Consequence;
import fuzzy.FuzzySystem;
import fuzzy.Rule;
import fuzzy.expression.Conjunction;
import fuzzy.expression.Disjunction;
import fuzzy.expression.Not;
import fuzzy.expression.Premise;
import fuzzy.norm.ZadehNorm;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import premises.RallyPremises;

/**
 *
 * @author jasper
 */
public class SpeedRallySteering {
    
    public static void main(String[] args) {
        FuzzySystem system = new FuzzySystem();
        
        RallyPremises premises = new RallyPremises();
        RallyConsequences consequences = new RallyConsequences();
        
        Premise speedLow = premises.get("speedLow");
        Premise speedMed = premises.get("speedMed");
        Premise speedHigh = premises.get("speedHigh");
        Premise speedNitro = premises.get("speedNitro");
        Premise speedInsane = premises.get("speedInsane");
        
        Premise distanceVeryLow = premises.get("distanceVeryLow");
        Premise distanceLow = premises.get("distanceLow");
        Premise distanceMed = premises.get("distanceMed");
        Premise distanceHigh = premises.get("distanceHigh");
        Premise distanceEndless = premises.get("distanceEndless");
        
        Premise ratioLow = premises.get("ratioLow");
        Premise ratioHigh = premises.get("ratioHigh");
        Premise ratioLowSpeedy = premises.get("ratioLowSpeedy");
        Premise ratioHighSpeedy = premises.get("ratioHighSpeedy");  
        Premise ratioMiddle = premises.get("ratioMiddle");
        
        Premise ratioLowDrift = premises.get("ratioLowDrift");
        Premise ratioHighDrift = premises.get("ratioHighDrift");
        
        Premise lateralVelocityLow = premises.get("lateralVelocityLow");
        Premise notDrifting = premises.get("notDrifting");
        
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
        Consequence accelDriftLow = consequences.get("accelDriftLow");
        Consequence accelDriftHigh = consequences.get("accelDriftHigh");
        
        Consequence brakeNone = consequences.get("brakeNone");
        Consequence brakeLow = consequences.get("brakeLow");
        Consequence brakeMed = consequences.get("brakeMed");
        Consequence brakeHigh = consequences.get("brakeHigh");
        Consequence brakeExtreme = consequences.get("brakeExtreme");

        Consequence steerLeft = consequences.get("steerLeft");
        Consequence steerRight = consequences.get("steerRight");
        Consequence steerGentleLeft = consequences.get("steerGentleLeft");
        Consequence steerGentleRight = consequences.get("steerGentleRight");
 
        Consequence driftLeft = consequences.get("driftLeft");
        Consequence driftRight = consequences.get("driftRight");

 /* ACCEL */
        // (SPEED = low \/ med \/ high) /\ (DISTANCE = high \/ endless) => ACCEL = nitro
        //system.addRule(new Rule(new Conjunction(new Disjunction(new Disjunction(speedLow,speedMed),speedHigh),new Disjunction(distanceHigh,distanceEndless)), accelNitro));
        system.addRule(new Rule(distanceEndless, accelNitro)); //TODO: if not turning?
        system.addRule(new Rule(new Conjunction(distanceHigh,new Disjunction(new Not(speedNitro),new Not(speedInsane))), accelHigh));
        
        // DISTANCE = low /\ SPEED = low => ACCEL = low
        system.addRule(new Rule(new Conjunction(distanceLow,distanceMed), accelBase));
        
/* BRAKE */
        // SPEED = (nitro \/ insane) /\ (DISTANCE = high \/ med) => BRAKE = extreme
        system.addRule(new Rule(new Conjunction(new Disjunction(speedNitro,speedInsane),new Disjunction(distanceHigh,distanceMed)), brakeExtreme));
        // SPEED = high  /\ (DISTANCE = low \/ med) => BRAKE = med
        //system.addRule(new Rule(new Conjunction(speedHigh,new Disjunction(distanceHigh,distanceMed)), brakeHigh));
        
        // RATIO = middle /\ (SPEED = nitro \/ insane => BRAKE = extreme
        //system.addRule(new Rule(new Conjunction(new Not(ratioMiddle),new Disjunction(speedNitro,speedInsane)), brakeExtreme));
        
/* STEERING */        
        system.addRule(new Rule(new Conjunction(ratioLow,new Disjunction(new Not(speedNitro),new Not(speedInsane))), steerRight));
        // RATIO = high => STEERING = left (low)
        system.addRule(new Rule(new Conjunction(ratioHigh,new Disjunction(new Not(speedNitro),new Not(speedInsane))), steerLeft));
        
        system.addRule(new Rule(new Conjunction(ratioLowSpeedy,new Disjunction(speedNitro,speedInsane)), steerGentleRight));
        // RATIO = high => STEERING = left (low)
        system.addRule(new Rule(new Conjunction(ratioHighSpeedy,new Disjunction(speedNitro,speedInsane)), steerGentleLeft));
        
        /* DRIFTING */
        
//STEER IN TURN      
        // RATIO = low /\ DISTANCE = low /\ FRICTION on front wheels (not drifting out of control) => DRIFT = left
        system.addRule(new Rule(new Conjunction(new Conjunction(ratioLowDrift,distanceLow),new Conjunction(new Not(noFrontLeftFriction),new Not(noFrontRightFriction))), driftRight));
        // RATIO = high /\ DISTANCE = low /\ FRICTION on front wheels (not drifting out of control) => DRIFT = right
        system.addRule(new Rule(new Conjunction(new Conjunction(ratioHighDrift,distanceLow),new Conjunction(new Not(noFrontLeftFriction),new Not(noFrontRightFriction))), driftLeft));

//ACCEL WHILE DRIFT        
        // (DISTANCE = low \/ med) /\ (SPEED = high) /\ Drifting => ACCEL = med
        system.addRule(new Rule(new Conjunction(new Conjunction(distanceLow ,new Not(notDrifting)),new Disjunction(new Not(speedNitro),new Not(speedInsane))), accelDriftLow));
        // In horizontal drift, avoid crash by nitro accel to gain grip and turn vertical 
        system.addRule(new Rule(new Conjunction(distanceVeryLow,new Not(notDrifting)), accelDriftHigh));
//OVERSTEERING       
        // noRightWheelfriction /\ STEERING = ratioLowDrift /\ DISTANCE = low  => DRIFT = right
        system.addRule(new Rule(new Conjunction(new Conjunction(new Conjunction(noFrontRightFriction,noBackRightFriction),ratioLowDrift),distanceLow), driftRight));
        
        // noLeftWheelfriction /\ STEERING = ratioHighDrift /\ DISTANCE = low  => DRIFT = left
        system.addRule(new Rule(new Conjunction(new Conjunction(new Conjunction(noFrontLeftFriction,noBackLeftFriction),ratioHighDrift),distanceLow), driftLeft));

//BRAKE
        // (DISTANCE = low \/ med) /\ (SPEED = high \/ nitro) => BRAKE = high
        system.addRule(new Rule(new Conjunction(new Conjunction(new Disjunction(speedHigh,speedNitro),new Disjunction(distanceLow,distanceMed)),lateralVelocityLow), brakeHigh));
        // (DISTANCE = low \/ med) /\ (SPEED = high \/ nitro) => BRAKE = high
        system.addRule(new Rule(new Conjunction(new Conjunction(new Disjunction(speedHigh,speedNitro),new Disjunction(distanceLow,distanceMed)),new Not(lateralVelocityLow)), brakeNone));

     
        
        /**
         * EVALUATION
         */
        
        List<Input> input = new ArrayList<Input>(){{
            add(new Input(new HashMap<String,Double>(){{
                    put("speed",(double) 210);
                    put("frontSensorDistance",(double) 190);
                    put("leftRightDistanceRatio",(double) 5);
                    put("lateralVelocity",(double) 0);
                    put("frontLeftFriction",(double) 1);
                    put("backLeftFriction",(double) 1);
                    put("frontRightFriction",(double) 1);
                    put("backRightFriction",(double) 1);
                    
            }}));
            add(new Input(new HashMap<String,Double>(){{
                    put("speed",(double) 210);
                    put("frontSensorDistance",(double) 190);
                    put("leftRightDistanceRatio",(double) 10);
                    put("lateralVelocity",(double) 0);
                    put("frontLeftFriction",(double) 1);
                    put("backLeftFriction",(double) 1);
                    put("frontRightFriction",(double) 1);
                    put("backRightFriction",(double) 1);
                    
            }}));
            add(new Input(new HashMap<String,Double>(){{
                    put("speed",(double) 210);
                    put("frontSensorDistance",(double) 190);
                    put("leftRightDistanceRatio",(double) 20);
                    put("lateralVelocity",(double) 0);
                    put("frontLeftFriction",(double) 1);
                    put("backLeftFriction",(double) 1);
                    put("frontRightFriction",(double) 1);
                    put("backRightFriction",(double) 1);
                    
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
