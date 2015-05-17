package fuzzy.transformation;

import fuzzy.membership.Membership;

/**
 * SymmetricFunction - returns the symmetric equivalent
 * @author Florian Dejonckheere <florian@floriandejonckheere.be>
 */
public class Symmetric implements Membership {

    private final Membership f;
    private final double axis;

    public Symmetric(Membership f) {
        this.f = f;
        this.axis = 0;
    }

    public Symmetric(Membership f, double axis) {
        this.f = f;
        this.axis = axis;
    }

    @Override
    public double value(double value) {
        return f.value(axis - value);
    }

    //TODO: gebruik en nut?
    @Override
    public double getLimitValue() {
        return (axis - f.getLimitValue());
    }


}
