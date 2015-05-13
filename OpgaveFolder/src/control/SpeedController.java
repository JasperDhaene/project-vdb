package control;

import car.VehicleProperties;
import consequences.RallyConsequences;
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
import fuzzy.membership.PIFunction;
import java.util.Map;
import premises.RallyPremises;

/**
 * SpeedController - Controller that gets the job done as fast as possible
 * @author Florian Dejonckheere <florian@floriandejonckheere.be>
 */
public class SpeedController implements Controller {

    private final FuzzySystem system;

    public SpeedController() {
        this.system = new FuzzySystem();
        
        RallyPremises premises = new RallyPremises();
        RallyConsequences consequences = new RallyConsequences();

        Premise speedLow = new Premise("speed",
                new PIFunction.TrapezoidPIFunction(0, 0, 40, 60));
        Premise speedMed = new Premise("speed",
                new PIFunction.TrapezoidPIFunction(50, 70, 90, 110));
        Premise speedHigh = new Premise("speed",
                new PIFunction.TrapezoidPIFunction(100, 130, 170, 190));
        Premise speedNitro = new Premise("speed",
                new PIFunction.TrapezoidPIFunction(180, 210, 230, 280));
        Premise speedInsane = new Premise("speed",
                new PIFunction.TrapezoidPIFunction(270, 290, 310, 400));
        /*
        Premise distanceLow = new Premise("frontSensorDistance",
                new PIFunction.TrapezoidPIFunction(0, 0, 30, 50));
        Premise distanceMed = new Premise("frontSensorDistance",
                new PIFunction.TrapezoidPIFunction(40, 50, 70, 90));
        Premise distanceHigh = new Premise("frontSensorDistance",
                new PIFunction.TrapezoidPIFunction(80, 100, 110, 130));
        Premise distanceEndless = new Premise("frontSensorDistance",
                new PIFunction.TrapezoidPIFunction(120, 130, Integer.MAX_VALUE, Integer.MAX_VALUE));
        */
        Premise distanceLow = new Premise("frontSensorDistance",
                new PIFunction.TrapezoidPIFunction(0, 0, 30, 50));
        Premise distanceMed = new Premise("frontSensorDistance",
                new PIFunction.TrapezoidPIFunction(40, 60, 90, 100));
        Premise distanceHigh = new Premise("frontSensorDistance",
                new PIFunction.TrapezoidPIFunction(90, 110, 130, 150));
        Premise distanceEndless = new Premise("frontSensorDistance",
                new PIFunction.TrapezoidPIFunction(140, 160, Integer.MAX_VALUE, Integer.MAX_VALUE));

        Premise ratioLow = new Premise("leftRightDistanceRatio",
                new PIFunction.TrapezoidPIFunction(
                        0,
                        0,
                        0.30, 0.35));
        Premise ratioMiddle = new Premise("leftRightDistanceRatio",
                new PIFunction.TrapezoidPIFunction(
                        0.35,
                        0.45,
                        0.55, 0.65));
        Premise ratioHigh = new Premise("leftRightDistanceRatio",
                new PIFunction.TrapezoidPIFunction(
                        0.65, 0.7,
                        1,
                        1));
        
        Premise ratioLowDrift = new Premise("leftRightDistanceRatio",
                    new PIFunction.TrapezoidPIFunction(
                        0,
                        0,
                        0.35, 0.40));
        Premise ratioHighDrift = new Premise("leftRightDistanceRatio",
                    new PIFunction.TrapezoidPIFunction(
                        0.6,
                        0.65,
                        1,1));
        
        Premise ratioLowSpeedy = new Premise("leftRightDistanceRatio",
                    new PIFunction.TrapezoidPIFunction(
                        0.35,
                        0.35,
                        0.4, 0.45));
        Premise ratioHighSpeedy = new Premise("leftRightDistanceRatio",
                    new PIFunction.TrapezoidPIFunction(
                        0.55, 0.6,
                        0.65,
                        0.65));
        
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

        
        /**
         * Actuators
         */
        Consequence accelLow = new Consequence("acceleration",
                new PIFunction.TrapezoidPIFunction(0, 0, 280, 560), 0, 1600);
        Consequence accelMed = new Consequence("acceleration",
                new PIFunction.TrapezoidPIFunction(480, 660, 840, 1120), 0, 1600);
        Consequence accelHigh = new Consequence("acceleration",
                new PIFunction.TrapezoidPIFunction(840, 1120, 1200, 1400), 0, 1600);
        Consequence accelNitro = new Consequence("acceleration",
                new PIFunction.TrapezoidPIFunction(1200, 1400, 1600, 1600), 0, 1600);
        Consequence accelNone = new Consequence("acceleration",
                new PIFunction.TrapezoidPIFunction(0, 0, 0, 0), 0, 1600);
        
        Consequence brakeNone = new Consequence("brake",
                new PIFunction.TrapezoidPIFunction(0, 0, 0, 0), 0, 40);
        Consequence brakeLow = new Consequence("brake",
                new PIFunction.TrapezoidPIFunction(0, 0, 10, 20), 0, 40);
        Consequence brakeMed = new Consequence("brake",
                new PIFunction.TrapezoidPIFunction(15, 20, 30, 35), 0, 40);
        Consequence brakeHigh = new Consequence("brake",
                new PIFunction.TrapezoidPIFunction(30, 35, 40, 40), 0, 40);
        Consequence brakeExtreme = new Consequence("brake",
                new PIFunction.TrapezoidPIFunction(30, 38, 40, 40), 0, 40);
        
        Consequence steerLeft = new Consequence("steering",
                new PIFunction.TriangularPIFunction(-1, -1, -0.5), -1, 1);
        Consequence steerRight = new Consequence("steering",
                new PIFunction.TriangularPIFunction(0.5, 1, 1), -1, 1);
        
        Consequence driftLeft = consequences.get("driftLeft");
        Consequence driftRight = consequences.get("driftRight");
        Consequence steerGentleLeft = consequences.get("steerGentleLeft");
        Consequence steerGentleRight = consequences.get("steerGentleRight");

        Expression highSpeedTurning = new Disjunction(ratioLowSpeedy,ratioHighSpeedy);
        
        // 1. Accelerate if nothing's in front of you, but mind your speed
        // SPEED = low \/ med \/ high => ACCEL = nitro
        system.addRule(new Rule(new Conjunction(new LessThanEqual(speedHigh),new GreaterThanEqual(distanceHigh)), accelNitro));
        system.addRule(new Rule(new Conjunction(new Conjunction(speedNitro,distanceEndless),ratioMiddle), accelMed));
        //system.addRule(new Rule(new Conjunction(new Disjunction(speedLow,speedMed),distanceHigh), accelNitro));
        
        // Note: Bochten kunnen veilig genomen worden aan max ~90 
        // 2. If something comes up in front of you, don't accelerate and use your brakes
        // SPEED = High /\ DISTANCE = low => BRAKE = high
        system.addRule(new Rule(new Conjunction(new Disjunction(speedHigh,speedNitro),new LessThanEqual(distanceMed)), brakeHigh));
        system.addRule(new Rule(new Conjunction(new Conjunction(new GreaterThanEqual(speedNitro),new LessThanEqual(distanceHigh)),new Not(highSpeedTurning)), brakeExtreme));
        
        // 3. Whatever happens, always have a base speed
        // DISTANCE = low /\ SPEED = low => ACCEL = low
        system.addRule(new Rule(new Conjunction(new Disjunction(speedLow,speedMed), distanceLow), accelLow));
        
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
        // RATIO = high => STEERING = left (low)
        system.addRule(new Rule(new Conjunction(ratioHigh,new LessThanEqual(speedHigh)), steerLeft));
        
        //TODO: not working. Drifts because steering is still too violent
        //system.addRule(new Rule(new Conjunction(ratioLowSpeedy,new GreaterThanEqual(speedNitro)), steerGentleRight));
        // RATIO = high => STEERING = left (low)
        //system.addRule(new Rule(new Conjunction(ratioHighSpeedy,new GreaterThanEqual(speedNitro)), steerGentleLeft));
        // 5. Don't turn on high speeds, but brake
        system.addRule(new Rule(new Conjunction(ratioMiddle,new Disjunction(speedNitro,speedInsane)), brakeExtreme));
        
//OVERSTEERING       
        // noRightWheelfriction /\ STEERING = ratioLowDrift /\ DISTANCE = low  => DRIFT = right
        //system.addRule(new Rule(new Conjunction(new Conjunction(new Disjunction(new Not(notDrifting),driftingFriction),ratioLowDrift),distanceLow), driftRight));
        
        // noLeftWheelfriction /\ STEERING = ratioHighDrift /\ DISTANCE = low  => DRIFT = left
        //system.addRule(new Rule(new Conjunction(new Conjunction(new Disjunction(new Not(notDrifting),driftingFriction),ratioHighDrift),distanceLow), driftLeft));

        
        
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
