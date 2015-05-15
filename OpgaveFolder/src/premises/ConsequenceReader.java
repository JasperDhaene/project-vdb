package premises;

import fuzzy.Consequence;
import fuzzy.expression.Premise;
import fuzzy.membership.Membership;
import fuzzy.membership.PIFunction;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


/**
 * PremiseReader - deserialize from JSON
 * @author florian
 */
public class ConsequenceReader {

    private static final JSONParser parser = new JSONParser();

    public static Map<String, Consequence> read(String input)
            throws FileNotFoundException, IOException, ParseException {
        Map<String, Consequence> map = new HashMap<>();
        JSONArray array = (JSONArray) parser.parse(
                new BufferedReader(
                        new InputStreamReader(
                                ClassLoader.getSystemResourceAsStream("resources/" + input + ".json"))));
        array.forEach((o) -> {
            JSONObject object = (JSONObject) o;
            JSONArray params = (JSONArray) object.get("params");
            Membership mem = null;
            switch((String) object.get("type")) {
                case "trapezoid":
                    mem = new PIFunction.TrapezoidPIFunction(
                            ((Number) params.get(0)).doubleValue(),
                            ((Number) params.get(1)).doubleValue(),
                            ((Number) params.get(2)).doubleValue(),
                            ((Number) params.get(3)).doubleValue()
                    );
                    break;
                case "triangular":
                    mem = new PIFunction.TriangularPIFunction(
                            ((Number) params.get(0)).doubleValue(),
                            ((Number) params.get(1)).doubleValue(),
                            ((Number) params.get(2)).doubleValue()
                    );
                    break;
            }
            JSONArray integration = (JSONArray) object.get("integration");
            Consequence c = new Consequence(
                    (String) object.get("variable"),
                    mem,
                    ((Number) integration.get(0)).intValue(),
                    ((Number) integration.get(1)).intValue()
            );

            map.put((String) object.get("name"), c);
        });

        return map;
    }

}
