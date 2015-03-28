package fuzzy;

import fuzzy.expression.Premise;
import fuzzy.membership.PIFunction;
import fuzzy.norm.ZadehNorm;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.math3.analysis.function.Identity;

/**
 *
 * @author Florian Dejonckheere <florian@floriandejonckheere.be>
 */
public class SpeedController {

    public static void main(String[] args) {
        FuzzySystem system = new FuzzySystem();

        Premise speed = new Premise("speed", new PIFunction.TrapezoidPIFunction(0, 0, 70, 80));
        Consequence accel = new Consequence(new Identity());

        Rule notTooFast = new Rule(speed, accel);

        Map<String, Double> map = new HashMap<>();
        map.put("speed", 40.0);
        System.out.println(notTooFast.evaluate(map));

        system.addRule(notTooFast);
        system.addInput("speed", 50);
        System.out.println(system.evaluate());
    }

}
