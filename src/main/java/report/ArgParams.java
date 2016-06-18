package report;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by nkalonia1 on 6/17/16.
 */
public class ArgParams {
    String title;
    Path root;
    ExecutionData execs;
    Collection<File> sources;

    public ArgParams() {
        title = "";
        root = Paths.get(System.getProperty("user.dir"));
        execs = new ExecutionData(new ArrayList<File>(0));
        sources = new ArrayList<File>(0);
    }

    public ArgParams shallowCopy() {
        ArgParams copy = new ArgParams();
        copy.title = title;
        copy.root = root;
        copy.execs = execs.shallowCopy();
        copy.sources = sources;
        return copy;
    }

    public ArgParams deepCopy() {
        ArgParams copy = new ArgParams();
        copy.title = title;
        copy.root = root;
        copy.execs = execs.deepCopy();
        copy.sources = new ArrayList<File>(sources);
        return copy;
    }
}
