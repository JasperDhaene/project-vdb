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

        Premise ratioLow = new Premise("frontDistanceRatio",
                new PIFunction.TrapezoidPIFunction(
                        -Double.MAX_VALUE,
                        -Double.MAX_VALUE,
                        -100, -40));
        Premise ratioHigh = new Premise("frontDistanceRatio",
                new PIFunction.TrapezoidPIFunction(
                        40, 100,
                        Double.MAX_VALUE,
                        Double.MAX_VALUE));
        
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
        
        Consequence steerLeft = new Consequence("steering",
                new PIFunction.TriangularPIFunction(-1, -1, -0.5), -1, 1);
        Consequence steerRight = new Consequence("steering",
                new PIFunction.TriangularPIFunction(0.5, 1, 1), -1, 1);

        // SPEED = low => ACCEL = high
        system.addRule(new Rule(new Conjunction(speedLow, distanceHigh), accelHigh));
        // SPEED = med => ACCEL = low
        system.addRule(new Rule(new Conjunction(speedMed, distanceHigh), accelLow));
        // DISTANCE = low => BRAKE = high
        system.addRule(new Rule(distanceLow, brakeHigh));
        // DISTANCE = low => BRAKE = high
        system.addRule(new Rule(distanceLow, accelNone));
        // RATIO = low => STEERING = right (high)
        system.addRule(new Rule(ratioLow, steerRight));
        // RATIO = high => STEERING = left (low)
        system.addRule(new Rule(ratioHigh, steerLeft));
    }

    @Override
    public FrameControl getFrameControl(VehicleProperties vp) {
        FrameControl fc;

        double steering = 0,
            acceleration = 0,
            brake = 0;

        system.addInput("speed", vp.getCurrentCarSpeedKph());
        system.addInput("frontSensorDistance", vp.getDistanceFromFrontSensor());
        system.addInput("frontDistanceRatio", 
                (vp.getDistanceFromFrontSensor() - vp.getDistanceFromRightSensor()));
        Map<String, Double> output = system.evaluate();

        /**
         * Steering
         */
        steering = output.get("steering");
        
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
