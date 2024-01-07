package acts;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

import javax.persistence.Persistence;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JToolBar;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import classes.*;
import main.Main;


public class display{
    private static final Logger log = LogManager.getLogger(display.class);

	protected int width, height;
	protected int currentClass;
	
	/**���� � �������� ����������� */
	protected JFrame window;
	
	protected JButton info;
	protected JButton delete;
	protected JButton add;
	protected JButton edit;
	protected JButton download;
	protected JButton mags;
	protected JButton readers;
	protected JButton address;
	protected JButton residents;
	
	protected JButton[] classes;
	protected JButton filter;
	
	/**������ � �������� ������������, ������ � ������ */
	protected JToolBar toolBar;
	protected JToolBar classesBar;
	protected JToolBar searchBar;
	
	protected ArrayList<JComboBox<String>> searchOpt;
	protected JScrollPane[] scroll;
	protected MyTable[] tables;
	protected JTextField search;
	protected int afterSearch;
	
	String[] Classes = { "Addresses", "Clients", "Magazines", "Postmen", "" };
	static String[][] Columns = {
		{"ID", "Name", "Postman"},
		{"ID", "Name", "Lastname", "Address"},
		{"ID", "Nomenclature", "Amount"}, 
		{"ID", "Name", "Lastname"}, 
		{}
	};
	
	
	
	public display(int _wid, int _hght) {
		log.info("Main menu is preparing");
		width = _wid;
		height = _hght;
		currentClass = Classes.length - 1;
		afterSearch = -1;
		
		window = new JFrame("Post Office");
		window.setSize(width, height);
		window.setLocation(100, 100);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		//�������� ������ � ������������ ������. ��������� ��������� ��� ������
		info = new JButton(new ImageIcon("./src/main/resources/icons/information.png"));
		info.setToolTipText("Information");
		
		delete = new JButton(new ImageIcon("./src/main/resources/icons/folder_remove.png"));
		delete.setToolTipText("Delete");
		
		add = new JButton(new ImageIcon("./src/main/resources/icons/add_folder.png"));
		add.setToolTipText("Add");
		
		edit = new JButton(new ImageIcon("./src/main/resources/icons/edit.png"));
		edit.setToolTipText("Edit");
		
		download = new JButton(new ImageIcon("./src/main/resources/icons/download.png"));
		download.setToolTipText("Print to pdf");
		
		mags = new JButton(new ImageIcon("./src/main/resources/icons/newspaper.png"));
		mags.setToolTipText("Magazines");
		mags.setEnabled(false);
		
		readers = new JButton(new ImageIcon("./src/main/resources/icons/avatar.png"));
		readers.setToolTipText("Readers");
		readers.setEnabled(false);
		
		address = new JButton(new ImageIcon("./src/main/resources/icons/address.png"));
		address.setToolTipText("Address");
		address.setEnabled(false);
		
		residents = new JButton(new ImageIcon("./src/main/resources/icons/icon-agent.png"));
		residents.setToolTipText("Residents");
		residents.setEnabled(false);
	
		// ������ ������
		toolBar = new JToolBar("Toolbar");
		toolBar.setFloatable(false);
		toolBar.add(info);
		toolBar.add(delete);
		toolBar.add(add);
		toolBar.add(edit);
		toolBar.add(download);
		toolBar.add(mags);
		toolBar.add(readers);
		toolBar.add(address);
		toolBar.add(residents);
		
		
		classesBar = new JToolBar();
		classesBar.setFloatable(false);
		classesBar.setLayout(new GridLayout(Classes.length, 1));
		
		classes = new JButton[Classes.length];
		searchOpt = new ArrayList<JComboBox<String>>();
		tables = new MyTable[Columns.length];
		scroll = new JScrollPane[Columns.length];
		// ������ ������
		for(int i = 0; i < classes.length; ++i) {
			tables[i] = new MyTable(Columns[i]);
			scroll[i] = new JScrollPane(tables[i]);
			classes[i] = new JButton(Classes[i]);
			searchOpt.add(new JComboBox<String>(Columns[i]));
			classesBar.add(classes[i]);
		};
		
		// ������ �����
		search = new JTextField("Enter data");
		filter = new JButton("Search");
		
		searchBar = new JToolBar();
		searchBar.setLayout(new BorderLayout());
		searchBar.setFloatable(false);
		searchBar.add(search, BorderLayout.CENTER);
		searchBar.add(filter, BorderLayout.EAST);
		searchBar.add(searchOpt.get(searchOpt.size() - 1), BorderLayout.WEST);

		window.setLayout(new BorderLayout());
		window.add(toolBar, BorderLayout.NORTH);
		
		window = new JFrame("Post Office");
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setSize(width, height);
		window.setLocationRelativeTo(null);
		window.setLayout(new BorderLayout());
		window.add(toolBar, BorderLayout.NORTH);
		window.add(searchBar, BorderLayout.SOUTH);
		window.add(classesBar, BorderLayout.EAST);
		
		//���������� ������� � �������
		window.add(scroll[scroll.length - 1], BorderLayout.CENTER);
		
		show();
		downloadBD();
		redrawTables();
	}
	
