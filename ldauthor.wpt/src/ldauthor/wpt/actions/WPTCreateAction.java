package ldauthor.wpt.actions;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import ldauthor.wpt.ui.dialogs.WPTCreateDialog;
import ldauthor.wpt.ui.editors.PathEditorInput;
import ldauthor.wpt.ui.editors.WPTEditor;

import org.eclipse.core.runtime.Path;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.eclipse.ui.PartInitException;



/**
 * Our sample action implements workbench action delegate.
 * The action proxy will be created by the workbench and
 * shown in the UI. When the user tries to use the action,
 * this delegate will be created and execution will be
 * delegated to it.
 * @see IWorkbenchWindowActionDelegate
 */
public class WPTCreateAction implements IWorkbenchWindowActionDelegate {
	private IWorkbenchWindow window;
	/**
	 * The constructor.
	 */
	public WPTCreateAction() {
	}

	/**
	 * The action has been activated. The argument of the
	 * method represents the 'real' action sitting
	 * in the workbench UI.
	 * @see IWorkbenchWindowActionDelegate#run
	 */
	public void run(IAction action)
	{
		WPTCreateDialog fd = new WPTCreateDialog(Display.getCurrent().getActiveShell());
		if (Dialog.CANCEL == fd.open()) return;


		File fileToOpen = new File(fd.getFileFullPath());
		if (false == fileToOpen.exists()) {
			try {
				fileToOpen.createNewFile();

				InputStream in = this.getClass().getResourceAsStream("icdl.template");
				byte[] icdl_template = new byte[in.available()];
				in.read(icdl_template);
				in.close();

				FileOutputStream out = new FileOutputStream(fileToOpen);
				out.write(icdl_template);
				out.close();

			} catch (IOException e) {
			    e.printStackTrace(System.err);
			}
		}


		IWorkbenchPage page = window.getActivePage();
		try {
			page.openEditor(new PathEditorInput(
				new Path(fileToOpen.getAbsolutePath())), WPTEditor.ID);
		} catch (PartInitException e) {
			e.printStackTrace(System.err);
		}
	}

	/**
	 * Selection in the workbench has been changed. We
	 * can change the state of the 'real' action here
	 * if we want, but this can only happen after
	 * the delegate has been created.
	 * @see IWorkbenchWindowActionDelegate#selectionChanged
	 */
	public void selectionChanged(IAction action, ISelection selection) {
	}

	/**
	 * We can use this method to dispose of any system
	 * resources we previously allocated.
	 * @see IWorkbenchWindowActionDelegate#dispose
	 */
	public void dispose() {
	}

	/**
	 * We will cache window object in order to
	 * be able to provide parent shell for the message dialog.
	 * @see IWorkbenchWindowActionDelegate#init
	 */
	public void init(IWorkbenchWindow window) {
		this.window = window;
	}
}