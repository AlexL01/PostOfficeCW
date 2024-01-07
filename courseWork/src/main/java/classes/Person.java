package classes;

import javax.persistence.*;

/**
 * @author Lukina Alexandra
 * <p>Superclass for person</p>
 */
@MappedSuperclass
public class Person{
	@Id
	@Column(name = "person_id")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	protected long id;
	
	@Column(name = "name")
	protected String name;
	
	@Column(name = "last_name")
	protected String lastName;
	
	
	public Person() {};
	public Person(int _id) {id =  _id; }
	
	public long GetId() { return id;}
	public void SetId(long _id) { id = _id;}

	public String GetName() {return name;}
	public boolean SetName(String _name) {
		if(_name == null) return false;
		boolean f = _name.length() > 1 && _name.length() < 100; 
		if(f) name = _name.substring(0, 1).toUpperCase() + _name.substring(1).toLowerCase();
		
		return f;
	}
	
	public String GetLastName() {return lastName;}
	public boolean SetLastName(String _lastName) {
		if(_lastName == null) return false;
		boolean f = _lastName.length() > 1 && _lastName.length() < 100;
		if(f) lastName = _lastName.substring(0, 1).toUpperCase() + _lastName.substring(1).toLowerCase();
		
		return f;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj == null)
			return false;
		if(obj instanceof Person)
			return ((Person) obj).GetId() == id;
		else return false;
	}
	
}