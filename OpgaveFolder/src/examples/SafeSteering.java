package examples;

import fuzzy.Consequence;
import fuzzy.FuzzySystem;
import fuzzy.Rule;
import fuzzy.expression.Premise;
import fuzzy.membership.PIFunction;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.json.simple.parser.ParseException;
import premises.ConsequenceReader;
import premises.PremiseReader;

/**
 *
 * @author Florian Dejonckheere <florian@floriandejonckheere.be>
 */
public class SafeSteering {

    public static void main(String[] args) throws IOException, FileNotFoundException, ParseException {
        FuzzySystem system = new FuzzySystem();

        Map<String, Premise> premises = PremiseReader.read("SafePremises");
        Map<String, Consequence> consequences = ConsequenceReader.read("SafeConsequences");

        // 4. Strive for a stable left/right ratio
        // RATIO = low => STEERING = right (high)
        system.addRule(new Rule(premises.get("ratioLow"),
                consequences.get("steerRight")));
        // RATIO = high => STEERING = left (low)
        system.addRule(new Rule(premises.get("ratioHigh"),
                consequences.get("steerLeft")));

        for(double d: new double[]{0,0.2,0.5,0.75,0.8,1,1.25,1.5,2,2.5,3}) {
            system.addInput("frontDistanceRatio", d);
            System.out.println(d + " => " + system.evaluate());
        }
    }

}
