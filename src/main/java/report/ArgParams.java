package report;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by nkalonia1 on 6/17/16.
 */
public class ArgParams {
    String title;
    Path root;
    Collection<File> execs;
    Collection<File> sources;

    public ArgParams shallowCopy() {
        ArgParams copy = new ArgParams();
        copy.title = title;
        copy.root = root;
        copy.execs = execs;
        copy.sources = sources;
        return copy;
    }

    public ArgParams deepCopy() {
        ArgParams copy = new ArgParams();
        copy.title = title;
        copy.root = root;
        copy.execs = new ArrayList<File>(execs);
        copy.sources = new ArrayList<File>(sources);
        return copy;
    }
}
