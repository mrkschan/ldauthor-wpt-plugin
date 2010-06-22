package ldauthor.wpt.parser;

import java.util.Properties;

import ldauthor.wpt.ontology.LearningDesign;

import org.tencompetence.imsldmodel.ILDModel;

import com.hp.hpl.jena.ontology.Individual;

public class ElementStore {

	private static Properties properties;

	public static void
	persist(ILDModel model, Individual individual, String content)
	{
		if (null == properties) {
			properties = new Properties();
			try {
				properties.load(
					ElementStore.class.getResourceAsStream("mapping.properties"));
			} catch (Exception e) {
				e.printStackTrace(System.err);
				return;
			}
		}


		String concept = LearningDesign.getInstance().getOntClass(individual);
		try {

			Class injector = Class.forName(
				"ldauthor.wpt.parser.persistence.impl." +
				properties.getProperty(concept));
			if (null != injector) {
				injector.getMethod("inject", new Class[] {ILDModel.class, String.class})
						.invoke(injector.newInstance(), new Object[] {model, content});
			}

		} catch (Exception e) {
			System.err.println(concept + " is not supported for HTML import.");
//			e.printStackTrace(System.err);
		}
	}
}
