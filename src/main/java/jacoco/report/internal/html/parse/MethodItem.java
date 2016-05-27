package jacoco.report.internal.html.parse;

import org.jacoco.core.analysis.ICoverageNode;
import org.jacoco.core.analysis.IMethodCoverage;

/**
 * Created by nkalonia1 on 5/10/16.
 */
public class MethodItem extends NewParseItem {
    MethodName method_name;

    @Override
    public boolean isDefined() {
        return method_name.defined();
    }

    @Override
    public boolean matches(ICoverageNode name) {
        if (name.getElementType() == ICoverageNode.ElementType.METHOD && isDefined()) {
            IMethodCoverage m = (IMethodCoverage) name;
            return method_name.matches(m.getName(), m.getDesc(), m.getSignature());
        }
        return false;
    }

    @Override
    public itemType getType() {
        return itemType.METHOD;
    }
}