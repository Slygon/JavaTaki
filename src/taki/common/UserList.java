package taki.common;

import java.io.Serializable;
import java.util.ArrayList;

public class UserList implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6985063962976746407L;

	private ArrayList<String> _alUsers;

	public ArrayList<String> getUsers() {
		return _alUsers;
	}

	public UserList(ArrayList<String> users) {
		_alUsers = users;
	}
}
