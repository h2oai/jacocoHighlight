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
        return apply(n, pil, true);
    }

    public static ICoverageNode apply(ICoverageNode n, Collection<NewParseItem> pil, boolean tree) {
        highlightTotal(n, pil, tree);
        highlightDisplay(n);
        return n;
    }

    private static NodeHighlightResults highlightTotal(ICoverageNode n, Collection<NewParseItem> pil, boolean tree) {
        for (NewParseItem pi : pil) {
            if (pi.matches(n)) {
                highlightNode(n, pi);
                if (!pi.isLeaf() && tree) {
                    for (ICoverageNode child : getChildren(n)) {
                        highlightTotal(child, pi.getChildren(), tree);
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

    private static void highlightNode(ICoverageNode n, NewParseItem pi, boolean apply_propagate) {
        if (!(n instanceof IHighlightNode)) {
            return;
        }
        NodeHighlightResults nhr = ((IHighlightNode) n).getHighlightResults();
        for (ICoverageNode.CounterEntity ce : ICoverageNode.CounterEntity.values()) {
            nhr.entity_total_results.put(ce, !(n.getCounter(ce).getCoveredRatio() < (pi.getValue(ce) / 100.0)));
        }
        if (apply_propagate && pi.propagate()) {
            for (ICoverageNode child : getChildren(n)) {
                highlightNode(child, pi);
            }
        }
    }

    private static void highlightNode(ICoverageNode n, NewParseItem pi) {
        highlightNode(n, pi, true);
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
