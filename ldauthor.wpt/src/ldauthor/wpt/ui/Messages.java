package ldauthor.wpt.ui;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {

    private static final String BUNDLE_NAME = "ldauthor.wpt.ui.messages";

    public static String ImportDialogShellTitle;

    public static String ImportDialogTitle;

    public static String ImportDialogAbstract;

    public static String ElementSelectBrowserURLLabel;

    public static String ElementSelectBrowserXPATHLabel;

    public static String BrowserNotSupportedMsg;

    public static String CreateDialogShellTitle;

    public static String CreateDialogTitle;

    public static String CreateDialogAbstract;

    public static String CreateDialogWPTLabel;

    public static String MappingDialogShellTitle;

    public static String MappingDialogTitle;

    public static String MappingDialogAbstract;

    public static String ElementSelectBrowserTitle;

    public static String WPTRawEditorTitle;

    static {
        // initialize resource bundle
        NLS.initializeMessages(BUNDLE_NAME, Messages.class);
    }

    private Messages() {
    }
}