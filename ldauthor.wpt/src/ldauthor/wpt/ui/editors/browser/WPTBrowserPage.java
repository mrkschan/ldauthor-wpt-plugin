package ldauthor.wpt.ui.editors.browser;

import java.io.InputStream;

import ldauthor.wpt.ui.Messages;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTError;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.LocationAdapter;
import org.eclipse.swt.browser.LocationEvent;
import org.eclipse.swt.browser.OpenWindowListener;
import org.eclipse.swt.browser.ProgressEvent;
import org.eclipse.swt.browser.ProgressListener;
import org.eclipse.swt.browser.StatusTextEvent;
import org.eclipse.swt.browser.StatusTextListener;
import org.eclipse.swt.browser.TitleEvent;
import org.eclipse.swt.browser.TitleListener;
import org.eclipse.swt.browser.WindowEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.EditorPart;


public class WPTBrowserPage extends EditorPart {

	private Browser browser;
	private Text    url_field;
	private Label   xpath_field;

	private String xpath_script;
	private String xpath_selected;

	private WPTBrowserPage self;

	public String getUrl() {
		return browser.getUrl();
	}

	@Override
	public void init(IEditorSite site, IEditorInput input)
	throws PartInitException
	{
		setSite(site);
		setInput(input);

		try {
			InputStream scriptS = this.getClass().getResourceAsStream("element-select.js");
			byte[] scriptB = new byte[scriptS.available()];
			scriptS.read(scriptB);
			scriptS.close();
			xpath_script = new String(scriptB);
		} catch (Exception e) {
			e.printStackTrace(System.err);
			throw new PartInitException(e.toString());
		}

		self = this;
	}

	@Override
	public void createPartControl(Composite parent)
	{
		GridLayout gridLayout = new GridLayout(7, false);
		parent.setLayout(gridLayout);

		try {
			// content creation
			createSurfToolbar(parent);
			createBrowserArea(parent);
			createXPathDisplayBar(parent);

			// display a blank page
			if (null != browser) browser.setUrl("about:blank");
		} catch (Exception e) {
			e.printStackTrace(System.err);
		}
	}

