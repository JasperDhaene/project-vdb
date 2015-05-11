package control;

import car.VehicleProperties;
import fuzzy.Consequence;
import fuzzy.FuzzySystem;
import fuzzy.Rule;
import fuzzy.expression.Conjunction;
import fuzzy.expression.Disjunction;
import fuzzy.expression.Not;
import fuzzy.expression.Premise;
import fuzzy.membership.PIFunction;
import fuzzy.norm.ProbabilisticNorm;
import java.util.Map;

/**
 * RallyController - Controller that gets the job done as awesome as possible
 * @author Florian Dejonckheere <florian@floriandejonckheere.be>
 */
public class RallyController implements Controller {

   private final FuzzySystem system;

    public RallyController() {
        this.system = new FuzzySystem();
        Premise speedLow = new Premise("speed",
                new PIFunction.TrapezoidPIFunction(0, 0, 30, 50));
        Premise speedMed = new Premise("speed",
                new PIFunction.TrapezoidPIFunction(40, 60, 80, 100));
        Premise speedHigh = new Premise("speed",
                new PIFunction.TrapezoidPIFunction(90, 110, 130, 150));
        Premise speedNitro = new Premise("speed",
                new PIFunction.TrapezoidPIFunction(140, 210, 230, 280));
        Premise speedInsane = new Premise("speed",
                new PIFunction.TrapezoidPIFunction(270, 290, 310, 400));
        
        Premise distanceVeryLow = new Premise("frontSensorDistance",
                new PIFunction.TrapezoidPIFunction(0, 0, 10, 15));
        Premise distanceLow = new Premise("frontSensorDistance",
                new PIFunction.TrapezoidPIFunction(0, 0, 30, 40));
        Premise distanceMed = new Premise("frontSensorDistance",
                new PIFunction.TrapezoidPIFunction(40, 50, 80, 90));
        Premise distanceHigh = new Premise("frontSensorDistance",
                new PIFunction.TrapezoidPIFunction(80, 110, 130, 150));
        Premise distanceEndless = new Premise("frontSensorDistance",
                new PIFunction.TrapezoidPIFunction(140, 160, Integer.MAX_VALUE, Integer.MAX_VALUE));
        
        Premise ratioLow = new Premise("leftRightDistanceRatio",
                new PIFunction.TrapezoidPIFunction(
                        -Double.MAX_VALUE,
                        -Double.MAX_VALUE,
                        -50, -15));
        Premise ratioHigh = new Premise("leftRightDistanceRatio",
                new PIFunction.TrapezoidPIFunction(
                        15, 50,
                        Double.MAX_VALUE,
                        Double.MAX_VALUE));
        
        Premise ratioMiddle = new Premise("leftRightDistanceRatio",
                new PIFunction.TrapezoidPIFunction(
                        -20,
                        -15,
                        15, 20));
        
        Premise ratioLowDrift = new Premise("leftRightDistanceRatio",
                new PIFunction.TrapezoidPIFunction(
                        -Double.MAX_VALUE,
                        -Double.MAX_VALUE,
                        -20, -1));
        Premise ratioHighDrift = new Premise("leftRightDistanceRatio",
                new PIFunction.TrapezoidPIFunction(
                        1,
                        20,
                        Double.MAX_VALUE, Double.MAX_VALUE));
        
        Premise lateralVelocityLow = new Premise("lateralVelocity",
                new PIFunction.TriangularPIFunction(
                        -3, 0,
                        3));
        
        Premise notDrifting = new Premise("lateralVelocity",
                new PIFunction.TriangularPIFunction(
                        -0.3, 0,
                        0.3));
        Premise noFrontLeftFriction = new Premise("frontLeftFriction",
                new PIFunction.TrapezoidPIFunction(
                        0,
                        0.1,
                        Double.MAX_VALUE, Double.MAX_VALUE));
        Premise noBackLeftFriction = new Premise("backLeftFriction",
                new PIFunction.TrapezoidPIFunction(
                        0,
                        0.1,
                        Double.MAX_VALUE, Double.MAX_VALUE));
        Premise noFrontRightFriction = new Premise("frontRightFriction",
                new PIFunction.TrapezoidPIFunction(
                        0,
                        0.1,
                        Double.MAX_VALUE, Double.MAX_VALUE));
        Premise noBackRightFriction = new Premise("backRightFriction",
                new PIFunction.TrapezoidPIFunction(
                        0,
                        0.1,
                        Double.MAX_VALUE, Double.MAX_VALUE));
        
        /////////////////////////////////////
        
        Consequence accelLow = new Consequence("acceleration",
                new PIFunction.TrapezoidPIFunction(0, 0, 280, 560), 0, 1600);
        Consequence accelMed = new Consequence("acceleration",
                new PIFunction.TrapezoidPIFunction(480, 660, 840, 1120), 0, 1600);
        Consequence accelHigh = new Consequence("acceleration",
                new PIFunction.TrapezoidPIFunction(840, 1120, 1200, 1400), 0, 1600);
        Consequence accelNitro = new Consequence("acceleration",
                new PIFunction.TrapezoidPIFunction(1200, 1400, 1600, 1600), 0, 1600);
        
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
        /*
        Consequence steerLeft = new Consequence("steering",
                new PIFunction.TriangularPIFunction(-1, -1, -0.5), -1, 1);
        Consequence steerRight = new Consequence("steering",
                new PIFunction.TriangularPIFunction(0.5, 1, 1), -1, 1);
        */
        Consequence steerLeft = new Consequence("steering",
                new PIFunction.TrapezoidPIFunction(-1, -1, -0.45, -0.4), -1, 1);
        Consequence steerRight = new Consequence("steering",
                new PIFunction.TrapezoidPIFunction(0.4, 0.45, 1, 1), -1, 1);
        /*
        Consequence driftLeft = new Consequence("steering",
                new PIFunction.TrapezoidPIFunction(-1, -1, 0, 0.5), -1, 1);
        Consequence driftRight = new Consequence("steering",
                new PIFunction.TrapezoidPIFunction(-0.5, 0, 1, 1), -1, 1);
        */
        Consequence driftLeft = new Consequence("steering",
                new PIFunction.TriangularPIFunction(-1, -1, -0.5), -1, 1);
        Consequence driftRight = new Consequence("steering",
                new PIFunction.TriangularPIFunction(0.5, 1, 1), -1, 1);
        
        /* ACCEL */
        // (SPEED = low \/ med \/ high) /\ (DISTANCE = high \/ endless) => ACCEL = nitro
        system.addRule(new Rule(new Conjunction(new Disjunction(new Disjunction(speedLow,speedMed),speedHigh),new Disjunction(distanceHigh,distanceEndless)), accelHigh));
        // (SPEED = nitro /\ DISTANCE = endless) /\ RATIO = middle  => ACCEL = med
        //system.addRule(new Rule(new Conjunction(new Conjunction(speedNitro,distanceEndless),notDrifting), accelLow));
        // DISTANCE = low /\ SPEED = low => ACCEL = low
        system.addRule(new Rule(new Conjunction(new Disjunction(speedLow,speedMed), distanceLow), accelLow));
        
        /* BRAKE */
        // (SPEED = high \/ nitro) /\ (DISTANCE = low \/ med) => BRAKE = med
        system.addRule(new Rule(new Conjunction(new Disjunction(speedHigh,speedNitro),new Disjunction(distanceLow,distanceMed)), brakeHigh));
        // (SPEED = nitro \/ insane) /\ (DISTANCE = low \/ med \/ high) => BRAKE = extreme
        system.addRule(new Rule(new Conjunction(new Disjunction(speedNitro,speedInsane),new Disjunction(new Disjunction(distanceLow,distanceMed),distanceHigh)), brakeExtreme));
        // RATIO = middle /\ (SPEED = nitro \/ insane => BRAKE = extreme
        system.addRule(new Rule(new Conjunction(ratioMiddle,new Disjunction(speedNitro,speedInsane)), brakeExtreme));
        
        /* STEERING */
        // RATIO = low => STEERING = right (high)
        system.addRule(new Rule(new Conjunction(ratioLow,new Not(new Disjunction(speedNitro,speedInsane))), steerRight));
        // RATIO = high => STEERING = left (low)
        system.addRule(new Rule(new Conjunction(ratioHigh,new Not(new Disjunction(speedNitro,speedInsane))), steerLeft));
        
        system.addRule(new Rule(new Conjunction(ratioLow,new Not(distanceLow)), steerRight));
        // RATIO = high => STEERING = left (low)
        system.addRule(new Rule(new Conjunction(ratioHigh,new Not(distanceLow)), steerLeft));
        
        /* DRIFTING */
        
//STEER IN TURN      
        // RATIO = low /\ DISTANCE = low /\ FRICTION on front wheels (not drifting out of control) => DRIFT = left
        system.addRule(new Rule(new Conjunction(new Conjunction(ratioLowDrift,distanceLow),new Conjunction(new Not(noFrontLeftFriction),new Not(noFrontRightFriction))), driftRight));
        // RATIO = high /\ DISTANCE = low /\ FRICTION on front wheels (not drifting out of control) => DRIFT = right
        system.addRule(new Rule(new Conjunction(new Conjunction(ratioHighDrift,distanceLow),new Conjunction(new Not(noFrontLeftFriction),new Not(noFrontRightFriction))), driftLeft));

//ACCEL WHILE DRIFT        
        // (DISTANCE = low \/ med) /\ (SPEED = high) /\ Drifting => ACCEL = med
        system.addRule(new Rule(new Conjunction(new Disjunction(distanceLow,distanceMed ),new Not(notDrifting)), accelMed));
        // In horizontal drift, avoid crash by nitro accel to gain grip and turn vertical //TODO: nitro is overkill but high doesn't work with base accel rules
        system.addRule(new Rule(new Conjunction(distanceVeryLow,new Not(notDrifting)), accelNitro));
//OVERSTEERING       
        // noRightWheelfriction /\ STEERING = ratioLowDrift /\ DISTANCE = low  => DRIFT = right
        system.addRule(new Rule(new Conjunction(new Conjunction(new Conjunction(noFrontRightFriction,noBackRightFriction),ratioLowDrift),distanceLow), driftRight));
        
        // noLeftWheelfriction /\ STEERING = ratioHighDrift /\ DISTANCE = low  => DRIFT = left
        system.addRule(new Rule(new Conjunction(new Conjunction(new Conjunction(noFrontLeftFriction,noBackLeftFriction),ratioHighDrift),distanceLow), driftLeft));

//BRAKE
        // (DISTANCE = low \/ med) /\ (SPEED = high \/ nitro) => BRAKE = high
        system.addRule(new Rule(new Conjunction(new Conjunction(new Disjunction(speedHigh,speedNitro),new Disjunction(distanceLow,distanceMed)),lateralVelocityLow), brakeHigh));
        // (DISTANCE = low \/ med) /\ (SPEED = high \/ nitro) => BRAKE = high
        system.addRule(new Rule(new Conjunction(new Conjunction(new Disjunction(speedHigh,speedNitro),new Disjunction(distanceLow,distanceMed)),new Not(lateralVelocityLow)), brakeNone));

        
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
        scanAngle = 0.9;
        
        /**
         * Debug output
         */
        
        System.out.println("acceleration: " + acceleration);
        System.out.println("speed: " + vp.getCurrentCarSpeedKph());
        System.out.println("brake: " + brake);
        System.out.println("ratio: " + 
                (vp.getDistanceFromLeftSensor() - vp.getDistanceFromRightSensor()) + 
                " => " + steering);
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
