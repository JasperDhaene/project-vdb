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

        Premise distanceLow = new Premise("distanceFront",
                new PIFunction.TrapezoidPIFunction(0, 0, 30, 50));
        Premise distanceHigh = new Premise("distanceFront",
                new PIFunction.TrapezoidPIFunction(40, 80, Integer.MAX_VALUE, Integer.MAX_VALUE));

        /**
         * Actuators
         */
        Consequence accelLow = new Consequence("acceleration",
                new PIFunction.TrapezoidPIFunction(0, 0, 480, 960), 0, 1600);
        Consequence accelHigh = new Consequence("acceleration",
                new PIFunction.TrapezoidPIFunction(640, 1120, 1600, 1600), 0, 1600);
        
        Consequence brakeNone = new Consequence("brake",
                new PIFunction.TrapezoidPIFunction(0, 0, 0, 0), 0, 40);
        Consequence brakeHigh = new Consequence("brake",
                new PIFunction.TrapezoidPIFunction(20, 30, 40, 40), 0, 40);
//
//        for(double d: new double[]{40, 45, 50, 55, 60, 65, 70, 75, 80}) {
//            double low = speedLow.membership.value(d);
//            double med = speedMed.membership.value(d);
//            double high = speedHigh.membership.value(d);
//            System.out.println(d + " => {" + Utils.visMem(low) + ", " + Utils.visMem(med) + ", " + Utils.visMem(high) + "} , " + String.format("{%1.3f; %1.3f; %1.3f}", low, med, high));
//        }

        // SPEED = low => ACCEL = high
        Rule r1 = new Rule(new Conjunction(speedLow, distanceHigh), accelHigh);
        // SPEED = med => ACCEL = low
        Rule r2 = new Rule(speedMed, accelLow);
        // DISTANCE = low => BRAKE = high
        Rule r3 = new Rule(distanceLow, brakeHigh);

        system.addRule(r1);
        system.addRule(r2);
        system.addRule(r3);
        
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
            add(new Pair(50, 1));
        }};
        
        for(Pair p: input) {
            system.addInput("speed", p.speed);
            system.addInput("distanceFront", p.distanceFront);
            System.out.println(p + " => " + system.evaluate());
        }
    }

}
