package ldauthor.wpt.ui.dialogs;

import java.util.List;

import ldauthor.wpt.ontology.LearningDesign;
import ldauthor.wpt.qs_string.QSString;
import ldauthor.wpt.ui.Messages;

import org.eclipse.core.databinding.observable.list.WritableList;
import org.eclipse.jface.databinding.viewers.ObservableListContentProvider;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;


public class WPTMappingDialog extends TitleAreaDialog {

	private String selected_ld_element;
	private ListViewer listviewer;
	private Text filter;

	private QSString abbr;

	private WPTMappingDialog self;

	public String selectedElement() {
		return selected_ld_element;
	}

	public WPTMappingDialog(Shell parentShell) {
		super(parentShell);

		self = this;
	}

	@Override
    protected void configureShell(Shell shell) {
        super.configureShell(shell);

        // set shell title
        shell.setText(Messages.MappingDialogShellTitle);
        Rectangle bounds = Display.getCurrent().getBounds();
        shell.setBounds(
    		(bounds.width - 640)/2, (bounds.height - 480)/2, 640, 480);
    }

    @Override
    protected Control createDialogArea(Composite parent) {
    	// set dialog content
        setTitle(Messages.MappingDialogTitle);
        setMessage(Messages.MappingDialogAbstract);

        Composite composite = new Composite(
        		(Composite)super.createDialogArea(parent), SWT.NULL);

        composite.setLayoutData(new GridData(GridData.FILL_BOTH));
        composite.setLayout(new GridLayout(2, false));

        Label l = new Label(composite, SWT.NULL);
        l.setText("Filter:");

        filter = new Text(composite, SWT.NULL);
        filter.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        filter.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent me) {
				abbr = new QSString(filter.getText());
				listviewer.refresh();
			}
        });

        LearningDesign ontology = null;
        try {
        	ontology = LearningDesign.getInstance();

        	List<String> concepts = ontology.dump();

	        listviewer = new ListViewer(composite, SWT.V_SCROLL);
	        listviewer.setContentProvider(new ObservableListContentProvider());
	        listviewer.setInput(new WritableList(concepts, String.class));
	        listviewer.addFilter(new ViewerFilter() {
				public boolean select(Viewer viewer, Object dummy, Object element)
				{
					if (null == abbr || abbr.toString().length() == 0) return true;

					QSString s = new QSString((String) element);
					return (s.score(abbr) > 0.0f);
				}
	        });
	        listviewer.addSelectionChangedListener(new ISelectionChangedListener() {
				public void selectionChanged(SelectionChangedEvent sce) {
					IStructuredSelection selection = (IStructuredSelection) listviewer.getSelection();
					selected_ld_element = selection.getFirstElement().toString();
				}
			});
	        listviewer.addDoubleClickListener(new IDoubleClickListener() {
				public void doubleClick(DoubleClickEvent dce) {
					IStructuredSelection selection = (IStructuredSelection) listviewer.getSelection();
					selected_ld_element = selection.getFirstElement().toString();

					self.okPressed();
				}
			});

            GridData data = new GridData();
    		data.horizontalSpan = 2;
            data.horizontalAlignment = GridData.FILL;
    		data.grabExcessVerticalSpace = true;
            listviewer.getList().setLayoutData(data);

        } catch (Exception e) {
        	e.printStackTrace(System.err);
        	return parent;
        }

    	return composite;
    }

}
