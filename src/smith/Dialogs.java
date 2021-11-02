package smith;

import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class Dialogs extends Dialog {

	protected Object result;
	protected Shell shell;
	
	public Dialogs(Shell parent, int style, String title) {
		super(parent, style);
		setText(title);
	}

	public Object openYesOrNo() {
		createContentsYN();
		shell.open();
		shell.layout();
		Display display = getParent().getDisplay();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		return result;
	}

	private void createContentsYN() {
		shell = new Shell(getParent(), getStyle());
		shell.setSize(450, 300);
		shell.setText(getText());
		
	}

}
