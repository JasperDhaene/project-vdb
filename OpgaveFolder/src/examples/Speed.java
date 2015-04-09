package examples;

import fuzzy.Consequence;
import fuzzy.FuzzySystem;
import fuzzy.Rule;
import fuzzy.Utils;
import fuzzy.expression.Premise;
import fuzzy.membership.PIFunction;

/**
 *
 * @author Florian Dejonckheere <florian@floriandejonckheere.be>
 */
public class Speed {

    public static void main(String[] args) {
        FuzzySystem system = new FuzzySystem();

        Premise speedLow = new Premise("speed",
                new PIFunction.TrapezoidPIFunction(0, 0, 40, 70));
        Premise speedMed = new Premise("speed",
                new PIFunction.TrapezoidPIFunction(40, 50, 70, 80));
        Premise speedHigh = new Premise("speed",
                new PIFunction.TrapezoidPIFunction(50, 80, 100, 100));

        Consequence accelLow = new Consequence("acceleration",
                new PIFunction.TrapezoidPIFunction(0, 0, 480, 960), 0, 1600);
        Consequence accelHigh = new Consequence("acceleration",
                new PIFunction.TrapezoidPIFunction(640, 1120, 1600, 1600), 0, 1600);

        for(double d: new double[]{40, 45, 50, 55, 60, 65, 70, 75, 80}) {
            double low = speedLow.membership.value(d);
            double med = speedMed.membership.value(d);
            double high = speedHigh.membership.value(d);
            System.out.println(d + " => {" + Utils.visMem(low) + ", " + Utils.visMem(med) + ", " + Utils.visMem(high) + "} , " + String.format("{%1.3f; %1.3f; %1.3f}", low, med, high));
        }

        // SPEED = low => ACCEL = high
        Rule r1 = new Rule(speedLow, accelHigh);
        // SPEED = med => ACCEL = low
        Rule r2 = new Rule(speedMed, accelLow);
        // SPEED = high => ACCEL = none

        system.addRule(r1);
        system.addRule(r2);

        for (double speed: new double[]{40,45,50,55,60,65,70,75,80}) {
            system.addInput("speed", speed);
            System.out.println(system.evaluate());
        }
    }

}
