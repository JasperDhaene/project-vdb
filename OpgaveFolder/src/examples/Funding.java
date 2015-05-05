package examples;

import fuzzy.Consequence;
import fuzzy.FuzzySystem;
import fuzzy.Rule;
import fuzzy.expression.Conjunction;
import fuzzy.expression.Disjunction;
import fuzzy.expression.Expression;
import fuzzy.expression.Premise;
import fuzzy.membership.PIFunction;
import fuzzy.norm.ZadehNorm;
import org.apache.commons.math3.analysis.function.Identity;

/**
 *
 * Project example
 *
 * http://petro.tanrei.ca/fuzzylogic/index.html
 *
 * @author Florian Dejonckheere <florian@floriandejonckheere.be>
 */
public class Funding {

    public static void main(String[] args) {

        FuzzySystem system = new FuzzySystem();

        // Funding
        PIFunction inadequate = new PIFunction.TrapezoidPIFunction(0, 0, 30, 40);
        PIFunction marginal = new PIFunction.TriangularPIFunction(30, 60, 90);
        PIFunction adequate = new PIFunction.TrapezoidPIFunction(70, 90, 100, 100);

        // Staffing
        PIFunction small = new PIFunction.TrapezoidPIFunction(0, 0, 30, 65);
        PIFunction large = new PIFunction.TrapezoidPIFunction(50, 70, 100, 100);

        Consequence c1 = new Consequence("riskLow", new PIFunction.TrapezoidPIFunction(30, 40, 100, 100), 0, 100);
        Consequence c2 = new Consequence("riskNormal", new PIFunction.TrapezoidPIFunction(30, 40, 100, 100), 0, 100);
        Consequence c3 = new Consequence("riskHigh", new PIFunction.TrapezoidPIFunction(30, 40, 100, 100), 0, 100);
        Expression e1 = new Disjunction(new Premise("funding", adequate), new Premise("staffing", small));
        Expression e2 = new Conjunction(new Premise("funding", marginal), new Premise("staffing", large));
        Expression e3 = new Premise("funding", inadequate);

        system.addRule(new Rule(e1, c1, new ZadehNorm()));
        system.addRule(new Rule(e2, c2, new ZadehNorm()));
        system.addRule(new Rule(e3, c3, new ZadehNorm()));

        system.addInput("funding", 40);
        system.addInput("staffing", 60);

        System.out.println(system.evaluate());
    }

}
