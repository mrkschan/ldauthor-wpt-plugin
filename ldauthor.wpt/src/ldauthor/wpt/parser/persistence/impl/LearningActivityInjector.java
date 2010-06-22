package ldauthor.wpt.parser.persistence.impl;

import ldauthor.wpt.parser.persistence.Injector;
import ldauthor.wpt.parser.persistence.PersistenceUtility;

import org.tencompetence.imsldmodel.ILDModel;
import org.tencompetence.imsldmodel.LDModelFactory;
import org.tencompetence.imsldmodel.activities.IActivityModel;
import org.tencompetence.ldauthor.ldmodel.util.LDModelUtils;

public class LearningActivityInjector implements Injector {

	private static int id = 1;

	public void inject(ILDModel model, String content)
	throws Exception
	{
		IActivityModel activity = (IActivityModel)
			LDModelFactory.createModelObject(LDModelFactory.LEARNING_ACTIVITY, model);

		id = PersistenceUtility.getValidResourceName(model, "LearningActivity-", id);
		activity.setTitle("LearningActivity-" + id);

		LDModelUtils.setNewObjectDefaults(activity, content, LDModelUtils.HTML_FILE);

	}

}
