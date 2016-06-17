package report;

import java.util.Collection;

/**
 * Created by nkalonia1 on 6/17/16.
 */
public class InvalidArgItem extends ArgItem {
    public InvalidArgItem() {
        super(null);
    }

    @Override
    public void addClass(Collection l) {
        throw new IllegalStateException("ArgItem is invalid");
    }
}
