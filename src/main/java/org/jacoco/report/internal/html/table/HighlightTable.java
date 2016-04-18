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
package org.jacoco.report.internal.html.table;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.jacoco.core.analysis.IHighlightNode;
import org.jacoco.core.analysis.ICoverageNode;
import org.jacoco.report.internal.ReportOutputFolder;
import org.jacoco.report.internal.html.HTMLElement;
import org.jacoco.report.internal.html.resources.Resources;
import org.jacoco.report.internal.html.resources.Styles;

/**
 * Renderer for a table of {@link ITableItem}s.
 */
public class HighlightTable extends Table {

    private final List<Column> columns;

    private Comparator<ITableItem> defaultComparator;

    /**
     * Create a new table without any columns yet.
     */
    public HighlightTable() {
        super();
        this.columns = new ArrayList<HighlightTable.Column>();
    }

    /**
     * Adds a new column with the given properties to the table.
     *
     * @param header
     *            column header caption
     * @param style
     *            optional CSS style class name for the td-Elements of this
     *            column
     * @param renderer
     *            callback for column rendering
     * @param defaultSorting
     *            If <code>true</code>, this column is the default sorting
     *            column. Only one column can be selected for default sorting.
     *
     */
    @Override
    public void add(final String header, final String style,
                    final IColumnRenderer renderer, final boolean defaultSorting) {
        add(header, null, style, renderer, defaultSorting);
    }

    public void add(final String header, final ICoverageNode.CounterEntity entity, final String style,
                    final IColumnRenderer renderer, final boolean defaultSorting) {
        columns.add(new Column(columns.size(), entity, header, style, renderer,
                defaultSorting));
        if (defaultSorting) {
            if (defaultComparator != null) {
                throw new IllegalStateException(
                        "Default sorting only allowed for one column.");
            }
            this.defaultComparator = renderer.getComparator();
        }
    }

    /**
     * Renders a table for the given icon
     *
     * @param parent
     *            parent element in which the table is created
     * @param items
     *            items that will make the table rows
     * @param total
     *            the summary of all coverage data items in the table static
     *            resources that might be referenced
     * @param resources
     *            static resources that might be referenced
     * @param base
     *            base folder of the table
     * @throws IOException
     *             in case of IO problems with the element output
     */
    @Override
    public void render(final HTMLElement parent,
                       final List<? extends ITableItem> items, final ICoverageNode total,
                       final Resources resources, final ReportOutputFolder base)
            throws IOException {
        final List<? extends ITableItem> sortedItems = sort(items);
        final HTMLElement table = parent.table(Styles.COVERAGETABLE);
        table.attr("id", "coveragetable");
        header(table, sortedItems, total);
        footer(table, total, resources, base);
        body(table, sortedItems, resources, base);
    }

    private void header(final HTMLElement table,
                        final List<? extends ITableItem> items, final ICoverageNode total)
            throws IOException {
        final HTMLElement tr = table.thead().tr();
        for (final Column c : columns) {
            c.init(tr, items, total);
        }
    }

    private void footer(final HTMLElement table, final ICoverageNode total,
                        final Resources resources, final ReportOutputFolder base)
            throws IOException {
        final HTMLElement tr = table.tfoot().tr();
        for (final Column c : columns) {
            c.footer(tr, total, resources, base);
        }
    }

    private void body(final HTMLElement table,
                      final List<? extends ITableItem> items, final Resources resources,
                      final ReportOutputFolder base) throws IOException {
        final HTMLElement tbody = table.tbody();
        int idx = 0;
        for (final ITableItem item : items) {
            final HTMLElement tr = tbody.tr();
            for (final Column c : columns) {
                c.body(tr, idx, item, resources, base);
            }
            idx++;
        }
    }

    private List<? extends ITableItem> sort(
            final List<? extends ITableItem> items) {
        if (defaultComparator != null) {
            final List<ITableItem> result = new ArrayList<ITableItem>(items);
            Collections.sort(result, defaultComparator);
            return result;
        }
        return items;
    }

    private static class Column {
        private static final String PASS = "pass";
        private static final String FAIL = "fail";

        private final ICoverageNode.CounterEntity entity;
        private final char idprefix;
        private final String header;
        private final IColumnRenderer renderer;
        private final SortIndex<ITableItem> index;
        private final String style, headerStyle;

        private boolean visible;

        Column(final int idx, final ICoverageNode.CounterEntity entity, final String header, final String style,
               final IColumnRenderer renderer, final boolean defaultSorting) {
            this.idprefix = (char) ('a' + idx);
            this.entity = entity;
            this.header = header;
            this.renderer = renderer;
            index = new SortIndex<ITableItem>(renderer.getComparator());
            this.style = style;
            this.headerStyle = Styles.combine(defaultSorting ? Styles.DOWN
                    : null, Styles.SORTABLE, style);
        }

        void init(final HTMLElement tr, final List<? extends ITableItem> items,
                  final ICoverageNode total) throws IOException {
            visible = renderer.init(items, total);
            if (visible) {
                index.init(items);
                final HTMLElement td = tr.td(headerStyle);
                td.attr("id", String.valueOf(idprefix));
                td.attr("onclick", "toggleSort(this)");
                td.text(header);
            }
        }

        void footer(final HTMLElement tr, final ICoverageNode total,
                    final Resources resources, final ReportOutputFolder base)
                throws IOException {
            if (visible) {
                String td_class = style;
                if (total instanceof IHighlightNode) {
                    td_class += " " + highlightTotal((IHighlightNode) total);
                }
                HTMLElement td = tr.td(td_class);
                renderer.footer(td, total, resources, base);
            }
        }

        void body(final HTMLElement tr, final int idx, final ITableItem item,
                  final Resources resources, final ReportOutputFolder base)
                throws IOException {
            if (visible) {
                String td_class = style;
                ICoverageNode node = item.getNode();
                if (node instanceof IHighlightNode) {
                    td_class += " " + highlightBody((IHighlightNode) node);
                }
                final HTMLElement td = tr.td(td_class);
                td.attr("id", idprefix + String.valueOf(index.getPosition(idx)));
                renderer.item(td, item, resources, base);
            }
        }

        String highlightBody(IHighlightNode h) throws IOException {
            String result = FAIL;
            if (entity == null) {
                if (h.getHighlightResults().getFinalBodyResult()) {
                    result = PASS;
                }
            } else {
                if (h.getHighlightResults().getEntityBodyResult(entity)) {
                    result = PASS;
                }
            }
            return result;
        }

        String highlightTotal(IHighlightNode h) throws IOException {
            String result = FAIL;
            if (entity == null) {
                if (h.getHighlightResults().getFinalTotalResult()) {
                    result = PASS;
                }
            } else {
                if (h.getHighlightResults().getEntityTotalResult(entity)) {
                    result = PASS;
                }
            }
            return result;
        }

    }

}
