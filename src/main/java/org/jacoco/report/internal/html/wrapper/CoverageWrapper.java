package org.jacoco.report.internal.html.wrapper;

import org.jacoco.core.analysis.IHighlightNode;
import org.jacoco.core.internal.analysis.BundleCoverageHighlight;
import org.jacoco.core.internal.analysis.ClassCoverageHighlight;
import org.jacoco.core.internal.analysis.MethodCoverageHighlight;
import org.jacoco.core.internal.analysis.PackageCoverageHighlight;
import org.jacoco.report.internal.html.parse.ParseItem;
import org.jacoco.report.internal.html.parse.YAMLParser;
import org.jacoco.core.analysis.*;

import java.io.*;
import java.util.*;

/**
 * Created by nkalonia1 on 3/17/16.
 */
public class CoverageWrapper {

    public static BundleCoverageHighlight wrapBundle(IBundleCoverage b) {
        List<IPackageCoverage> packages = new LinkedList<IPackageCoverage>();
        for (IPackageCoverage p : b.getPackages()) {
            packages.add(wrapPackage(p));
        }
        BundleCoverageHighlight bch = new BundleCoverageHighlight(b.getName(), packages);
        return bch;
    }

    public static PackageCoverageHighlight wrapPackage(IPackageCoverage p) {
        List<IClassCoverage> classes = new LinkedList<IClassCoverage>();
        for (IClassCoverage c : p.getClasses()) {
            classes.add(wrapClass(c));
        }
        PackageCoverageHighlight pch = new PackageCoverageHighlight(p.getName(), classes, p.getSourceFiles());

        return pch;
    }

    public static ClassCoverageHighlight wrapClass(IClassCoverage c) {
        ClassCoverageHighlight cch = new ClassCoverageHighlight(c.getName(), c.getId(), c.isNoMatch());
        cch.setSignature(c.getSignature());
        cch.setSuperName(c.getSuperName());
        cch.setInterfaces(c.getInterfaceNames());
        for (IMethodCoverage m : c.getMethods()) {
            cch.addMethod(wrapMethod(m));
        }
        cch.setSourceFileName(c.getSourceFileName());
        return cch;
    }

    public static MethodCoverageHighlight wrapMethod(IMethodCoverage m) {
        MethodCoverageHighlight mch = new MethodCoverageHighlight(m.getName(), m.getDesc(), m.getSignature());
        mch.increment(m);
        return mch;
    }

    public static List<ParseItem> parse(File path) {
        return (new YAMLParser(System.out, System.err)).parse(path);
    }

    public static BundleCoverageHighlight parseBundle(BundleCoverageHighlight b, File path) {
        for (ParseItem p : parse(path)) {
            apply(b, p);
        }
        updateDisplay(b);
        return b;
    }

    private static void apply(IBundleCoverage b, ParseItem pi) {
        apply(b, pi, false);
    }

    private static void apply(IPackageCoverage p, ParseItem pi) {
        apply(p, pi, false);
    }

    private static void apply(IClassCoverage c, ParseItem pi) {
        apply(c, pi, false);
    }

    private static void apply(IMethodCoverage m, ParseItem pi) {
        apply(m, pi, false);
    }

    private static void apply(IBundleCoverage b, ParseItem pi, boolean propagate) {
        if (propagate) {
            for (IPackageCoverage p : b.getPackages()) {
                apply(p, pi, true);
            }
            applyValues(b, pi);
        } else if (pi.hasPackageName()) {
            boolean found = false;
            for (IPackageCoverage p : b.getPackages()) {
                if (pi.matches(p)) {
                    found = true;
                    apply(p, pi);
                }
            }
            if (!found) {
                err("Could not find package: " + pi.getPackageName());
            }
        } else {
            applyValues(b, pi);
            if (pi.propagate()) apply(b, pi, true);
        }
    }

    private static void apply(IPackageCoverage p, ParseItem pi, boolean propagate) {
        if (propagate) {
            for (IClassCoverage c : p.getClasses()) {
                apply(c, pi, true);
            }
            applyValues(p, pi);
        } else if (pi.hasClassName()){
            boolean found = false;
            for (IClassCoverage c : p.getClasses()) {
                if (pi.matches(c)) {
                    found = true;
                    apply(c, pi);
                }
            }
            if (!found) {
                err("Could not find class: " + pi.getClassName());
            }
        } else {
            applyValues(p, pi);
            if (pi.propagate()) apply(p, pi, true);
        }
    }

    private static void apply(IClassCoverage c, ParseItem pi, boolean propagate) {
        if (propagate) {
            for (IMethodCoverage m : c.getMethods()) {
                apply(m, pi, true);
            }
            applyValues(c, pi);
        } else if (pi.hasMethodName()) {
            boolean found = false;
            for (IMethodCoverage m : c.getMethods()) {
                if (pi.matches(m)) {
                    found = true;
                    apply(m, pi);
                }
            }
            if (!found) {
                err("Could not find method: " + pi.getMethodName());
            }
        } else {
            applyValues(c, pi);
            if (pi.propagate()) apply(c, pi, true);
        }
    }

    private static void apply(IMethodCoverage m, ParseItem pi, boolean propagate) {
        applyValues(m, pi);
    }

    private static void applyValues(ICoverageNode h, ParseItem pi) {
        if (h instanceof IHighlightNode) {
            NodeHighlightResults nhr = ((IHighlightNode) h).getHighlightResults();
            for (ICoverageNode.CounterEntity ce : pi.getHeaders()) {
                nhr.entity_total_results.put(ce, !(h.getCounter(ce).getCoveredRatio() < pi.getValue(ce) / 100));
            }
        }
    }

    private static void err(String s) {
        System.err.println("ERR: " + s);
    }

    private static void updateDisplay(IBundleCoverage b) {
        for (IPackageCoverage p : b.getPackages()) {
            updateDisplay(p);
        }
    }

    private static NodeHighlightResults updateDisplay(IPackageCoverage p) {
        NodeHighlightResults p_nhr;
        if (p instanceof IHighlightNode) {
            p_nhr = ((IHighlightNode) p).getHighlightResults();
            p_nhr.mergeTotaltoBody();
        } else {
            p_nhr = new NodeHighlightResults();
        }
        for (IClassCoverage c : p.getClasses()) {
            p_nhr.mergeBodyResults(updateDisplay(c));
        }
        return p_nhr;
    }

    private static NodeHighlightResults updateDisplay(IClassCoverage c) {
        NodeHighlightResults c_nhr;
        if (c instanceof IHighlightNode) {
            c_nhr = ((IHighlightNode) c).getHighlightResults();
            c_nhr.mergeTotaltoBody();
        } else {
            c_nhr = new NodeHighlightResults();
        }
        for (IMethodCoverage m : c.getMethods()) {
            c_nhr.mergeBodyResults(updateDisplay(m));
        }
        return c_nhr;
    }

    private static NodeHighlightResults updateDisplay(IMethodCoverage m) {
        NodeHighlightResults m_nhr;
        if (m instanceof IHighlightNode) {
            m_nhr = ((IHighlightNode) m).getHighlightResults();
            m_nhr.mergeTotaltoBody();
        } else {
            m_nhr = new NodeHighlightResults();
        }
        return m_nhr;
    }
}
