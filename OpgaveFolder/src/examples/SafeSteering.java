package examples;

import fuzzy.Consequence;
import fuzzy.FuzzySystem;
import fuzzy.Rule;
import fuzzy.expression.Premise;
import fuzzy.membership.PIFunction;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Florian Dejonckheere <florian@floriandejonckheere.be>
 */
public class SafeSteering {

    public static void main(String[] args) {
        FuzzySystem system = new FuzzySystem();

        Premise ratioLow = new Premise("frontDistanceRatio",
                new PIFunction.TrapezoidPIFunction(
                        -Double.MAX_VALUE,
                        -Double.MAX_VALUE,
                        -50, 20));
        Premise ratioHigh = new Premise("frontDistanceRatio",
                new PIFunction.TrapezoidPIFunction(
                        -20, 50,
                        Double.MAX_VALUE,
                        Double.MAX_VALUE));

        Consequence steerLeft = new Consequence("steering",
                new PIFunction.TriangularPIFunction(-1, -1, -0.5), -1, 1);
        Consequence steerRight = new Consequence("steering",
                new PIFunction.TriangularPIFunction(0.5, 1, 1), -1, 1);
        
        // 4. Strive for a stable left/right ratio
        // RATIO = low => STEERING = right (high)
        system.addRule(new Rule(ratioLow, steerRight));
        // RATIO = high => STEERING = left (low)
        system.addRule(new Rule(ratioHigh, steerLeft));

        List<Pair> input = new ArrayList<Pair>(){{
            // Straight on road
            add(new Pair(50, 50));
            
            // Front left is seeing a bend in the road
            add(new Pair(150, 50));
            
            // Against right outer wall
            add(new Pair(300, 30));
        }};
        
//        for(Pair p: input) {
//            system.addInput("frontDistanceRatio", (p.left - p.right));
//            System.out.println(p + " => " + system.evaluate());
//        }
        
        for(double d: new double[]{-150,-100,-50,0,50,100,150}) {
            system.addInput("frontDistanceRatio", d);
            System.out.println(d + " => " + system.evaluate());
        }
    }

}
