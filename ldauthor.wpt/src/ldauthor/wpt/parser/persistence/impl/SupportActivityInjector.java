package ldauthor.wpt.parser.persistence.impl;

import ldauthor.wpt.parser.persistence.Injector;
import ldauthor.wpt.parser.persistence.PersistenceUtility;

import org.tencompetence.imsldmodel.ILDModel;
import org.tencompetence.imsldmodel.LDModelFactory;
import org.tencompetence.imsldmodel.activities.IActivityModel;
import org.tencompetence.ldauthor.ldmodel.util.LDModelUtils;

public class SupportActivityInjector implements Injector {

	private static int id = 1;

	public void inject(ILDModel model, String content)
	throws Exception
	{
		IActivityModel activity = (IActivityModel)
			LDModelFactory.createModelObject(LDModelFactory.SUPPORT_ACTIVITY, model);

		id = PersistenceUtility.getValidResourceName(model, "SupportActivity-", id);
		activity.setTitle("SupportActivity-" + id);

		LDModelUtils.setNewObjectDefaults(activity, content, LDModelUtils.HTML_FILE);

	}
}
