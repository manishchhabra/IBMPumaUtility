import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.naming.NamingException;

import com.ibm.portal.um.Group;
import com.ibm.portal.um.PagingIterator;
import com.ibm.portal.um.PumaController;
import com.ibm.portal.um.PumaHome;
import com.ibm.portal.um.PumaLocator;
import com.ibm.portal.um.PumaProfile;
import com.ibm.portal.um.User;
import com.ibm.portal.um.exceptions.PumaAttributeException;
import com.ibm.portal.um.exceptions.PumaException;
import com.ibm.portal.um.exceptions.PumaMissingAccessRightsException;
import com.ibm.portal.um.exceptions.PumaModelException;
import com.ibm.portal.um.exceptions.PumaSystemException;

public class IBMPumaUtility {
	
	private static IBMPumaUtility pumaUtility;
	private PumaHome pumaHome;
	
	private IBMPumaUtility() throws NamingException {
		javax.naming.Context ctx = new javax.naming.InitialContext();
		pumaHome = (PumaHome) ctx.lookup(PumaHome.JNDI_NAME);
	}

	public static IBMPumaUtility getInstance() throws NamingException {
		if(pumaUtility == null) {
			pumaUtility = new IBMPumaUtility();
		} 
		return pumaUtility;
	}

	public User getCurrentUser() throws PumaException {
		//To retrieve current User
		PumaProfile profile = pumaHome.getProfile();
		return profile.getCurrentUser();
	}
	
	public List<User> getUsersByAttribute(String attributeName, String attributeValue)
		throws PumaSystemException, PumaAttributeException, PumaMissingAccessRightsException {
		//To find Users by attribute
		//(eg. attrName = "cn" and attrValue="Manish")
		PumaLocator locator = pumaHome.getLocator();
		return locator.findUsersByAttribute(attributeName, attributeValue);
	}
	
	public List<User> findUsersByQuery(String query) 
		throws PumaSystemException, PumaAttributeException, PumaMissingAccessRightsException {
		//If you need to search by mutilple attributes, search users by query
		PumaLocator locator = pumaHome.getLocator();
		//String query = "((cn != 'Manish') and (givenName != 'wpsadmin'))";
		return locator.findUsersByQuery(query);
	}
	
	public Map<String, Object> getUserAttributes(User user)
		throws PumaAttributeException, PumaSystemException, PumaModelException, PumaMissingAccessRightsException {
		List<String> returnAttributes = new ArrayList<String>();
		returnAttributes.add("cn");
		returnAttributes.add("mail");
		PumaProfile profile = pumaHome.getProfile();
		Map<String, Object> values = profile.getAttributes(user, returnAttributes);
		return values;
	}
	
	public List<Group> findGroupsByAttribute(String attributeName, String attributeValue) 
		throws PumaSystemException, PumaAttributeException, PumaMissingAccessRightsException {
		//Just like findUsers you can find Groups by attribute
		PumaLocator locator = pumaHome.getLocator();		
		List<Group> groups = locator.findGroupsByAttribute(attributeName, attributeValue);
		return groups;
	}

	public PagingIterator<User> paginationFindUsersByQuery(String query, int resultsPerPage)
		throws PumaSystemException, PumaAttributeException, PumaMissingAccessRightsException {
		//PumaLocator provides method to either return a List of PagingIterator
		//int resultsPerPage = 10;		
		PumaLocator locator = pumaHome.getLocator();
		Map<String, Integer> pProperties = new HashMap<String, Integer>();
		pProperties.put(PumaLocator.RESULTS_PER_PAGE, resultsPerPage);
		PagingIterator<User> pIterator = locator.findUsersByQuery(query, pProperties);
		return pIterator;
	}
	
	public void updateUserAttribute(User user, String attribute, String value)
		throws PumaAttributeException, PumaSystemException, PumaModelException, PumaMissingAccessRightsException {
		PumaController pController = pumaHome.getController();		
		//Use PumaController to set or remove User attribute
		Map<String, Object> attributes = new HashMap<String, Object>();
		attributes.put(attribute, value);
		pController.setAttributes(user, attributes); 		
	}
	
	public void updateUserAttributes(User user, Map<String, Object> attributes)
	throws PumaAttributeException, PumaSystemException, PumaModelException, PumaMissingAccessRightsException {
		PumaController pController = pumaHome.getController();		
		//Use PumaController to set or remove User attribute
		pController.setAttributes(user, attributes); 		
	}
	
	public void removeUserAttribute(User user, String attribute)
		throws PumaAttributeException, PumaSystemException, PumaModelException, PumaMissingAccessRightsException {
		List<String> keys = new ArrayList<String>();
		//keys.add("mail");
		keys.add(attribute);
		
		PumaController pController = pumaHome.getController();
		pController.removeAttributes(user, keys);			
	}

	public void removeUserAttributes(User user, List<String> attributes)
		throws PumaAttributeException, PumaSystemException, PumaModelException, PumaMissingAccessRightsException {
		PumaController pController = pumaHome.getController();
		pController.removeAttributes(user, attributes);			
	}

}
