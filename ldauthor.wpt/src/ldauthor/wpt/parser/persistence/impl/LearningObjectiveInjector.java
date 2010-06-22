package ldauthor.wpt.parser.persistence.impl;

import ldauthor.wpt.parser.persistence.Injector;
import ldauthor.wpt.parser.persistence.PersistenceUtility;

import org.tencompetence.imsldmodel.ILDModel;
import org.tencompetence.imsldmodel.LDModelFactory;
import org.tencompetence.imsldmodel.types.IItemType;
import org.tencompetence.ldauthor.ldmodel.util.LDModelUtils;

public class LearningObjectiveInjector implements Injector {

	private static int id = 1;

	public void inject(ILDModel model, String content)
	throws Exception
	{
		IItemType objective = (IItemType)
			LDModelFactory.createModelObject(LDModelFactory.ITEM, model);

		id = PersistenceUtility.getValidResourceName(model, "Objective-", id);
		objective.setTitle("Objective-" + id);

		LDModelUtils.setNewObjectDefaults(objective, content, LDModelUtils.HTML_FILE);

		model.getLearningObjectives().addChildItem(objective);
	}

}
