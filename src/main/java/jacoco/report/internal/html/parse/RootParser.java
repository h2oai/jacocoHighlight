package jacoco.report.internal.html.parse;

import jacoco.report.internal.html.parse.util.NameString;
import org.jacoco.core.analysis.ICoverageNode;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by nkalonia1 on 5/11/16.
 */
public class RootParser {

    protected String key;
    protected final NewYAMLParser yaml;

    public RootParser() {
        this(null);
    }

    public RootParser(final NewYAMLParser yaml) {
        this.yaml = yaml;
        key = "group";
    }

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
            while(i.hasNext()) {
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
            NewParseItem child = (new BundleParser(yaml)).parse(m);
            if (!child.isValid()) return new InvalidParseItem();
            NewParseItem pi = createWildItem();
            pi.children.add(child);
            return pi;
        }
    }

    protected boolean verify(Map m) {
        Object val;
        if (!((val = m.get("group")) instanceof List)) {
            err("\"" + key + "\" should map to a List");
            return false;
        }
        List l = (List) val;
        if (l.size() < 1) {
            err("List is empty");
            return false;
        }
        log("Checking for properties...");
        if (!(l.get(0) instanceof Map)) {
            err("Properties is not a map");
            return false;
        }
        return true;
    }

    protected NewParseItem parseRequired(Map m) {
        Object val;
        NewParseItem p;
        if ((val = m.get("name")) != null) {
            log("Found \"name\"");
            if (val instanceof Map) {
                p = createItem((Map) val);
            } else if (val instanceof String) {
                p = createItem((String) val);
            } else {
                err("Invalid value for \"name\": " + val);
                return new InvalidParseItem();
            }
        } else {
            err("Required property \"name\" not found");
            return new InvalidParseItem();
        }
        return p;
    }

    protected NewParseItem createItem(Map m) {
        NameString name;
        GroupItem gi = new GroupItem();
        if (m.containsKey("name") && m.get("name") instanceof String) {
            String string_name = (String) m.get("name");
            log("Found name: " + string_name);
            name = new NameString(string_name);
            gi.group_name = new GroupName(name);
        }
        return gi;
    }

    protected NewParseItem createItem(String s) {
        Map<String, String> m = new HashMap<String, String>(1, 1);
        m.put("name", s);
        return createItem(m);
    }

    protected NewParseItem createWildItem() {
        return createItem("*");
    }

    protected NewParseItem parseValues(Map m, NewParseItem p) {
        Object val;
        if ((val = m.get("values")) != null) {
            log("Found \"values\"");
            if (val instanceof Map) {
                p.values = getValues((Map) val);
            } else if (val instanceof Number) {
                p.values = getValues((Number) val);
            } else {
                err("Invalid value for \"values\"");
            }
        }
        return p;
    }

    protected Map<ICoverageNode.CounterEntity, Double> getValues(Map m) {
        HashMap<ICoverageNode.CounterEntity, Double> v = new HashMap<ICoverageNode.CounterEntity, Double>(yaml.getHeaders().size(), 1);
        for (ICoverageNode.CounterEntity ce : yaml.getHeaders()) {
            String key = ce.name().toLowerCase();
            if (m.get(key) instanceof Number) {
                v.put(ce, ((Number) m.get(key)).doubleValue());
            }
            log("For \"" + key + "\": " + v.get(ce));
        }
        return v;
    }

    protected Map<ICoverageNode.CounterEntity, Double> getValues(Number n) {
        Map<String, Number> m = new HashMap<String, Number>(yaml.getHeaders().size(), 1);
        for (ICoverageNode.CounterEntity ce : yaml.getHeaders()) {
            m.put(ce.name().toLowerCase(), n);
        }
        return getValues(m);
    }

    protected NewParseItem parsePropagate(Map m, NewParseItem p) {
        Object val;
        if ((val = m.get("propagate")) != null) {
            log("Found \"propagate\"");
            if (val instanceof Boolean) {
                p.propagate = (boolean) val;
            } else {
                err("Invalid value for \"propagate\": " + val);
            }
        }
        return p;
    }

    protected void log(String s) {
        if (yaml != null) yaml.log(s);
    }

    protected void err(String s) {
        if (yaml != null) yaml.err(s);
    }
}
