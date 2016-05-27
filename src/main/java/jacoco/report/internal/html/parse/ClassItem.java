package jacoco.report.internal.html.parse;

import org.jacoco.core.analysis.IClassCoverage;
import org.jacoco.core.analysis.ICoverageNode;

/**
 * Created by nkalonia1 on 5/10/16.
 */
public class ClassItem extends NewParseItem {
    ClassName class_name;

    @Override
    public boolean isDefined() {
        return class_name.defined();
    }

    @Override
    public boolean matches(ICoverageNode name) {
        if (name.getElementType() == ICoverageNode.ElementType.CLASS && isDefined()) {
            IClassCoverage c = (IClassCoverage) name;
            return class_name.matches(c.getName(), c.getSignature(), c.getSuperName(), c.getInterfaceNames());
        }
        return false;
    }

    @Override
    public itemType getType() {
        return itemType.CLASS;
    }
}