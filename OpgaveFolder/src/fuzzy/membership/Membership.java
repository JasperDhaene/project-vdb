/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fuzzy.membership;

import org.apache.commons.math3.analysis.UnivariateFunction;

/**
 *
 * @author jasper
 */
public interface Membership extends UnivariateFunction{
    public double getUpperLimit();
    public double getLowerLimit();
}
