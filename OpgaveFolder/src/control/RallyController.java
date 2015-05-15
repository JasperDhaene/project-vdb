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
public class RallyController implements Controller {

   private final FuzzySystem system;

    public RallyController() throws IOException, FileNotFoundException, ParseException {
        this.system = new FuzzySystem();

        Map<String, Premise> premises = PremiseReader.read("RallyPremises");
        Map<String, Consequence> consequences = ConsequenceReader.read("RallyConsequences");

        Premise speedLow = premises.get("speedLow");
        Premise speedMed = premises.get("speedMed");
        Premise speedHigh = premises.get("speedHigh");
        Premise speedNitro = premises.get("speedNitro");
        Premise speedInsane = premises.get("speedInsane");

        Premise distanceVeryLow = premises.get("distanceVeryLow");
        Premise distanceLow = premises.get("distanceLow");
        Premise distanceMed = premises.get("distanceMed");
        Premise distanceHigh = premises.get("distanceHigh");
        Premise distanceEndless = premises.get("distanceEndless");

        Premise ratioLow = premises.get("ratioLow");
        Premise ratioHigh = premises.get("ratioHigh");
        Premise ratioLowSpeedy = premises.get("ratioLowSpeedy");
        Premise ratioHighSpeedy = premises.get("ratioHighSpeedy");
        Premise ratioMiddle = premises.get("ratioMiddle");

        Premise ratioLowDrift = premises.get("ratioLowDrift");
        Premise ratioHighDrift = premises.get("ratioHighDrift");

        Premise lateralVelocityLow = premises.get("lateralVelocityLow");
        Premise notDrifting = premises.get("notDrifting");

        Premise noFrontLeftFriction = premises.get("noFrontLeftFriction");
        Premise noBackLeftFriction = premises.get("noBackLeftFriction");
        Premise noFrontRightFriction = premises.get("noFrontRightFriction");
        Premise noBackRightFriction = premises.get("noBackRightFriction");
        Premise frontLeftFriction = premises.get("frontLeftFriction");
        Premise backLeftFriction = premises.get("backLeftFriction");
        Premise frontRightFriction = premises.get("frontRightFriction");
        Premise backRightFriction = premises.get("backRightFriction");
        Expression driftingFriction = new Disjunction(new Disjunction(frontLeftFriction,frontRightFriction),new Disjunction(backLeftFriction,backRightFriction));
        Expression notDriftingFriction = new Disjunction(new Disjunction(noFrontLeftFriction,noFrontRightFriction),new Disjunction(noBackLeftFriction,noBackRightFriction));

        /////////////////////////////////////

        Consequence accelBase = consequences.get("accelBase");
        Consequence accelLow = consequences.get("accelLow");
        Consequence accelMed = consequences.get("accelMed");
        Consequence accelHigh = consequences.get("accelHigh");
        Consequence accelNitro = consequences.get("accelNitro");
        Consequence accelDriftLow = consequences.get("accelDriftLow");
        Consequence accelDriftHigh = consequences.get("accelDriftHigh");

        Consequence brakeNone = consequences.get("brakeNone");
        Consequence brakeLow = consequences.get("brakeLow");
        Consequence brakeMed = consequences.get("brakeMed");
        Consequence brakeHigh = consequences.get("brakeHigh");
        Consequence brakeExtreme = consequences.get("brakeExtreme");

        Consequence steerLeft = consequences.get("steerLeft");
        Consequence steerRight = consequences.get("steerRight");
        Consequence steerGentleLeft = consequences.get("steerGentleLeft");
        Consequence steerGentleRight = consequences.get("steerGentleRight");

        Consequence driftLeft = consequences.get("driftLeft");
        Consequence driftRight = consequences.get("driftRight");

/* ACCEL */
        // (SPEED = low \/ med \/ high) /\ (DISTANCE = high \/ endless) => ACCEL = nitro
        //system.addRule(new Rule(new Conjunction(new Disjunction(new Disjunction(speedLow,speedMed),speedHigh),new Disjunction(distanceHigh,distanceEndless)), accelNitro));
        system.addRule(new Rule(distanceEndless, accelNitro));
        system.addRule(new Rule(new Conjunction(distanceHigh,new LessThanEqual(speedHigh)), accelHigh));

        // DISTANCE < med => ACCEL = base
        system.addRule(new Rule(new LessThanEqual(distanceMed), accelBase));

/* BRAKE */
        // SPEED = (nitro \/ insane) /\ (DISTANCE = high \/ med) => BRAKE = extreme
        system.addRule(new Rule(new Conjunction(new Disjunction(speedNitro,speedInsane),new Disjunction(distanceHigh,distanceMed)), brakeExtreme));
        // SPEED = high  /\ (DISTANCE = low \/ med) => BRAKE = med
        //system.addRule(new Rule(new Conjunction(speedHigh,new Disjunction(distanceHigh,distanceMed)), brakeHigh));

        // RATIO = middle /\ (SPEED = nitro \/ insane => BRAKE = extreme
        //system.addRule(new Rule(new Conjunction(new Not(ratioMiddle),new Disjunction(speedNitro,speedInsane)), brakeExtreme));

/* STEERING */
        system.addRule(new Rule(new Conjunction(ratioLow,new Not(new Disjunction(speedNitro,speedInsane))), steerRight));
        // RATIO = high => STEERING = left (low)
        system.addRule(new Rule(new Conjunction(ratioHigh,new Not(new Disjunction(speedNitro,speedInsane))), steerLeft));

        //TODO: fix
        system.addRule(new Rule(new Conjunction(ratioLowSpeedy,new GreaterThanEqual(speedNitro)), steerGentleRight));
        // RATIO = high => STEERING = left (low)
        system.addRule(new Rule(new Conjunction(ratioHighSpeedy,new GreaterThanEqual(speedNitro)), steerGentleLeft));

        /* DRIFTING */

//STEER IN TURN
        // RATIO = low /\ DISTANCE = low /\ FRICTION on front wheels (not drifting out of control) => DRIFT = left
        //system.addRule(new Rule(new Conjunction(new Conjunction(ratioLowDrift,distanceLow),new Disjunction(new Not(noFrontLeftFriction),new Not(noFrontRightFriction))), driftRight));
        // RATIO = high /\ DISTANCE = low /\ FRICTION on front wheels (not drifting out of control) => DRIFT = right
        //system.addRule(new Rule(new Conjunction(new Conjunction(ratioHighDrift,distanceLow),new Disjunction(new Not(noFrontLeftFriction),new Not(noFrontRightFriction))), driftLeft));
        // RATIO = low /\ DISTANCE = low /\ FRICTION on front wheels (not drifting out of control) => DRIFT = left
        system.addRule(new Rule(new Conjunction(new Conjunction(ratioLowDrift,distanceLow),new Disjunction(notDrifting,notDriftingFriction)), driftRight));
        // RATIO = high /\ DISTANCE = low /\ FRICTION on front wheels (not drifting out of control) => DRIFT = right
        system.addRule(new Rule(new Conjunction(new Conjunction(ratioHighDrift,distanceLow),new Disjunction(notDrifting,notDriftingFriction)), driftLeft));


//ACCEL WHILE DRIFT
        // (DISTANCE = low \/ med) /\ (SPEED = high) /\ Drifting => ACCEL = med
        system.addRule(new Rule(new Conjunction(new Conjunction(distanceLow ,new Not(notDrifting)),new Not(new Disjunction(speedNitro,speedInsane))), accelDriftLow));
        // In horizontal drift, avoid crash by nitro accel to gain grip and turn vertical
        system.addRule(new Rule(new Conjunction(distanceVeryLow,new Not(notDrifting)), accelDriftHigh));
//OVERSTEERING
        // noRightWheelfriction /\ STEERING = ratioLowDrift /\ DISTANCE = low  => DRIFT = right
        system.addRule(new Rule(new Conjunction(new Conjunction(new Disjunction(new Not(notDrifting),driftingFriction),ratioLowDrift),distanceLow), driftRight));

        // noLeftWheelfriction /\ STEERING = ratioHighDrift /\ DISTANCE = low  => DRIFT = left
        system.addRule(new Rule(new Conjunction(new Conjunction(new Disjunction(new Not(notDrifting),driftingFriction),ratioHighDrift),distanceLow), driftLeft));

//BRAKE
        // (DISTANCE = low \/ med) /\ (SPEED = high \/ nitro) => BRAKE = high
        //system.addRule(new Rule(new Conjunction(new Conjunction(new Disjunction(speedHigh,speedNitro),new Disjunction(distanceLow,distanceMed)),lateralVelocityLow), brakeHigh));
        // (DISTANCE = low \/ med) /\ (SPEED = high \/ nitro) => BRAKE = high
        system.addRule(new Rule(new Conjunction(new Conjunction(new Disjunction(speedHigh,speedNitro),new Disjunction(distanceLow,distanceMed)),new Not(lateralVelocityLow)), brakeHigh));


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
        system.addInput("leftRightDistanceRatio",
                (vp.getDistanceFromLeftSensor() - vp.getDistanceFromRightSensor()));
        system.addInput("lateralVelocity", vp.getLateralVelocity());
        system.addInput("frontLeftFriction", vp.getFrontLeftWheelFriction());
        system.addInput("frontRightFriction", vp.getFrontRightWheelFriction());
        system.addInput("backLeftFriction", vp.getBackLeftWheelFriction());
        system.addInput("backRightFriction", vp.getBackRightWheelFriction());
        Map<String, Double> output = system.evaluate();

        /**
         * Steering
         */
        steering = output.get("steering");
        steering -= (steering - vp.getAngleFrontWheels())/8;

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
        scanAngle = 0.8;

        /**
         * Debug output
         */

        System.out.println("acceleration: " + acceleration);
        System.out.println("speed: " + vp.getCurrentCarSpeedKph());
        System.out.println("brake: " + brake);
        System.out.println("ratio: " +
                (vp.getDistanceFromLeftSensor() - vp.getDistanceFromRightSensor()) +
                " => " + steering);
        System.out.println("frontSensor: " + vp.getDistanceFromFrontSensor());
        System.out.println("lateralVelocity: " + vp.getLateralVelocity());
        System.out.println("frontFriction: " + vp.getFrontLeftWheelFriction() + " | " + vp.getFrontRightWheelFriction());
        System.out.println("backFriction: " + vp.getBackLeftWheelFriction() + " | " + vp.getBackRightWheelFriction());
        System.out.println("#######################");

        fc = new FrameControl((float) steering,
                                (float) acceleration,
                                (float) brake,
                                scanAngle);

        return fc;
    }

}
