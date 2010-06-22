package ldauthor.wpt.ui.dialogs;

import ldauthor.wpt.ui.Messages;

import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;


public class WPTCreateDialog extends TitleAreaDialog {

	private Text wpt_field;
	private WPTCreateDialog self;
	private String location;

	public WPTCreateDialog(Shell parentShell) {
		super(parentShell);

		self = this;
		location = null;
	}

	@Override
    protected void configureShell(Shell shell) {
        super.configureShell(shell);

        // set shell title
        shell.setText(Messages.CreateDialogShellTitle);
    }

    @Override
    protected Control createDialogArea(Composite parent) {
    	// set dialog content
        setTitle(Messages.CreateDialogTitle);
        setMessage(Messages.CreateDialogAbstract);

        Composite composite = new Composite(
        		(Composite)super.createDialogArea(parent), SWT.NULL);

        composite.setLayoutData(new GridData(GridData.FILL_BOTH));
        composite.setLayout(new GridLayout(2, false));


		GridData data;

		data = new GridData();
		data.horizontalSpan = 2;

        Label l = new Label(composite, SWT.NONE);
        l.setText(Messages.CreateDialogWPTLabel);
        l.setLayoutData(data);


		data = new GridData();
        data.horizontalAlignment = GridData.FILL;
		data.grabExcessHorizontalSpace = true;

        wpt_field = new Text(composite, SWT.BORDER);
        wpt_field.setLayoutData(data);
        wpt_field.setEnabled(false);


        Button btn_browse = new Button(composite, SWT.NONE);
        btn_browse.setText("Browse...");

        btn_browse.addSelectionListener(new SelectionListener() {
        	private void selectFile() {
				FileDialog fd = new FileDialog(self.getShell());
				if (null == fd.open()) return;

				self.location = fd.getFilterPath() + "/" + fd.getFileName();
				wpt_field.setText(self.location);
        	}

			public void widgetDefaultSelected(SelectionEvent e) {
				selectFile();
			}
			public void widgetSelected(SelectionEvent e) {
				selectFile();
			}
        });

        return composite;
    }

    public String getFileFullPath()
    {
    	if (null == location) return null;

    	if (location.endsWith(".icdl")) return location;
    	else return location + ".icdl";
    }
}
