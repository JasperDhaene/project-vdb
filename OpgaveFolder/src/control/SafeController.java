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

        Premise ratioLow = new Premise("frontDistanceRatio",
                new PIFunction.TrapezoidPIFunction(
                        -Double.MAX_VALUE,
                        -Double.MAX_VALUE,
                        -100, -20));
        Premise ratioHigh = new Premise("frontDistanceRatio",
                new PIFunction.TrapezoidPIFunction(
                        20, 100,
                        Double.MAX_VALUE,
                        Double.MAX_VALUE));
        
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
        
        Consequence steerLeft = new Consequence("steering",
                new PIFunction.TriangularPIFunction(-1, -1, -0.5), -1, 1);
        Consequence steerRight = new Consequence("steering",
                new PIFunction.TriangularPIFunction(0.5, 1, 1), -1, 1);

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
        
        // 4. Strive for a stable left/right ratio
        // RATIO = low => STEERING = right (high)
        system.addRule(new Rule(ratioLow, steerRight));
        // RATIO = high => STEERING = left (low)
        system.addRule(new Rule(ratioHigh, steerLeft));
        
        system.addRule(new Rule(speedBackwards, brakeHigh));
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
                (vp.getDistanceFromLeftSensor() - vp.getDistanceFromRightSensor()));
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
        
        /**
         * Debug output
         */
        
        System.out.println("steering: " + steering);
        System.out.println("acceleration: " + acceleration);
        System.out.println("brake: " + brake);
        System.out.println("#######################");

        fc = new FrameControl((float) steering,
                                (float) acceleration,
                                (float) brake,
                                vp.getScanAngle());

        return fc;
    }

}
