package jacoco.core.analysis;

import org.jacoco.core.analysis.ICoverageNode;

import java.util.Collection;

/**
 * Created by nkalonia1 on 5/26/16.
 */
public interface ITreeNode {
    Collection<? extends ICoverageNode> getChildren();
}