	private void show() {
		log.info("Launch display");
		for(int i = 0; i < classes.length; ++i) {
			final int _i = i;
			classes[i].addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					log.info("Moving through tables");
					if(afterSearch == 1) {
						downloadBD();
						redrawTables();
						afterSearch = -1;
					}
					searchBar.remove(searchOpt.get(currentClass));
					searchBar.add(searchOpt.get(_i), BorderLayout.WEST);
					searchBar.repaint();
					searchBar.revalidate();
					window.remove(scroll[currentClass]);
					window.add(scroll[_i], BorderLayout.CENTER);
					window.revalidate();
					classes[currentClass].setEnabled(true);
					currentClass = _i;
					classes[currentClass].setEnabled(false);
					
					residents.setEnabled(currentClass == 0);
					mags.setEnabled(currentClass == 1);
					readers.setEnabled(currentClass == 2);
					address.setEnabled(currentClass == 3);
					add.setEnabled(currentClass != 4);
					delete.setEnabled(currentClass != 4);
					edit.setEnabled(currentClass != 4);
					download.setEnabled(currentClass != 4);
					filter.setEnabled(currentClass != 4);
				}
			});
		}
		
		/** <p>������ ������. ������� �� ��� � �����</p>*/
		search.addKeyListener(new KeyListener() {
			public void keyTyped(KeyEvent e) {}
			public void keyPressed(KeyEvent e) {}
			public void keyReleased(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_ENTER && currentClass != 4)
					filter.getActionListeners()[0].actionPerformed(null);
			}
			
		});
		filter.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				log.info("Click on the search button");
				tables[currentClass].selectAll();
				
				int colIndex = tables[currentClass].getColumnIndex(searchOpt.get(currentClass).getSelectedItem().toString());
				var selectionModel = tables[currentClass].getSelectionModel();
				
				for(int i = 0; i < tables[currentClass].countRow; ++i)
					if(tables[currentClass].getValueAt(i, colIndex).toString().equals(search.getText())) {
						selectionModel.removeSelectionInterval(i, i);
					}
				
				var selection = tables[currentClass].getSelectedRows();	
				for(int i = selection.length - 1; i >= 0; --i)
					Main.megaList.get(currentClass).remove(selection[i]);
				
				redrawTables();

				afterSearch = 1;
				JOptionPane.showMessageDialog(window, "����� ��������.");
			}
		});
		
		
		/**<p>������ �������. ������� �� ��� � ����� ����������� ����</p>*/
		info.addActionListener (new ActionListener() {
			public void actionPerformed (ActionEvent event){
				log.info("Click on the information button");
				JOptionPane.showMessageDialog(window, "�������������� ������ ��� ������������ ����������.\n"
					+ "\n��������� ��������:"
					+ "\n���������� - ��������� ��� ���� � ����� ���� � ������ ������ �������������" 
					+ "\n�������� - ������� ������ ������ �������, ������ �� ������."
					+ "\n����������� � pdf - ������ �� ������ � ����� �� ������� ����� ������ � ����� �������"
					+ "\n�������������� - ������� ������ ������, ������ ������. �������� ������ ���� � ������ ������ �������������"
					+ "\n����� - � ������ ������� �������� �������, � ���� ������ �������� ��� ������. ����� ������� ��� ���������� ������ � �������� �� ������."
					+ "\n������� - � ������� Clents ������ �� ������ � ������ ���������, � ����� ���� ����� �������� ��� ��� �������/������"
					+ "\n������ - � ������� Postmen ������ �� ������ � ������ �����������, � ����� ���� ����� �������� ��� ��������� � ��� ������"
					+ "\n�������� - � ������� Magazines ������ �� ������ � ������ ��������/�������, � ����� ���� ����� �������� ��� ��������"
					+ "\n������ - � ������� Addresses ������ �� ������ � ������ �������, � ����� ���� ����� �������� ��� ����, ������� ���");
			}
		});
		
		/**<p>���������� ����� � �������</p>*/
		add.addActionListener(new AddButton(this));
		
		/** <p>�������� ����� �� �������</p>*/
		delete.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				log.info("Click on the delete button");
				
				var selectedRows = tables[currentClass].getSelectedRows();
				var emf = Persistence.createEntityManagerFactory("test");
				var em = emf.createEntityManager();
				if(JOptionPane.showConfirmDialog(window, "Delete all selected rows?") != JOptionPane.OK_OPTION)
					return;
					
				for(int row = selectedRows.length - 1; row >= 0; --row) {
					tables[currentClass].deleteRow(selectedRows[row]);
					
					var obj = Main.megaList.get(currentClass).get(selectedRows[row]);
					
					em.getTransaction().begin();
					em.remove(em.contains(obj) ? obj : em.merge(obj));
                    em.getTransaction().commit();
                    
                    
					if(currentClass == 1) {
						em.getTransaction().begin();
						em.createNativeQuery("DELETE FROM client_magazine WHERE person_id=" + obj.GetId()).executeUpdate();
						em.getTransaction().commit();
					} else 
					if(currentClass == 2) {
						em.getTransaction().begin();
						em.createNativeQuery("DELETE FROM client_magazine WHERE magazine_id=" + obj.GetId()).executeUpdate();
						em.getTransaction().commit();
					}
				}
				log.info("Delete successful");
				if(afterSearch != 1) {
					downloadBD();
				}
				redrawTables();
				
				emf.close();
				em.close();
			}
	    });

		/**<p>�������� ������ �� ������� � pdf ����</p>*/
		download.addActionListener(new ExportPdf(this));
		
		edit.addActionListener(new EditButton(this));
		
		mags.addActionListener (new ActionListener() { // in client table
			public void actionPerformed (ActionEvent event) {
				log.info("Click on the magazines button");
				
				int selected = tables[currentClass].getSelectedRow();
				if(selected == -1) return;
				
				
				var obj = Main.megaList.get(currentClass).get(selected);
				String data = "";
				data = "�������:\n";
				for(var i : ((Client)obj).GetMagazines()) {
					int n = 0;
					for(var j : i.toStringArray())
						data += Columns[2][n++] + ": '" + j.toString() + "' ";
					data += "\n";
				}
				
				JOptionPane.showMessageDialog(window, data);
			}
		});
		
		readers.addActionListener (new ActionListener() {  //in class magazines
			public void actionPerformed (ActionEvent event) {
				log.info("Click on the reader button");
				int selected = tables[currentClass].getSelectedRow();
				if(selected == -1) return;
				
				var obj = Main.megaList.get(currentClass).get(selected);
				String data = "";
				data = "��������:\n";
				for(var i : ((Magazine)obj).GetClients()) {
					int n = 0;
					for(var j : i.toStringArray())
						data += Columns[1][n++] + ": '" + j.toString() + "' ";
					data += "\n";
					
				}
				
				JOptionPane.showMessageDialog(window, data);
			}
		});
		
		address.addActionListener (new ActionListener() {// in class postmen
			public void actionPerformed (ActionEvent event) {
				log.info("Click on the postman button");
				int selected = tables[currentClass].getSelectedRow();
				if(selected == -1) return;
				
				var obj = Main.megaList.get(currentClass).get(selected);
				String data = "";
				data = "������:\n";
				
				for(var i : ((Postman)obj).GetAddress()) {
					int n = 0;
					for(var j : i.toStringArray())
						data += Columns[0][n++] + ": '" + j.toString() + "' ";
					data += "\n";
				}
				
				JOptionPane.showMessageDialog(window, data);
					
			}
		});
		
		residents.addActionListener (new ActionListener() {// in class address
			public void actionPerformed (ActionEvent event) {
				log.info("Click on the address button");
				int selected = tables[currentClass].getSelectedRow();
				if(selected == -1) return;
				
				var obj = Main.megaList.get(currentClass).get(selected);
				String data = "";
				data = "������:\n";
				
				for(var i : ((Address)obj).GetClients()) {
					int n = 0;
					for(var j : i.toStringArray())
						data += Columns[1][n++] + ": '" + j.toString() + "' ";
					data += "\n";
				}
				JOptionPane.showMessageDialog(window, data);
			}
		});
		
	}
	
	@SuppressWarnings("unchecked")
	public void downloadBD() {
		var emf = Persistence.createEntityManagerFactory("test");
		var em = emf.createEntityManager();
		
		for(int i = 0; i < Names.TableClass.length; ++i) {
			Main.megaList.get(i).clear();
			var res = em.createNativeQuery("SELECT * FROM " + Names.SQLTables[i], Names.TableClass[i]).getResultList();
			Main.megaList.set(i, (ArrayList<CommonEntity>) res);
		}
		
		emf.close();
		em.close();
	}
	
	public void redrawTables() {
		for(var i = 0; i < Classes.length - 1; i ++) {
			tables[i].clean();
			for(var j : Main.megaList.get(i)) {
				tables[i].addRow();
				tables[i].fillRow(j.toStringArray(), tables[i].countRow - 1);
			}
		}
	}
	
	public void setVisible(boolean visible) {
		window.setVisible(visible);
		log.info("Window visible - success");
	}
}
