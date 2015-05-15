package control;

import car.VehicleProperties;
import consequences.SpeedConsequences;
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
import java.util.Map;
import premises.SpeedPremises;

/**
 * SpeedController - Controller that gets the job done as fast as possible
 * @author Florian Dejonckheere <florian@floriandejonckheere.be>
 */
public class SpeedController implements Controller {

    private final FuzzySystem system;
    private SpeedPremises premises;
    private SpeedConsequences consequences;


    public SpeedController() {
        this.system = new FuzzySystem();
        this.premises = new SpeedPremises();        
        this.consequences = new SpeedConsequences();

        Premise speedLow = premises.get("speedLow");
        Premise speedMed = premises.get("speedMed");
        Premise speedHigh = premises.get("speedHigh");
        Premise speedVeryHigh = premises.get("speedVeryHigh");
        Premise speedNitro = premises.get("speedNitro");
        //Premise speedInsane = premises.get("speedInsane");
        

        Premise distanceLow = premises.get("distanceLow");
        Premise distanceMed = premises.get("distanceMed");
        Premise distanceHigh = premises.get("distanceHigh");
        Premise distanceEndless = premises.get("distanceEndless");

        Premise ratioLow = premises.get("ratioLow");
        Premise ratioHigh = premises.get("ratioHigh");
        Premise ratioMiddle = premises.get("ratioMiddle");
        
        Premise ratioLowDrift = premises.get("ratioLowDrift");
        Premise ratioHighDrift = premises.get("ratioHighDrift");
        
        Premise ratioLowSpeedy = premises.get("ratioLowSpeedy");
        Premise ratioHighSpeedy = premises.get("ratioHighSpeedy");
        
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
        Expression frictionNotDrifting = new Disjunction(new Disjunction(frontLeftFriction,frontRightFriction),new Disjunction(backLeftFriction,backRightFriction));
        Expression noFrictionDrifting = new Disjunction(new Disjunction(noFrontLeftFriction,noFrontRightFriction),new Disjunction(noBackLeftFriction,noBackRightFriction));

        Expression highSpeedTurning = new Disjunction(ratioLowSpeedy,ratioHighSpeedy);
        Expression drifting = new Disjunction(noFrictionDrifting,new Not(notDrifting));
        
        /**
         * Actuators
         */
        Consequence accelBase = consequences.get("accelBase");
        Consequence accelLow = consequences.get("accelLow");
        Consequence accelMed = consequences.get("accelMed");
        Consequence accelHigh = consequences.get("accelHigh");
        Consequence accelNitro = consequences.get("accelNitro");
        Consequence accelNone = consequences.get("accelNone");
        
        Consequence brakeNone = consequences.get("brakeNone");
        Consequence brakeLow = consequences.get("brakeLow");
        Consequence brakeMed = consequences.get("brakeMed");
        Consequence brakeHigh = consequences.get("brakeHigh");
        Consequence brakeExtreme = consequences.get("brakeExtreme");
        Consequence brakeEpic = consequences.get("brakeEpic");
        
        Consequence steerLeft = consequences.get("steerLeft");
        Consequence steerRight = consequences.get("steerRight");
        
        Consequence driftLeft = consequences.get("driftLeft");
        Consequence driftRight = consequences.get("driftRight");
        Consequence steerGentleLeft = consequences.get("steerGentleLeft");
        Consequence steerGentleRight = consequences.get("steerGentleRight");

        
        // 1. Accelerate if nothing's in front of you, but mind your speed
        // SPEED = low \/ med \/ high => ACCEL = nitro
        system.addRule(new Rule(new Conjunction(new LessThanEqual(speedVeryHigh),new GreaterThanEqual(distanceHigh)), accelNitro));
        system.addRule(new Rule(new Conjunction(new Conjunction(speedNitro,distanceEndless),ratioMiddle), accelMed));
        //system.addRule(new Rule(new Conjunction(new Disjunction(speedLow,speedMed),distanceHigh), accelNitro));
        
        // Note: Bochten kunnen veilig genomen worden aan max ~90 
        // 2. If something comes up in front of you, don't accelerate and use your brakes
        // SPEED = High /\ DISTANCE = low => BRAKE = high
        //system.addRule(new Rule(new Conjunction(new Disjunction(speedHigh,speedNitro),new Disjunction(distanceLow,distanceMed)), brakeHigh));
        system.addRule(new Rule(new Conjunction(new GreaterThanEqual(speedHigh),new LessThanEqual(distanceMed)), brakeExtreme));
        //system.addRule(new Rule(new Conjunction(new GreaterThanEqual(speedVeryHigh),new LessThanEqual(distanceHigh)), brakeEpic));
        
        // 3. Whatever happens, always have a base speed
        // DISTANCE = low /\ SPEED = low => ACCEL = low
        system.addRule(new Rule(new Conjunction(new Disjunction(speedLow,speedMed), distanceLow), accelLow));
        
//NORMAL STEERING
        system.addRule(new Rule(new Conjunction(ratioLow,new LessThanEqual(distanceMed)), steerRight));
        system.addRule(new Rule(new Conjunction(ratioHigh,new LessThanEqual(distanceMed)), steerLeft));

//SPEED STEERING
        system.addRule(new Rule(new Conjunction(new Conjunction(ratioLowSpeedy,new GreaterThanEqual(distanceHigh)),new LessThanEqual(speedHigh)), steerGentleRight));
        system.addRule(new Rule(new Conjunction(new Conjunction(ratioHighSpeedy,new GreaterThanEqual(distanceHigh)),new LessThanEqual(speedHigh)), steerGentleLeft));
        
        // 5. Don't turn on high speeds, but brake
        system.addRule(new Rule(new Conjunction(ratioMiddle,speedNitro), brakeHigh));
        
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
        system.addInput("leftRightDistanceRatio", 
               vp.getDistanceFromLeftSensor() / (vp.getDistanceFromLeftSensor() + vp.getDistanceFromRightSensor()));
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
        //steering -= (steering - vp.getAngleFrontWheels())/8;
        
        //Premise speedNitro = premises.get("speedNitro");
        steering -= (steering - vp.getAngleFrontWheels())/8;
        /*if(vp.getCurrentCarSpeedKph() > speedNitro.getLowerLimit()){
            steering -= (steering - vp.getAngleFrontWheels())/256;
        }else{
            steering -= (steering - vp.getAngleFrontWheels())/8;
        }*/
        
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
                vp.getDistanceFromLeftSensor() / (vp.getDistanceFromLeftSensor() + vp.getDistanceFromRightSensor()) + 
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
