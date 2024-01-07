package classes;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;

/**
 * @author Lukina Alexandra
 * <p>Class magazines</p>
 */
@Entity
@Table(name="magazine")
public class Magazine implements CommonEntity{
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "magazine_id")
	private long magazine_id;
	
	@Column(name = "nomenclature")
	private String nomenclature;
	
	@Column(name = "amount")
	private int amount;
	
	@ManyToMany(fetch = FetchType.EAGER ) 
    @JoinTable(
        name = "client_magazine", 
        joinColumns = @JoinColumn(name = "magazine_id"), 
        inverseJoinColumns = @JoinColumn(name = "person_id"))
	private List<Client> clients;
	
	
	public Magazine() { clients = new ArrayList<Client>(); }
	public Magazine(long _id) {
		magazine_id =  _id;
		clients = new ArrayList<Client>();
		}
	
	public long GetId() { return magazine_id;}
	public void SetId (long _id) { magazine_id = _id;}
	
	public boolean SetNomenclature(String _nomenclature) {
		if (_nomenclature != null && _nomenclature.length() > 0) {
			nomenclature = _nomenclature;
			return true;
		}
		return false;
	}
	public String GetNomenclature(){
		return nomenclature;
	}
	
	public boolean SetAmount(int new_amount) { 
		if(new_amount < 0) {
            return false;
        }
        this.amount = new_amount;
    	return true;
	}
	public int GetAmount(){
		return amount;
	}
	
	public void SetClients(List<Client> new_clients) {
		clients = new_clients;
	}
	public List<Client> GetClients() { return clients; }
	
	public boolean AddClient(Client client) {
		if (client != null) {
			clients.add(client);
			return true;
		}
		else return false;
	}
	
	@Override
	public Object[] toStringArray() {
		var arr = new Object[3];
		
		arr[0] = Long.toString(magazine_id);
		arr[1] = nomenclature;
		arr[2] = Integer.toString(amount);
		
		return arr;
	}
	
}
