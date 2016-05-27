package jacoco.report.internal.html.parse;

import org.jacoco.core.analysis.ICoverageNode;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Created by nkalonia1 on 3/27/16.
 */
public abstract class NewParseItem {

    public enum itemType {
        INVALID, GROUP, BUNDLE, PACKAGE, CLASS, METHOD
    }

    List<NewParseItem> children;
    boolean propagate;
    Map<ICoverageNode.CounterEntity, Double> values;

    public NewParseItem() {
        propagate = false;
        children = new ArrayList<NewParseItem>();
    }

    public abstract boolean matches(ICoverageNode name);

    public abstract boolean isDefined();

    public boolean propagate() { return propagate; }

    public boolean hasValues() { return !(values == null || values.isEmpty()); }

    public Collection<ICoverageNode.CounterEntity> getHeaders() {
        return values.keySet();
    }

    public double getValue(ICoverageNode.CounterEntity ce) {
        return values.get(ce);
    }

    public Collection<NewParseItem> getChildren() {
        return children;
    }

    public boolean isLeaf() {
        return children == null || children.isEmpty();
    }

    public boolean isValid() { return true; }

    public abstract itemType getType();

}