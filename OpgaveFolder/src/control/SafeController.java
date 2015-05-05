package control;

import car.VehicleProperties;
import fuzzy.Consequence;
import fuzzy.FuzzySystem;
import fuzzy.Rule;
import fuzzy.expression.Conjunction;
import fuzzy.expression.Premise;
import fuzzy.membership.PIFunction;
import java.util.Map;

/**
 * SafeController - Controller that gets the job done as safe as possible
 * @author Florian Dejonckheere <florian@floriandejonckheere.be>
 */
public class SafeController implements Controller {

    private final FuzzySystem system;

    public SafeController() {
        this.system = new FuzzySystem();

        Premise speedLow = new Premise("speed",
                new PIFunction.TrapezoidPIFunction(0, 0, 40, 70));
        Premise speedMed = new Premise("speed",
                new PIFunction.TrapezoidPIFunction(40, 50, 70, 80));
        Premise speedHigh = new Premise("speed",
                new PIFunction.TrapezoidPIFunction(50, 80, 100, 100));

        Premise distanceLow = new Premise("frontSensorDistance",
                new PIFunction.TrapezoidPIFunction(0, 0, 30, 50));
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
    }

    @Override
    public FrameControl getFrameControl(VehicleProperties vp) {
        FrameControl fc;

        double steering = 0,
            acceleration = 0,
            brake = 0;

        system.addInput("speed", vp.getCurrentCarSpeedKph());
        system.addInput("frontSensorDistance", vp.getDistanceFromFrontSensor());
        Map<String, Double> output = system.evaluate();

        /**
         * Steering
         */

        /**
         * Acceleration
         */
        acceleration = output.get("acceleration");

        /**
         * Brake
         */
        
        brake = output.get("brake");

        fc = new FrameControl((float) steering,
                                (float) acceleration,
                                (float) brake,
                                vp.getScanAngle());

        return fc;
    }

}
