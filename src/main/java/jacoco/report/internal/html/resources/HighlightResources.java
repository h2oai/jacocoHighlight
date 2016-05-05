package jacoco.report.internal.html.resources;

import org.jacoco.report.internal.ReportOutputFolder;
import org.jacoco.report.internal.html.resources.Resources;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Static resource that are included with the coverage report and might be
 * referenced from created HTML pages.
 */
public class HighlightResources extends Resources {

    /** The name of the style sheet */
    public static final String HIGHLIGHT_STYLESHEET = "highlight_report.css";

    ReportOutputFolder folder;

    public HighlightResources(final ReportOutputFolder root) {
        super(root);
        folder = root.subFolder(".resources");
    }

    @Override
    public String getLink(final ReportOutputFolder base, final String name) {
        return super.getLink(base, name.equals(Resources.STYLESHEET) ? HIGHLIGHT_STYLESHEET : name);
    }

    /**
     * Copies all static resources into the report.
     *
     * @throws IOException
     *             if the resources can't be written to the report
     */
    @Override
    public void copyResources() throws IOException {
        super.copyResources();
        copyResource(HIGHLIGHT_STYLESHEET);
    }

    private void copyResource(final String name) throws IOException {
        System.out.println(name);
        final InputStream in = HighlightResources.class.getResourceAsStream(name);
        final OutputStream out = folder.createFile(name);
        final byte[] buffer = new byte[256];
        int len;
        while ((len = in.read(buffer)) != -1) {
            out.write(buffer, 0, len);
        }
        in.close();
        out.close();
    }

}
