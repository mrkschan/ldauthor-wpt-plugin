package ldauthor.wpt.ui.dialogs;

import ldauthor.wpt.ui.Messages;

import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class WPTImportDialog extends TitleAreaDialog {

	private WPTImportDialog self;

	private Text t_icdl;
	private String icdl_location;

	private Text t_url;
	private String urls;

	public String getIcdl() {
		return icdl_location;
	}

	public String getUrls() {
		return urls;
	}

	public WPTImportDialog(Shell parentShell) {
		super(parentShell);
		self = this;
	}

    @Override
    protected void configureShell(Shell shell) {
        super.configureShell(shell);

        // set shell title
        shell.setText(Messages.ImportDialogShellTitle);
        Rectangle bounds = Display.getCurrent().getBounds();
        shell.setBounds(
    		(bounds.width - 640)/2, (bounds.height - 480)/2, 640, 480);
    }

    @Override
    protected Control createDialogArea(Composite parent) {
    	// set dialog content
        setTitle(Messages.ImportDialogTitle);
        setMessage(Messages.ImportDialogAbstract);

        Composite composite = new Composite(
        		(Composite)super.createDialogArea(parent), SWT.NULL);

        composite.setLayoutData(new GridData(GridData.FILL_BOTH));
        composite.setLayout(new GridLayout(2, false));

        GridData data;


        data = new GridData();
		data.horizontalSpan = 2;

        Label l_icdl = new Label(composite, SWT.NONE);
        l_icdl.setText("Website Parse Template File:");
        l_icdl.setLayoutData(data);

        data = new GridData();
        data.horizontalAlignment = GridData.FILL;
		data.grabExcessHorizontalSpace = true;

        t_icdl = new Text(composite, SWT.BORDER);
        t_icdl.setLayoutData(data);
        t_icdl.setEnabled(false);

        Button b_icdl = new Button(composite, SWT.FLAT);
        b_icdl.setText("Browse...");
        b_icdl.addSelectionListener(new SelectionListener() {
        	private void selectFile() {
				FileDialog fd = new FileDialog(self.getShell());
				if (null == fd.open()) return;

				t_icdl.setText(fd.getFilterPath() + "/" + fd.getFileName());
        	}

			public void widgetDefaultSelected(SelectionEvent e) {
				selectFile();
			}
			public void widgetSelected(SelectionEvent e) {
				selectFile();
			}
        });


        data = new GridData();
		data.horizontalSpan = 2;

        Label l_url = new Label(composite, SWT.NONE);
        l_url.setText("Web pages to be extracted from (Provide URLs line by line):");
        l_url.setLayoutData(data);

        data = new GridData();
		data.horizontalSpan = 2;
        data.horizontalAlignment = GridData.FILL;
		data.grabExcessHorizontalSpace = true;
        data.verticalAlignment = GridData.FILL;
		data.grabExcessVerticalSpace = true;

        t_url = new Text(composite, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL);
        t_url.setLayoutData(data);

        return composite;
    }

    @Override
    protected void okPressed() {
    	urls = t_url.getText();
    	icdl_location = t_icdl.getText();

    	super.okPressed();
    }
}
