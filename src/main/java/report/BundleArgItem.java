package report;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by nkalonia1 on 6/15/16.
 */
public class BundleArgItem extends ArgItem<File> {
    private Collection<File> _classes;

    public BundleArgItem(ArgParams params) {
        super(params);
        _classes = new ArrayList<File>();
    }

    @Override
    public Type getType() {
        return Type.BUNDLE;
    }

    @Override
    public void addClass(Collection<File> l) {
        for (File f : l) {
            _classes.add(f);
        }
    }
}
