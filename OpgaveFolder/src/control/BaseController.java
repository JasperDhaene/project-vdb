package control;

import car.VehicleProperties;
import fuzzy.Consequence;
import fuzzy.FuzzySystem;
import fuzzy.expression.Premise;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;
import org.json.simple.parser.ParseException;
import premises.ConsequenceReader;
import premises.PremiseReader;

/**
 * Controller - Controller framework
 * @author florian
 */
public abstract class BaseController implements Controller {

    protected final FuzzySystem system;
    private boolean debug;

    private final Map<String, Premise> premises;
    private final Map<String, Consequence> consequences;

    public BaseController()
            throws IOException, FileNotFoundException, ParseException {
        this(false);
    }

    public BaseController(boolean debug)
            throws IOException, FileNotFoundException, ParseException {
        this.system = new FuzzySystem();
        this.debug = false;

        /**
         * Read in premises and consequences
         *
         * */
        String name = this.getClass().getSimpleName();
        this.premises = PremiseReader.read(name.replace("Controller", "Premises"));
        this.consequences = ConsequenceReader.read(name.replaceAll("Controller", "Consequences"));
    }

    @Override
    public FrameControl getFrameControl(VehicleProperties vp) {
        FrameControl fc;

        this.system.addInput("speed", vp.getCurrentCarSpeedKph());
        this.system.addInput("frontSensorDistance", vp.getDistanceFromFrontSensor());
        this.system.addInput("frontDistanceRatio",
                calcRatio(vp.getDistanceFromLeftSensor(), vp.getDistanceFromRightSensor()));
        this.system.addInput("lateralVelocity", vp.getLateralVelocity());
        this.system.addInput("frontLeftFriction", vp.getFrontLeftWheelFriction());
        this.system.addInput("frontRightFriction", vp.getFrontRightWheelFriction());
        this.system.addInput("backLeftFriction", vp.getBackLeftWheelFriction());
        this.system.addInput("backRightFriction", vp.getBackRightWheelFriction());
        Map<String, Double> output = this.system.evaluate();

        /**
         * Steering
         *
         */
        double steering = output.get("steering");
        // Dampen steering
        steering -= (steering - vp.getAngleFrontWheels()) / 8;

        /**
         * Acceleration
         *
         */
        double acceleration = output.get("acceleration");

        /**
         * Brake
         *
         */
        double brake = output.get("brake");

        /**
         * Scanangle
         *
         */
        double scanAngle = 0.9;

        /**
         * Debug output
         * 
         */
        if (this.debug) {
            System.out.println("acceleration: " + acceleration);
            System.out.println("speed: " + vp.getCurrentCarSpeedKph());
            System.out.println("brake: " + brake);
            System.out.println("ratio: " +
                    calcRatio(vp.getDistanceFromLeftSensor(), vp.getDistanceFromRightSensor()) +
                    " => " + steering);
            System.out.println("frontSensor: " + vp.getDistanceFromFrontSensor() +
                    " | " + "lateralVelocity: " + vp.getLateralVelocity() );
            System.out.println("frontFriction: " + vp.getFrontLeftWheelFriction() + " | " + vp.getFrontRightWheelFriction());
            System.out.println("backFriction: " + vp.getBackLeftWheelFriction() + " | " + vp.getBackRightWheelFriction());
            System.out.println("#######################");
        }

        fc = new FrameControl((float) steering,
                                (float) acceleration,
                                (float) brake,
                                scanAngle);

        return fc;
    }

    /**
     * Helper functions
     *
     * */
    protected Premise p(String key) { return this.premises.get(key); }
    protected Consequence c(String key) { return this.consequences.get(key); }

    /**
     * calcRatio - calculate correct distance ratio.
     *
     */
    private double calcRatio(double left, double right) {
        if(left == 0 && right == 0)
            return 1;

        if(left == 0)
            return Double.MAX_VALUE;

        if(right == 0)
            return -Double.MAX_VALUE;

        return (left / right);
    }

}
