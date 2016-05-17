package jacoco.report.internal.html.parse;

import jacoco.report.internal.html.parse.util.NameList;
import jacoco.report.internal.html.parse.util.NameString;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by nkalonia1 on 5/11/16.
 */
public class ClassParser extends RootParser {
    private static final NameString _wild_name = new NameString("*");
    private static final NameList _wild_list = new NameList(new NameString[] {_wild_name}, true);


    public ClassParser(final NewYAMLParser yaml) {
        super(yaml);
        key = "class";
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
            MethodParser mp = new MethodParser(yaml);
            while (i.hasNext()) {
                if ((val = i.next()) instanceof Map) {
                    NewParseItem c = mp.parse((Map) val);
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
            NewParseItem child = (new MethodParser(yaml)).parse(m);
            if (!child.isValid()) return new InvalidParseItem();
            NewParseItem mi = createWildItem();
            mi.children.add(child);
            return mi;
        }
    }

    @Override
    protected NewParseItem createItem(Map m) {
        NameString name;
        NameString signature;
        NameString superclass;
        NameList interfaces;
        ClassItem ci = new ClassItem();
        if (m.containsKey("name") && m.get("name") instanceof String) {
            String string_name = (String) m.get("name");
            log("Found name: " + string_name);
            name = new NameString(string_name);
        } else {
            log("Did not find valid value for \"name\"");
            name = _wild_name;
        }
        if (m.containsKey("signature") && m.get("signature") instanceof String) {
            String string_sig = (String) m.get("signature");
            log("Found signature: " + string_sig);
            signature = new NameString(string_sig);
        } else {
            log("Did not find valid value for \"signature\"");
            signature = _wild_name;
        }
        if (m.containsKey("superclass") && m.get("superclass") instanceof String) {
            String string_super = (String) m.get("superclass");
            log("Found superclass: " + string_super);
            superclass = new NameString(string_super);
        } else {
            log("Did not find valid value for \"superclass\"");
            superclass = _wild_name;
        }
        if (m.containsKey("interfaces") && m.get("interfaces") instanceof String[]) {
            String[] string_inter = (String[]) m.get("interfaces");
            log("Found interfaces: " + Arrays.toString(string_inter));
            interfaces = new NameList(string_inter);
        } else {
            log("Did not find valid value for \"interfaces\"");
            interfaces = _wild_list;
        }
        ci.class_name = new ClassName(name, signature, superclass, interfaces);
        return ci;
    }
}
