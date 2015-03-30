package fuzzy;

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
public class Main {

    public static void main(String[] args) {

        FuzzySystem system = new FuzzySystem();

        // Funding
        PIFunction inadequate = new PIFunction.TrapezoidPIFunction(0, 0, 30, 40);
        PIFunction marginal = new PIFunction.TriangularPIFunction(30, 60, 90);
        PIFunction adequate = new PIFunction.TrapezoidPIFunction(70, 90, 100, 100);

        // Staffing
        PIFunction small = new PIFunction.TrapezoidPIFunction(0, 0, 35, 63);
        PIFunction large = new PIFunction.TrapezoidPIFunction(45, 65, 100, 100);

        Consequence c = new Consequence(new Identity());
        Expression e1 = new Disjunction(new Premise("funding", adequate), new Premise("staffing", small));
        Expression e2 = new Conjunction(new Premise("funding", marginal), new Premise("staffing", large));
        Expression e3 = new Premise("funding", inadequate);

        system.addRule(new Rule(e1, c, new ZadehNorm()));
        system.addRule(new Rule(e2, c, new ZadehNorm()));
        system.addRule(new Rule(e3, c, new ZadehNorm()));

        system.addInput("funding", 35);
        system.addInput("staffing", 60);

        System.out.println(system.evaluate());
    }

}
