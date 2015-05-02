package control;

import car.VehicleProperties;
import fuzzy.Consequence;
import fuzzy.FuzzySystem;
import fuzzy.Rule;
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

        Consequence accelLow = new Consequence("acceleration",
                new PIFunction.TrapezoidPIFunction(0, 0, 480, 960), 0, 1600);
        Consequence accelHigh = new Consequence("acceleration",
                new PIFunction.TrapezoidPIFunction(640, 1120, 1600, 1600), 0, 1600);

        // SPEED = low => ACCEL = high
        Rule rSpeedLow = new Rule(speedLow, accelHigh);
        // SPEED = med => ACCEL = low
        Rule rSpeedMed = new Rule(speedMed, accelLow);

        system.addRule(rSpeedLow);
        system.addRule(rSpeedMed);
    }

    @Override
    public FrameControl getFrameControl(VehicleProperties vp) {
        FrameControl fc;

        double steering = 0,
            acceleration = 0,
            brake = 0;

        system.addInput("speed", vp.getCurrentCarSpeedKph());
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

        fc = new FrameControl((float) steering,
                                (float) acceleration,
                                (float) brake,
                                vp.getScanAngle());

        return fc;
    }

}
