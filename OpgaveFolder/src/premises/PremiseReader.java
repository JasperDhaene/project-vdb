package premises;

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
public class PremiseReader {

    private static final JSONParser parser = new JSONParser();

    public static Map<String, Premise> read(String input)
            throws FileNotFoundException, IOException, ParseException {
        Map<String, Premise> map = new HashMap<>();
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
                            (double) params.get(0),
                            (double) params.get(1),
                            (double) params.get(2)
                    );
                    break;
            }
            Premise p = new Premise(
                    (String) object.get("variable"),
                    mem
            );

            map.put((String) object.get("name"), p);
        });

        return map;
    }

}
