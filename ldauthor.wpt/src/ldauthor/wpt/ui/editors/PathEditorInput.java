package ldauthor.wpt.ui.editors;

import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IPathEditorInput;
import org.eclipse.ui.IPersistableElement;
import org.eclipse.ui.PlatformUI;

public class PathEditorInput implements IPathEditorInput {
	private IPath fPath;

	/**
	 * Creates an editor input based of the given file resource.
	 * @param path the file
	 */
	public PathEditorInput(IPath path) {
		if (null == path) {
			throw new IllegalArgumentException();
		}
		this.fPath = path;
	}

	public int hashCode() {
		return fPath.hashCode();
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof PathEditorInput))
			return false;
		PathEditorInput other = (PathEditorInput) obj;
		return fPath.equals(other.fPath);
	}

	public boolean exists() {
		return fPath.toFile().exists();
	}

	public ImageDescriptor getImageDescriptor() {
		return PlatformUI.getWorkbench().getEditorRegistry().getImageDescriptor(fPath.toString());
	}

	public String getName() {
		return fPath.toString();
	}

	public String getToolTipText() {
		return fPath.makeRelative().toOSString();
	}

	public IPath getPath() {
		return fPath;
	}

	public Object getAdapter(Class adapter) {
		return null;
	}

	public IPersistableElement getPersistable() {
		// no persistence
		return null;
	}
}

