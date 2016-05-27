package jacoco.core.internal.analysis;

import jacoco.core.analysis.IHighlightNode;
import jacoco.core.analysis.ITreeNode;
import org.jacoco.core.analysis.IMethodCoverage;
import org.jacoco.core.internal.analysis.ClassCoverageImpl;
import jacoco.report.internal.html.highlighter.NodeHighlightResults;

import java.util.Collection;

/**
 * Created by nkalonia1 on 3/17/16.
 */
public class ClassCoverageHighlight extends ClassCoverageImpl implements IHighlightNode, ITreeNode {
    private NodeHighlightResults _nhr;

    public ClassCoverageHighlight(final String name, final long id,
                                  final boolean noMatch) {
        super(name, id, noMatch);
        _nhr = new NodeHighlightResults();
    }

    @Override
    public NodeHighlightResults getHighlightResults() {
        return _nhr;
    }

    @Override
    public Collection<IMethodCoverage> getChildren() {
        return getMethods();
    }
}
