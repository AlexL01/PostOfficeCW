package classes;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;

/**<p>Class addresses</p>*/
@Entity
@Table(name = "address")
public class Address implements CommonEntity {
	@Id
	@Column(name = "address_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	@Column(name = "name")
	private String name;
	
	@ManyToOne(targetEntity = Postman.class, fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
	@JoinColumn(name = "person_id")
	private Postman postman;
	
	@OneToMany(	targetEntity = Client.class, fetch = FetchType.EAGER ,  mappedBy = "address", cascade = {CascadeType.MERGE , CascadeType.REMOVE})
	private List<Client> client;
	
	public Address() { client = new ArrayList<Client>(); }
	public Address(int _id) {
		id = _id;
		client = new ArrayList<Client>();
	} 
	
	public long GetId() { return id;}
	public void SetId(long _id) { id = _id;}
	
	public boolean SetName(String _name) {
		if (_name != null && _name.length() > 0) {
			name = _name;
			return true;
		}
		return false;
	}
	public String GetName(){
		return name;
	}
	
	public void SetPostman(Postman new_postman) {
		postman = new_postman;
	}
	public Postman GetPostman(){
		return postman;
	}
	
	public void SetClients(List<Client> new_client) {
		client = new_client;
	}
	public List<Client> GetClients() {
		return client;
	}
	
	@Override
	public Object[] toStringArray() {
		var arr = new Object[3];
		
		arr[0] = Long.toString(id);
		arr[1] = name;
		arr[2] = postman.GetName();
		
		return arr;
	}
}
