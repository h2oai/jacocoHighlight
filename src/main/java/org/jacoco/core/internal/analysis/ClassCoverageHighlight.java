package org.jacoco.core.internal.analysis;

import org.jacoco.core.analysis.IHighlightNode;
import org.jacoco.report.internal.html.wrapper.NodeHighlightResults;

/**
 * Created by nkalonia1 on 3/17/16.
 */
public class ClassCoverageHighlight extends ClassCoverageImpl implements IHighlightNode {
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
}
