package control;

import fuzzy.Consequence;
import fuzzy.Rule;
import fuzzy.expression.Conjunction;
import fuzzy.expression.Premise;
import java.io.FileNotFoundException;
import java.io.IOException;
import org.json.simple.parser.ParseException;

/**
 * SafeController - Controller that gets the job done as safe as possible
 * @author Florian Dejonckheere <florian@floriandejonckheere.be>
 */
public class SafeController extends BaseController {

    public SafeController()
            throws IOException, FileNotFoundException, ParseException {
        this(false);
    }

    public SafeController(boolean debug)
            throws IOException, FileNotFoundException, ParseException {
        super(debug);

        /**
         * Shorthand notation for premises and consequences. DO NOT try to read or
         * interpret the following two lines. Instead, read the JSON file and
         * assume all premises and consequences are available
         * using their *name* as *variable* in the code below.
         *
         * */
        Premise speedLow = p("speedLow"), speedMed = p("speedMed"), speedHigh = p("speedHigh"), speedBackwards = p("speedBackwards"), distanceLow = p("distanceLow"), distanceHigh = p("distanceHigh"), ratioLow = p("ratioLow"), ratioHigh = p("ratioHigh");
        Consequence accelLow = c("accelLow"), accelMed = c("accelMed"), accelHigh = c("accelHigh"), accelNone = c("accelNone"), brakeNone = c("brakeNone"), brakeMed = c("brakeMed"), brakeHigh = c("brakeHigh"), steerLeft = c("steerLeft"), steerRight = c("steerRight");

        /**
         * Acceleration. Accelerate if nothing's in front of you, but mind your speed
         *
         */
        // SPEED = low AND DISTANCE = high => ACCEL = high
        system.addRule(new Rule(new Conjunction(speedLow, distanceHigh), accelHigh));
        // SPEED = med AND DISTANCE = high => ACCEL = low
        system.addRule(new Rule(new Conjunction(speedMed, distanceHigh), accelMed));
        // SPEED = high AND DISTANCE = high => ACCEL = none
        system.addRule(new Rule(new Conjunction(speedHigh, distanceHigh), accelLow));

        // Whatever happens, always have a base speed
        // SPEED = low AND DISTANCE = low => ACCEL = low
        system.addRule(new Rule(new Conjunction(speedLow, distanceLow), accelLow));

        /**
         * Braking. If something comes up in front of you, don't accelerate and use your brakes.
         *
         */
        // DISTANCE = low => BRAKE = high
        system.addRule(new Rule(distanceLow, brakeHigh));
        // DISTANCE = low => ACCEL = none
        system.addRule(new Rule(distanceLow, accelNone));
        // Don't go backwards
        system.addRule(new Rule(speedBackwards, brakeHigh));

        /**
         * Steering. Strive for a stable left/right ratio
         *
         */
        // RATIO = low => STEERING = right (high)
        system.addRule(new Rule(ratioLow, steerRight));
        // RATIO = high => STEERING = left (low)
        system.addRule(new Rule(ratioHigh, steerLeft));

    }

}
