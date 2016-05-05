package jacoco.core.internal.analysis;

import jacoco.core.analysis.IHighlightNode;
import jacoco.report.internal.html.wrapper.NodeHighlightResults;
import org.jacoco.core.analysis.ICoverageNode;
import org.jacoco.core.internal.analysis.BundleCoverageImpl;
import org.jacoco.core.analysis.IPackageCoverage;
import org.jacoco.core.internal.analysis.CounterImpl;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by nkalonia1 on 3/17/16.
 */
public class BundleCoverageHighlight extends BundleCoverageImpl implements IHighlightNode {
    NodeHighlightResults _nhr;
    public BundleCoverageHighlight(final String name,
                                   final Collection<IPackageCoverage> packages) {
        super(name, packages);
        _nhr = new NodeHighlightResults();
    }

    @Override
    public NodeHighlightResults getHighlightResults() {
        return _nhr;
    }

    @Override
    public ICoverageNode getPlainCopy() {
        BundleCoverageHighlight copy = new BundleCoverageHighlight(getName(), new ArrayList<IPackageCoverage>());
        copy._nhr = _nhr.getPlainCopy();
        copy.instructionCounter = CounterImpl.getInstance(instructionCounter);
        copy.branchCounter = CounterImpl.getInstance(branchCounter);
        copy.lineCounter = CounterImpl.getInstance(lineCounter);
        copy.complexityCounter = CounterImpl.getInstance(complexityCounter);
        copy.methodCounter = CounterImpl.getInstance(methodCounter);
        copy.classCounter = CounterImpl.getInstance(classCounter);
        return copy;
    }
}
