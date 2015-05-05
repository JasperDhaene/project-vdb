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
                new PIFunction.TrapezoidPIFunction(0, 0, 40, 70));
        Premise speedMed = new Premise("speed",
                new PIFunction.TrapezoidPIFunction(40, 50, 70, 80));
        Premise speedHigh = new Premise("speed",
                new PIFunction.TrapezoidPIFunction(50, 80, 100, 100));

        Premise distanceLow = new Premise("frontSensorDistance",
                new PIFunction.TrapezoidPIFunction(0, 0, 50, 70));
        Premise distanceHigh = new Premise("frontSensorDistance",
                new PIFunction.TrapezoidPIFunction(80, 100, Integer.MAX_VALUE, Integer.MAX_VALUE));

        /**
         * Actuators
         */
        Consequence accelLow = new Consequence("acceleration",
                new PIFunction.TrapezoidPIFunction(0, 0, 480, 960), 0, 1600);
        Consequence accelHigh = new Consequence("acceleration",
                new PIFunction.TrapezoidPIFunction(640, 1120, 1600, 1600), 0, 1600);
        Consequence accelNone = new Consequence("acceleration",
                new PIFunction.TrapezoidPIFunction(0, 0, 0, 0), 0, 1600);
        
        Consequence brakeNone = new Consequence("brake",
                new PIFunction.TrapezoidPIFunction(0, 0, 0, 0), 0, 40);
        Consequence brakeMed = new Consequence("brake",
                new PIFunction.TrapezoidPIFunction(15, 17, 23, 25), 0, 40);
        Consequence brakeHigh = new Consequence("brake",
                new PIFunction.TrapezoidPIFunction(30, 40, 40, 40), 0, 40);

        // SPEED = low => ACCEL = high
        Rule r1 = new Rule(new Conjunction(speedLow, distanceHigh), accelHigh);
        // SPEED = med => ACCEL = low
        Rule r2 = new Rule(new Conjunction(speedMed, distanceHigh), accelLow);
        // DISTANCE = low => BRAKE = high
        Rule r3 = new Rule(distanceLow, brakeHigh);
        // DISTANCE = low => BRAKE = high
        Rule r4 = new Rule(distanceLow, accelNone);

        system.addRule(r1);
        system.addRule(r2);
        system.addRule(r3);
        system.addRule(r4);
        
        
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
        }};
        
        for(Pair p: input) {
            system.addInput("speed", p.speed);
            system.addInput("distanceFront", p.distanceFront);
            System.out.println(p + " => " + system.evaluate());
        }
    }

}
