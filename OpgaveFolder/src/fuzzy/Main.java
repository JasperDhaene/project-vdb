package fuzzy;

import fuzzy.expression.Premise;
import fuzzy.membership.SFunction;
import fuzzy.norm.ZadehNorm;

/**
 * fuzzy.Main - Simulates a fuzzy rule-based system
 * @author Florian Dejonckheere <florian@floriandejonckheere.be>
 *
 */
public class Main {

	public static void main(String[] args) {
            /**
             * Setup: create a new fuzzy system and add rules and inputs
             *
             * http://petro.tanrei.ca/fuzzylogic/index.html
             *
             * Example: IF (temperature is "low") THEN (heater is "high")
             *
             */
	    FuzzySystem system = new FuzzySystem();
            Rule rule = new Rule(new Premise("temperature", new SFunction(20, 25)));
            rule.setConsequence(new Consequence(new SFunction(20, 25)));

            system.setNorm(new ZadehNorm());
            system.addRule(rule);

            system.addInput("temperature", 23);

            System.out.println("system => " + system.evaluate());

	}

}
