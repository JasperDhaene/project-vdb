/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package examples;

import fuzzy.Consequence;
import fuzzy.FuzzySystem;
import fuzzy.Rule;
import fuzzy.expression.Conjunction;
import fuzzy.expression.GreaterThanEqual;
import fuzzy.expression.LessThanEqual;
import fuzzy.expression.Premise;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;
import org.json.simple.parser.ParseException;
import premises.ConsequenceReader;
import premises.PremiseReader;

/**
 *
 * @author jasper
 */
public class SpeedSteering {
        public static void main(String[] args) throws IOException, FileNotFoundException, ParseException {
        FuzzySystem system = new FuzzySystem();

        Map<String, Premise> premises = PremiseReader.read("SpeedPremises");
        Map<String, Consequence> consequences = ConsequenceReader.read("SpeedConsequences");

        system.addRule(new Rule(new Conjunction(premises.get("ratioLow"), new LessThanEqual(premises.get("speedMed"))),
                consequences.get("steerRight")));
        system.addRule(new Rule(new Conjunction(premises.get("ratioHigh"), new LessThanEqual(premises.get("speedMed"))),
                consequences.get("steerLeft")));

        system.addRule(new Rule(new Conjunction(premises.get("ratioLow"), new GreaterThanEqual(premises.get("speedHigh"))),
                consequences.get("steerGentleRight")));
        system.addRule(new Rule(new Conjunction(premises.get("ratioHigh"), new GreaterThanEqual(premises.get("speedHigh"))),
                consequences.get("steerGentleLeft")));

        double[] ratios = new double[]{0,0.2,0.5,0.75,0.8,1,1.25,1.5,2,2.5,3};


        // SPEED is LOW
        for(double d: ratios) {
            system.addInput("frontDistanceRatio", d);
            system.addInput("speed", 50);
            System.out.println(d + " => " + system.evaluate());
        }
        System.out.println("===========================");
        // SPEED is HIGH
        for(double d: ratios) {
            system.addInput("frontDistanceRatio", d);
            system.addInput("speed", 200);
            System.out.println(d + " => " + system.evaluate());
        }
    }
}
