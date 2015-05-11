package examples;

import fuzzy.Consequence;
import fuzzy.FuzzySystem;
import fuzzy.Rule;
import fuzzy.expression.Conjunction;
import fuzzy.expression.Disjunction;
import fuzzy.expression.Not;
import fuzzy.expression.Premise;
import fuzzy.membership.PIFunction;
import fuzzy.norm.LukasiewicsNorm;
import fuzzy.norm.ProbabilisticNorm;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author jasper
 */
public class DriftSteering {

    public static void main(String[] args) {
        FuzzySystem system = new FuzzySystem();
        
        Premise speedLow = new Premise("speed",
                new PIFunction.TrapezoidPIFunction(0, 0, 30, 50));
        Premise speedMed = new Premise("speed",
                new PIFunction.TrapezoidPIFunction(40, 60, 80, 100));
        Premise speedHigh = new Premise("speed",
                new PIFunction.TrapezoidPIFunction(90, 120, 150, 170));
        Premise speedNitro = new Premise("speed",
                new PIFunction.TrapezoidPIFunction(160, 210, 230, 280));
        Premise speedInsane = new Premise("speed",
                new PIFunction.TrapezoidPIFunction(270, 290, 310, 400));

        Premise distanceLow = new Premise("frontSensorDistance",
                new PIFunction.TrapezoidPIFunction(0, 0, 30, 40));
        Premise distanceMed = new Premise("frontSensorDistance",
                new PIFunction.TrapezoidPIFunction(40, 50, 80, 90));
        Premise distanceHigh = new Premise("frontSensorDistance",
                new PIFunction.TrapezoidPIFunction(80, 110, 130, 150));
        Premise distanceEndless = new Premise("frontSensorDistance",
                new PIFunction.TrapezoidPIFunction(140, 160, Integer.MAX_VALUE, Integer.MAX_VALUE));
        
        Premise ratioLow = new Premise("leftRightDistanceRatio",
                new PIFunction.TrapezoidPIFunction(
                        -Double.MAX_VALUE,
                        -Double.MAX_VALUE,
                        -50, -15));
        Premise ratioHigh = new Premise("leftRightDistanceRatio",
                new PIFunction.TrapezoidPIFunction(
                        15, 50,
                        Double.MAX_VALUE,
                        Double.MAX_VALUE));
        
        Premise ratioMiddle = new Premise("leftRightDistanceRatio",
                new PIFunction.TrapezoidPIFunction(
                        -20,
                        -15,
                        15, 20));
        
        Premise ratioLowDrift = new Premise("leftRightDistanceRatio",
                new PIFunction.TrapezoidPIFunction(
                        -Double.MAX_VALUE,
                        -Double.MAX_VALUE,
                        -20, -1));
        Premise ratioHighDrift = new Premise("leftRightDistanceRatio",
                new PIFunction.TrapezoidPIFunction(
                        1,
                        20,
                        Double.MAX_VALUE, Double.MAX_VALUE));
        
        Premise notDrifting = new Premise("lateralVelocity",
                new PIFunction.TriangularPIFunction(
                        -0.3, 0,
                        0.3));
        Premise noFrontLeftFriction = new Premise("frontLeftFriction",
                new PIFunction.TrapezoidPIFunction(
                        0,
                        0.1,
                        Double.MAX_VALUE, Double.MAX_VALUE));
        Premise noBackLeftFriction = new Premise("backLeftFriction",
                new PIFunction.TrapezoidPIFunction(
                        0,
                        0.1,
                        Double.MAX_VALUE, Double.MAX_VALUE));
        Premise noFrontRightFriction = new Premise("frontRightFriction",
                new PIFunction.TrapezoidPIFunction(
                        0,
                        0.1,
                        Double.MAX_VALUE, Double.MAX_VALUE));
        Premise noBackRightFriction = new Premise("backRightFriction",
                new PIFunction.TrapezoidPIFunction(
                        0,
                        0.1,
                        Double.MAX_VALUE, Double.MAX_VALUE));
        
        /////////////////////////////////////
        
        Consequence accelLow = new Consequence("acceleration",
                new PIFunction.TrapezoidPIFunction(0, 0, 280, 560), 0, 1600);
        Consequence accelMed = new Consequence("acceleration",
                new PIFunction.TrapezoidPIFunction(480, 660, 840, 1120), 0, 1600);
        Consequence accelHigh = new Consequence("acceleration",
                new PIFunction.TrapezoidPIFunction(840, 1120, 1200, 1400), 0, 1600);
        Consequence accelNitro = new Consequence("acceleration",
                new PIFunction.TrapezoidPIFunction(1200, 1400, 1600, 1600), 0, 1600);
        
        Consequence brakeNone = new Consequence("brake",
                new PIFunction.TrapezoidPIFunction(0, 0, 0, 0), 0, 40);
        Consequence brakeLow = new Consequence("brake",
                new PIFunction.TrapezoidPIFunction(0, 0, 10, 20), 0, 40);
        Consequence brakeMed = new Consequence("brake",
                new PIFunction.TrapezoidPIFunction(15, 20, 30, 35), 0, 40);
        Consequence brakeHigh = new Consequence("brake",
                new PIFunction.TrapezoidPIFunction(30, 35, 40, 40), 0, 40);
        Consequence brakeExtreme = new Consequence("brake",
                new PIFunction.TrapezoidPIFunction(30, 38, 40, 40), 0, 40);
        /*
        Consequence steerLeft = new Consequence("steering",
                new PIFunction.TriangularPIFunction(-1, -1, -0.5), -1, 1);
        Consequence steerRight = new Consequence("steering",
                new PIFunction.TriangularPIFunction(0.5, 1, 1), -1, 1);
        */
        Consequence steerLeft = new Consequence("steering",
                new PIFunction.TrapezoidPIFunction(-1, -1, -0.45, -0.4), -1, 1);
        Consequence steerRight = new Consequence("steering",
                new PIFunction.TrapezoidPIFunction(0.4, 0.45, 1, 1), -1, 1);
        
        Consequence driftLeft = new Consequence("steering",
                new PIFunction.TrapezoidPIFunction(-1, -1, 0, 0.5), -1, 1);
        Consequence driftRight = new Consequence("steering",
                new PIFunction.TrapezoidPIFunction(-0.5, 0, 1, 1), -1, 1);
        
        /* ACCEL */
        // (SPEED = low \/ med \/ high) /\ (DISTANCE = high \/ endless) => ACCEL = nitro
        system.addRule(new Rule(new Conjunction(new Disjunction(new Disjunction(speedLow,speedMed),speedHigh),new Disjunction(distanceHigh,distanceEndless)), accelHigh));
        // (SPEED = nitro /\ DISTANCE = endless) /\ RATIO = middle  => ACCEL = med
        //system.addRule(new Rule(new Conjunction(new Conjunction(speedNitro,distanceEndless),notDrifting), accelLow));
        // DISTANCE = low /\ SPEED = low => ACCEL = low
        system.addRule(new Rule(new Conjunction(new Disjunction(speedLow,speedMed), distanceLow), accelLow));
        
        /* BRAKE */
        // (SPEED = high \/ nitro) /\ (DISTANCE = low \/ med) => BRAKE = med
        system.addRule(new Rule(new Conjunction(new Disjunction(speedHigh,speedNitro),new Disjunction(distanceLow,distanceMed)), brakeHigh));
        // (SPEED = nitro \/ insane) /\ (DISTANCE = low \/ med \/ high) => BRAKE = extreme
        system.addRule(new Rule(new Conjunction(new Disjunction(speedNitro,speedInsane),new Disjunction(new Disjunction(distanceLow,distanceMed),distanceHigh)), brakeExtreme));
        // RATIO = middle /\ (SPEED = nitro \/ insane => BRAKE = extreme
        system.addRule(new Rule(new Conjunction(ratioMiddle,new Disjunction(speedNitro,speedInsane)), brakeExtreme));
        
        /* STEERING */
        // RATIO = low => STEERING = right (high)
        //system.addRule(new Rule(new Conjunction(ratioLeft,new Not(new Disjunction(speedNitro,speedInsane))), steerRight));
        // RATIO = high => STEERING = left (low)
        //system.addRule(new Rule(new Conjunction(ratioRight,new Not(new Disjunction(speedNitro,speedInsane))), steerLeft));
        
        system.addRule(new Rule(new Conjunction(ratioLow,new Not(distanceLow)), steerRight));
        // RATIO = high => STEERING = left (low)
        system.addRule(new Rule(new Conjunction(ratioHigh,new Not(distanceLow)), steerLeft));
        
        /* DRIFTING */
        
//STEER IN TURN      
        // RATIO = left => DRIFT = left
        system.addRule(new Rule(new Conjunction(new Conjunction(ratioLowDrift,distanceLow),new Conjunction(noFrontLeftFriction,noBackLeftFriction)), driftRight));
        // RATIO = right => STEERING = left
        system.addRule(new Rule(new Conjunction(new Conjunction(ratioHighDrift,distanceLow),new Conjunction(noFrontRightFriction,noBackRightFriction)), driftLeft));

//ACCEL WHILE DRIFT        
        // (DISTANCE = low \/ med) /\ (SPEED = high) /\ Drifting => ACCEL = med
        system.addRule(new Rule(new Conjunction(new Conjunction(new Disjunction(distanceLow,distanceMed ),speedHigh),new Not(notDrifting)), accelMed));

//OVERSTEERING       
        // Drifting /\ ratioLeftDrift  => STEERING = right
        //system.addRule(new Rule(new Conjunction(new Conjunction(new Not(notDrifting),ratioLeftDrift),distanceLow), driftRight));
        
        // Drifting /\ ratioLeftDrift  => STEERING = right
        //system.addRule(new Rule(new Conjunction(new Conjunction(new Not(notDrifting),ratioRightDrift),distanceLow), driftLeft));

//BRAKE
        // (DISTANCE = low \/ med) /\ (SPEED = high \/ nitro) => BRAKE = high
        system.addRule(new Rule(new Conjunction(new Disjunction(speedHigh,speedNitro),new Disjunction(distanceLow,distanceMed)), brakeHigh));
        
   
        /**
         * EVALUATION
         */
        
        List<Input> input = new ArrayList<Input>(){{
            add(new Input(new HashMap<String,Double>(){{
                    put("speed",(double) 180);
                    put("frontSensorDistance",(double) 30);
                    put("leftRightDistanceRatio",(double) 7);
                    put("lateralVelocity",(double) 3);
                    put("frontLeftFriction",(double) 0.3);
                    put("backLeftFriction",(double) 0.5);
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
