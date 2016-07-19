package jacoco.report.internal.html.parse;

/**
 * Created by nkalonia1 on 7/18/16.
 */
public class NewGlobalParseItem extends NewParseItem {
    public NewGlobalParseItem() {
        super(itemType.ANY);
        name = new MatchingName();
    }
}
