package jacoco.report.internal.html.highlighter;

import jacoco.core.analysis.IHighlightNode;
import jacoco.core.analysis.ITreeNode;
import jacoco.report.internal.html.parse.NewParseItem;
import org.jacoco.core.analysis.*;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by nkalonia1 on 5/17/16.
 */
public class NodeHighlighter {

    public static ICoverageNode apply(ICoverageNode n, Collection<NewParseItem> pil) {
        return apply(n, pil, null);
    }

    public static ICoverageNode apply(ICoverageNode n, Collection<NewParseItem> pil, ICoverageNode.ElementType filter) {
        highlightTotal(n, pil, filter);
        highlightDisplay(n);
        return n;
    }

    private static NodeHighlightResults highlightTotal(ICoverageNode n, Collection<NewParseItem> pil, ICoverageNode.ElementType filter) {
        for (NewParseItem pi : pil) {
            if (filter != null && n.getElementType() != filter)
                continue;
            if (pi.matches(n)) {
                highlightNode(n, pi);
                if (!pi.isLeaf()) {
                    for (ICoverageNode child : getChildren(n)) {
                        highlightTotal(child, pi.getChildren(), filter);
                    }
                }
            }
        }
        return getHighlightResults(n);
    }

    private static NodeHighlightResults highlightDisplay(ICoverageNode n) {
        NodeHighlightResults nhr = getHighlightResults(n);
        for (ICoverageNode child : getChildren(n)) {
            nhr.mergeBodyResults(highlightDisplay(child));
        }
        nhr.mergeTotaltoBody();

        return nhr;
    }

    private static void highlightNode(ICoverageNode n, NewParseItem pi) {
        if (!(n instanceof IHighlightNode) || !pi.hasValues()) {
            return;
        }
        NodeHighlightResults nhr = ((IHighlightNode) n).getHighlightResults();
        for (ICoverageNode.CounterEntity ce : pi.getHeaders()) {
            nhr.entity_total_results.put(ce, !(n.getCounter(ce).getCoveredRatio() < (pi.getValue(ce) / 100.0)));
        }
        if (pi.propagate()) {
            for (ICoverageNode child : getChildren(n)) {
                highlightNode(child, pi);
            }
        }
    }

    private static Collection<? extends ICoverageNode> getChildren(ICoverageNode n) {
        if (n instanceof ITreeNode) {
            return ((ITreeNode) n).getChildren();
        }
        switch(n.getElementType()) {
            case BUNDLE:
                return ((IBundleCoverage) n).getPackages();
            case PACKAGE:
                return ((IPackageCoverage) n).getClasses();
            case CLASS:
                return ((IClassCoverage) n).getMethods();
            default:
                return new ArrayList<ICoverageNode>(0);
        }
    }

    private static NodeHighlightResults getHighlightResults(ICoverageNode n) {
        if (n instanceof IHighlightNode) {
            return ((IHighlightNode) n).getHighlightResults();
        }
        return new NodeHighlightResults(true);
    }
}
