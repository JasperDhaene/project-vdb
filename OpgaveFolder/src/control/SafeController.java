package control;

import car.VehicleProperties;
import fuzzy.FuzzySystem;
import fuzzy.expression.Premise;
import fuzzy.membership.PIFunction;

/**
 * SafeController - Controller that gets the job done as safe as possible
 * @author Florian Dejonckheere <florian@floriandejonckheere.be>
 */
public class SafeController implements Controller {

    private final FuzzySystem system;

    public SafeController() {
        this.system = new FuzzySystem();

        Premise acceleration = new Premise("acceleration", new PIFunction.TrapezoidPIFunction(0, 50, 60, 70));
        
    }

    @Override
    public FrameControl getFrameControl(VehicleProperties vp) {
        FrameControl fc;

        float steering = 0,
            acceleration = 0,
            brake = 0;

        /**
         * Steering
         */

        /**
         * Acceleration
         */


        /**
         * Brake
         */

        fc = new FrameControl(steering, acceleration, brake, vp.getScanAngle());

        return fc;
    }

}
