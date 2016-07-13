package jacoco.report.internal.html.parse;

import org.jacoco.core.analysis.ICoverageNode;

import java.util.*;

/**
 * Created by nkalonia1 on 3/27/16.
 */
public class NewParseItem {

    public enum itemType {
        INVALID, ANY, GROUP, BUNDLE, PACKAGE, CLASS, METHOD
    }

    NewParseItem parent;
    List<NewParseItem> children;
    boolean propagate = false;
    boolean propagate_set = false;
    Name name;
    Map<ICoverageNode.CounterEntity, Double> values;
    itemType type;

    public NewParseItem() {
        this(itemType.ANY);
    }

    public NewParseItem(itemType type) {
        this.type = type;
        Name name = new InvalidAntName();
        children = new LinkedList<NewParseItem>();
        values = new HashMap<ICoverageNode.CounterEntity, Double>(6,1);
    }

    public boolean matches(ICoverageNode n) { return name.matches(NameCreator.create(n)); }

//    public abstract boolean isDefined();

    public boolean propagate() {
        if (propagate_set) return propagate;
        return setPropagate(!isRoot() && parent.propagate());
    }

    public boolean hasValue(ICoverageNode.CounterEntity ce) { return values.containsKey(ce); }

    public double getValue(ICoverageNode.CounterEntity ce) {
        if (hasValue(ce)) return values.get(ce);
        double value = 0;
        if (!isRoot() && parent.propagate()) {
            value = parent.getValue(ce);
        }
        values.put(ce, value);
        return value;
    }

    public Collection<NewParseItem> getChildren() {
        return children;
    }

    public boolean isLeaf() {
        return children == null || children.isEmpty();
    }

    public boolean isRoot() { return parent == null || !parent.isValid(); }

    public boolean isValid() { return true; }

    public itemType getType() { return type; }

    public void setParent(NewParseItem pi) {
        parent = pi;
    }

    boolean matchesType(ICoverageNode.ElementType et) {
        if (getType() == itemType.ANY) return true;
        switch (et) {
            case METHOD:
                return getType() == itemType.METHOD;
            case CLASS:
                return getType() == itemType.CLASS;
            case PACKAGE:
                return getType() == itemType.PACKAGE;
            case BUNDLE:
                return getType() == itemType.BUNDLE;
            case GROUP:
                return getType() == itemType.GROUP;
            default:
                return false;
        }
    }

    boolean setPropagate(boolean propagate) {
        this.propagate = propagate;
        this.propagate_set = true;
        return propagate;
    }

    void setName(Name n) {
        name = n;
    }

}