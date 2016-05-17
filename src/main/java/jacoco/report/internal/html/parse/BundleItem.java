package jacoco.report.internal.html.parse;

import org.jacoco.core.analysis.ICoverageNode;

/**
 * Created by nkalonia1 on 5/10/16.
 */
public class BundleItem extends NewParseItem {
    BundleName bundle_name;

    @Override
    public boolean isDefined() {
        return bundle_name.defined();
    }

    @Override
    public boolean matches(ICoverageNode name) {
        return name.getElementType() == ICoverageNode.ElementType.BUNDLE && isDefined() && bundle_name.matches(name.getName());
    }
}