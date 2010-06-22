package ldauthor.wpt.parser.persistence;

import org.tencompetence.imsldmodel.ILDModel;
import org.tencompetence.imsldmodel.resources.IResourcesModel;

public class PersistenceUtility {

	public static int
	getValidResourceName(ILDModel model, String prefix, int index)
	{
		IResourcesModel rm = model.getResourcesModel();
		String name = prefix + index;
		while (null != rm.getResourceByIdentifier(name)) {
			name = prefix + (++index);
		}

		return index;
	}
}
