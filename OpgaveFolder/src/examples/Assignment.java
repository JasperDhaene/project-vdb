package examples;

import fuzzy.Consequence;
import fuzzy.FuzzySystem;
import fuzzy.Rule;
import fuzzy.Utils;
import fuzzy.expression.Conjunction;
import fuzzy.expression.Expression;
import fuzzy.expression.Premise;
import fuzzy.membership.PIFunction;
import java.util.HashMap;
import java.util.Map;

/**
 * Assignment - Example system in assignment
 * @author Florian Dejonckheere <florian@floriandejonckheere.be>
 */
public class Assignment {

    public static void main(String[] args) {
        FuzzySystem system = new FuzzySystem();

        Premise p1a = new Premise("var1", new PIFunction.TrapezoidPIFunction(0, 0, 15, 35));
        Premise p1b = new Premise("var2", new PIFunction.TrapezoidPIFunction(0, 0, 15, 35));
        Expression e1 = new Conjunction(p1a, p1b);
        Consequence c1 = new Consequence(new PIFunction.TrapezoidPIFunction(60, 85, 100, 100));
        Rule r1 = new Rule(e1, c1);

        Premise p2a = new Premise("var3", new PIFunction.TrapezoidPIFunction(0, 0, 15, 25));
        Premise p2b = new Premise("var4", new PIFunction.TriangularPIFunction(40, 50, 70));
        Expression e2 = new Conjunction(p2a, p2b);
        Consequence c2 = new Consequence(new PIFunction.TriangularPIFunction(30, 50, 70));
        Rule r2 = new Rule(e2, c2);

//        Utils.visualizeFunc(p2a.membership);

        system.addInput("var1", 25);
        system.addInput("var2", 30);
        system.addInput("var3", 15);
        system.addInput("var4", 47);

        system.addRule(r1);
        system.addRule(r2);

        System.out.println(system.evaluate());

        Map<String, Double> map = new HashMap<>();
        map.put("var1", 25.0);
        map.put("var2", 30.0);
        map.put("var3", 15.0);
        map.put("var4", 47.0);
        Consequence co1 = r1.evaluate(map);
        Consequence co2 = r2.evaluate(map);
//        System.out.println(co2.value(55));
//        System.out.println(co2.limit);
    }

}
