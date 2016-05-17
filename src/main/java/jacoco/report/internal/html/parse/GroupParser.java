package jacoco.report.internal.html.parse;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by nkalonia1 on 5/11/16.
 */
public class GroupParser extends RootParser {

    public GroupParser(final NewYAMLParser yaml) {
        super(yaml);
        key = "group";
    }

    @Override
    public NewParseItem parse(Map m) {
        if (m.containsKey(key)) {
            log("Found \"" + key + "\"");

            if (!verify(m)) return new InvalidParseItem();

            log("Parsing properties...");
            Object val;
            List l = (List) m.get(key);

            NewParseItem pi = parseRequired((Map) l.get(0));
            if (!pi.isValid()) return new InvalidParseItem();
            parseValues(m, pi);
            parsePropagate(m, pi);

            Iterator i = l.iterator();
            i.next();
            log("Parsing children...");
            GroupParser g = new GroupParser(yaml);
            while (i.hasNext()) {
                if ((val = i.next()) instanceof Map) {
                    NewParseItem c = g.parse((Map) val);
                    if (!c.isValid()) {
                        err("Invalid child. Skipping...");
                        continue;
                    }
                    pi.children.add(c);
                } else {
                    err("Child expected to be a Map. Skipping...");
                }
            }
            return pi;
        } else {
            return (new BundleParser(yaml)).parse(m);
        }
    }
}
