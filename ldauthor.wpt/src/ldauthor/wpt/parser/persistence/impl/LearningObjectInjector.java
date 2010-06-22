package ldauthor.wpt.parser.persistence.impl;

import ldauthor.wpt.parser.persistence.Injector;
import ldauthor.wpt.parser.persistence.PersistenceUtility;

import org.tencompetence.imsldmodel.ILDModel;
import org.tencompetence.imsldmodel.LDModelFactory;
import org.tencompetence.imsldmodel.environments.IEnvironmentModel;
import org.tencompetence.imsldmodel.environments.IEnvironmentsModel;
import org.tencompetence.imsldmodel.environments.ILearningObjectModel;
import org.tencompetence.imsldmodel.types.IItemType;
import org.tencompetence.ldauthor.graphicsmodel.environment.IGraphicalEnvironmentModel;
import org.tencompetence.ldauthor.graphicsmodel.environment.IGraphicalEnvironmentsModel;
import org.tencompetence.ldauthor.graphicsmodel.environment.impl.GraphicalLearningObjectModel;
import org.tencompetence.ldauthor.ldmodel.IReCourseInfoModel;
import org.tencompetence.ldauthor.ldmodel.impl.ReCourseLDModel;
import org.tencompetence.ldauthor.ldmodel.util.LDModelUtils;

public class LearningObjectInjector implements Injector {

	private static int id = 1;

	public void inject(ILDModel model, String content)
	throws Exception
	{
		id = PersistenceUtility.getValidResourceName(model, "LearningObject-", id);
		String title = "LearningObject-" + id;

        IItemType object_item = (IItemType)
        	LDModelFactory.createModelObject(LDModelFactory.ITEM, model);

        object_item.setTitle(title);

        LDModelUtils.setNewObjectDefaults(object_item, content, LDModelUtils.HTML_FILE);


        ILearningObjectModel object = (ILearningObjectModel)
        	LDModelFactory.createModelObject(LDModelFactory.LEARNING_OBJECT, model);

        object.getItems().addChildItem(object_item);

        object.setTitle(title);


        // assign to "default" environment
        IReCourseInfoModel info_model = ((ReCourseLDModel) model).getReCourseInfoModel();

        IGraphicalEnvironmentsModel g_envs = info_model.getGraphicalEnvironmentsModel();
        IEnvironmentsModel envs = model.getEnvironmentsModel();

        IGraphicalEnvironmentModel g_env = null;
        IEnvironmentModel env = null;

        if (0 == envs.getChildren().size()) {
        	envs.addChild(
    			LDModelFactory
    			.createModelObject(LDModelFactory.ENVIRONMENT, model));
        	g_envs.reconcile();
        }

    	env = (IEnvironmentModel) envs.getChildren().get(0);
    	g_env = (IGraphicalEnvironmentModel) g_envs.getChildren().get(0);

        GraphicalLearningObjectModel g_object = new GraphicalLearningObjectModel(model);
        g_object.setLDModelObject(object);

        env.addChild(object);
        g_env.addChild(g_object, false);
	}

}
