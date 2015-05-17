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
        Premise speedBackwards = p("speedBackwards"), speedVeryLow = p("speedVeryLow"), speedLow = p("speedLow"), speedMed = p("speedMed"), speedDrift = p("speedDrift"), speedHigh = p("speedHigh"), speedVeryHigh = p("speedHigh"), speedNitro = p("speedNitro"), speedInsane = p("speedInsane"), distanceVeryLow = p("distanceVeryLow"), distanceLow = p("distanceLow"), distanceMed = p("distanceMed"), distanceDrift = p("distanceDrift"), distanceHigh = p("distanceHigh"), distanceVeryHigh = p("distanceVeryHigh"), distanceStop = p("distanceStop"), distanceEndless = p("distanceEndless"), ratioLow = p("ratioLow"), ratioHigh = p("ratioHigh"), ratioLowSpeedy = p("ratioLowSpeedy"), ratioHighSpeedy = p("ratioHighSpeedy"), ratioLowDrift = p("ratioLowDrift"), ratioHighDrift = p("ratioHighDrift"), ratioLowBeforeDrift = p("ratioLowBeforeDrift"), ratioHighBeforeDrift = p("ratioHighBeforeDrift"), lateralVelocityLeft = p("lateralVelocityLeft"), lateralVelocityRight = p("lateralVelocityRight"), notLateralVelocityHigh = p("notLateralVelocityHigh"), notDriftingLateral = p("notDriftingLateral"), noFrontLeftFriction = p("noFrontLeftFriction"), noBackLeftFriction = p("noBackLeftFriction"), noFrontRightFriction = p("noFrontRightFriction"), noBackRightFriction = p("noBackRightFriction");
        Consequence accelBase = c("accelBase"), accelLow = c("accelLow"), accelMed = c("accelMed"), accelHigh = c("accelHigh"), accelNitro = c("accelNitro"), accelDriftHigh = c("accelDriftHigh"), accelDriftVeryHigh = c("accelDriftVeryHigh"), brakeNone = c("brakeNone"), brakeLow = c("brakeLow"), brakeMed = c("brakeMed"), brakeHigh = c("brakeHigh"), brakeExtreme = c("brakeExtreme"), brakeEpic = c("brakeEpic"), brakeDrift = c("brakeDrift"), steerLeft = c("steerLeft"), steerRight = c("steerRight"), steerGentleLeft = c("steerGentleLeft"), steerGentleRight = c("steerGentleRight"), driftLeft = c("driftLeft"), driftRight = c("driftRight"), steerIntoDriftLeft = c("steerIntoDriftLeft"), steerIntoDriftRight = c("steerIntoDriftRight");

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
        // SPEED <= very high AND DISTANCE <= high => ACCEL = extremely high
        system.addRule(new Rule(new Conjunction(new LessThanEqual(speedVeryHigh), new GreaterThanEqual(distanceHigh)),
                accelNitro));
        //system.addRule(new Rule(new Conjunction(speedNitro,distanceVeryHigh), accelMed));
        system.addRule(new Rule(new Conjunction(new LessThanEqual(speedNitro), new GreaterThanEqual(distanceEndless)),
                accelHigh));
        //Note: beter speedMed hier, maar dan slipt ij in een van de begin bochten.
        system.addRule(new Rule(new Conjunction(new LessThanEqual(speedDrift), new Disjunction(distanceLow,distanceMed)),
                accelLow));
        system.addRule(new Rule(new Disjunction(speedBackwards, speedVeryLow),
                accelBase));

        /**
         * Braking.
         *
         */
        // SPEED >= high AND DISTANCE <= med => BRAKE = extremely high
        system.addRule(new Rule(new Conjunction(new GreaterThanEqual(speedHigh), new LessThanEqual(distanceMed)),
                brakeExtreme));
        // SPEED >= very high AND DISTANCE <= stop => BRAKE = epically high
        system.addRule(new Rule(new Conjunction(new GreaterThanEqual(speedVeryHigh), new LessThanEqual(distanceStop)),
                brakeEpic));
        // SPEED >= low AND DISTANCE = very low => BRAKE = high
        system.addRule(new Rule(new Conjunction(new GreaterThanEqual(speedLow), distanceVeryLow),
                brakeHigh));

        // DRIFTING => BRAKE = low
        system.addRule(new Rule(drifting, brakeLow));
        system.addRule(new Rule(new Conjunction(new Not(notLateralVelocityHigh), new LessThanEqual(speedDrift)),
                brakeDrift));

        /**
         * Steering.
         *
         */
        system.addRule(new Rule(new Conjunction(new Conjunction(ratioLow, new LessThanEqual(speedHigh)), new Not(drifting)),
                steerRight));
        system.addRule(new Rule(new Conjunction(new Conjunction(ratioHigh, new LessThanEqual(speedHigh)), new Not(drifting)),
                steerLeft));

        /**
         * High-speed steering.
         *
         */
        system.addRule(new Rule(new Conjunction(ratioLow,new GreaterThanEqual(speedVeryHigh)),
                steerGentleRight));
        system.addRule(new Rule(new Conjunction(ratioHigh,new GreaterThanEqual(speedVeryHigh)),
                steerGentleLeft));

        /**
         * Drifting.
         *
         */
        system.addRule(new Rule(drifting, accelDriftHigh));
        system.addRule(new Rule(new Conjunction(new Not(notLateralVelocityHigh), new LessThanEqual(speedDrift)),
                accelDriftVeryHigh));

        /**
         * Oversteering.
         *
         * */
        // noRightWheelfriction /\ STEERING = ratioLow /\ DISTANCE = low  => DRIFT = right
        system.addRule(new Rule(new Conjunction(new Conjunction(drifting, lateralVelocityRight), new LessThanEqual(distanceDrift)),
                driftRight));
        system.addRule(new Rule(new Conjunction(new Conjunction(drifting, lateralVelocityLeft), new LessThanEqual(distanceDrift)),
                driftLeft));
        //system.addRule(new Rule(new Conjunction(new Conjunction(drifting,new Conjunction(ratioLowDrift,lateralVelocityLeft)),new LessThanEqual(distanceDrift)), driftLeft));
        //system.addRule(new Rule(new Conjunction(new Conjunction(drifting,new Conjunction(ratioHighDrift,lateralVelocityRight)),new LessThanEqual(distanceDrift)), driftRight));

    }

}
