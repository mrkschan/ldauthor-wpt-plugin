package ldauthor.wpt.parser.persistence.impl;

import ldauthor.wpt.parser.persistence.Injector;
import ldauthor.wpt.parser.persistence.PersistenceUtility;

import org.tencompetence.imsldmodel.ILDModel;
import org.tencompetence.imsldmodel.LDModelFactory;
import org.tencompetence.imsldmodel.types.IItemType;
import org.tencompetence.ldauthor.ldmodel.util.LDModelUtils;

public class InformationInjector implements Injector {

	private static int id = 1;

	public void inject(ILDModel model, String content)
	throws Exception
	{
		IItemType info = (IItemType)
			LDModelFactory.createModelObject(LDModelFactory.ITEM, model);

		id = PersistenceUtility.getValidResourceName(model, "Information-", id);
		info.setTitle("Information-" + id);

		LDModelUtils.setNewObjectDefaults(
			info, content, LDModelUtils.HTML_FILE);

	}

}
