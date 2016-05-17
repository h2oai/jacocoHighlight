package jacoco.report.internal.html.parse;

import org.jacoco.core.analysis.ICoverageNode;
import java.util.Collection;

/**
 * Created by nkalonia1 on 3/27/16.
 */
public class InvalidParseItem extends NewParseItem{

    @Override
    public boolean matches(ICoverageNode name) { throw new IllegalStateException("ParseItem is invalid"); }

    @Override
    public boolean isDefined() { throw new IllegalStateException("ParseItem is invalid"); }

    @Override
    public boolean propagate() { throw new IllegalStateException("ParseItem is invalid"); }

    @Override
    public boolean hasValues() { throw new IllegalStateException("ParseItem is invalid"); }

    @Override
    public Collection<ICoverageNode.CounterEntity> getHeaders() { throw new IllegalStateException("ParseItem is invalid"); }

    @Override
    public double getValue(ICoverageNode.CounterEntity ce) { throw new IllegalStateException("ParseItem is invalid"); }

    @Override
    public Collection<NewParseItem> getChildren() { throw new IllegalStateException("ParseItem is invalid"); }

    @Override
    public boolean isLeaf() { throw new IllegalStateException("ParseItem is invalid"); }

    @Override
    public boolean isValid() { return false; }
}