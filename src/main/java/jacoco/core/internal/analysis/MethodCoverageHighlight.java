package jacoco.core.internal.analysis;

import jacoco.core.analysis.IHighlightNode;
import jacoco.core.analysis.ITreeNode;
import org.jacoco.core.analysis.ICoverageNode;
import org.jacoco.core.internal.analysis.MethodCoverageImpl;
import jacoco.report.internal.html.highlighter.NodeHighlightResults;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by nkalonia1 on 3/17/16.
 */
public class MethodCoverageHighlight extends MethodCoverageImpl implements IHighlightNode, ITreeNode {
    private NodeHighlightResults _nhr;

    public MethodCoverageHighlight(final String name, final String desc,
                                   final String signature) {
        super(name, desc, signature);
        _nhr = new NodeHighlightResults();
    }

    @Override
    public NodeHighlightResults getHighlightResults() {
        return _nhr;
    }

    @Override
    public Collection<ICoverageNode> getChildren() {
        return new ArrayList<ICoverageNode>(0);
    }
}
