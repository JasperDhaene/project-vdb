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
import fuzzy.membership.PIFunction;
import fuzzy.norm.LukasiewicsNorm;
import fuzzy.norm.ProbabilisticNorm;
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
public class DriftSteering {

    public static void main(String[] args) throws IOException, FileNotFoundException, ParseException {
        FuzzySystem system = new FuzzySystem();
        
       Map<String, Premise> premises = PremiseReader.read("RallyPremises");
        Map<String, Consequence> consequences = ConsequenceReader.read("RallyConsequences");

        Premise speedVeryLow = premises.get("speedVeryLow");
        Premise speedLow = premises.get("speedLow");
        Premise speedMed = premises.get("speedMed");
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
        //Premise ratioMiddle = premises.get("ratioMiddle");
        
        Premise ratioLowDrift = premises.get("ratioLowDrift");
        Premise ratioHighDrift = premises.get("ratioHighDrift");
        
        Premise lateralVelocityLow = premises.get("lateralVelocityLow");
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
        
        Consequence brakeNone = consequences.get("brakeNone");
        Consequence brakeLow = consequences.get("brakeLow");
        Consequence brakeMed = consequences.get("brakeMed");
        Consequence brakeHigh = consequences.get("brakeHigh");
        Consequence brakeExtreme = consequences.get("brakeExtreme");
        Consequence brakeEpic = consequences.get("brakeEpic");

        Consequence steerLeft = consequences.get("steerLeft");
        Consequence steerRight = consequences.get("steerRight");
        
        Consequence steerGentleLeft = consequences.get("steerGentleLeft");
        Consequence steerGentleRight = consequences.get("steerGentleRight");
 
        Consequence driftLeft = consequences.get("driftLeft");
        Consequence driftRight = consequences.get("driftRight");
        
        Expression driftingNoFriction = new Disjunction(new Disjunction(noFrontLeftFriction,noFrontRightFriction),new Disjunction(noBackLeftFriction,noBackRightFriction));
        Expression driftingLateral = new Not(notDriftingLateral);
        Expression drifting = new Conjunction(driftingNoFriction,driftingLateral);
        
/* ACCEL */      
        system.addRule(new Rule(new Conjunction(new LessThanEqual(speedVeryHigh),new GreaterThanEqual(distanceHigh)), accelNitro));
        //Note: zet hier accelMed als je niet met 200+kph tegen de muur wil bokken. Maar je kan daarna wel verder rijden.
        system.addRule(new Rule(new Conjunction(new GreaterThanEqual(speedVeryHigh),new GreaterThanEqual(distanceEndless)), accelHigh));
        //Note: beter speedMed hier, maar dan slipt ij in een van de begin bochten.
        system.addRule(new Rule(new Conjunction(new LessThanEqual(speedLow), new Disjunction(distanceLow,distanceMed)), accelLow));
        
/* BRAKE */
        system.addRule(new Rule(new Conjunction(new GreaterThanEqual(speedVeryHigh),new LessThanEqual(distanceMed)), brakeExtreme));
        system.addRule(new Rule(new Conjunction(speedNitro,new LessThanEqual(distanceVeryHigh)), brakeHigh));
        system.addRule(new Rule(new Conjunction(new GreaterThanEqual(speedVeryLow),distanceVeryLow), brakeEpic));
        
/* STEERING */
        system.addRule(new Rule(new Conjunction(ratioLow,new LessThanEqual(speedHigh)), steerRight));
        system.addRule(new Rule(new Conjunction(ratioHigh,new LessThanEqual(speedHigh)), steerLeft));
/* SPEED STEERING */
        system.addRule(new Rule(new Conjunction(ratioLow,new GreaterThanEqual(speedVeryHigh)), steerGentleRight));
        system.addRule(new Rule(new Conjunction(ratioHigh,new GreaterThanEqual(speedVeryHigh)), steerGentleLeft));
        
//STEER IN TURN      
        // RATIO = low /\ DISTANCE = low /\ FRICTION on front wheels (not drifting out of control) => DRIFT = left
 //       system.addRule(new Rule(new Conjunction(new Conjunction(ratioLowDrift,distanceLow),new Conjunction(new Not(noFrontLeftFriction),new Not(noFrontRightFriction))), driftRight));
        // RATIO = high /\ DISTANCE = low /\ FRICTION on front wheels (not drifting out of control) => DRIFT = right
 //       system.addRule(new Rule(new Conjunction(new Conjunction(ratioHighDrift,distanceLow),new Conjunction(new Not(noFrontLeftFriction),new Not(noFrontRightFriction))), driftLeft));

//ACCEL WHILE DRIFT
        system.addRule(new Rule(new Conjunction(drifting,new GreaterThanEqual(distanceLow)), accelDriftHigh));
        
//OVERSTEERING       
        // noRightWheelfriction /\ STEERING = ratioLow /\ DISTANCE = low  => DRIFT = right
        system.addRule(new Rule(new Conjunction(new Conjunction(drifting,ratioLowDrift),new LessThanEqual(distanceLow)), driftRight));
        system.addRule(new Rule(new Conjunction(new Conjunction(drifting,ratioHighDrift),new LessThanEqual(distanceLow)), driftLeft));

//BRAKE
        system.addRule(new Rule(drifting, brakeLow));

   
        /**
         * EVALUATION
         */
        
        List<Input> input = new ArrayList<Input>(){{
            add(new Input(new HashMap<String,Double>(){{
                    put("speed",(double) 98);
                    put("frontSensorDistance",(double) 23);
                    put("frontDistanceRatio",(double) 5.7);
                    put("lateralVelocity",(double) -1);
                    put("frontLeftFriction",(double) 0.4);
                    put("frontRightFriction",(double) 0.4);
                    put("backLeftFriction",(double) 1);
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
