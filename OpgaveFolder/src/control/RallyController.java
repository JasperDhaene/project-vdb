package control;

import car.VehicleProperties;
import fuzzy.Consequence;
import fuzzy.FuzzySystem;
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
import java.util.Map;
import org.json.simple.parser.ParseException;
import premises.ConsequenceReader;
import premises.PremiseReader;

/**
 * RallyController - Controller that gets the job done as awesome as possible
 * @author Florian Dejonckheere <florian@floriandejonckheere.be>
 */
public class RallyController extends BaseController {

    public RallyController()
            throws IOException, FileNotFoundException, ParseException {
        this(false);
    }

    public RallyController(boolean debug)
            throws IOException, FileNotFoundException, ParseException {
        super(debug);

        /**
         * Shorthand notation for premises and consequences. DO NOT try to read or
         * interpret the following two lines. Instead, read the JSON file and
         * assume all premises and consequences are available
         * using their *name* as *variable* in the code below.
         *
         * */
        Premise speedBackwards = p("speedBackwards"), speedVeryLow = p("speedVeryLow"), speedLow = p("speedLow"), speedDrift = p("speedDrift"), speedHigh = p("speedHigh"), speedVeryHigh = p("speedHigh"), speedNitro = p("speedNitro"), distanceVeryLow = p("distanceVeryLow"), distanceLow = p("distanceLow"), distanceMed = p("distanceMed"), distanceDrift = p("distanceDrift"), distanceHigh = p("distanceHigh"), distanceStop = p("distanceStop"), distanceEndless = p("distanceEndless"), ratioLow = p("ratioLow"), ratioHigh = p("ratioHigh"), lateralVelocityLeft = p("lateralVelocityLeft"), lateralVelocityRight = p("lateralVelocityRight"), notLateralVelocityHigh = p("notLateralVelocityHigh"), notDriftingLateral = p("notDriftingLateral"), noFrontLeftFriction = p("noFrontLeftFriction"), noBackLeftFriction = p("noBackLeftFriction"), noFrontRightFriction = p("noFrontRightFriction"), noBackRightFriction = p("noBackRightFriction");
        Consequence accelBase = c("accelBase"), accelLow = c("accelLow"), accelHigh = c("accelHigh"), accelNitro = c("accelNitro"), accelDriftHigh = c("accelDriftHigh"), accelDriftVeryHigh = c("accelDriftVeryHigh"), brakeLow = c("brakeLow"), brakeHigh = c("brakeHigh"), brakeExtreme = c("brakeExtreme"), brakeEpic = c("brakeEpic"), brakeDrift = c("brakeDrift"), steerLeft = c("steerLeft"), steerRight = c("steerRight"), steerGentleLeft = c("steerGentleLeft"), steerGentleRight = c("steerGentleRight"), driftLeft = c("driftLeft"), driftRight = c("driftRight");

        /**
         * Extra premises
         *
         */
        Expression driftingNoFriction = new Disjunction(new Disjunction(noFrontLeftFriction, noFrontRightFriction), new Disjunction(noBackLeftFriction, noBackRightFriction));
        Expression driftingLateral = new Not(notDriftingLateral);
        Expression drifting = new Conjunction(driftingNoFriction, driftingLateral);

        /**
         * Acceleration.
         *
         */
        // SPEED <= very high AND DISTANCE >= high => ACCEL = nitro (extremely high)
        system.addRule(new Rule(new Conjunction(new LessThanEqual(speedVeryHigh), new GreaterThanEqual(distanceHigh)),
                accelNitro));
        // SPEED <= nitro (extremely high) AND DISTANCE >= endless (extremely high) => ACCEL = high
        system.addRule(new Rule(new Conjunction(new LessThanEqual(speedNitro), new GreaterThanEqual(distanceEndless)),
                accelHigh));
        // SPEED <= drift AND (DISTANCE = low OR DISTANCE = med) => ACCEL = low
        system.addRule(new Rule(new Conjunction(new LessThanEqual(speedDrift), new Disjunction(distanceLow,distanceMed)),
                accelLow));
        // SPEED = backwards OR SPEED = very low => ACCEL = base
        system.addRule(new Rule(new Disjunction(speedBackwards, speedVeryLow),
                accelBase));

        /**
         * Braking.
         *
         */
        // SPEED >= high AND DISTANCE <= med => BRAKE = extremely high
        system.addRule(new Rule(new Conjunction(new GreaterThanEqual(speedHigh), new LessThanEqual(distanceMed)),
                brakeExtreme));
        // SPEED >= very high AND DISTANCE <= stop (stop distance for high speeds) => BRAKE = epically high
        system.addRule(new Rule(new Conjunction(new GreaterThanEqual(speedVeryHigh), new LessThanEqual(distanceStop)),
                brakeEpic));
        // SPEED >= low AND DISTANCE = very low => BRAKE = high
        system.addRule(new Rule(new Conjunction(new GreaterThanEqual(speedLow), distanceVeryLow),
                brakeHigh));

        // DRIFTING => BRAKE = low
        system.addRule(new Rule(drifting, brakeLow));
        // LATERALVELOCITY = high AND SPEED <= drift => BRAKE = drift
        system.addRule(new Rule(new Conjunction(new Not(notLateralVelocityHigh), new LessThanEqual(speedDrift)),
                brakeDrift));

        /**
         * Steering.
         *
         */
        // RATIO = low (right) AND SPEED <= high AND NOT DRIFTING => STEERING = high (right)
        system.addRule(new Rule(new Conjunction(new Conjunction(ratioLow, new LessThanEqual(speedHigh)), new Not(drifting)),
                steerRight));
        // RATIO = high (left) AND SPEED <= high AND NOT DRIFTING => STEERING = negative high (left)
        system.addRule(new Rule(new Conjunction(new Conjunction(ratioHigh, new LessThanEqual(speedHigh)), new Not(drifting)),
                steerLeft));

        /**
         * High-speed steering.
         *
         */
        // RATIO = low (right) AND SPEED <= very high => STEERING = low (right)
        system.addRule(new Rule(new Conjunction(ratioLow,new GreaterThanEqual(speedVeryHigh)),
                steerGentleRight));
        // RATIO = high (left) AND SPEED <= very high => STEERING = negative low (left)
        system.addRule(new Rule(new Conjunction(ratioHigh,new GreaterThanEqual(speedVeryHigh)),
                steerGentleLeft));

        /**
         * Drifting.
         *
         */
        // DRIFTING => ACCEL = low (right)
        system.addRule(new Rule(drifting, accelDriftHigh));
        // LATERALVELOCITY = high AND SPEED <= drift => ACCEL = very high (for drifting)
        system.addRule(new Rule(new Conjunction(new Not(notLateralVelocityHigh), new LessThanEqual(speedDrift)),
                accelDriftVeryHigh));

        /**
         * Countersteering.
         *
         * */
        // drifting AND LATERALVELOCITY = positive (right) AND DISTANCE <= drift  => DRIFT = right
        system.addRule(new Rule(new Conjunction(new Conjunction(drifting, lateralVelocityRight), new LessThanEqual(distanceDrift)),
                driftRight));
        // drifting AND LATERALVELOCITY = negative (left) AND DISTANCE <= drift  => DRIFT = left
        system.addRule(new Rule(new Conjunction(new Conjunction(drifting, lateralVelocityLeft), new LessThanEqual(distanceDrift)),
                driftLeft));


    }

}
