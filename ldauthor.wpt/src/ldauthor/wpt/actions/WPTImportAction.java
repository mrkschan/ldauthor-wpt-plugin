package ldauthor.wpt.actions;

import java.util.List;

import ldauthor.wpt.parser.ElementStore;
import ldauthor.wpt.parser.Parser;
import ldauthor.wpt.parser.Parser.ElementPair;
import ldauthor.wpt.ui.dialogs.WPTImportDialog;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.ui.IActionDelegate2;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.tencompetence.imsldmodel.ILDModel;
import org.tencompetence.ldauthor.ui.editors.ILDMultiPageEditor;
import org.tencompetence.ldauthor.ui.editors.LDEditorContextDelegate;

public class WPTImportAction
implements IWorkbenchWindowActionDelegate, IActionDelegate2 {

	private IAction self_action;
	private LDEditorContextDelegate delegate;
	private ILDModel ldmodel;

	public WPTImportAction() {
	}

	public void run(IAction action) {
		// @see runWithEvent
	}

	public void selectionChanged(IAction action, ISelection selection) {
	}

	public void dispose() {
		delegate.dispose();
		self_action = null;
	}

	public void init(IWorkbenchWindow window)
	{
        delegate = new LDEditorContextDelegate(window) {
            public void setActiveEditor(ILDMultiPageEditor editor) {
                if (null != editor) self_action.setEnabled(true);
                else self_action.setEnabled(false);

                if (null != editor) {
                	ldmodel = (ILDModel) editor.getAdapter(ILDModel.class);
                }
            }
        };
	}

	public void init(IAction action)
	{
		// invoke by EarlyStartup
		self_action = action;
		self_action.setEnabled(false);
	}

	public void runWithEvent(IAction action, Event evt)
	{
		final WPTImportDialog import_dlg =
			new WPTImportDialog(Display.getCurrent().getActiveShell());

		if(Dialog.CANCEL == import_dlg.open()) return;

		BusyIndicator.showWhile(Display.getCurrent(), new Thread() {
			public void run()
			{
				String urls = import_dlg.getUrls();
				String icdl = import_dlg.getIcdl();

				if (0 == urls.length() || 0 == icdl.length()) return;

				String[] _urls = urls.split("\n");

				List<ElementPair> l = Parser.parse(_urls, icdl);

				for (ElementPair p : l) {
					ElementStore.persist(ldmodel, p.individual, p.content);
				}
			}
		});
	}
}