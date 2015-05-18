package control;

import fuzzy.Consequence;
import fuzzy.Rule;
import fuzzy.expression.Conjunction;
import fuzzy.expression.Disjunction;
import fuzzy.expression.Expression;
import fuzzy.expression.GreaterThanEqual;
import fuzzy.expression.LessThanEqual;
import fuzzy.expression.Not;
import fuzzy.expression.Premise;
import java.io.FileNotFoundException;
import java.io.IOException;
import org.json.simple.parser.ParseException;

/**
 * SpeedController - Controller that gets the job done as fast as possible
 * @author Florian Dejonckheere <florian@floriandejonckheere.be>
 */
public class SpeedController extends BaseController {

    public SpeedController()
            throws IOException, FileNotFoundException, ParseException {
        this(false);
    }

    public SpeedController(boolean debug)
            throws IOException, FileNotFoundException, ParseException {
        super(debug);

        /**
         * Shorthand notation for premises and consequences. DO NOT try to read or
         * interpret the following two lines. Instead, read the JSON file and
         * assume all premises and consequences are available
         * using their *name* as *variable* in the code below.
         *
         * */
        Premise speedLow = p("speedLow"), speedHigh = p("speedHigh"), speedVeryHigh = p("speedHigh"), speedNitro = p("speedNitro"), distanceVeryLow = p("distanceVeryLow"), distanceLow = p("distanceLow"), distanceMed = p("distanceMed"), distanceHigh = p("distanceHigh"), distanceVeryHigh = p("distanceVeryHigh"), distanceEndless = p("distanceEndless"),  ratioLow = p("ratioLow"), ratioHigh = p("ratioHigh"), ratioLowDrift = p("ratioLowDrift"), ratioHighDrift = p("ratioHighDrift"), notDriftingLateral = p("notDriftingLateral"),  noFrontLeftFriction = p("noFrontLeftFriction"), noBackLeftFriction = p("noBackLeftFriction"), noFrontRightFriction = p("noFrontRightFriction"), noBackRightFriction = p("noBackRightFriction");
        Consequence accelLow = c("accelLow"), accelHigh = c("accelHigh"), accelNitro = c("accelNitro"), accelDriftHigh = c("accelDriftHigh"), brakeLow = c("brakeLow"), brakeHigh = c("brakeHigh"), brakeExtreme = c("brakeExtreme"), brakeEpic = c("brakeEpic"), steerLeft = c("steerLeft"), steerRight = c("steerRight"), steerGentleLeft = c("steerGentleLeft"), steerGentleRight = c("steerGentleRight"), driftLeft = c("driftLeft"), driftRight = c("driftRight");

        /**
         * Extra premises
         *
         */
        Expression driftingNoFriction = new Disjunction(new Disjunction(noFrontLeftFriction, noFrontRightFriction),new Disjunction(noBackLeftFriction,noBackRightFriction));
        Expression driftingLateral = new Not(notDriftingLateral);
        Expression drifting = new Conjunction(driftingNoFriction, driftingLateral);


        /**
         * Acceleration.
         *
         */
        // SPEED <= very high AND DISTANCE >= high => ACCEL = nitro (extremely high)
        system.addRule(new Rule(new Conjunction(new LessThanEqual(speedVeryHigh), new GreaterThanEqual(distanceHigh)),
                accelNitro));
        // SPEED >= very high AND DISTANCE >= extremely high => ACCEL = high
        system.addRule(new Rule(new Conjunction(new GreaterThanEqual(speedVeryHigh), new GreaterThanEqual(distanceEndless)),
                accelHigh));
        // SPEED <= low AND (DISTANCE = low OR DISTANCE = med) => ACCEL = low
        system.addRule(new Rule(new Conjunction(new LessThanEqual(speedLow), new Disjunction(distanceLow, distanceMed)),
                accelLow));

        /**
         * Braking.
         *
         */
        // SPEED >= very high AND DISTANCE <= med => BRAKE = extremely high
        system.addRule(new Rule(new Conjunction(new GreaterThanEqual(speedVeryHigh), new LessThanEqual(distanceMed)),
                brakeExtreme));
        // SPEED = nitro (extremely fast) AND DISTANCE <= very high => BRAKE = high
        system.addRule(new Rule(new Conjunction(speedNitro, new LessThanEqual(distanceVeryHigh)),
                brakeHigh));
        // SPEED >= low AND DISTANCE = very low => BRAKE = epically high
        system.addRule(new Rule(new Conjunction(new GreaterThanEqual(speedLow), distanceVeryLow),
                brakeEpic));

        /**
         * Steering.
         *
         */
        // RATIO = low AND SPEED <= high => STEERING = high (right)
        system.addRule(new Rule(new Conjunction(ratioLow, new LessThanEqual(speedHigh)),
                steerRight));
        // RATIO = low AND SPEED <= high => STEERING = negative high (left)
        system.addRule(new Rule(new Conjunction(ratioHigh, new LessThanEqual(speedHigh)),
                steerLeft));

        /**
         * High-speed steering.
         *
         */
        // RATIO = low AND SPEED >= very high => STEERING = low (right)
        system.addRule(new Rule(new Conjunction(ratioLow, new GreaterThanEqual(speedVeryHigh)),
                steerGentleRight));
        // RATIO = high AND SPEED >= very high => STEERING = negative low (left)
        system.addRule(new Rule(new Conjunction(ratioHigh, new GreaterThanEqual(speedVeryHigh)),
                steerGentleLeft));

        /**
         * Drifting.
         * 
         */
        // DRIFTING => ACCEL = high (for drifting)
        system.addRule(new Rule(drifting, accelDriftHigh));
        // DRIFTING => BRAKE = low
        system.addRule(new Rule(drifting, brakeLow));

        /**
         * Countersteering.
         * 
         */
        // drifting AND STEERING = ratioLowDrift AND DISTANCE <= med => DRIFT = right
        system.addRule(new Rule(new Conjunction(new Conjunction(drifting, ratioLowDrift), new LessThanEqual(distanceMed)),
                driftRight));
        // drifting AND STEERING = ratioHighDrift AND DISTANCE <= med => DRIFT = left
        system.addRule(new Rule(new Conjunction(new Conjunction(drifting, ratioHighDrift), new LessThanEqual(distanceMed)),
                driftLeft));

    }

}
