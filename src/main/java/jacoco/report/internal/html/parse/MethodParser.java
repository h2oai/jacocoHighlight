package jacoco.report.internal.html.parse;

import jacoco.report.internal.html.parse.util.NameString;

import java.util.List;
import java.util.Map;

/**
 * Created by nkalonia1 on 5/11/16.
 */
public class MethodParser extends RootParser {
    private static final NameString _wild_name = new NameString("*");

    public MethodParser(final NewYAMLParser yaml) {
        super(yaml);
        key = "method";
    }

    @Override
    public NewParseItem parse(Map m) {
        if (m.containsKey(key)) {
            log("Found \"" + key + "\"");

            if (!verify(m)) return new InvalidParseItem();

            log("Parsing properties...");
            List l = (List) m.get(key);

            NewParseItem pi = parseRequired((Map) l.get(0));
            if (!pi.isValid()) return new InvalidParseItem();
            parseValues(m, pi);
            parsePropagate(m, pi);
            return pi;
        } else {
            err("No valid key found: " + m.keySet());
            return new InvalidParseItem();
        }
    }

    @Override
    protected NewParseItem createItem(Map m) {
        NameString name;
        NameString desc;
        NameString signature;
        MethodItem mi = new MethodItem();
        if (m.containsKey("name") && m.get("name") instanceof String) {
            String string_name = (String) m.get("name");
            log("Found name: " + string_name);
            name = new NameString(string_name);
        } else {
            log("Did not find valid value for \"name\"");
            name = _wild_name;
        }
        if (m.containsKey("description") && m.get("description") instanceof String) {
            String string_super = (String) m.get("description");
            log("Found description: " + string_super);
            desc = new NameString(string_super);
        } else {
            log("Did not find valid value for \"description\"");
            desc = _wild_name;
        }
        if (m.containsKey("signature") && m.get("signature") instanceof String) {
            String string_sig = (String) m.get("signature");
            log("Found signature: " + string_sig);
            signature = new NameString(string_sig);
        } else {
            log("Did not find valid value for \"signature\"");
            signature = _wild_name;
        }
        mi.method_name = new MethodName(name, desc, signature);
        return mi;
    }
}
