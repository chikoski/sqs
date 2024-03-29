/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/* $Id: TableCellLayoutManager.java 893238 2009-12-22 17:20:51Z vhennebert $ */

package org.apache.fop.layoutmgr.table;

import java.util.LinkedList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.apache.fop.area.Area;
import org.apache.fop.area.Block;
import org.apache.fop.area.Trait;
import org.apache.fop.fo.flow.table.ConditionalBorder;
import org.apache.fop.fo.flow.table.GridUnit;
import org.apache.fop.fo.flow.table.PrimaryGridUnit;
import org.apache.fop.fo.flow.table.Table;
import org.apache.fop.fo.flow.table.TableCell;
import org.apache.fop.fo.flow.table.TableColumn;
import org.apache.fop.fo.flow.table.TablePart;
import org.apache.fop.fo.flow.table.TableRow;
import org.apache.fop.fo.properties.CommonBorderPaddingBackground;
import org.apache.fop.fo.properties.CommonBorderPaddingBackground.BorderInfo;
import org.apache.fop.layoutmgr.AreaAdditionUtil;
import org.apache.fop.layoutmgr.BlockLevelLayoutManager;
import org.apache.fop.layoutmgr.BlockStackingLayoutManager;
import org.apache.fop.layoutmgr.ElementListUtils;
import org.apache.fop.layoutmgr.Keep;
import org.apache.fop.layoutmgr.KnuthBox;
import org.apache.fop.layoutmgr.KnuthElement;
import org.apache.fop.layoutmgr.KnuthGlue;
import org.apache.fop.layoutmgr.KnuthPenalty;
import org.apache.fop.layoutmgr.LayoutContext;
import org.apache.fop.layoutmgr.LayoutManager;
import org.apache.fop.layoutmgr.Position;
import org.apache.fop.layoutmgr.PositionIterator;
import org.apache.fop.layoutmgr.SpaceResolver;
import org.apache.fop.layoutmgr.TraitSetter;
import org.apache.fop.traits.BorderProps;
import org.apache.fop.traits.MinOptMax;
import org.apache.fop.util.ListUtil;

/**
 * LayoutManager for a table-cell FO.
 * A cell contains blocks. These blocks fill the cell.
 */
