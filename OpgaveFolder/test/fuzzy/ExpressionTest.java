package fuzzy;

import fuzzy.expression.Conjunction;
import fuzzy.expression.Disjunction;
import fuzzy.expression.Expression;
import fuzzy.expression.Premise;
import fuzzy.norm.ZadehNorm;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.math3.analysis.function.Identity;
import static org.junit.Assert.assertEquals;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author Florian Dejonckheere <florian@floriandejonckheere.be>
 */
public class ExpressionTest {

    private static Map<String, Double> in;
    private static Map<String, Premise> p;

    @BeforeClass
    public static void setUpClass(){
        in = new HashMap<>();
        p = new HashMap<>();
        for(int i = 0; i < 10; i++){
            in.put("var" + i, (double) i / 10);
            p.put("var" + i, new Premise("var" + i, new Identity()));
        }
    }

    @Test
    public void testZadehConjunction(){
        Expression expr = new Conjunction(p.get("var3"), p.get("var7"));

        // Zadeh t-norm (conjunction) is min(a, b)
        assertEquals(0.3, expr.evaluate(new ZadehNorm(), in), 10^-10);
    }

    @Test
    public void testZadehDisjunction(){
        Expression expr = new Disjunction(p.get("var3"), p.get("var7"));

        // Zadeh t-conorm (conjunction) is max(a, b)
        assertEquals(0.7, expr.evaluate(new ZadehNorm(), in), 10^-10);
    }

    @Test
    public void testExtremelyComplicatedExpression(){
        Expression expr = new Conjunction(
                new Disjunction(p.get("var9"), p.get("var4")),
                new Conjunction(p.get("var5"), p.get("var3"))
            );

        assertEquals(0.3, expr.evaluate(new ZadehNorm(), in), 10^-10);
    }

}
