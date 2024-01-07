package acts;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.persistence.Persistence;
import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;

import org.apache.log4j.Logger;

import classes.*;
import main.Main;


final class AddButton implements ActionListener {
	private display display;
	@SuppressWarnings("unused")
	private MyTable table;

	private static final Logger log = Logger.getLogger("AddButton.class");
	
	public AddButton(display _display) { display = _display; }
	
	private void checkName (JTextField bName) throws Exception {
		String sName = bName.getText();
		if (sName.length() == 0 || sName.length() > 100) throw new Exception();
	}
	
	public void actionPerformed(ActionEvent e) {
		log.info("Adding started");
		table = display.tables[display.currentClass];
		var dataPosts = Main.megaList.get(3).stream().map(posts -> ((Postman)posts).GetName()).toArray(String[]::new);
		var dataMags = Main.megaList.get(2).stream().map(mag -> ((Magazine)mag).GetNomenclature()).toArray(String[]::new);
		var emf = Persistence.createEntityManagerFactory("test");
		var em = emf.createEntityManager();
		JPanel myPanel = new JPanel();
		int result;
		
		switch (display.currentClass) {
        	case 1:
	            JTextField name = new JTextField(15);
	            JTextField lastname = new JTextField(15);
	            JTextField address = new JTextField(15);
	            
	            myPanel.setSize(100, 100);
	            myPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
	            
	            myPanel.add(new JLabel("Name: "));
	            myPanel.add(name);
	            myPanel.add(new JLabel("Lastname: "));
	            myPanel.add(lastname);
	            myPanel.add(new JLabel("Address: "));
	            myPanel.add(address);
	            myPanel.add(new JLabel("Postman: "));
	            
	            DefaultListModel<String> men = new DefaultListModel<String>();
	            DefaultListModel<String> mags = new DefaultListModel<String>();
	            for (String string : dataPosts) men.addElement(string);
	            for (String string : dataMags) mags.addElement(string);
	            
	            JList<String> lst_single = new JList<String>(men);
	            JList<String> multiple = new JList<String>(mags);
	            
	            lst_single.setPrototypeCellValue("Установленный"); // Размер ячейки
	            multiple.setPrototypeCellValue("Установленный");
	            lst_single.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	            multiple.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
	            myPanel.add(new JScrollPane(lst_single));
	            myPanel.add(new JLabel("Magazines: "));
	            myPanel.add(new JScrollPane(multiple));
	            
	            result = JOptionPane.showConfirmDialog(null, myPanel, "Add a new client", JOptionPane.OK_CANCEL_OPTION);
	            if (result == JOptionPane.OK_OPTION) {
	                try {
	                    checkName(name);
	                    checkName(lastname);
	                    Client man = new Client();
	                    Main.megaList.get(1).add(man);
	                    man.SetName(name.getText());
	                    man.SetLastName(lastname.getText());
	                    
	                    Address addr = null;
	                    for(var ent: Main.megaList.get(0)) {
	                    	if(address.getText().equals(((Address)ent).GetName())) {
	                    		addr = (Address)ent;
	                    		JOptionPane.showMessageDialog(null, "Postman selected according to address");
	                    		break;
	                    	}
	                    }
	                    
	                    if(addr == null) {
	                    	addr = new Address();
	                    	addr.SetName(address.getText());
	                    	Main.megaList.get(0).add(addr);
	                    	addr.SetPostman((Postman)Main.megaList.get(3).get(lst_single.getSelectedIndex()));
	                    	((Postman)Main.megaList.get(3).get(lst_single.getSelectedIndex())).AddAddress(addr);
	                    	
	                    	em.getTransaction().begin();
	                    	em.persist(addr);
	                    	em.getTransaction().commit();
	                    	
	                    	display.tables[0].addRow();
	                    	display.tables[0].fillRow(addr.toStringArray(), display.tables[0].countRow - 1);
	                    }
	                    man.SetAddress(addr);
	                    
	                    addr.GetClients().add(man);
	                    
	                    ArrayList<Magazine> listMag = new ArrayList<Magazine>();
	                    for(var n: multiple.getSelectedIndices()) {
	                    	listMag.add((Magazine)Main.megaList.get(2).get(n));
	                    }
	                    
	                    man.SetMagazines(listMag);
	                    
	                    
	                    em.getTransaction().begin();
	                    em.persist(man);
	                    em.getTransaction().commit();
	        			
	        			display.tables[1].addRow();
	                	display.tables[1].fillRow(man.toStringArray(), display.tables[1].countRow - 1);
	                
	                	for(var i: listMag) System.out.println(i.GetId());
	                	
	                	for(var i: listMag) {
	                    	i.AddClient(man);
	                    	
	                    	em.getTransaction().begin();
	                    	em.createNativeQuery("INSERT INTO client_magazine VALUES (" + man.GetId() + "," + i.GetId() + ")").executeUpdate();
	                    	em.getTransaction().commit();
	                    }
	                	
	                    JOptionPane.showMessageDialog(null, "Data is successfully added!");
	                } 
	                catch (Exception ex) {
	                    JOptionPane.showMessageDialog(null, "You entered a wrong data");
	                }
	            }
	            break;
        	case 2:
        		JTextField nomenclature = new JTextField(15);
	            JTextField amount = new JTextField(15);
	            
	            myPanel.setSize(100, 100);
	            myPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
	            
	            myPanel.add(new JLabel("Nomenclature: "));
	            myPanel.add(nomenclature);
	            myPanel.add(new JLabel("Amount: "));
	            myPanel.add(amount);
	            
            	int h;
            	result = JOptionPane.showConfirmDialog(null, myPanel, "Add a new magazine", JOptionPane.OK_CANCEL_OPTION);
            	if(result == JOptionPane.OK_OPTION)
	            	try {
	            		h = Integer.parseInt(amount.getText());
	            		
	            		checkName(nomenclature);
	            		Magazine magazine = new Magazine();
	                    Main.megaList.get(2).add(magazine);
	                    magazine.SetNomenclature(nomenclature.getText());
	                    magazine.SetAmount(h);
	                    
	                    em.getTransaction().begin();
	                    em.persist(magazine);
	                    em.getTransaction().commit();
	                    
	                    display.tables[2].addRow();
	                	display.tables[2].fillRow(magazine.toStringArray(), display.tables[2].countRow - 1);
	                	
	                	JOptionPane.showMessageDialog(null, "Data is successfully added!");
	            	} catch (Exception ex) {
	            		JOptionPane.showMessageDialog(null, "You entered a wrong data");
	            	}
	            break;
        	case 3:
        		JTextField nameP = new JTextField(15);
	            JTextField lastnameP = new JTextField(15);
	            
	            myPanel.setSize(100, 100);
	            myPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
	            
	            myPanel.add(new JLabel("Name: "));
	            myPanel.add(nameP);
	            myPanel.add(new JLabel("Lastname: "));
	            myPanel.add(lastnameP);
        		
	            result = JOptionPane.showConfirmDialog(null, myPanel, "Add a new postman", JOptionPane.OK_CANCEL_OPTION);
	            if (result == JOptionPane.OK_OPTION) {
	                try {
	                    checkName(nameP);
	                    checkName(lastnameP);
	                    Postman postman = new Postman();
	                    Main.megaList.get(3).add(postman);
	                    postman.SetName(nameP.getText());
	                    postman.SetLastName(lastnameP.getText());
	                    
	                    em.getTransaction().begin();
	                    em.persist(postman);
	                    em.getTransaction().commit();
                    	
	                    display.tables[3].addRow();
	                	display.tables[3].fillRow(postman.toStringArray(), display.tables[3].countRow - 1);
	                	
	                    
	                 JOptionPane.showMessageDialog(null, "Data is successfully added!");
	                } catch (Exception ex) {
	                	JOptionPane.showMessageDialog(null, "You entered a wrong data");
	                }
	            }
	            break;
        	case 0:
        		JOptionPane.showMessageDialog(null, "Address can be created only by the creation of a client");
        	
		}
		emf.close();
		em.close();
		log.info("End of add data");
	}
}