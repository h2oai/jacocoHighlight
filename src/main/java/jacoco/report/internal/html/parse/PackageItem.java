package jacoco.report.internal.html.parse;

import org.jacoco.core.analysis.ICoverageNode;

/**
 * Created by nkalonia1 on 5/10/16.
 */
public class PackageItem extends NewParseItem {
    PackageName package_name;

    @Override
    public boolean isDefined() {
        return package_name.defined();
    }

    @Override
    public boolean matches(ICoverageNode name) {
        return name.getElementType() == ICoverageNode.ElementType.PACKAGE && isDefined() && package_name.matches(name.getName());
    }

    @Override
    public itemType getType() {
        return itemType.PACKAGE;
    }
}