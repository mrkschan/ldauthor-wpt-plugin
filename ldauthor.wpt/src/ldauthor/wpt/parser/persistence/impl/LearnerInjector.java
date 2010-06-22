package ldauthor.wpt.parser.persistence.impl;

import ldauthor.wpt.parser.persistence.Injector;
import ldauthor.wpt.parser.persistence.PersistenceUtility;

import org.tencompetence.imsldmodel.ILDModel;
import org.tencompetence.imsldmodel.LDModelFactory;
import org.tencompetence.imsldmodel.roles.IRoleModel;
import org.tencompetence.imsldmodel.types.IItemType;
import org.tencompetence.ldauthor.ldmodel.util.LDModelUtils;

public class LearnerInjector implements Injector {

	private static int id = 1;

	public void inject(ILDModel model, String content)
	throws Exception
	{
		id = PersistenceUtility.getValidResourceName(model, "Learner-", id);
		String title = "Learner-" + id;

		IRoleModel learner = (IRoleModel)
			LDModelFactory.createModelObject(LDModelFactory.LEARNER, model);
		learner.setTitle(title);

		IItemType learner_item = (IItemType)
			LDModelFactory.createModelObject(LDModelFactory.ITEM, model);
		learner_item.setTitle(title);

		LDModelUtils.setNewObjectDefaults(learner_item, content, LDModelUtils.HTML_FILE);

		learner.getInformationModel().addChildItem(learner_item);

		model.getRolesModel().addChild(learner);
	}

}
