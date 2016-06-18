package report;

import org.jacoco.report.IReportGroupVisitor;
import org.jacoco.report.IReportVisitor;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by nkalonia1 on 6/17/16.
 */
public class InvalidArgItem extends ArgItem {
    public InvalidArgItem() {
        super(null);
    }

    @Override
    public void addClass(Collection l) {
        undefined();
    }

    @Override
    public ExecutionData getAllExecs() {
        undefined();
        return new ExecutionData(new ArrayList<File>(0));
    }

    @Override
    public void analyze(IReportGroupVisitor visitor) {
        undefined();
    }

    private void undefined() {
        throw new IllegalStateException("ArgItem is invalid");
    }
}
