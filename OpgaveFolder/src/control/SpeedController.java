package control;

import car.VehicleProperties;
import fuzzy.Consequence;
import fuzzy.FuzzySystem;
import fuzzy.Rule;
import fuzzy.expression.Conjunction;
import fuzzy.expression.Disjunction;
import fuzzy.expression.GreaterThanEqual;
import fuzzy.expression.LessThanEqual;
import fuzzy.expression.Premise;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;
import org.json.simple.parser.ParseException;
import premises.ConsequenceReader;
import premises.PremiseReader;

/**
 * SpeedController - Controller that gets the job done as fast as possible
 * @author Florian Dejonckheere <florian@floriandejonckheere.be>
 */
public class SpeedController implements Controller {

    private final FuzzySystem system;

    public SpeedController()
            throws IOException, FileNotFoundException, ParseException {

        this.system = new FuzzySystem();
        Map<String, Premise> premises = PremiseReader.read("SpeedPremises");
        Map<String, Consequence> consequences = ConsequenceReader.read("SpeedConsequences");

        Premise speedLow = premises.get("speedLow");
        Premise speedMed = premises.get("speedMed");
        Premise speedHigh = premises.get("speedHigh");
        Premise speedVeryHigh = premises.get("speedVeryHigh");
        Premise speedNitro = premises.get("speedNitro");


        Premise distanceLow = premises.get("distanceLow");
        Premise distanceMed = premises.get("distanceMed");
        Premise distanceHigh = premises.get("distanceHigh");
        Premise distanceEndless = premises.get("distanceEndless");

        Premise ratioLow = premises.get("ratioLow");
        Premise ratioHigh = premises.get("ratioHigh");



        /**
         * Actuators
         */

        Consequence accelLow = consequences.get("accelLow");
        Consequence accelMed = consequences.get("accelMed");
        Consequence accelHigh = consequences.get("accelHigh");
        Consequence accelNitro = consequences.get("accelNitro");

        Consequence brakeLow = consequences.get("brakeLow");
        Consequence brakeMed = consequences.get("brakeMed");
        Consequence brakeHigh = consequences.get("brakeHigh");
        Consequence brakeExtreme = consequences.get("brakeExtreme");
        Consequence brakeEpic = consequences.get("brakeEpic");

        Consequence steerLeft = consequences.get("steerLeft");
        Consequence steerRight = consequences.get("steerRight");

//        Consequence steerGentleLeft = consequences.get("steerGentleLeft");
//        Consequence steerGentleRight = consequences.get("steerGentleRight");


//ACCEL
        system.addRule(new Rule(new Conjunction(new LessThanEqual(speedVeryHigh), new GreaterThanEqual(distanceHigh)), accelNitro));
        system.addRule(new Rule(new Conjunction(speedNitro, distanceEndless), accelMed));

//BRAKE
        //system.addRule(new Rule(new Conjunction(new Disjunction(speedHigh,speedNitro),new Disjunction(distanceLow,distanceMed)), brakeHigh));
        system.addRule(new Rule(new Conjunction(new GreaterThanEqual(speedHigh), new LessThanEqual(distanceHigh)), brakeExtreme));
        system.addRule(new Rule(new Conjunction(new GreaterThanEqual(speedHigh), new LessThanEqual(distanceMed)), brakeEpic));

//BASE ACCEL
        // DISTANCE = low /\ SPEED = low => ACCEL = low
        system.addRule(new Rule(new Conjunction(new Disjunction(speedLow,speedMed), distanceLow), accelLow));

//NORMAL STEERING
        system.addRule(new Rule(new Conjunction(premises.get("ratioLow"), new LessThanEqual(premises.get("speedHigh"))),
                consequences.get("steerRight")));
        system.addRule(new Rule(new Conjunction(premises.get("ratioHigh"), new LessThanEqual(premises.get("speedHigh"))),
                consequences.get("steerLeft")));

//SPEED STEERING
        system.addRule(new Rule(new Conjunction(premises.get("ratioLow"), new GreaterThanEqual(premises.get("speedHigh"))),
                consequences.get("steerGentleRight")));
        system.addRule(new Rule(new Conjunction(premises.get("ratioHigh"), new GreaterThanEqual(premises.get("speedHigh"))),
                consequences.get("steerGentleLeft")));

//SPEED BRAKE
        // 5. Don't turn on high speeds, but brake
        system.addRule(new Rule(new Conjunction(new Disjunction(premises.get("ratioLow"), premises.get("ratioHigh")), premises.get("speedNitro")),
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

//        System.out.println("acceleration: " + acceleration);
//        System.out.println("speed: " + vp.getCurrentCarSpeedKph());
//        System.out.println("brake: " + brake);
        System.out.println("ratio: " +
                Ratio.calc(vp.getDistanceFromLeftSensor(), vp.getDistanceFromRightSensor()) +
                " => " + steering);
//        System.out.println("frontSensor: " + vp.getDistanceFromFrontSensor());
//        System.out.println("lateralVelocity: " + vp.getLateralVelocity());
//        System.out.println("frontFriction: " + vp.getFrontLeftWheelFriction() + " | " + vp.getFrontRightWheelFriction());
//        System.out.println("backFriction: " + vp.getBackLeftWheelFriction() + " | " + vp.getBackRightWheelFriction());
        System.out.println("#######################");

        fc = new FrameControl((float) steering,
                                (float) acceleration,
                                (float) brake,
                                scanAngle);

        return fc;
    }

}
