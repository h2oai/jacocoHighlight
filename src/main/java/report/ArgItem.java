package report;

import org.jacoco.report.IReportGroupVisitor;
import org.jacoco.report.IReportVisitor;

import java.io.IOException;
import java.util.Collection;

/**
 * Created by nkalonia1 on 6/15/16.
 */
public abstract class ArgItem<T> {
    enum Type { GROUP, BUNDLE, INVALID }

    protected ArgParams params;

    public ArgItem(ArgParams params) { this.params = params; }

    public Type getType() { return Type.INVALID; }

    public abstract void addClass(Collection<T> l);

    public abstract ExecutionData getAllExecs();

    public abstract void analyze(IReportGroupVisitor visitor) throws IOException;
}
