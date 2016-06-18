package report;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import org.jacoco.core.tools.ExecFileLoader;

/**
 * Created by nkalonia1 on 6/17/16.
 */
public class ExecutionData {
    private Collection<File> execs;
    private ExecFileLoader efl;

    public ExecutionData(Collection<File> execs) {
        this.execs = execs;
    }

    public ExecFileLoader createLoader() throws IOException {
        if (efl == null) {
            efl = new ExecFileLoader();
            for (File f : execs) {
                efl.load(f);
            }
        }
        return efl;
    }

    public ExecutionData shallowCopy() {
        return new ExecutionData(execs);
    }

    public ExecutionData deepCopy() {
        return new ExecutionData(new ArrayList<File>(execs));
    }

    public Collection<File> getExecs() {
        return execs;
    }
}
