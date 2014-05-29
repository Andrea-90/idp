package org.pssif.consistencyExtern.consistencyGui;

import java.util.List;

import javax.swing.table.AbstractTableModel;

import org.pssif.matchingLogic.MatchMethod;

/**
 * @author Andreas
 * 
 *         This class is an own implementation of the class AbstractTable Model.
 *         It is used to display the possible match methods in a JTable in the
 *         dialogue after the user hit the button "merge the models".
 */
public class MethodChooseTableModel extends AbstractTableModel {

	/**
	 * The names of the column of the table
	 */
	private static final String[] COLUMN_NAMES = { "Methodname", "Weigth",
			"Active?" };

	/**
	 * indexes for the columns
	 */
	private static final int COLUMN_IDX_METHODNAME = 0;
	private static final int COLUMN_IDX_WEIGTH = 1;
	private static final int COLUMN_IDX_ACTIVE = 2;

	/**
	 * the table data
	 */
	private final List<MatchMethod> methods;

	/**
	 * the types of the columns
	 */
	private static final Class<?>[] COLUMN_CLASSES = { String.class,
			Double.class, Boolean.class };

	public MethodChooseTableModel(final List<MatchMethod> methods) {
		this.methods = methods;
	}

	@Override
	public int getRowCount() {
		return methods.size();
	}

	@Override
	public int getColumnCount() {
		return COLUMN_NAMES.length;
	}

	@Override
	public String getColumnName(final int columnIndex) {
		return (COLUMN_NAMES[columnIndex]);
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		final MatchMethod method = methods.get(rowIndex);

		if (columnIndex == COLUMN_IDX_METHODNAME) {
			return method.getMatchMethod().getDescription();
		} else if (columnIndex == COLUMN_IDX_WEIGTH) {
			return method.getWeigth();
		} else if (columnIndex == COLUMN_IDX_ACTIVE) {
			return method.isActive();
		}

		throw new IllegalArgumentException("Invalid column index "
				+ columnIndex);
	}

	@Override
	public Class<?> getColumnClass(final int columnIndex) {
		return COLUMN_CLASSES[columnIndex];
	}

	@Override
	public boolean isCellEditable(final int rowIndex, final int columnIndex) {
		return (columnIndex > COLUMN_IDX_METHODNAME);
	}

	@Override
	public void setValueAt(final Object value, final int rowIndex,
			final int columnIndex) {
		final MatchMethod method = methods.get(rowIndex);

		if (columnIndex == COLUMN_IDX_WEIGTH) {
			method.setWeigth((Double) value);
			// TODO users have to enter "1.0" for the weigth being correctly
			// applied. If the user enters "1" the weight isn't correctly
			// applied.
		} else if (columnIndex == COLUMN_IDX_ACTIVE) {
			method.setActive((Boolean) value);
		}

		fireTableCellUpdated(rowIndex, columnIndex);
	}

}
