/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package examples;

import fuzzy.Consequence;
import fuzzy.Utils;
import fuzzy.membership.PIFunction;

/**
 *
 * @author Florian Dejonckheere <florian@floriandejonckheere.be>
 */
public class TestClass {

    public static void main(String[] args) {

        Consequence accelNone = new Consequence("acceleration",
                new PIFunction.TrapezoidPIFunction(0, 0, 0, 0), 0, 1600);

        System.out.println(accelNone.value(0.1));

        Utils.visualizeFunc(accelNone, 0, 1, 0.01);

    }

}
