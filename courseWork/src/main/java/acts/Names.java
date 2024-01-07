package acts;

import classes.*;

public class Names {
	
	public static final String[] SQLTables = {"address", "client", "magazine", "postman" };
	
	@SuppressWarnings("rawtypes")
	public static final Class[] TableClass = {Address.class, Client.class, Magazine.class, Postman.class };
	
	private Names() {};
}
