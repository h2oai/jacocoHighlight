package jacoco.core.internal.analysis;

import jacoco.core.analysis.IHighlightNode;
import jacoco.core.analysis.ITreeNode;
import jacoco.report.internal.html.highlighter.NodeHighlightResults;
import org.jacoco.core.analysis.CoverageNodeImpl;
import org.jacoco.core.analysis.ICoverageNode;
import org.jacoco.core.internal.analysis.CounterImpl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by nkalonia1 on 3/17/16.
 */
public class CoverageNodeHighlight extends CoverageNodeImpl implements IHighlightNode, ITreeNode {
    private NodeHighlightResults _nhr;
    private List<ICoverageNode> _children;

    public CoverageNodeHighlight(final ElementType elementType, final String name, NodeHighlightResults nhr) {
        super(elementType, name);
        _nhr = nhr;
        _children = new ArrayList<ICoverageNode>();
    }

    public CoverageNodeHighlight(final ElementType elementType, final String name) {
        this(elementType, name, new NodeHighlightResults());
    }

    @Override
    public NodeHighlightResults getHighlightResults() {
        return _nhr;
    }

    @Override
    public ICoverageNode getPlainCopy() {
        CoverageNodeHighlight copy = new CoverageNodeHighlight(getElementType(), getName(), _nhr.getPlainCopy());
        copy.instructionCounter = CounterImpl.getInstance(instructionCounter);
        copy.branchCounter = CounterImpl.getInstance(branchCounter);
        copy.lineCounter = CounterImpl.getInstance(lineCounter);
        copy.complexityCounter = CounterImpl.getInstance(complexityCounter);
        copy.methodCounter = CounterImpl.getInstance(methodCounter);
        copy.classCounter = CounterImpl.getInstance(classCounter);
        return copy;
    }

    public void addChild(ICoverageNode n) {
        _children.add(n);
    }

    @Override
    public Collection<ICoverageNode> getChildren() {
        return _children;
    }
}
