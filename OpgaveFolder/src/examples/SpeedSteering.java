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
import fuzzy.membership.PIFunction;
import java.util.ArrayList;
import java.util.List;
import premises.RallyPremises;

/**
 *
 * @author jasper
 */
public class SpeedSteering {
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

        
        /* STEERING */        
        system.addRule(new Rule(new Conjunction(ratioLow,new Disjunction(new Not(speedNitro),new Not(speedInsane))), steerRight));
        // RATIO = high => STEERING = left (low)
        system.addRule(new Rule(new Conjunction(ratioHigh,new Disjunction(new Not(speedNitro),new Not(speedInsane))), steerLeft));
        
        system.addRule(new Rule(new Conjunction(ratioLowSpeedy,new Disjunction(speedNitro,speedInsane)), steerGentleRight));
        // RATIO = high => STEERING = left (low)
        system.addRule(new Rule(new Conjunction(ratioHighSpeedy,new Disjunction(speedNitro,speedInsane)), steerGentleLeft));
            
        
        /**
         * EVALUATION
         */
        
        List<Pair> input = new ArrayList<Pair>(){{
            
            add(new Pair(170, 0));
            add(new Pair(170, 5));
            add(new Pair(170, 10));
            add(new Pair(170, 15));
            add(new Pair(170, 20));
            add(new Pair(170, 25));
            add(new Pair(170, -0));
            add(new Pair(170, -5));
            add(new Pair(170, -10));
            add(new Pair(170, -15));
            add(new Pair(170, -20));
            add(new Pair(170, -25));
 
            
        }};
        
        for(Pair p: input) {
            system.addInput("speed", p.left);
            system.addInput("leftRightDistanceRatio", p.right);
            System.out.println(p + " => " + system.evaluate());
        }
    }
}
