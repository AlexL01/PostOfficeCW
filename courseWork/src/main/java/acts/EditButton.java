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

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import classes.*;
import main.Main;

final class EditButton implements ActionListener {
	private static final Logger log = LogManager.getLogger(EditButton.class);
	display display;
	MyTable table;

	public EditButton(display _display) { display = _display; }
	
	private void checkName (JTextField bName) throws Exception {
		String sName = bName.getText();
		if (sName.length() == 0 || sName.length() > 100) throw new Exception();
	}
	
	public void actionPerformed(ActionEvent e) {
		log.info("Start edit row");
		table = display.tables[display.currentClass];
		var selectedRows = display.tables[display.currentClass].getSelectedRows();
		
		if(selectedRows.length == 0) return;
		
		int result;
		var emf = Persistence.createEntityManagerFactory("test");
		var em = emf.createEntityManager();
		JPanel myPanel = new JPanel();	
		var dataPosts = Main.megaList.get(3).stream().map(posts -> ((Postman)posts).GetName()).toArray(String[]::new);
		DefaultListModel<String> men = new DefaultListModel<String>();
		for (String string : dataPosts) men.addElement(string);
		JList<String> lst_single = new JList<String>(men);
		lst_single.setPrototypeCellValue("Установленный");
		lst_single.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		switch (display.currentClass) {
    		case 1:
    			var clientid = Main.megaList.get(display.currentClass).get(selectedRows[0]).GetId();
    			
    			em.getTransaction().begin();
    			Client client = em.find(Client.class, clientid);
    			
    			var dataMags = Main.megaList.get(2).stream().map(mag -> ((Magazine)mag).GetNomenclature()).toArray(String[]::new);
				
				
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
				
		        
		        DefaultListModel<String> mags = new DefaultListModel<String>();
		        for (String string : dataMags) mags.addElement(string);
		        JList<String> multiple = new JList<String>(mags);
		        multiple.setPrototypeCellValue("Установленный");
		        
		        multiple.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		        myPanel.add(new JScrollPane(lst_single));
		        myPanel.add(new JLabel("Magazines: "));
		        myPanel.add(new JScrollPane(multiple));
		        
		        name.setText(client.GetName());
		        lastname.setText(client.GetLastName());
		        address.setText(client.GetAddress().GetName());
		        
		        result = JOptionPane.showConfirmDialog(null, myPanel, "Edit", JOptionPane.OK_CANCEL_OPTION);
		        if (result == JOptionPane.OK_OPTION) {
		        	try {
	                    checkName(name);
	                    checkName(lastname);
	                    client.SetName(name.getText());
	                    client.SetLastName(lastname.getText());
	                    
	                    Address addr = null;
	                    for(var ent: Main.megaList.get(0)) {
	                    	if(address.getText().equals(((Address)ent).GetName())) {
	                    		addr = (Address)ent;
	                    		break;
	                    	}
	                    }
	                    
	                    if(addr == null) {
	                    	addr = new Address();
	                    	addr.SetName(address.getText());
	                    	Main.megaList.get(0).add(addr);
	                    	addr.SetPostman((Postman)Main.megaList.get(3).get(lst_single.getSelectedIndex()));
	                    	((Postman)Main.megaList.get(3).get(lst_single.getSelectedIndex())).AddAddress(addr);
	                    	
	                    	
	                    	em.persist(addr);
	                    	
	                    	display.tables[0].addRow();
	                    	display.tables[0].fillRow(addr.toStringArray(), display.tables[0].countRow - 1);
	                    }
	                    
	                    client.GetAddress().GetClients().remove(client); 
	                    client.SetAddress(addr);
	                    
	                    client.GetMagazines().clear();
						em.createNativeQuery("DELETE FROM client_magazine WHERE person_id=" + client.GetId()).executeUpdate();
	                    
	                    ArrayList<Magazine> listMag = new ArrayList<Magazine>();
	                    for(var n: multiple.getSelectedIndices()) {
	                    	listMag.add((Magazine)Main.megaList.get(2).get(n));
	                    }
	                    
	                    client.SetMagazines(listMag);
	                    
	                    for(var i: listMag) {
	                    	i.AddClient(client); 
	                    	em.createNativeQuery("INSERT INTO client_magazine VALUES (" + client.GetId() + "," + i.GetId() + ")").executeUpdate();
	                    }
	                    
	                    em.getTransaction().commit();
	                    
		        	} catch (Exception ex) {
	                    JOptionPane.showMessageDialog(null, "You entered a wrong data");
	                }
		        }
		        break;
    		case 0:
    			var addressid = Main.megaList.get(display.currentClass).get(selectedRows[0]).GetId();
    			
    			em.getTransaction().begin();
    			Address addressE = em.find(Address.class, addressid);
    			
    			JTextField nameA = new JTextField(15);
	            
	            myPanel.setSize(100, 100);
	            myPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
	            
	            myPanel.add(new JLabel("Name: "));
	            myPanel.add(nameA);
	            myPanel.add(new JLabel("Postman: "));
	            
	            myPanel.add(new JScrollPane(lst_single));
	            nameA.setText(addressE.GetName());
	            
            	result = JOptionPane.showConfirmDialog(null, myPanel, "Edit", JOptionPane.OK_CANCEL_OPTION);
            	if(result == JOptionPane.OK_OPTION) {
            		try {
            		checkName(nameA);
                    addressE.SetName(nameA.getText());
                    
                    addressE.SetPostman((Postman)Main.megaList.get(3).get(lst_single.getSelectedIndex()));
                    
                    em.getTransaction().commit();
                    
                	
            		} catch (Exception ex) {
	                    JOptionPane.showMessageDialog(null, "You entered a wrong data");
	                }
            	}
    			break;
    		case 2:
    			var magazineid = Main.megaList.get(display.currentClass).get(selectedRows[0]).GetId();
    			
    			em.getTransaction().begin();
    			Magazine magazine = em.find(Magazine.class, magazineid);
    			
    			JTextField nomenclature = new JTextField(15);
    			JTextField amount = new JTextField(15);
	            
	            myPanel.setSize(100, 100);
	            myPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
	            
	            myPanel.add(new JLabel("Nomenclature: "));
	            myPanel.add(nomenclature);
	            myPanel.add(new JLabel("Amount: "));
	            myPanel.add(amount);
	            
	            nomenclature.setText(magazine.GetNomenclature());
	            amount.setText(Integer.toString(magazine.GetAmount()));
	            
	            int h;
            	result = JOptionPane.showConfirmDialog(null, myPanel, "Edit", JOptionPane.OK_CANCEL_OPTION);
            	if(result == JOptionPane.OK_OPTION) {
            		try {
            		h = Integer.parseInt(amount.getText());
            		checkName(nomenclature);
            		
            		magazine.SetNomenclature(nomenclature.getText());
                    magazine.SetAmount(h);
                    
                    em.getTransaction().commit();
                    
                	
            		} catch (Exception ex) {
	                    JOptionPane.showMessageDialog(null, "You entered a wrong data");
	                }
            	}
    			break;
    		case 3:
    			var postmanid = Main.megaList.get(display.currentClass).get(selectedRows[0]).GetId();
    			
    			em.getTransaction().begin();
    			Postman postman = em.find(Postman.class, postmanid);
    			
    			JTextField nameP = new JTextField(15);
    			JTextField lastnameP = new JTextField(15);
	            
	            myPanel.setSize(100, 100);
	            myPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
	            
	            myPanel.add(new JLabel("Name: "));
		        myPanel.add(nameP);
		        myPanel.add(new JLabel("Lastname: "));
		        myPanel.add(lastnameP);
	            
		        nameP.setText(postman.GetName());
		        lastnameP.setText(postman.GetLastName());
	            
            	result = JOptionPane.showConfirmDialog(null, myPanel, "Edit", JOptionPane.OK_CANCEL_OPTION);
            	if(result == JOptionPane.OK_OPTION) {
            		try {
            		
            		postman.SetName(nameP.getText());
            		postman.SetLastName(lastnameP.getText());
                    
                    em.getTransaction().commit();
                    
                	
            		} catch (Exception ex) {
	                    JOptionPane.showMessageDialog(null, "You entered a wrong data");
	                }
            	}
    			
    			break;
		}
			display.downloadBD();
			display.redrawTables();
	}
}
