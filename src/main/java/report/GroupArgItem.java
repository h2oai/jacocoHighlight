package report;

import jacoco.report.internal.html.wrapper.CoverageWrapper;
import org.jacoco.core.analysis.Analyzer;
import org.jacoco.core.analysis.CoverageBuilder;
import org.jacoco.core.analysis.IBundleCoverage;
import org.jacoco.report.DirectorySourceFileLocator;
import org.jacoco.report.IReportGroupVisitor;
import org.jacoco.report.IReportVisitor;
import org.jacoco.report.MultiSourceFileLocator;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * Created by nkalonia1 on 6/15/16.
 */
public class GroupArgItem extends ArgItem<ArgItem> {

    private Collection<ArgItem> _children;

    public GroupArgItem(ArgParams params) {
        super(params);
        _children = new ArrayList<ArgItem>(0);

    }

    @Override
    public Type getType() {
        return Type.GROUP;
    }

    @Override
    public void addClass(Collection<ArgItem> l) {
        _children = new ArrayList<ArgItem>(l.size());
        for(ArgItem ai : l) {
            _children.add(ai);
        }
    }

    @Override
    public ExecutionData getAllExecs() {
        Set<File> execs = new HashSet<File>();
        for (ArgItem ai : _children) {
            execs.addAll(ai.getAllExecs().getExecs());
        }
        return new ExecutionData(execs);
    }

    @Override
    public void analyze(IReportGroupVisitor visitor) throws IOException {
        IReportGroupVisitor gi = visitor.visitGroup(params.title);
        for (ArgItem child : _children) {
            child.analyze(gi);
        }
    }
}