	private void createSurfToolbar(Composite parent)
	throws Exception
	{
		GridData data;

		// Buttons
		data = new GridData();
		data.horizontalAlignment = GridData.END;

		Button btn_back = new Button(parent, SWT.PUSH);
		btn_back.setImage(
				PlatformUI.getWorkbench().getSharedImages()
				.getImage(ISharedImages.IMG_TOOL_BACK));
		btn_back.setToolTipText("Go back one page");
		btn_back.setLayoutData(data);

		Button btn_forward = new Button(parent, SWT.PUSH);
		btn_forward.setImage(
				PlatformUI.getWorkbench().getSharedImages()
				.getImage(ISharedImages.IMG_TOOL_FORWARD));
		btn_forward.setToolTipText("Go forward one page");

		Button btn_reload = new Button(parent, SWT.PUSH);
		btn_reload.setImage(
				PlatformUI.getWorkbench().getSharedImages()
				.getImage(ISharedImages.IMG_ELCL_SYNCED));
		btn_reload.setToolTipText("Reload current page");

		Button btn_stop = new Button(parent, SWT.PUSH);
		btn_stop.setImage(
				PlatformUI.getWorkbench().getSharedImages()
				.getImage(ISharedImages.IMG_OBJS_ERROR_TSK));
		btn_stop.setToolTipText("Stop loading this page");

		// URL field
		Label url_label = new Label(parent, SWT.NONE);
		url_label.setText(Messages.ElementSelectBrowserURLLabel);

		data = new GridData();
		data.horizontalAlignment = GridData.FILL;
		data.grabExcessHorizontalSpace = true;

		url_field = new Text(parent, SWT.BORDER);
		url_field.setLayoutData(data);

		Button btn_go = new Button(parent, SWT.PUSH);
		btn_go.setImage(
				PlatformUI.getWorkbench().getSharedImages()
				.getImage(ISharedImages.IMG_TOOL_UNDO));
		btn_go.setToolTipText("Browse");


		// Event handlers
		btn_back.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent se) {
				if (null != browser) browser.back();
			}
			public void widgetSelected(SelectionEvent se) {
				if (null != browser) browser.back();
			}
		});

		btn_forward.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent se) {
				if (null != browser) browser.forward();
			}
			public void widgetSelected(SelectionEvent se) {
				if (null != browser) browser.forward();
			}
		});

		btn_reload.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent se) {
				if (null != browser) browser.refresh();
			}
			public void widgetSelected(SelectionEvent se) {
				if (null != browser) browser.refresh();
			}
		});

		btn_stop.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent se) {
				if (null != browser) browser.stop();
			}
			public void widgetSelected(SelectionEvent se) {
				if (null != browser) browser.stop();
			}
		});

        url_field.addSelectionListener(new SelectionAdapter() {
            public void widgetDefaultSelected(SelectionEvent se) {
            	if (null != browser) browser.setUrl(url_field.getText());
			}
		});

        btn_go.addSelectionListener(new SelectionListener() {
			public void widgetSelected(SelectionEvent se) {
				if (null != browser) browser.setUrl(url_field.getText());
			}
			public void widgetDefaultSelected(SelectionEvent se) {
				if (null != browser) browser.setUrl(url_field.getText());
			}
		});
	}

	private void createBrowserArea(Composite parent)
	throws Exception
	{
		// Browser
		GridData data = new GridData();
		data.horizontalAlignment = GridData.FILL;
		data.verticalAlignment = GridData.FILL;
		data.horizontalSpan = 7;
		data.grabExcessHorizontalSpace = true;
		data.grabExcessVerticalSpace = true;

        try {
			browser = new Browser(parent, SWT.MOZILLA);
        } catch(SWTError ex) {
            // Mozilla Browser widget not supported
            Label label = new Label(parent, SWT.CENTER);
            label.setLayoutData(data);
            label.setText(Messages.BrowserNotSupportedMsg);

        	browser.dispose();
        	browser = null;

            throw ex;
        }

        if (null == browser) return;
        browser.setLayoutData(data);


        // Event handler
        browser.addProgressListener(new ProgressListener() {
			public void changed(ProgressEvent pe) {}
			public void completed(ProgressEvent pe) {
				browser.execute(xpath_script);
			}
		});

		browser.addTitleListener(new TitleListener() {
			public void changed(TitleEvent te) {
				if (te.title.startsWith("xpath: ")) {
					String old_xpath_selected = xpath_selected;
					xpath_selected = te.title.replace("xpath: ", "");
					xpath_field.setText(xpath_selected);

					self.firePartPropertyChanged(
						"xpath_selected", old_xpath_selected, xpath_selected);
				}
			}
		});

		browser.addStatusTextListener(new StatusTextListener() {
			public void changed(StatusTextEvent ste) {
				if (ste.text.startsWith("xpath:")) {
					xpath_field.setText(ste.text.replace("xpath: ", ""));
				}
			}
		});

        browser.addOpenWindowListener(new OpenWindowListener() {
			public void open(WindowEvent event) {
                if(event.browser != null) { // this can be null
                    browser.setUrl(event.browser.getUrl());
                }
			}
		});

        browser.addLocationListener(new LocationAdapter() {
            public void changed(LocationEvent event) {
				url_field.setText(event.location);
			}
		});
	}

	private void createXPathDisplayBar(Composite parent)
	throws Exception
	{
		GridData data = new GridData();
		data.grabExcessHorizontalSpace = true;

		Label xpath_label = new Label(parent, SWT.NONE);
		xpath_label.setText(Messages.ElementSelectBrowserXPATHLabel);

		data = new GridData();
		data.horizontalSpan = 6;
		data.horizontalAlignment = GridData.FILL;
		data.grabExcessHorizontalSpace = true;

		xpath_field = new Label(parent, SWT.NONE);
		xpath_field.setLayoutData(data);
	}

	@Override
	public void doSave(IProgressMonitor arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void doSaveAs() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isDirty() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isSaveAsAllowed() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}

}
