package jacoco.report.internal.html.parse;

import jacoco.report.internal.html.parse.util.NameString;
import org.jacoco.core.analysis.ICoverageNode;

import java.util.*;

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
        key = "item";
    }

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
            parseType(m, pi);

            Iterator i = getChildIterator(m);
            log("Parsing children...");
            RootParser g = new RootParser(yaml);
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
            err("No key found named \"" + key + "\". Skipping...");
            return new InvalidParseItem();
        }
    }

    protected boolean verify(Map m) {
        Object val;
        if (!((val = m.get(key)) instanceof Map)) {
            err("\"" + key + "\" should map to a Map");
            return false;
        }
        return true;
    }

    protected NewParseItem parseRequired(Map m) {
        Object val;
        NewParseItem p;
        if ((val = m.get("name")) != null) {
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
        AntName name = new AntName();
        NewParseItem pi = new NewParseItem();
        for (Name.String_Field s : Name.String_Field.values()) {
            String n = s.toString().toLowerCase();
            if (m.containsKey(n) && m.get(n) instanceof String) {
                String string_name = (String) m.get("name");
                log("Found '" + n + "': " + string_name);
                name.put(new NameString(string_name), s);
            }
        }
        pi.setName(name);
        return pi;
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

    protected NewParseItem parseType(Map m, NewParseItem p) {
        Object val;
        if ((val = m.get("type")) != null) {
            log("Found \"type\"");
            if (val instanceof String) {
                for (NewParseItem.itemType type : NewParseItem.itemType.values()) {
                    if (type.toString().toLowerCase().equals(((String) val).toLowerCase())) {
                        p.type = type;
                        return p;
                    }
                }
                err("Invalid value for \"type\": " + val);
            }
            err("Invalid value for \"type\": " + val);
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
                p.setPropagate((Boolean) val);
            } else {
                err("Invalid value for \"propagate\": " + val);
            }
        }
        return p;
    }

    protected Iterator getChildIterator(Map m) {
        Object val;
        if ((val = m.get("children")) instanceof List) {
            log("Found \"children\"");
            return ((List) val).iterator();
        } else if (val instanceof Map) {
            log("Found \"children\"");
            List<Map> l = new ArrayList<Map>(1);
            l.add((Map) val);
            return l.iterator();
        } else {
            log("No children found");
            return (new ArrayList<Map>(0)).iterator();
        }
    }

    protected void log(String s) {
        if (yaml != null) yaml.log(s);
    }

    protected void err(String s) {
        if (yaml != null) yaml.err(s);
    }
}
