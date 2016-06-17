package report;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

/**
 * Created by nkalonia1 on 6/15/16.
 */
public class GroupArgItem extends ArgItem<ArgItem> {

    private Collection<ArgItem> _children;

    public GroupArgItem(ArgParams params) {
        super(params);
        _children = new ArrayList<ArgItem>();

    }

    @Override
    public Type getType() {
        return Type.GROUP;
    }

    @Override
    public void addClass(Collection<ArgItem> l) {
    }
}
