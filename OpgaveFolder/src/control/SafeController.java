package control;

import car.VehicleProperties;
import fuzzy.Consequence;
import fuzzy.FuzzySystem;
import fuzzy.Rule;
import fuzzy.expression.Conjunction;
import fuzzy.expression.Premise;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;
import org.json.simple.parser.ParseException;
import premises.ConsequenceReader;
import premises.PremiseReader;

/**
 * SafeController - Controller that gets the job done as safe as possible
 * @author Florian Dejonckheere <florian@floriandejonckheere.be>
 */
public class SafeController implements Controller {

    private final FuzzySystem system;

    public SafeController()
            throws IOException, FileNotFoundException, ParseException {
        this.system = new FuzzySystem();

        Map<String, Premise> premises = PremiseReader.read("SafePremises");
        Map<String, Consequence> consequences = ConsequenceReader.read("SafeConsequences");

        // 1. Accelerate if nothing's in front of you, but mind your speed
        // SPEED = low AND distance = high => ACCEL = high
        system.addRule(new Rule(new Conjunction(premises.get("speedLow"), premises.get("distanceHigh")),
                consequences.get("accelHigh")));
        // SPEED = med AND distance = high => ACCEL = low
        system.addRule(new Rule(new Conjunction(premises.get("speedMed"), premises.get("distanceHigh")),
                consequences.get("accelMed")));
        // SPEED = high AND distance = high => ACCEL = none
        system.addRule(new Rule(new Conjunction(premises.get("speedHigh"), premises.get("distanceHigh")),
                consequences.get("accelLow")));

        // 2. If something comes up in front of you, don't accelerate and use your brakes
        // DISTANCE = low => BRAKE = high
        system.addRule(new Rule(premises.get("distanceLow"),
                consequences.get("brakeHigh")));
        // DISTANCE = low => ACCEL = none
        system.addRule(new Rule(premises.get("distanceLow"),
                consequences.get("accelNone")));

        // 3. Whatever happens, always have a base speed
        // DISTANCE = low AND SPEED = low => ACCEL = low
        system.addRule(new Rule(new Conjunction(premises.get("speedLow"), premises.get("distanceLow")),
                consequences.get("accelLow")));

        // 4. Strive for a stable left/right ratio
        // RATIO = low => STEERING = right (high)
        system.addRule(new Rule(premises.get("ratioLow"),
                consequences.get("steerRight")));
        // RATIO = high => STEERING = left (low)
        system.addRule(new Rule(premises.get("ratioHigh"),
                consequences.get("steerLeft")));

        // 5. Don't go backwards
        system.addRule(new Rule(premises.get("speedBackwards"),
                consequences.get("brakeHigh")));
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
                Ratio.calc(vp.getDistanceFromLeftSensor(), vp.getDistanceFromRightSensor()));
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
                Ratio.calc(vp.getDistanceFromLeftSensor(), vp.getDistanceFromRightSensor()) +
                " => " + steering);
        System.out.println("#######################");

        fc = new FrameControl((float) steering,
                                (float) acceleration,
                                (float) brake,
                                scanAngle);

        return fc;
    }

}
