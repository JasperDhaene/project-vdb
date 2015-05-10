package examples;

import fuzzy.Consequence;
import fuzzy.FuzzySystem;
import fuzzy.Rule;
import fuzzy.expression.Conjunction;
import fuzzy.expression.Not;
import fuzzy.expression.Premise;
import fuzzy.membership.PIFunction;
import fuzzy.norm.LukasiewicsNorm;
import fuzzy.norm.ProbabilisticNorm;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author jasper
 */
public class DriftSteering {

    public static void main(String[] args) {
        FuzzySystem system = new FuzzySystem();

        Premise ratioLeft = new Premise("leftRightDistanceRatio",
                new PIFunction.TrapezoidPIFunction(
                        -Double.MAX_VALUE,
                        -Double.MAX_VALUE,
                        -50, 5));
        Premise ratioRight = new Premise("leftRightDistanceRatio",
                new PIFunction.TriangularPIFunction(
                        -5, 50,
                        Double.MAX_VALUE));
        
        /*
        Premise ratioLeft = new Premise("leftRightDistanceRatio",
                new PIFunction.TrapezoidPIFunction(
                        -Double.MAX_VALUE,
                        -Double.MAX_VALUE,
                        -50, -10));
        Premise ratioRight = new Premise("leftRightDistanceRatio",
                new PIFunction.TriangularPIFunction(
                        10, 50,
                        Double.MAX_VALUE)); 
        */
        
        Premise distanceLow = new Premise("frontSensorDistance",
                new PIFunction.TrapezoidPIFunction(0, 0, 20, 40));
        Premise distanceMed = new Premise("frontSensorDistance",
                new PIFunction.TrapezoidPIFunction(30, 50, 80, 90));
        Premise distanceHigh = new Premise("frontSensorDistance",
                new PIFunction.TrapezoidPIFunction(80, 110, 130, 150));
        Premise distanceEndless = new Premise("frontSensorDistance",
                new PIFunction.TrapezoidPIFunction(140, 160, Integer.MAX_VALUE, Integer.MAX_VALUE));

        /*Consequence steerLeft = new Consequence("steering",
                new PIFunction.TriangularPIFunction(-1, -1, -0.5), -1, 0);
        Consequence steerRight = new Consequence("steering",
                new PIFunction.TriangularPIFunction(0.5, 1, 1), 0, 1);*/
        
        Consequence steerLeft = new Consequence("steering",
                new PIFunction.TrapezoidPIFunction(-1, -1, -0.45, -0.4), -1, 1);
        Consequence steerRight = new Consequence("steering",
                new PIFunction.TrapezoidPIFunction(0.4, 0.45, 1, 1), -1, 1);
        
        Consequence driftLeft = new Consequence("steering",
                new PIFunction.TrapezoidPIFunction(-1, -1, 0, 0.5), -1, 1);
        Consequence driftRight = new Consequence("steering",
                new PIFunction.TrapezoidPIFunction(-0.5, 0, 1, 1), -1, 1);
        
        system.addRule(new Rule(new Conjunction(ratioLeft,new Not(distanceLow)), steerRight,new ProbabilisticNorm()));
        // RATIO = high => STEERING = left (low)
        system.addRule(new Rule(new Conjunction(ratioRight,new Not(distanceLow)), steerLeft,new ProbabilisticNorm()));
        
        /* DRIFTING */
        system.addRule(new Rule(new Conjunction(ratioLeft,distanceLow), driftRight));
        // RATIO = high => STEERING = left (low)
        system.addRule(new Rule(new Conjunction(ratioRight,distanceLow), driftLeft));
        
        

        List<Pair> input = new ArrayList<Pair>(){{
            add(new Pair(10, 10));
            add(new Pair(10, 20));
            add(new Pair(10, 30));
            add(new Pair(10, 40));
            add(new Pair(10, 50));
            
            add(new Pair(15, 10));
            add(new Pair(15, 20));
            add(new Pair(15, 30));
            add(new Pair(15, 40));
            add(new Pair(15, 50));
            
            add(new Pair(20, 10));
            add(new Pair(20, 20));
            add(new Pair(20, 30));
            add(new Pair(20, 40));
            add(new Pair(20, 50));
        }};
        
        for(Pair p: input) {
            system.addInput("leftRightDistanceRatio", p.left );
            system.addInput("frontSensorDistance", p.right);
            System.out.println(p + " => " + system.evaluate());
        }
        
    }

}
