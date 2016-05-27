package jacoco.report.internal.html.parse;

import org.jacoco.core.analysis.ICoverageNode;

/**
 * Created by nkalonia1 on 5/10/16.
 */
public class GroupItem extends NewParseItem {
    GroupName group_name;

    @Override
    public boolean isDefined() {
        return group_name.defined();
    }

    @Override
    public boolean matches(ICoverageNode name) {
        return name.getElementType() == ICoverageNode.ElementType.GROUP && isDefined() && group_name.matches(name.getName());
    }

    @Override
    public itemType getType() {
        return itemType.GROUP;
    }
}
