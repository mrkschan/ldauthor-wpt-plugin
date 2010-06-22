package ldauthor.wpt.parser.persistence;

import org.tencompetence.imsldmodel.ILDModel;

public interface Injector {
	public void inject(ILDModel model, String content) throws Exception;
}
