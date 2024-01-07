package classes;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;

/**
 * @author Lukina Alexandra
 * <p>Class postman</p>
 */
@Entity
@Table(name="postman")
public class Postman extends Person implements CommonEntity {
	@OneToMany(	targetEntity = Address.class, fetch = FetchType.EAGER,
			mappedBy = "postman", cascade = {CascadeType.MERGE, CascadeType.REMOVE})
	private List<Address> addresses;

	public Postman() {
		super();
		addresses = new ArrayList<Address>();
	}
	public Postman(int _id) {
		super(_id);
		addresses = new ArrayList<Address>();
	}

	public void SetAddress(List<Address> new_address) {
		addresses = new_address;
	}
	
	public List<Address> GetAddress(){
		return addresses;
	}
	
	public boolean AddAddress(Address address) {
		if (address != null) {
			addresses.add(address);
			return true;
		}
		else return false;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj == null)
			return false;
		if(obj instanceof Client)
			return ((Postman) obj).GetId() == id;
		else return false;
	}
	
	@Override
	public Object[] toStringArray() {
		var arr = new Object[3];
		
		arr[0] = Long.toString(id);
		arr[1] = name;
		arr[2] = lastName;
		
		return arr;
	}
}