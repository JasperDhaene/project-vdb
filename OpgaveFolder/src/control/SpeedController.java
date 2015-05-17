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
        Premise speedVeryLow = p("speedVeryLow"), speedLow = p("speedLow"), speedMed = p("speedMed"), speedHigh = p("speedHigh"), speedVeryHigh = p("speedHigh"), speedNitro = p("speedNitro"), distanceVeryLow = p("distanceVeryLow"), distanceLow = p("distanceLow"), distanceMed = p("distanceMed"), distanceHigh = p("distanceHigh"), distanceVeryHigh = p("distanceVeryHigh"), distanceEndless = p("distanceEndless"),  ratioLow = p("ratioLow"), ratioHigh = p("ratioHigh"), ratioLowSpeedy = p("ratioLowSpeedy"), ratioHighSpeedy = p("ratioHighSpeedy"),  ratioLowDrift = p("ratioLowDrift"), ratioHighDrift = p("ratioHighDrift"),  lateralVelocityLow = p("lateralVelocityLow"), notDriftingLateral = p("notDriftingLateral"),  noFrontLeftFriction = p("noFrontLeftFriction"), noBackLeftFriction = p("noBackLeftFriction"), noFrontRightFriction = p("noFrontRightFriction"), noBackRightFriction = p("noBackRightFriction");
        Consequence accelBase = c("accelBase"), accelLow = c("accelLow"), accelMed = c("accelMed"), accelHigh = c("accelHigh"), accelNitro = c("accelNitro"), accelDriftHigh = c("accelDriftHigh"), brakeNone = c("brakeNone"), brakeLow = c("brakeLow"), brakeMed = c("brakeMed"), brakeHigh = c("brakeHigh"), brakeExtreme = c("brakeExtreme"), brakeEpic = c("brakeEpic"), steerLeft = c("steerLeft"), steerRight = c("steerRight"), steerGentleLeft = c("steerGentleLeft"), steerGentleRight = c("steerGentleRight"), driftLeft = c("driftLeft"), driftRight = c("driftRight");

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
        // SPEED <= very high AND DISTANCE <= high => ACCEL = extremely high
        system.addRule(new Rule(new Conjunction(new LessThanEqual(speedVeryHigh), new GreaterThanEqual(distanceHigh)),
                accelNitro));
        // SPEED >= very high AND DISTANCE >= extremely high => ACCEL = high
        // TODO: zet hier accelMed als je niet met 200+kph tegen de muur wil bokken. Maar je kan daarna wel verder rijden.
        system.addRule(new Rule(new Conjunction(new GreaterThanEqual(speedVeryHigh), new GreaterThanEqual(distanceEndless)),
                accelHigh));
        // SPEED <= low AND (DISTANCE = low OR DISTANCE = med) => ACCEL = low
        // TODO: beter speedMed hier, maar dan slipt hij in een van de begin bochten.
        system.addRule(new Rule(new Conjunction(new LessThanEqual(speedLow), new Disjunction(distanceLow, distanceMed)),
                accelLow));

        /**
         * Braking.
         *
         */
        // SPEED >= very high AND DISTANCE <= med => BRAKE = extremely high
        system.addRule(new Rule(new Conjunction(new GreaterThanEqual(speedVeryHigh), new LessThanEqual(distanceMed)),
                brakeExtreme));
        // SPEED = extremely high AND DISTANCE <= very high => BRAKE = high
        system.addRule(new Rule(new Conjunction(speedNitro, new LessThanEqual(distanceVeryHigh)),
                brakeHigh));
        // SPEED <= low AND DISTANCE = very low => BRAKE = epically high
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
         */
        // RATIO = low AND SPEED >= very high => STEERING = low (right)
        system.addRule(new Rule(new Conjunction(ratioLow, new GreaterThanEqual(speedVeryHigh)),
                steerGentleRight));
        // RATIO = high AND SPEED >= very high => STEERING = negative low (left)
        system.addRule(new Rule(new Conjunction(ratioHigh, new GreaterThanEqual(speedVeryHigh)),
                steerGentleLeft));

// TODO: STEER IN TURN
        // RATIO = low /\ DISTANCE = low /\ FRICTION on front wheels (not drifting out of control) => DRIFT = left
 //       system.addRule(new Rule(new Conjunction(new Conjunction(ratioLowDrift,distanceLow),new Conjunction(new Not(noFrontLeftFriction),new Not(noFrontRightFriction))), driftRight));
        // RATIO = high /\ DISTANCE = low /\ FRICTION on front wheels (not drifting out of control) => DRIFT = right
 //       system.addRule(new Rule(new Conjunction(new Conjunction(ratioHighDrift,distanceLow),new Conjunction(new Not(noFrontLeftFriction),new Not(noFrontRightFriction))), driftLeft));


        /**
         * Drifting.
         */
        system.addRule(new Rule(drifting, accelDriftHigh));
        system.addRule(new Rule(drifting, brakeLow));

        /**
         * Oversteering.
         */
        // noRightWheelfriction AND STEERING = ratioLow AND DISTANCE = low => DRIFT = right
        system.addRule(new Rule(new Conjunction(new Conjunction(drifting, ratioLowDrift), new LessThanEqual(distanceMed)),
                driftRight));
        // noRightWheelfriction AND STEERING = ratioHigh AND DISTANCE = low => DRIFT = left
        system.addRule(new Rule(new Conjunction(new Conjunction(drifting, ratioHighDrift), new LessThanEqual(distanceMed)),
                driftLeft));

    }

}
