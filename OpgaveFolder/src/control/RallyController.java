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

        Premise speedBackwards = premises.get("speedBackwards");
        Premise speedVeryLow = premises.get("speedVeryLow");
        Premise speedLow = premises.get("speedLow");
        Premise speedMed = premises.get("speedMed");
        Premise speedDrift = premises.get("speedDrift");
        Premise speedHigh = premises.get("speedHigh");
        Premise speedVeryHigh = premises.get("speedHigh");
        Premise speedNitro = premises.get("speedNitro");

        Premise distanceVeryLow = premises.get("distanceVeryLow");
        Premise distanceLow = premises.get("distanceLow");
        Premise distanceMed = premises.get("distanceMed");
        Premise distanceHigh = premises.get("distanceHigh");
        Premise distanceVeryHigh = premises.get("distanceVeryHigh");
        Premise distanceEndless = premises.get("distanceEndless");
        
        Premise ratioLow = premises.get("ratioLow");
        Premise ratioHigh = premises.get("ratioHigh");
        Premise ratioLowSpeedy = premises.get("ratioLowSpeedy");
        Premise ratioHighSpeedy = premises.get("ratioHighSpeedy");
        
        Premise ratioLowDrift = premises.get("ratioLowDrift");
        Premise ratioHighDrift = premises.get("ratioHighDrift");
        
        Premise ratioLowBeforeDrift = premises.get("ratioLowBeforeDrift");
        Premise ratioHighBeforeDrift = premises.get("ratioHighBeforeDrift");
        
        Premise lateralVelocityLow = premises.get("lateralVelocityLow");
        Premise notLateralVelocityHigh = premises.get("notLateralVelocityHigh");
        Premise notDriftingLateral = premises.get("notDriftingLateral");
        
        Premise noFrontLeftFriction = premises.get("noFrontLeftFriction");
        Premise noBackLeftFriction = premises.get("noBackLeftFriction");
        Premise noFrontRightFriction = premises.get("noFrontRightFriction");
        Premise noBackRightFriction = premises.get("noBackRightFriction");
        
        /////////////////////////////////////
        
        Consequence accelBase = consequences.get("accelBase");
        Consequence accelLow = consequences.get("accelLow");
        Consequence accelMed = consequences.get("accelMed");
        Consequence accelHigh = consequences.get("accelHigh");
        Consequence accelNitro = consequences.get("accelNitro");
        Consequence accelDriftHigh = consequences.get("accelDriftHigh");
        Consequence accelDriftVeryHigh = consequences.get("accelDriftVeryHigh");
        
        Consequence brakeNone = consequences.get("brakeNone");
        Consequence brakeLow = consequences.get("brakeLow");
        Consequence brakeMed = consequences.get("brakeMed");
        Consequence brakeHigh = consequences.get("brakeHigh");
        Consequence brakeExtreme = consequences.get("brakeExtreme");
        Consequence brakeEpic = consequences.get("brakeEpic");
        Consequence brakeDrift = consequences.get("brakeDrift");

        Consequence steerLeft = consequences.get("steerLeft");
        Consequence steerRight = consequences.get("steerRight");
        
        Consequence steerGentleLeft = consequences.get("steerGentleLeft");
        Consequence steerGentleRight = consequences.get("steerGentleRight");
 
        Consequence driftLeft = consequences.get("driftLeft");
        Consequence driftRight = consequences.get("driftRight");
        
        Consequence steerIntoDriftLeft = consequences.get("steerIntoDriftLeft");
        Consequence steerIntoDriftRight = consequences.get("steerIntoDriftRight");
        
        Expression driftingNoFriction = new Disjunction(new Disjunction(noFrontLeftFriction,noFrontRightFriction),new Disjunction(noBackLeftFriction,noBackRightFriction));
        Expression driftingLateral = new Not(notDriftingLateral);
        Expression drifting = new Conjunction(driftingNoFriction,driftingLateral);

