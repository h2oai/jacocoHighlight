package report;

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
}
