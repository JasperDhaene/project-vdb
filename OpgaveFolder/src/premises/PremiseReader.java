package premises;

import fuzzy.expression.Premise;
import fuzzy.membership.Membership;
import fuzzy.membership.PIFunction;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
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

    public static Map<String, Premise> read(String filename)
            throws FileNotFoundException, IOException, ParseException {
        Map<String, Premise> map = new HashMap<>();
        JSONArray array = (JSONArray) parser.parse(new FileReader(filename));

        array.forEach((o) -> {
            JSONObject object = (JSONObject) o;
            JSONArray params = (JSONArray) object.get("params");
            Membership mem = null;
            switch((String) object.get("type")) {
                case "trapezoid":
                    mem = new PIFunction.TrapezoidPIFunction(
                            (Integer) params.get(0),
                            (Integer) params.get(1),
                            (Integer) params.get(2),
                            (Integer) params.get(3)
                    );
                    break;
                case "triangular":
                    mem = new PIFunction.TriangularPIFunction(
                            (Integer) params.get(0),
                            (Integer) params.get(1),
                            (Integer) params.get(2)
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