/* ACCEL */      
        system.addRule(new Rule(new Conjunction(new LessThanEqual(speedVeryHigh),new GreaterThanEqual(distanceHigh)), accelNitro));
        //Note: zet hier accelMed als je niet met 200+kph tegen de muur wil bokken. Maar je kan daarna wel verder rijden.
        system.addRule(new Rule(new Conjunction(new GreaterThanEqual(speedVeryHigh),new GreaterThanEqual(distanceEndless)), accelHigh));
        //Note: beter speedMed hier, maar dan slipt ij in een van de begin bochten.
        system.addRule(new Rule(new Conjunction(new LessThanEqual(speedLow), new Disjunction(distanceLow,distanceMed)), accelLow));
        system.addRule(new Rule(speedBackwards, accelBase));
        
/* BRAKE */
        system.addRule(new Rule(new Conjunction(new GreaterThanEqual(speedHigh),new LessThanEqual(distanceMed)), brakeExtreme));
        system.addRule(new Rule(new Conjunction(new GreaterThanEqual(speedVeryHigh),new LessThanEqual(distanceVeryHigh)), brakeHigh));
        system.addRule(new Rule(new Conjunction(new GreaterThanEqual(speedLow),distanceVeryLow), brakeEpic));
        
/* STEERING */
        system.addRule(new Rule(new Conjunction(new Conjunction(ratioLow,new LessThanEqual(speedHigh)),new Not(drifting)), steerRight));
        system.addRule(new Rule(new Conjunction(new Conjunction(ratioHigh,new LessThanEqual(speedHigh)),new Not(drifting)), steerLeft));
/* SPEED STEERING */
        system.addRule(new Rule(new Conjunction(ratioLow,new GreaterThanEqual(speedVeryHigh)), steerGentleRight));
        system.addRule(new Rule(new Conjunction(ratioHigh,new GreaterThanEqual(speedVeryHigh)), steerGentleLeft));
        
//STEER IN TURN      
        // RATIO = low /\ DISTANCE = low /\ FRICTION on front wheels (not drifting out of control) => DRIFT = left
        //system.addRule(new Rule(new Conjunction(new Conjunction(ratioLowDrift,new LessThanEqual(distanceMed)),new Not(drifting)), steerIntoDriftRight));
        // RATIO = high /\ DISTANCE = low /\ FRICTION on front wheels (not drifting out of control) => DRIFT = right
        //system.addRule(new Rule(new Conjunction(new Conjunction(ratioHighDrift,new LessThanEqual(distanceMed)),new Not(drifting)), steerIntoDriftLeft));
        system.addRule(new Rule(new Conjunction(new Conjunction(ratioLowBeforeDrift,new LessThanEqual(distanceLow)),new Not(drifting)), steerIntoDriftRight));
        // RATIO = high /\ DISTANCE = low /\ FRICTION on front wheels (not drifting out of control) => DRIFT = right
        system.addRule(new Rule(new Conjunction(new Conjunction(ratioHighBeforeDrift,new LessThanEqual(distanceLow)),new Not(drifting)), steerIntoDriftLeft));
//ACCEL WHILE DRIFT
        system.addRule(new Rule(drifting, accelDriftHigh));
        system.addRule(new Rule(new Conjunction(new Not(notLateralVelocityHigh),new LessThanEqual(speedDrift)), accelDriftVeryHigh));
        
//OVERSTEERING       
        // noRightWheelfriction /\ STEERING = ratioLow /\ DISTANCE = low  => DRIFT = right
        system.addRule(new Rule(new Conjunction(new Conjunction(drifting,ratioLowDrift),new LessThanEqual(distanceMed)), driftRight));
        system.addRule(new Rule(new Conjunction(new Conjunction(drifting,ratioHighDrift),new LessThanEqual(distanceMed)), driftLeft));

//BRAKE
        system.addRule(new Rule(drifting, brakeLow));
        system.addRule(new Rule(new Conjunction(new Not(notLateralVelocityHigh),new LessThanEqual(speedDrift)), brakeDrift));

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
        scanAngle = 0.9;

        /**
         * Debug output
         */

        System.out.println("acceleration: " + acceleration);
        System.out.println("speed: " + vp.getCurrentCarSpeedKph());
        System.out.println("brake: " + brake);
        System.out.println("ratio: " +
                Ratio.calc(vp.getDistanceFromLeftSensor(), vp.getDistanceFromRightSensor()) +
                " => " + steering);
        System.out.println("frontSensor: " + vp.getDistanceFromFrontSensor() + 
                " | " + "lateralVelocity: " + vp.getLateralVelocity() );
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
