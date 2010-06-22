package ldauthor.wpt.ui.editors;

import java.io.File;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

import ldauthor.wpt.ontology.LearningDesign;
import ldauthor.wpt.ui.Messages;
import ldauthor.wpt.ui.dialogs.WPTMappingDialog;
import ldauthor.wpt.ui.editors.browser.WPTBrowserPage;
import ldauthor.wpt.ui.editors.wpteditor.WPTRawEditPage;
import ldauthor.wpt.ui.editors.wpteditor.XMLDocumentProvider;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.editors.text.TextEditor;
import org.eclipse.ui.ide.FileStoreEditorInput;
import org.eclipse.ui.part.EditorPart;
import org.eclipse.ui.part.MultiPageEditorPart;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.xpath.XPath;
import org.tencompetence.jdom.JDOMXMLUtils;

public class WPTEditor extends MultiPageEditorPart {

	public static final String ID = "ldauthor.wpt.ui.editors.WPTEditor";

	private WPTEditor self;

	private EditorPart browserpage;
	private EditorPart editorpage;

	@Override
	public void init(IEditorSite site, IEditorInput input)
	throws PartInitException
	{
		self = this;

		if (FileStoreEditorInput.class == input.getClass()) {
			File edit = new File(((FileStoreEditorInput) input).getURI());
			input = new PathEditorInput(new Path(edit.getAbsolutePath()));
		}

		setSite(site);
		setInput(input);

		setPartName(input.getName());
	}

	@Override
	protected void createPages()
	{
		IEditorInput i = this.getEditorInput();

		browserpage = new WPTBrowserPage();
		editorpage  = new WPTRawEditPage();

		try {
			setPageText(
				addPage(browserpage, i),
				Messages.ElementSelectBrowserTitle);

			setPageText(
				addPage(editorpage, i),
				Messages.WPTRawEditorTitle);

		} catch (Exception e) {
			e.printStackTrace(System.err);
		}

		browserpage.addPartPropertyListener(new IPropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent pce)
			{
				// early return for non-xpath_selected property change
				// handle xpath_selected property change only
				if (!pce.getProperty().equals("xpath_selected")) return;

				BusyIndicator.showWhile(Display.getCurrent(), new Thread() {
					public void run() {
						// prepare cache of the ontology listing before display
						LearningDesign.getInstance().prepare_cache();
					}
				});

				// display the ontology listing
				WPTMappingDialog md = new WPTMappingDialog(
					Display.getCurrent().getActiveShell());

				if (Dialog.CANCEL == md.open()) return;

				String ld_element = md.selectedElement();
				String xpath      = (String) pce.getNewValue();
				String page_url   = ((WPTBrowserPage) browserpage).getUrl();

				if (null == ld_element) return;


				TextEditor te = (TextEditor) editorpage;
				XMLDocumentProvider dp = (XMLDocumentProvider) te.getDocumentProvider();

				String xml_text = dp.getDocument().get();

				try {
					Document xml_doc = JDOMXMLUtils.readXMLString(xml_text);
					Element xml_root = xml_doc.getRootElement();

					// add rule
					Element template = (Element) XPath.selectSingleNode(
						xml_root, "//template[@name='default-template']");

					if (null == template) {
						template = new Element("template");
						template.setAttribute("name", "default-template");
						template.setAttribute("language", "icdl:template");

						xml_root.addContent(template);
					}

					Element rule = new Element("html_tag");
					rule.setAttribute("xpath", xpath);
					rule.setAttribute("content", ld_element);

					template.addContent(rule);

					// add url
					Element urls = (Element) XPath.selectSingleNode(
						xml_root, "//urls[@name='default-urls']");

					if (null == urls) {
						urls = new Element("urls");
						urls.setAttribute("name", "default-urls");
						urls.setAttribute("template", "default-template");

						xml_root.addContent(urls);
					}

					boolean exist = false;
					List url_list = XPath.selectNodes(urls, "//url");
					Iterator iter = url_list.iterator();
					Element url;
					String url_location;
					while (iter.hasNext()) {
						url = (Element) iter.next();

						Pattern regex = Pattern.compile(url.getAttributeValue("url"));
						if (regex.matcher(page_url).find()) exist = true;
					}
					if (!exist) {
						url = new Element("url");
						url.setAttribute("url", page_url);

						urls.addContent(url);
					}

					dp.getDocument().set(JDOMXMLUtils.write2XMLString(xml_doc));

					self.setActivePage(1);

				} catch (Exception e) {
					// fallback
					dp.getDocument().set(xml_text);

					e.printStackTrace(System.err);
				}
			}
		});
	}

	@Override
	public void doSave(IProgressMonitor monitor) {
		TextEditor te = (TextEditor) editorpage;
		XMLDocumentProvider dp = (XMLDocumentProvider) te.getDocumentProvider();
		try {
			dp.saveDocument(monitor, getEditorInput(), dp.getDocument(), true);
		} catch (CoreException e) {
			e.printStackTrace(System.err);
		}
	}

	@Override
	public void doSaveAs() {
	}

	@Override
	public boolean isSaveAsAllowed() {
		return false;
	}

}
