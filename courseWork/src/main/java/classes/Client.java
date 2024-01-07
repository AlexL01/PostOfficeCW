package classes;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;

/**
 * @author Lukina Alexandra
 * <p>Class clients</p>
 */
@Entity
@Table(name = "client")
public class Client extends Person implements CommonEntity {
	@ManyToMany(mappedBy = "clients", fetch = FetchType.EAGER) 
	private List<Magazine> magazines;
	
	@ManyToOne(targetEntity = Address.class, fetch = FetchType.EAGER, cascade = {CascadeType.MERGE})
	@JoinColumn(name = "address_id")
	private Address address;
	
	public Client() {
		super();
		magazines = new ArrayList<Magazine>();
	}
	
	public Client(int _id) {
		super(_id);
		magazines = new ArrayList<Magazine>(); 
	}
	
	public void SetMagazines(ArrayList<Magazine> new_magazines) {
		magazines = new_magazines;
	}
	public List<Magazine> GetMagazines(){
		return magazines;
	}
	
	public void SetAddress(Address new_address) {
		address = new_address;
	}
	public Address GetAddress(){
		return address;
	}

	@Override
	public Object[] toStringArray() {
		var arr = new Object[4];
		
		arr[0] = Long.toString(id);
		arr[1] = name;
		arr[2] = lastName;
		arr[3] = address.GetName();
		
		return arr;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj == null)
			return false;
		if(obj instanceof Client)
			return ((Client) obj).GetId() == id;
		else return false;
	}
}