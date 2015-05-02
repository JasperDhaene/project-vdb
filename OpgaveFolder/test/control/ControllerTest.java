package control;

import car.VehicleProperties;
import java.util.HashMap;
import java.util.Map;

/**
 * ControllerTest - Simulate a controller's usage
 * @author Florian Dejonckheere <florian@floriandejonckheere.be>
 */
public abstract class ControllerTest {

    private final Controller controller;

    public ControllerTest(Controller controller) {
        this.controller = controller;
    }


    //public FrameControl getFrameControl(VehicleProperties vp) {
    public Map<String, Float> simulate(VehicleProperties vp) {
        FrameControl fc = this.controller.getFrameControl(vp);
        Map<String, Float> map = new HashMap<>();
        map.put("acceleration", fc.getAcceleration());
        map.put("brake", fc.getBrake());
        map.put("steering", fc.getSteering());

        return map;
    }
}
