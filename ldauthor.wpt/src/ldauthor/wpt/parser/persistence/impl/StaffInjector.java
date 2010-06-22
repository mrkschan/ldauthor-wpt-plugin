package ldauthor.wpt.parser.persistence.impl;

import ldauthor.wpt.parser.persistence.Injector;
import ldauthor.wpt.parser.persistence.PersistenceUtility;

import org.tencompetence.imsldmodel.ILDModel;
import org.tencompetence.imsldmodel.LDModelFactory;
import org.tencompetence.imsldmodel.roles.IRoleModel;
import org.tencompetence.imsldmodel.types.IItemType;
import org.tencompetence.ldauthor.ldmodel.util.LDModelUtils;

public class StaffInjector implements Injector {

	private static int id = 1;

	public void inject(ILDModel model, String content)
	throws Exception
	{
		id = PersistenceUtility.getValidResourceName(model, "Staff-", id);
		String title = "Staff-" + id;

		IRoleModel staff = (IRoleModel)
			LDModelFactory.createModelObject(LDModelFactory.STAFF, model);
		staff.setTitle(title);

		IItemType staff_item = (IItemType)
			LDModelFactory.createModelObject(LDModelFactory.ITEM, model);
		staff_item.setTitle(title);

		LDModelUtils.setNewObjectDefaults(staff_item, content, LDModelUtils.HTML_FILE);

		staff.getInformationModel().addChildItem(staff_item);

		model.getRolesModel().addChild(staff);
	}

}
