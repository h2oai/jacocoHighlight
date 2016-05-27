package jacoco.report.internal.html.parse;

import jacoco.report.internal.html.parse.util.NameString;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by nkalonia1 on 5/11/16.
 */
public class BundleParser extends RootParser {

    public BundleParser(final NewYAMLParser yaml) {
        super(yaml);
        key = "bundle";
    }

    @Override
    public NewParseItem parse(Map m) {
        if (m.containsKey(key)) {
            log("Found \"" + key + "\"");

            if (!verify(m)) return new InvalidParseItem();

            log("Parsing properties...");
            Object val;
            m = (Map) m.get(key);

            NewParseItem pi = parseRequired(m);
            if (!pi.isValid()) return new InvalidParseItem();
            parseValues(m, pi);
            parsePropagate(m, pi);

            Iterator i = getChildIterator(m);
            log("Parsing children...");
            PackageParser p = new PackageParser(yaml);
            while (i.hasNext()) {
                if ((val = i.next()) instanceof Map) {
                    NewParseItem c = p.parse((Map) val);
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
            NewParseItem child = (new PackageParser(yaml)).parse(m);
            if (!child.isValid()) return new InvalidParseItem();
            NewParseItem bi = createWildItem();
            bi.children.add(child);
            return bi;
        }
    }

    @Override
    protected NewParseItem createItem(Map m) {
        NameString name;
        BundleItem bi = new BundleItem();
        if (m.containsKey("name") && m.get("name") instanceof String) {
            String string_name = (String) m.get("name");
            log("Found name: " + string_name);
            name = new NameString(string_name);
            bi.bundle_name = new BundleName(name);
        }
        return bi;
    }
}
