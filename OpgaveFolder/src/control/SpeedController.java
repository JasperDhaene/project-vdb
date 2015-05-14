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
        Premise speedNitro = premises.get("speedNitro");
        Premise speedInsane = premises.get("speedInsane");
        
        Premise distanceVeryLow = premises.get("distanceVeryLow");
        Premise distanceLow = premises.get("distanceLow");
        Premise distanceMed = premises.get("distanceMed");
        Premise distanceHigh = premises.get("distanceHigh");
        Premise distanceVeryHigh = premises.get("distanceVeryHigh");
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
        
        Consequence steerLeft = consequences.get("steerLeft");
        Consequence steerRight = consequences.get("steerRight");
        
        Consequence driftLeft = consequences.get("driftLeft");
        Consequence driftRight = consequences.get("driftRight");
        Consequence steerGentleLeft = consequences.get("steerGentleLeft");
        Consequence steerGentleRight = consequences.get("steerGentleRight");

        
        
//ACCEL
        //system.addRule(new Rule(new Conjunction(new LessThanEqual(speedHigh),new GreaterThanEqual(distanceHigh)), accelNitro));
        //system.addRule(new Rule(new Conjunction(new Conjunction(new GreaterThanEqual(speedNitro),distanceEndless),ratioMiddle), accelMed));
        //system.addRule(new Rule(new Conjunction(new LessThanEqual(speedMed),distanceLow), accelLow));
        // 3. Whatever happens, always have a base speed
        // DISTANCE = low /\ SPEED = low => ACCEL = low
        //system.addRule(new Rule(speedLow, accelBase));
        
        //system.addRule(new Rule(new Conjunction(distanceEndless,ratioMiddle), accelHigh));
        //system.addRule(new Rule(new Conjunction(distanceHigh,new LessThanEqual(speedHigh)), accelNitro));
        //system.addRule(new Rule(new Conjunction(distanceMed,new LessThanEqual(speedMed)), accelLow));
        //system.addRule(new Rule(new Conjunction(distanceLow,speedLow), accelMed));
        
        system.addRule(new Rule(new Conjunction(new LessThanEqual(speedNitro),new GreaterThanEqual(distanceHigh)), accelNitro));
        system.addRule(new Rule(new Conjunction(new LessThanEqual(speedMed),new LessThanEqual(distanceMed)), accelMed));
        //system.addRule(new Rule(new Conjunction(new LessThanEqual(speedNitro),distanceHigh), accelMed));
        //system.addRule(new Rule(new Conjunction(new Conjunction(new Not(drifting),new LessThanEqual(speedHigh)),new LessThanEqual(distanceMed)), accelLow));
        //system.addRule(new Rule(new Conjunction(new Conjunction(drifting,new LessThanEqual(speedHigh)),new LessThanEqual(distanceMed)), accelHigh));
        //system.addRule(new Rule(new Conjunction(new LessThanEqual(speedNitro),distanceVeryLow), accelHigh));
        

//BRAKE
        //system.addRule(new Rule(new Conjunction(new GreaterThanEqual(speedHigh),distanceLow), brakeMed));
        //system.addRule(new Rule(new Conjunction(new GreaterThanEqual(speedHigh),distanceMed), brakeExtreme));
        system.addRule(new Rule(new Conjunction(new LessThanEqual(distanceMed),new GreaterThanEqual(speedMed)), brakeExtreme));
        //system.addRule(new Rule(new Conjunction(distanceHigh,speedInsane), brakeExtreme));
        system.addRule(new Rule(new Conjunction(new GreaterThanEqual(speedNitro),new Not(ratioMiddle)), brakeHigh));
        //system.addRule(new Rule(new Conjunction(distanceMed,new GreaterThanEqual(speedHigh)), brakeHigh));
        //system.addRule(new Rule(new Conjunction(distanceLow,new GreaterThanEqual(speedHigh)), brakeMed));
        
        system.addRule(new Rule(new Conjunction(ratioMiddle,new Disjunction(speedNitro,speedInsane)), brakeMed));
        //system.addRule(new Rule(drifting, brakeMed));
        
//STEERING   
        // Note: er mag pas gedraaid worden onder de 300
        // 4. Strive for a stable left/right ratio
        // RATIO = low => STEERING = right (high)
        /* Without GTE/LTE
        system.addRule(new Rule(new Conjunction(ratioLow,new Not(new Disjunction(speedNitro,speedInsane))), steerRight));
        // RATIO = high => STEERING = left (low)
        system.addRule(new Rule(new Conjunction(ratioHigh,new Not(new Disjunction(speedNitro,speedInsane))), steerLeft));
        
        system.addRule(new Rule(new Conjunction(ratioLowSpeedy,new Disjunction(speedNitro,speedInsane)), steerGentleRight));
        // RATIO = high => STEERING = left (low)
        system.addRule(new Rule(new Conjunction(ratioHighSpeedy,new Disjunction(speedNitro,speedInsane)), steerGentleLeft));
        */
        
        system.addRule(new Rule(new Conjunction(ratioLow,new LessThanEqual(speedHigh)), steerRight));
        system.addRule(new Rule(new Conjunction(ratioHigh,new LessThanEqual(speedHigh)), steerLeft));
        
        //system.addRule(new Rule(new Conjunction(distanceLow, new Conjunction(new LessThanEqual(speedMed),ratioLow)), steerRight));
        //system.addRule(new Rule(new Conjunction(distanceLow, new Conjunction(new LessThanEqual(speedMed),ratioHigh)), steerLeft));
        
        //system.addRule(new Rule(new Conjunction(new GreaterThanEqual(distanceMed), new Conjunction(new LessThanEqual(speedMed),ratioLow)), steerGentleRight));
        //system.addRule(new Rule(new Conjunction(new GreaterThanEqual(distanceMed), new Conjunction(new LessThanEqual(speedMed),ratioHigh)), steerGentleLeft));
        
        //system.addRule(new Rule(new Conjunction(new LessThanEqual(distanceLow),new Conjunction(ratioLowSpeedy,new GreaterThanEqual(speedNitro))), driftRight));
        //system.addRule(new Rule(new Conjunction(new LessThanEqual(distanceLow),new Conjunction(ratioHighSpeedy,new GreaterThanEqual(speedNitro))), driftLeft));
        
        //TODO: not working. Drifts because steering is still too violent
        system.addRule(new Rule(new Conjunction(new GreaterThanEqual(distanceMed),new Conjunction(ratioLowSpeedy,new GreaterThanEqual(speedNitro))), steerGentleRight));
        // RATIO = high => STEERING = left (low)
        system.addRule(new Rule(new Conjunction(new GreaterThanEqual(distanceMed),new Conjunction(ratioHighSpeedy,new GreaterThanEqual(speedNitro))), steerGentleLeft));
        // 5. Don't turn on high speeds, but brake
        //system.addRule(new Rule(new Conjunction(ratioMiddle,new Disjunction(speedNitro,speedInsane)), brakeExtreme));
        
//OVERSTEERING
        //system.addRule(new Rule(new Conjunction(new Conjunction(drifting,ratioLowDrift),distanceLow), driftRight));
        //system.addRule(new Rule(new Conjunction(new Conjunction(drifting,ratioHighDrift),distanceLow), driftLeft));

        
        
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
        
        Premise speedNitro = premises.get("speedNitro");
        
        if(vp.getCurrentCarSpeedKph() > speedNitro.getLowerLimit()){
            steering -= (steering - vp.getAngleFrontWheels())/256;
        }else{
            steering -= (steering - vp.getAngleFrontWheels())/8;
        }
        
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
