package jacoco.core.internal.analysis;

import jacoco.core.analysis.IHighlightNode;
import jacoco.core.analysis.ITreeNode;
import org.jacoco.core.internal.analysis.PackageCoverageImpl;
import jacoco.report.internal.html.highlighter.NodeHighlightResults;
import org.jacoco.core.analysis.IClassCoverage;
import org.jacoco.core.analysis.ISourceFileCoverage;

import java.util.Collection;

/**
 * Created by nkalonia1 on 3/17/16.
 */
public class PackageCoverageHighlight extends PackageCoverageImpl implements IHighlightNode, ITreeNode {
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

    @Override
    public Collection<IClassCoverage> getChildren() {
        return getClasses();
    }
}
