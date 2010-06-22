package ldauthor.wpt.ui.editors.wpteditor;

import org.eclipse.ui.editors.text.TextEditor;

public class WPTRawEditPage extends TextEditor {

	private ColorManager colorManager;

	public WPTRawEditPage()
	{
		super();

		colorManager = new ColorManager();
		setSourceViewerConfiguration(new XMLConfiguration(colorManager));
		setDocumentProvider(new XMLDocumentProvider());
	}

	public void dispose()
	{
		colorManager.dispose();
		super.dispose();
	}
}
