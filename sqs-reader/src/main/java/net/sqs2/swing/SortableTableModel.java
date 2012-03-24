/**
 * 
 */
package net.sqs2.swing;

import java.util.Collections;
import java.util.Comparator;
import java.util.Vector;

import javax.swing.table.DefaultTableModel;

public class SortableTableModel extends DefaultTableModel {

	private static final long serialVersionUID = 1L;

	public SortableTableModel(int numRows, int numColumns) {
		super(numRows, numColumns);
	}

	@SuppressWarnings("unchecked")
	public void sort(int columnIndex, boolean isAscent) {
		Collections.sort(getDataVector(), new ColumnComparator(columnIndex, isAscent));
		fireTableDataChanged();
	}

	@SuppressWarnings("unchecked")
	public void sort(int[] columnIndices, boolean isAscent) {
		Collections.sort(getDataVector(), new ColumnComparator(columnIndices, isAscent));
		fireTableDataChanged();
	}

	class ColumnComparator implements Comparator<Object> {
		protected int[] indices;
		protected boolean ascending;

		public ColumnComparator(int index, boolean ascending) {
			this.indices = new int[] { index };
			this.ascending = ascending;
		}

		public ColumnComparator(int[] indices, boolean ascending) {
			this.indices = indices;
			this.ascending = ascending;
		}

		@SuppressWarnings("unchecked")
		public int compare(Object one, Object two) {
			if (one instanceof Vector && two instanceof Vector) {
				for (int index : this.indices) {
					Object oOne = ((Vector) one).elementAt(index);
					Object oTwo = ((Vector) two).elementAt(index);
					if (oOne == null && oTwo == null) {
						continue;
					} else if (oOne == null) {
						return this.ascending ? -1 : 1;
					} else if (oTwo == null) {
						return this.ascending ? 1 : -1;
					} else if (oOne instanceof Comparable && oTwo instanceof Comparable) {
						Comparable cOne = (Comparable) oOne;
						Comparable cTwo = (Comparable) oTwo;
						return this.ascending ? cOne.compareTo(cTwo) : cTwo.compareTo(cOne);
					}
				}
			}
			return 0;
		}

		public int compare(Number o1, Number o2) {
			double n1 = o1.doubleValue();
			double n2 = o2.doubleValue();
			if (n1 < n2) {
				return -1;
			} else if (n1 > n2) {
				return 1;
			} else {
				return 0;
			}
		}
	}
}
