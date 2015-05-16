package fuzzy.membership;

import org.apache.commons.math3.analysis.UnivariateFunction;

/**
 * Membership interface
 * @author jasper
 */
public interface Membership extends UnivariateFunction{
    /**
    * @return The mean value where the membership evaluates to 1
    * 
    * This is used in the evaluation of GreaterThanEqual or LessThanEqual expressions.
    */
    public double getLimitValue();
    
}
