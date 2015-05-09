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
import fuzzy.expression.Premise;
import fuzzy.membership.PIFunction;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author jasper
 */
public class SpeedSpeed {

    public static void main(String[] args) {
        FuzzySystem system = new FuzzySystem();
        Premise speedLow = new Premise("speed",
                new PIFunction.TrapezoidPIFunction(0, 0, 40, 60));
        Premise speedMed = new Premise("speed",
                new PIFunction.TrapezoidPIFunction(50, 70, 80, 100));
        Premise speedHigh = new Premise("speed",
                new PIFunction.TrapezoidPIFunction(90, 110, 150, 170));
        Premise speedNitro = new Premise("speed",
                new PIFunction.TrapezoidPIFunction(160, 200, 400, 400));
        Premise speedBackwards = new Premise("speed",
                new PIFunction.TrapezoidPIFunction(-20, -20, 0, 0));

        Premise distanceLow = new Premise("frontSensorDistance",
                new PIFunction.TrapezoidPIFunction(0, 0, 30, 50));
        Premise distanceMed = new Premise("frontSensorDistance",
                new PIFunction.TrapezoidPIFunction(40, 60, 80, 100));
        Premise distanceHigh = new Premise("frontSensorDistance",
                new PIFunction.TrapezoidPIFunction(90, 110, Integer.MAX_VALUE, Integer.MAX_VALUE));



        /**
         * Actuators
         */
        Consequence accelLow = new Consequence("acceleration",
                new PIFunction.TrapezoidPIFunction(0, 0, 280, 560), 0, 1600);
        Consequence accelMed = new Consequence("acceleration",
                new PIFunction.TrapezoidPIFunction(480, 660, 840, 1120), 0, 1600);
        Consequence accelHigh = new Consequence("acceleration",
                new PIFunction.TrapezoidPIFunction(840, 1120, 1200, 1400), 0, 1600);
        Consequence accelNitro = new Consequence("acceleration",
                new PIFunction.TrapezoidPIFunction(1200, 1400, 1600, 1600), 0, 1600);
        Consequence accelNone = new Consequence("acceleration",
                new PIFunction.TrapezoidPIFunction(0, 0, 0, 0), 0, 1600);

        Consequence brakeNone = new Consequence("brake",
                new PIFunction.TrapezoidPIFunction(0, 0, 0, 0), 0, 40);
        Consequence brakeLow = new Consequence("brake",
                new PIFunction.TrapezoidPIFunction(0, 0, 10, 20), 0, 40);
        Consequence brakeMed = new Consequence("brake",
                new PIFunction.TrapezoidPIFunction(15, 20, 30, 35), 0, 40);
        Consequence brakeHigh = new Consequence("brake",
                new PIFunction.TrapezoidPIFunction(30, 35, 40, 40), 0, 40);

        Consequence steerLeft = new Consequence("steering",
                new PIFunction.TriangularPIFunction(-1, -1, -0.5), -1, 1);
        Consequence steerRight = new Consequence("steering",
                new PIFunction.TriangularPIFunction(0.5, 1, 1), -1, 1);

        // 1. Accelerate if nothing's in front of you, but mind your speed
        // SPEED = low => ACCEL = high
        /*system.addRule(new Rule(new Conjunction(speedLow, distanceHigh), accelHigh));
         // SPEED = med => ACCEL = low
         system.addRule(new Rule(new Conjunction(speedMed, distanceHigh), accelMed));
         // SPEED = high => ACCEL = none
         system.addRule(new Rule(new Conjunction(speedHigh, distanceHigh), accelLow));*/
        // 1. Accelerate if nothing's in front of you, but mind your speed
        // SPEED = low => ACCEL = high
                system.addRule(new Rule(new Conjunction(new Disjunction(new Disjunction(speedLow,speedMed),speedHigh),distanceHigh), accelNitro));

        // 2. If something comes up in front of you, don't accelerate and use your brakes
        // SPEED = High /\ DISTANCE = low => BRAKE = high
        //system.addRule(new Rule(new Conjunction(new Disjunction(speedMed,speedHigh), new Disjunction(distanceLow,distanceMed)), brakeHigh));
        system.addRule(new Rule(new Conjunction(new Disjunction(speedHigh,speedNitro),distanceMed), brakeMed));
        // DISTANCE = low => ACCEL = none
        //system.addRule(new Rule(new Conjunction(speedMed, distanceLow), accelNone));

        // 3. Whatever happens, always have a base speed
        // DISTANCE = low /\ SPEED = low => ACCEL = low
        //system.addRule(new Rule(new Conjunction(speedLow, distanceLow), accelLow));


        //system.addRule(new Rule(speedBackwards, brakeHigh));
        
        
        /**
         * EVALUATION
         */
        
        List<Pair> input = new ArrayList<Pair>(){{
            //Nitro accel
            add(new Pair(0, Integer.MAX_VALUE));
            add(new Pair(40, Integer.MAX_VALUE));
            add(new Pair(50, Integer.MAX_VALUE));
            add(new Pair(60, Integer.MAX_VALUE));
            add(new Pair(65, Integer.MAX_VALUE));
            add(new Pair(70, Integer.MAX_VALUE));
            add(new Pair(75, Integer.MAX_VALUE));
            add(new Pair(80, Integer.MAX_VALUE));
            add(new Pair(90, Integer.MAX_VALUE));
            add(new Pair(100, Integer.MAX_VALUE));
            add(new Pair(110, Integer.MAX_VALUE));
            add(new Pair(120, Integer.MAX_VALUE));
            add(new Pair(130, Integer.MAX_VALUE));
            add(new Pair(140, Integer.MAX_VALUE));
            add(new Pair(150, Integer.MAX_VALUE));
            add(new Pair(160, Integer.MAX_VALUE));
            add(new Pair(170, Integer.MAX_VALUE));
            add(new Pair(180, Integer.MAX_VALUE));
            
            //brake if wall close
            add(new Pair(170, 100));
            add(new Pair(170, 90));
            add(new Pair(200, 80));
            add(new Pair(100, 70));
            add(new Pair(100, 60));
            add(new Pair(100, 50));
            add(new Pair(100, 40));
            add(new Pair(170, 30));
            add(new Pair(170, 20));
            add(new Pair(150, 10));
            
        }};
        
        for(Pair p: input) {
            system.addInput("speed", p.left);
            system.addInput("frontSensorDistance", p.right);
            System.out.println(p + " => " + system.evaluate());
        }
    }
}
