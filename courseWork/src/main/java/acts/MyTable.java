package acts;

import java.awt.Color;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public final class MyTable extends JTable{
	private static final long serialVersionUID = 1L;
	private static final Logger log = LogManager.getLogger(display.class);
	
	protected DefaultTableModel model;
	protected int editedRow;
	protected int countRow;
	
	public MyTable(String[] headers) {
		super(new DefaultTableModel(headers, 0));
		editedRow = -1;
		getTableHeader().setReorderingAllowed(false);
		this.getSelectionModel().setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		model = (DefaultTableModel) this.getModel();
		countRow = model.getRowCount();
		this.setSelectionBackground(Color.cyan);
		this.setBackground(new Color(204, 204, 153));
		log.info("Table created");
	}
	
	@Override
	public boolean isCellEditable(int row, int col) {
		return row == editedRow;
	}
	
	public void clean() {
		while(countRow > 0)
			deleteRow(0);
	}
	
	public void startEditRow(int row) { editedRow = row; }
	public void stopEditRow() { editedRow = -1; }
	
	public void addRow() {
		log.info("Add row");
		model.addRow(display.Columns[display.Columns.length - 1]);
		++countRow;
	}
	
	public void deleteRow(int row) {
		this.clearSelection();
		this.editCellAt(-1, -1);
		stopEditRow();
		--countRow;
		model.removeRow(row);
	}
	
	public int getColumnIndex(String name) {
		for(int i = 0; i < this.getColumnCount(); ++i)
			if(name.equals(this.getColumnName(i)))
				return i;
		return -1;
	}
	
	public Object[] getRow(int index) {
		var row = new Object[this.getColumnCount()];
		for(int i = 0; i < row.length; ++i)
			row[i] = this.getValueAt(index, i);
		return row;
	}
	
	public void fillRow(Object[] content, int row) {
		if(content != null && this.getColumnCount() <= content.length) {
			for (int i = 0; i < content.length && i < getColumnCount(); ++i)
				if(content[i] != null)
					setValueAt(content[i].toString(), row, i);
				else
					setValueAt("", row, i);
		}
	}
}
