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
import fuzzy.expression.Disjunction;
import fuzzy.expression.GreaterThanEqual;
import fuzzy.expression.LessThanEqual;
import fuzzy.expression.Not;
import fuzzy.expression.Premise;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.json.simple.parser.ParseException;
import premises.ConsequenceReader;
import premises.PremiseReader;

/**
 *
 * @author jasper
 */
public class SpeedRallySteering {

    public static void main(String[] args) throws IOException, FileNotFoundException, ParseException {
        FuzzySystem system = new FuzzySystem();

        Map<String, Premise> premises = PremiseReader.read("RallyPremises");
        Map<String, Consequence> consequences = ConsequenceReader.read("RallyConsequences");

        system.addRule(new Rule(new Conjunction(premises.get("ratioLowDrift"), new LessThanEqual(premises.get("speedMed"))),
                consequences.get("driftRight")));
        system.addRule(new Rule(new Conjunction(premises.get("ratioHighDrift"), new LessThanEqual(premises.get("speedMed"))),
                consequences.get("driftLeft")));

        double[] ratios = new double[]{0,0.2,0.5,0.75,0.8,1,1.25,1.5,2,2.5,3};


        // SPEED is LOW
        for(double d: ratios) {
            system.addInput("frontDistanceRatio", d);
            system.addInput("speed", 50);
            System.out.println(d + " => " + system.evaluate());
        }

    }
}