public class TableCellLayoutManager extends BlockStackingLayoutManager
            implements BlockLevelLayoutManager {

    /**
     * logging instance
     */
    private static Log log = LogFactory.getLog(TableCellLayoutManager.class);

    private PrimaryGridUnit primaryGridUnit;

    private Block curBlockArea;

    private int xoffset;
    private int yoffset;
    private int cellIPD;
    private int totalHeight;
    private int usedBPD;
    private boolean emptyCell = true;

    /**
     * Create a new Cell layout manager.
     * @param node table-cell FO for which to create the LM
     * @param pgu primary grid unit for the cell
     */
    public TableCellLayoutManager(TableCell node, PrimaryGridUnit pgu) {
        super(node);
        this.primaryGridUnit = pgu;
    }

    /** @return the table-cell FO */
    public TableCell getTableCell() {
        return (TableCell)this.fobj;
    }

    private boolean isSeparateBorderModel() {
        return getTable().isSeparateBorderModel();
    }

    /**
     * @return the table owning this cell
     */
    public Table getTable() {
        return getTableCell().getTable();
    }


    /** {@inheritDoc} */
    protected int getIPIndents() {
        int[] startEndBorderWidths = primaryGridUnit.getStartEndBorderWidths();
        startIndent = startEndBorderWidths[0];
        endIndent = startEndBorderWidths[1];
        if (isSeparateBorderModel()) {
            int borderSep = getTable().getBorderSeparation().getLengthPair().getIPD().getLength()
                    .getValue(this);
            startIndent += borderSep / 2;
            endIndent += borderSep / 2;
        } else {
            startIndent /= 2;
            endIndent /= 2;
        }
        startIndent += getTableCell().getCommonBorderPaddingBackground().getPaddingStart(false,
                this);
        endIndent += getTableCell().getCommonBorderPaddingBackground().getPaddingEnd(false, this);
        return startIndent + endIndent;
    }

    /**
     * {@inheritDoc}
     */
    public List getNextKnuthElements(LayoutContext context, int alignment) {
        MinOptMax stackLimit = context.getStackLimitBP();

        referenceIPD = context.getRefIPD();
        cellIPD = referenceIPD;
        cellIPD -= getIPIndents();

        List returnedList;
        List contentList = new LinkedList();
        List returnList = new LinkedList();

        LayoutManager curLM; // currently active LM
        LayoutManager prevLM = null; // previously active LM
        while ((curLM = getChildLM()) != null) {
            LayoutContext childLC = new LayoutContext(0);
            // curLM is a ?
            childLC.setStackLimitBP(context.getStackLimitBP().minus(stackLimit));
            childLC.setRefIPD(cellIPD);

            // get elements from curLM
            returnedList = curLM.getNextKnuthElements(childLC, alignment);
            if (childLC.isKeepWithNextPending()) {
                log.debug("child LM signals pending keep with next");
            }
            if (contentList.isEmpty() && childLC.isKeepWithPreviousPending()) {
                primaryGridUnit.setKeepWithPrevious(childLC.getKeepWithPreviousPending());
                childLC.clearKeepWithPreviousPending();
            }

            if (prevLM != null
                    && !ElementListUtils.endsWithForcedBreak(contentList)) {
                // there is a block handled by prevLM
                // before the one handled by curLM
                addInBetweenBreak(contentList, context, childLC);
            }
            contentList.addAll(returnedList);
            if (returnedList.isEmpty()) {
                //Avoid NoSuchElementException below (happens with empty blocks)
                continue;
            }
            if (childLC.isKeepWithNextPending()) {
                //Clear and propagate
                context.updateKeepWithNextPending(childLC.getKeepWithNextPending());
                childLC.clearKeepWithNextPending();
            }
            prevLM = curLM;
        }
        primaryGridUnit.setKeepWithNext(context.getKeepWithNextPending());

        returnedList = new LinkedList();
        if (!contentList.isEmpty()) {
            wrapPositionElements(contentList, returnList);
        } else {
            // In relaxed validation mode, table-cells having no children are authorised.
            // Add a zero-width block here to not have to take this special case into
            // account later
            // Copied from BlockStackingLM
            returnList.add(new KnuthBox(0, notifyPos(new Position(this)), true));
        }
        //Space resolution
        SpaceResolver.resolveElementList(returnList);
        if (((KnuthElement) returnList.get(0)).isForcedBreak()) {
            primaryGridUnit.setBreakBefore(((KnuthPenalty) returnList.get(0)).getBreakClass());
            returnList.remove(0);
            assert !returnList.isEmpty();
        }
        final KnuthElement lastItem = (KnuthElement) ListUtil
                .getLast(returnList);
        if (lastItem.isForcedBreak()) {
            KnuthPenalty p = (KnuthPenalty) lastItem;
            primaryGridUnit.setBreakAfter(p.getBreakClass());
            p.setPenalty(0);
        }

        setFinished(true);
        return returnList;
    }

    /**
     * Set the y offset of this cell.
     * This offset is used to set the absolute position of the cell.
     *
     * @param off the y direction offset
     */
    public void setYOffset(int off) {
        yoffset = off;
    }

    /**
     * Set the x offset of this cell (usually the same as its parent row).
     * This offset is used to determine the absolute position of the cell.
     *
     * @param off the x offset
     */
    public void setXOffset(int off) {
        xoffset = off;
    }

    /**
     * Set the content height for this cell. This method is used during
     * addAreas() stage.
     *
     * @param h the height of the contents of this cell
     */
    public void setContentHeight(int h) {
        usedBPD = h;
    }

    /**
     * Sets the total height of this cell on the current page. That is, the cell's bpd
     * plus before and after borders and paddings, plus the table's border-separation.
     *
     * @param h the height of cell
     */
    public void setTotalHeight(int h) {
        totalHeight = h;
    }

    /**
     * Add the areas for the break points. The cell contains block stacking layout
     * managers that add block areas.
     *
     * <p>In the collapsing-border model, the borders of a cell that spans over several
     * rows or columns are drawn separately for each grid unit. Therefore we must know the
     * height of each grid row spanned over by the cell. Also, if the cell is broken over
     * two pages we must know which spanned grid rows are present on the current page.</p>
     *
     * @param parentIter the iterator of the break positions
     * @param layoutContext the layout context for adding the areas
     * @param spannedGridRowHeights in collapsing-border model for a spanning cell, height
     * of each spanned grid row
     * @param startRow first grid row on the current page spanned over by the cell,
     * inclusive
     * @param endRow last grid row on the current page spanned over by the cell, inclusive
     * @param borderBeforeWhich one of {@link ConditionalBorder#NORMAL},
     * {@link ConditionalBorder#LEADING_TRAILING} or {@link ConditionalBorder#REST}
     * @param borderAfterWhich one of {@link ConditionalBorder#NORMAL},
     * {@link ConditionalBorder#LEADING_TRAILING} or {@link ConditionalBorder#REST}
     * @param firstOnPage true if the cell will be the very first one on the page, in
     * which case collapsed before borders must be drawn in the outer mode
     * @param lastOnPage true if the cell will be the very last one on the page, in which
     * case collapsed after borders must be drawn in the outer mode
     * @param painter painter
     * @param firstRowHeight height of the first row spanned by this cell (may be zero if
     * this row is placed on a previous page). Used to calculate the placement of the
     * row's background image if any
     */
    public void addAreas(PositionIterator parentIter,
                         LayoutContext layoutContext,
                         int[] spannedGridRowHeights,
                         int startRow,
                         int endRow,
                         int borderBeforeWhich,
                         int borderAfterWhich,
                         boolean firstOnPage,
                         boolean lastOnPage,
                         RowPainter painter,
                         int firstRowHeight) {
        getParentArea(null);

        addId();

        int borderBeforeWidth = primaryGridUnit.getBeforeBorderWidth(startRow, borderBeforeWhich);
        int borderAfterWidth = primaryGridUnit.getAfterBorderWidth(endRow, borderAfterWhich);

        CommonBorderPaddingBackground padding = primaryGridUnit.getCell()
                .getCommonBorderPaddingBackground();
        int paddingRectBPD = totalHeight - borderBeforeWidth - borderAfterWidth;
        int cellBPD = paddingRectBPD;
        cellBPD -= padding.getPaddingBefore(borderBeforeWhich == ConditionalBorder.REST, this);
        cellBPD -= padding.getPaddingAfter(borderAfterWhich == ConditionalBorder.REST, this);

        addBackgroundAreas(painter, firstRowHeight, borderBeforeWidth, paddingRectBPD);

        if (isSeparateBorderModel()) {
            if (!emptyCell || getTableCell().showEmptyCells()) {
                if (borderBeforeWidth > 0) {
                    int halfBorderSepBPD = getTableCell().getTable().getBorderSeparation().getBPD()
                            .getLength().getValue() / 2;
                    adjustYOffset(curBlockArea, halfBorderSepBPD);
                }
                TraitSetter.addBorders(curBlockArea,
                        getTableCell().getCommonBorderPaddingBackground(),
                        borderBeforeWidth == 0, borderAfterWidth == 0,
                        false, false, this);
            }
        } else {
            boolean inFirstColumn = (primaryGridUnit.getColIndex() == 0);
            boolean inLastColumn = (primaryGridUnit.getColIndex()
                    + getTableCell().getNumberColumnsSpanned() == getTable()
                    .getNumberOfColumns());
            if (!primaryGridUnit.hasSpanning()) {
                adjustYOffset(curBlockArea, -borderBeforeWidth);
                //Can set the borders directly if there's no span
                boolean[] outer = new boolean[] {firstOnPage, lastOnPage, inFirstColumn,
                        inLastColumn};
                TraitSetter.addCollapsingBorders(curBlockArea,
                        primaryGridUnit.getBorderBefore(borderBeforeWhich),
                        primaryGridUnit.getBorderAfter(borderAfterWhich),
                        primaryGridUnit.getBorderStart(),
                        primaryGridUnit.getBorderEnd(), outer);
            } else {
                adjustYOffset(curBlockArea, borderBeforeWidth);
                Block[][] blocks = new Block[getTableCell().getNumberRowsSpanned()][getTableCell()
                        .getNumberColumnsSpanned()];
                GridUnit[] gridUnits = (GridUnit[]) primaryGridUnit.getRows().get(startRow);
                for (int x = 0; x < getTableCell().getNumberColumnsSpanned(); x++) {
                    GridUnit gu = gridUnits[x];
                    BorderInfo border = gu.getBorderBefore(borderBeforeWhich);
                    int borderWidth = border.getRetainedWidth() / 2;
                    if (borderWidth > 0) {
                        addBorder(blocks, startRow, x, Trait.BORDER_BEFORE, border, firstOnPage);
                        adjustYOffset(blocks[startRow][x], -borderWidth);
                        adjustBPD(blocks[startRow][x], -borderWidth);
                    }
                }
                gridUnits = (GridUnit[]) primaryGridUnit.getRows().get(endRow);
                for (int x = 0; x < getTableCell().getNumberColumnsSpanned(); x++) {
                    GridUnit gu = gridUnits[x];
                    BorderInfo border = gu.getBorderAfter(borderAfterWhich);
                    int borderWidth = border.getRetainedWidth() / 2;
                    if (borderWidth > 0) {
                        addBorder(blocks, endRow, x, Trait.BORDER_AFTER, border, lastOnPage);
                        adjustBPD(blocks[endRow][x], -borderWidth);
                    }
                }
                for (int y = startRow; y <= endRow; y++) {
                    gridUnits = (GridUnit[]) primaryGridUnit.getRows().get(y);
                    BorderInfo border = gridUnits[0].getBorderStart();
                    int borderWidth = border.getRetainedWidth() / 2;
                    if (borderWidth > 0) {
                        addBorder(blocks, y, 0, Trait.BORDER_START, border, inFirstColumn);
                        adjustXOffset(blocks[y][0], borderWidth);
                        adjustIPD(blocks[y][0], -borderWidth);
                    }
                    border = gridUnits[gridUnits.length - 1].getBorderEnd();
                    borderWidth = border.getRetainedWidth() / 2;
                    if (borderWidth > 0) {
                        addBorder(blocks, y, gridUnits.length - 1, Trait.BORDER_END, border,
                                inLastColumn);
                        adjustIPD(blocks[y][gridUnits.length - 1], -borderWidth);
                    }
                }
                int dy = yoffset;
                for (int y = startRow; y <= endRow; y++) {
                    int bpd = spannedGridRowHeights[y - startRow];
                    int dx = xoffset;
                    for (int x = 0; x < gridUnits.length; x++) {
                        int ipd = getTable().getColumn(primaryGridUnit.getColIndex() + x)
                                .getColumnWidth().getValue(getParent());
                        if (blocks[y][x] != null) {
                            Block block = blocks[y][x];
                            adjustYOffset(block, dy);
                            adjustXOffset(block, dx);
                            adjustIPD(block, ipd);
                            adjustBPD(block, bpd);
                            parentLayoutManager.addChildArea(block);
                        }
                        dx += ipd;
                    }
                    dy += bpd;
                }
            }
        }

        TraitSetter.addPadding(curBlockArea,
                padding,
                borderBeforeWhich == ConditionalBorder.REST,
                borderAfterWhich == ConditionalBorder.REST,
                false, false, this);

        //Handle display-align
        if (usedBPD < cellBPD) {
            if (getTableCell().getDisplayAlign() == EN_CENTER) {
                Block space = new Block();
                space.setBPD((cellBPD - usedBPD) / 2);
                curBlockArea.addBlock(space);
            } else if (getTableCell().getDisplayAlign() == EN_AFTER) {
                Block space = new Block();
                space.setBPD(cellBPD - usedBPD);
                curBlockArea.addBlock(space);
            }
        }

        AreaAdditionUtil.addAreas(this, parentIter, layoutContext);
        // Re-adjust the cell's bpd as it may have been modified by the previous call
        // for some reason (?)
        curBlockArea.setBPD(cellBPD);

        // Add background after we know the BPD
        if (!isSeparateBorderModel() || !emptyCell || getTableCell().showEmptyCells()) {
            TraitSetter.addBackground(curBlockArea,
                    getTableCell().getCommonBorderPaddingBackground(), this);
        }

        flush();

        curBlockArea = null;

        notifyEndOfLayout();
    }

    /** Adds background areas for the column, body and row, if any. */
    private void addBackgroundAreas(RowPainter painter, int firstRowHeight, int borderBeforeWidth,
            int paddingRectBPD) {
        TableColumn column = getTable().getColumn(primaryGridUnit.getColIndex());
        if (column.getCommonBorderPaddingBackground().hasBackground()) {
            Block colBackgroundArea = getBackgroundArea(paddingRectBPD, borderBeforeWidth);
            ((TableLayoutManager) parentLayoutManager).registerColumnBackgroundArea(column,
                    colBackgroundArea, -startIndent);
        }

        TablePart body = primaryGridUnit.getTablePart();
        if (body.getCommonBorderPaddingBackground().hasBackground()) {
            painter.registerPartBackgroundArea(
                    getBackgroundArea(paddingRectBPD, borderBeforeWidth));
        }

        TableRow row = primaryGridUnit.getRow();
        if (row != null && row.getCommonBorderPaddingBackground().hasBackground()) {
            Block rowBackgroundArea = getBackgroundArea(paddingRectBPD, borderBeforeWidth);
            ((TableLayoutManager) parentLayoutManager).addBackgroundArea(rowBackgroundArea);
            TraitSetter.addBackground(rowBackgroundArea, row.getCommonBorderPaddingBackground(),
                    parentLayoutManager,
                    -xoffset - startIndent, -borderBeforeWidth,
                    parentLayoutManager.getContentAreaIPD(), firstRowHeight);
        }
    }

    private void addBorder(Block[][] blocks, int i, int j, Integer side, BorderInfo border,
            boolean outer) {
        if (blocks[i][j] == null) {
            blocks[i][j] = new Block();
            blocks[i][j].addTrait(Trait.IS_REFERENCE_AREA, Boolean.TRUE);
            blocks[i][j].setPositioning(Block.ABSOLUTE);
        }
        blocks[i][j].addTrait(side, new BorderProps(border.getStyle(),
                border.getRetainedWidth(), border.getColor(),
                outer ? BorderProps.COLLAPSE_OUTER : BorderProps.COLLAPSE_INNER));
    }

    private static void adjustXOffset(Block block, int amount) {
        block.setXOffset(block.getXOffset() + amount);
    }

    private static void adjustYOffset(Block block, int amount) {
        block.setYOffset(block.getYOffset() + amount);
    }

    private static void adjustIPD(Block block, int amount) {
        block.setIPD(block.getIPD() + amount);
    }

    private static void adjustBPD(Block block, int amount) {
        block.setBPD(block.getBPD() + amount);
    }

    private Block getBackgroundArea(int bpd, int borderBeforeWidth) {
        CommonBorderPaddingBackground padding = getTableCell().getCommonBorderPaddingBackground();
        int paddingStart = padding.getPaddingStart(false, this);
        int paddingEnd = padding.getPaddingEnd(false, this);

        Block block = new Block();
        TraitSetter.setProducerID(block, getTable().getId());
        block.setPositioning(Block.ABSOLUTE);
        block.setIPD(cellIPD + paddingStart + paddingEnd);
        block.setBPD(bpd);
        block.setXOffset(xoffset + startIndent - paddingStart);
        block.setYOffset(yoffset + borderBeforeWidth);
        return block;
    }

    /**
     * Return an Area which can contain the passed childArea. The childArea
     * may not yet have any content, but it has essential traits set.
     * In general, if the LayoutManager already has an Area it simply returns
     * it. Otherwise, it makes a new Area of the appropriate class.
     * It gets a parent area for its area by calling its parent LM.
     * Finally, based on the dimensions of the parent area, it initializes
     * its own area. This includes setting the content IPD and the maximum
     * BPD.
     *
     * @param childArea the child area to get the parent for
     * @return the parent area
     */
    public Area getParentArea(Area childArea) {
        if (curBlockArea == null) {
            curBlockArea = new Block();
            curBlockArea.addTrait(Trait.IS_REFERENCE_AREA, Boolean.TRUE);
            TraitSetter.setProducerID(curBlockArea, getTableCell().getId());
            curBlockArea.setPositioning(Block.ABSOLUTE);
            curBlockArea.setXOffset(xoffset + startIndent);
            curBlockArea.setYOffset(yoffset);
            curBlockArea.setIPD(cellIPD);

            /*Area parentArea =*/ parentLayoutManager.getParentArea(curBlockArea);
            // Get reference IPD from parentArea
            setCurrentArea(curBlockArea); // ??? for generic operations
        }
        return curBlockArea;
    }

    /**
     * Add the child to the cell block area.
     *
     * @param childArea the child to add to the cell
     */
    public void addChildArea(Area childArea) {
        if (curBlockArea != null) {
            curBlockArea.addBlock((Block) childArea);
        }
    }

    /**
     * {@inheritDoc}
     */
    public int negotiateBPDAdjustment(int adj, KnuthElement lastElement) {
        // TODO Auto-generated method stub
        return 0;
    }

    /**
     * {@inheritDoc}
     */
    public void discardSpace(KnuthGlue spaceGlue) {
        // TODO Auto-generated method stub
    }

    /** {@inheritDoc} */
    public Keep getKeepTogether() {
        Keep keep = Keep.KEEP_AUTO;
        if (primaryGridUnit.getRow() != null) {
            keep = Keep.getKeep(primaryGridUnit.getRow().getKeepTogether());
        }
        keep = keep.compare(getParentKeepTogether());
        return keep;
    }

    /** {@inheritDoc} */
    public Keep getKeepWithNext() {
        return Keep.KEEP_AUTO; //TODO FIX ME (table-cell has no keep-with-next!)
    }

    /** {@inheritDoc} */
    public Keep getKeepWithPrevious() {
        return Keep.KEEP_AUTO; //TODO FIX ME (table-cell has no keep-with-previous!)
    }

    // --------- Property Resolution related functions --------- //

    /**
     * Returns the IPD of the content area
     * @return the IPD of the content area
     */
    public int getContentAreaIPD() {
        return cellIPD;
    }

    /**
     * Returns the BPD of the content area
     * @return the BPD of the content area
     */
    public int getContentAreaBPD() {
        if (curBlockArea != null) {
            return curBlockArea.getBPD();
        } else {
            log.error("getContentAreaBPD called on unknown BPD");
            return -1;
        }
    }

    /**
     * {@inheritDoc}
     */
    public boolean getGeneratesReferenceArea() {
        return true;
    }

    /**
     * {@inheritDoc}
     */
    public boolean getGeneratesBlockArea() {
        return true;
    }

}
