package jacoco.core.internal.analysis;

import jacoco.core.analysis.IHighlightNode;
import org.jacoco.core.internal.analysis.PackageCoverageImpl;
import jacoco.report.internal.html.wrapper.NodeHighlightResults;
import org.jacoco.core.analysis.IClassCoverage;
import org.jacoco.core.analysis.ISourceFileCoverage;

import java.util.Collection;

/**
 * Created by nkalonia1 on 3/17/16.
 */
public class PackageCoverageHighlight extends PackageCoverageImpl implements IHighlightNode {
    private NodeHighlightResults _nhr;

    public PackageCoverageHighlight(final String name,
                                    final Collection<IClassCoverage> classes,
                                    final Collection<ISourceFileCoverage> sourceFiles) {
        super(name, classes, sourceFiles);
        _nhr = new NodeHighlightResults();
    }

    @Override
    public NodeHighlightResults getHighlightResults() {
        return _nhr;
    }
}
