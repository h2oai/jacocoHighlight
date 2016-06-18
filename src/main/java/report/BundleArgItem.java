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
import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by nkalonia1 on 6/15/16.
 */
public class BundleArgItem extends ArgItem<File> {
    private Collection<File> _classes;

    public BundleArgItem(ArgParams params) {
        super(params);
        _classes = new ArrayList<File>(0);
    }

    @Override
    public Type getType() {
        return Type.BUNDLE;
    }

    @Override
    public void addClass(Collection<File> l) {
        _classes = new ArrayList<File>(l.size());
        for (File f : l) {
            _classes.add(f);
        }
    }

    @Override
    public ExecutionData getAllExecs() {
        return params.execs;
    }

    @Override
    public void analyze(IReportGroupVisitor visitor) throws IOException {
        MultiSourceFileLocator src_dir = new MultiSourceFileLocator(4);
        for(File dir : params.sources) {
            src_dir.add(new DirectorySourceFileLocator(
                    dir, "utf-8", 4));
        }

        CoverageBuilder coverageBuilder = new CoverageBuilder();
        Analyzer analyzer = new Analyzer(
                params.execs.createLoader().getExecutionDataStore(), coverageBuilder);
        for (File c : _classes) {
            analyzer.analyzeAll(c);
        }
        IBundleCoverage bundle = CoverageWrapper.wrapBundle(coverageBuilder.getBundle(params.title));

        visitor.visitBundle(bundle, src_dir);
    }
}
