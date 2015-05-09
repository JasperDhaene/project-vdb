package control;

import car.VehicleProperties;
import fuzzy.Consequence;
import fuzzy.FuzzySystem;
import fuzzy.Rule;
import fuzzy.expression.Conjunction;
import fuzzy.expression.Disjunction;
import fuzzy.expression.Premise;
import fuzzy.membership.PIFunction;
import java.util.Map;

/**
 * SpeedController - Controller that gets the job done as fast as possible
 * @author Florian Dejonckheere <florian@floriandejonckheere.be>
 */
public class SpeedController implements Controller {

    private final FuzzySystem system;

    public SpeedController() {
        this.system = new FuzzySystem();

        Premise speedLow = new Premise("speed",
                new PIFunction.TrapezoidPIFunction(0, 0, 40, 60));
        Premise speedMed = new Premise("speed",
                new PIFunction.TrapezoidPIFunction(50, 70, 90, 110));
        Premise speedHigh = new Premise("speed",
                new PIFunction.TrapezoidPIFunction(100, 130, 170, 190));
        Premise speedNitro = new Premise("speed",
                new PIFunction.TrapezoidPIFunction(180, 210, 400, 400));
        Premise speedBackwards = new Premise("speed",
                new PIFunction.TrapezoidPIFunction(-20, -20, 0, 0));

        Premise distanceLow = new Premise("frontSensorDistance",
                new PIFunction.TrapezoidPIFunction(0, 0, 30, 50));
        Premise distanceMed = new Premise("frontSensorDistance",
                new PIFunction.TrapezoidPIFunction(40, 50, 70, 90));
        Premise distanceHigh = new Premise("frontSensorDistance",
                new PIFunction.TrapezoidPIFunction(80, 100, Integer.MAX_VALUE, Integer.MAX_VALUE));

        Premise ratioLow = new Premise("frontDistanceRatio",
                new PIFunction.TrapezoidPIFunction(
                        -Double.MAX_VALUE,
                        -Double.MAX_VALUE,
                        -50, -10));
        Premise ratioHigh = new Premise("frontDistanceRatio",
                new PIFunction.TrapezoidPIFunction(
                        10, 50,
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
        // SPEED = low \/ med \/ high => ACCEL = nitro
        system.addRule(new Rule(new Conjunction(new Disjunction(new Disjunction(speedLow,speedMed),speedHigh),distanceHigh), accelNitro));
        system.addRule(new Rule(new Conjunction(speedNitro,distanceHigh), accelLow));
        //system.addRule(new Rule(new Conjunction(new Disjunction(speedLow,speedMed),distanceHigh), accelNitro));
        
        // Note: Bochten kunnen veilig genomen worden aan max ~90 
        // 2. If something comes up in front of you, don't accelerate and use your brakes
        // SPEED = High /\ DISTANCE = low => BRAKE = high
        system.addRule(new Rule(new Conjunction(new Disjunction(speedHigh,speedNitro),new Disjunction(distanceLow,distanceMed)), brakeHigh));
        
        // 3. Whatever happens, always have a base speed
        // DISTANCE = low /\ SPEED = low => ACCEL = low
        system.addRule(new Rule(new Conjunction(speedLow, distanceLow), accelLow));
        
        // 4. Strive for a stable left/right ratio
        // RATIO = low => STEERING = right (high)
        system.addRule(new Rule(ratioLow, steerRight));
        // RATIO = high => STEERING = left (low)
        system.addRule(new Rule(ratioHigh, steerLeft));
        
        //system.addRule(new Rule(speedBackwards, brakeHigh));
    }

    @Override
    public FrameControl getFrameControl(VehicleProperties vp) {
        FrameControl fc;
        
        double steering = 0,
            acceleration = 0,
            brake = 0,
            scanAngle = 0;

        system.addInput("speed", vp.getCurrentCarSpeedKph());
        system.addInput("frontSensorDistance", vp.getDistanceFromFrontSensor());
        system.addInput("frontDistanceRatio", 
                (vp.getDistanceFromLeftSensor() - vp.getDistanceFromRightSensor()));
        Map<String, Double> output = system.evaluate();

        /**
         * Steering
         */
        steering = output.get("steering");
        steering -= (steering - vp.getAngleFrontWheels())/2;
        
        /**
         * Acceleration
         */
        acceleration = output.get("acceleration");

        /**
         * Brake
         */
        
        brake = output.get("brake");
        
        /**
         * Scanangle
         */
        scanAngle = 0.9;
        
        /**
         * Debug output
         */
        
        System.out.println("steering: " + steering);
        System.out.println("acceleration: " + acceleration);
        System.out.println("brake: " + brake);
        System.out.println("ratio: " + 
                (vp.getDistanceFromLeftSensor() - vp.getDistanceFromRightSensor()) + 
                " => " + steering);
        System.out.println("#######################");

        fc = new FrameControl((float) steering,
                                (float) acceleration,
                                (float) brake,
                                scanAngle);

        return fc;
    }

}
