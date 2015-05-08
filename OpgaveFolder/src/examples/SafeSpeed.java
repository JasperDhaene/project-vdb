package examples;

import fuzzy.Consequence;
import fuzzy.FuzzySystem;
import fuzzy.Rule;
import fuzzy.expression.Conjunction;
import fuzzy.expression.Premise;
import fuzzy.membership.PIFunction;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Florian Dejonckheere <florian@floriandejonckheere.be>
 */
public class SafeSpeed {

    public static void main(String[] args) {
        FuzzySystem system = new FuzzySystem();

        /**
         * Sensors
         */
        Premise speedLow = new Premise("speed",
                new PIFunction.TrapezoidPIFunction(0, 0, 40, 60));
        Premise speedMed = new Premise("speed",
                new PIFunction.TrapezoidPIFunction(50, 70, 80, 90));
        Premise speedHigh = new Premise("speed",
                new PIFunction.TrapezoidPIFunction(80, 90, 100, 110));
                Premise speedBackwards = new Premise("speed",
                new PIFunction.TrapezoidPIFunction(-20, -20, 0, 0));

        Premise distanceLow = new Premise("frontSensorDistance",
                new PIFunction.TrapezoidPIFunction(0, 0, 40, 60));
        Premise distanceHigh = new Premise("frontSensorDistance",
                new PIFunction.TrapezoidPIFunction(50, 70, Integer.MAX_VALUE, Integer.MAX_VALUE));

        
        /**
         * Actuators
         */
        Consequence accelLow = new Consequence("acceleration",
                new PIFunction.TrapezoidPIFunction(0, 0, 280, 560), 0, 1600);
        Consequence accelMed = new Consequence("acceleration",
                new PIFunction.TrapezoidPIFunction(480, 660, 840, 1120), 0, 1600);
        Consequence accelHigh = new Consequence("acceleration",
                new PIFunction.TrapezoidPIFunction(840, 1120, 1600, 1600), 0, 1600);
        Consequence accelNone = new Consequence("acceleration",
                new PIFunction.TrapezoidPIFunction(0, 0, 0, 0), 0, 1600);
        
        Consequence brakeNone = new Consequence("brake",
                new PIFunction.TrapezoidPIFunction(0, 0, 0, 0), 0, 40);
        Consequence brakeMed = new Consequence("brake",
                new PIFunction.TrapezoidPIFunction(15, 17, 23, 25), 0, 40);
        Consequence brakeHigh = new Consequence("brake",
                new PIFunction.TrapezoidPIFunction(30, 40, 40, 40), 0, 40);
        


        // 1. Accelerate if nothing's in front of you, but mind your speed
        // SPEED = low => ACCEL = high
        system.addRule(new Rule(new Conjunction(speedLow, distanceHigh), accelHigh));
        // SPEED = med => ACCEL = low
        system.addRule(new Rule(new Conjunction(speedMed, distanceHigh), accelMed));
        // SPEED = med => ACCEL = none
        system.addRule(new Rule(new Conjunction(speedHigh, distanceHigh), accelLow));
        
        // 2. If something comes up in front of you, don't accelerate and use your brakes
        // SPEED = High /\ DISTANCE = low => BRAKE = high
        system.addRule(new Rule(new Conjunction(speedMed, distanceLow), brakeHigh));
        // DISTANCE = low => ACCEL = none
        system.addRule(new Rule(new Conjunction(speedMed, distanceLow), accelNone));
        
        // 3. Whatever happens, always have a base speed
        // DISTANCE = low /\ SPEED = low => ACCEL = low
        system.addRule(new Rule(new Conjunction(speedLow, distanceLow), accelLow));
        
        system.addRule(new Rule(speedBackwards, brakeHigh));
        
        
        /**
         * EVALUATION
         */
        
        List<Pair> input = new ArrayList<Pair>(){{
            add(new Pair(0, Integer.MAX_VALUE));
            add(new Pair(40, Integer.MAX_VALUE));
            add(new Pair(50, Integer.MAX_VALUE));
            add(new Pair(60, Integer.MAX_VALUE));
            add(new Pair(65, Integer.MAX_VALUE));
            add(new Pair(70, Integer.MAX_VALUE));
            add(new Pair(75, Integer.MAX_VALUE));
            add(new Pair(80, Integer.MAX_VALUE));
            
            add(new Pair(0, 20));
            add(new Pair(40, 10));
            add(new Pair(40, 5));
            add(new Pair(45, 20));
            add(new Pair(100, 1));
            
            add(new Pair(80, 40));
            //Backwards
            add(new Pair(-5, 10));
        }};
        
        for(Pair p: input) {
            system.addInput("speed", p.left);
            system.addInput("frontSensorDistance", p.right);
            System.out.println(p + " => " + system.evaluate());
        }
    }

}
