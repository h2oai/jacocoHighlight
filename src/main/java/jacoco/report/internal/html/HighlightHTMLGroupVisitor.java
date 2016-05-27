/*******************************************************************************
 * Copyright (c) 2009, 2016 Mountainminds GmbH & Co. KG and Contributors
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Marc R. Hoffmann - initial API and implementation
 *
 *******************************************************************************/
package jacoco.report.internal.html;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import jacoco.report.internal.AbstractHighlightGroupVisitor;
import jacoco.report.internal.html.parse.NewParseItem;
import org.jacoco.core.analysis.IBundleCoverage;
import org.jacoco.core.analysis.ICoverageNode;
import org.jacoco.report.ISourceFileLocator;
import org.jacoco.report.internal.AbstractGroupVisitor;
import org.jacoco.report.internal.ReportOutputFolder;
import org.jacoco.report.internal.html.IHTMLReportContext;
import org.jacoco.report.internal.html.page.BundlePage;
import org.jacoco.report.internal.html.page.GroupPage;
import org.jacoco.report.internal.html.page.NodePage;
import org.jacoco.report.internal.html.page.ReportPage;

/**
 * Group visitor for HTML reports.
 */
public class HighlightHTMLGroupVisitor extends AbstractHighlightGroupVisitor {

    private final ReportOutputFolder folder;

    private final IHTMLReportContext context;

    private final GroupPage page;

    /**
     * Create a new group handler.
     *
     * @param parent
     *            optional hierarchical parent
     * @param folder
     *            base folder for this group
     * @param context
     *            settings context
     * @param name
     *            group name
     */
    public HighlightHTMLGroupVisitor(final ReportPage parent,
                                     final ReportOutputFolder folder, final IHTMLReportContext context,
                                     final String name, Collection<NewParseItem> pil) {
        super(name, pil);
        this.folder = folder;
        this.context = context;
        page = new GroupPage(total, parent, folder, context);
    }

    public HighlightHTMLGroupVisitor(final ReportPage parent,
                                     final ReportOutputFolder folder, final IHTMLReportContext context,
                                     final String name) {
        this(parent, folder, context, name, new ArrayList<NewParseItem>(0));
    }

    /**
     * Returns the page rendered for this group.
     *
     * @return page for this group
     */
    public NodePage<ICoverageNode> getPage() {
        return page;
    }

    @Override
    protected void handleBundle(final IBundleCoverage bundle,
                                final ISourceFileLocator locator) throws IOException {
        final BundlePage bundlepage = new BundlePage(bundle, page, locator,
                folder.subFolder(bundle.getName()), context);
        bundlepage.render();
        page.addItem(bundlepage);
    }

    @Override
    protected AbstractHighlightGroupVisitor handleGroup(final String name)
            throws IOException {
        final HighlightHTMLGroupVisitor handler = new HighlightHTMLGroupVisitor(page,
                folder.subFolder(name), context, name, child_parse_items);
        page.addItem(handler.getPage());
        return handler;
    }

    @Override
    protected void handleEnd() throws IOException {
        page.render();
    }

}
