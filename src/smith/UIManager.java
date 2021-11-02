package smith;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Shell;

import swing2swt.layout.BorderLayout;
import swing2swt.layout.FlowLayout;

public class UIManager {

	private static Composite bottomComposite;
	private static Button btnCancel;
	private static Button btnStart;
	private static Composite centerComposite;
	public static Display display;
	private static Composite dupeFilesSelection;
	private static Button fileHashCheck;
	private static Label lblDupeFiles;
	private static Composite leftComposite;
	private static Button numberSuffixCheck;
	private static Button numberSuffixCheckModifiedSort;
	private static Button numberSuffixCheckRecentSort;
	private static Button numberSuffixCheckSizeSort;
	private static ProgressBar progressBar;
	private static Composite rightComposite;
	public static Shell shell;
	private static Composite topComposite;

	private static void defineElements() {
		// Define Basics
		topComposite = new Composite(shell, SWT.NONE);
		bottomComposite = new Composite(shell, SWT.NONE);
		leftComposite = new Composite(shell, SWT.NONE);
		rightComposite = new Composite(shell, SWT.NONE);
		centerComposite = new Composite(shell, SWT.NONE);

		// Define Top

		// Define Bottom
		btnStart = new Button(bottomComposite, SWT.NONE);
		progressBar = new ProgressBar(bottomComposite, SWT.SMOOTH);
		btnCancel = new Button(bottomComposite, SWT.NONE);

		// Define Left

		// Define Right

		// Define Center
		dupeFilesSelection = new Composite(centerComposite, SWT.NONE);

		// Define Layout
		centerComposite.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		bottomComposite.setLayout(new FillLayout(SWT.HORIZONTAL));

		dupeFilesSelection.setLayout(new RowLayout(SWT.VERTICAL));

		// Define Basic Composite Elements
		lblDupeFiles = new Label(dupeFilesSelection, SWT.NONE);

		fileHashCheck = new Button(dupeFilesSelection, SWT.CHECK);
		numberSuffixCheck = new Button(dupeFilesSelection, SWT.CHECK);
		numberSuffixCheckSizeSort = new Button(dupeFilesSelection, SWT.RADIO);
		numberSuffixCheckRecentSort = new Button(dupeFilesSelection, SWT.RADIO);
		numberSuffixCheckModifiedSort = new Button(dupeFilesSelection, SWT.RADIO);
	}

	private static void setData() {
		// Text
		numberSuffixCheckModifiedSort.setText("Keep most recent modified");
		numberSuffixCheckRecentSort.setText("Keep most recently created");
		numberSuffixCheckSizeSort.setText("Keep largest");
		lblDupeFiles.setText("Duplicate Files");
		numberSuffixCheck.setText("(#) File suffix");
		fileHashCheck.setText("Same file hash");
		btnStart.setText("Start");
		shell.setText("SmithCleaner");
		btnCancel.setText("Cancel");

		// Button Stuff
		numberSuffixCheckModifiedSort.setEnabled(false);
		numberSuffixCheckRecentSort.setEnabled(false);
		numberSuffixCheckSizeSort.setEnabled(false);
		btnCancel.setEnabled(false);

		// Layouts
		bottomComposite.setLayoutData(BorderLayout.SOUTH);
		rightComposite.setLayoutData(BorderLayout.WEST);
		topComposite.setLayoutData(BorderLayout.NORTH);
		leftComposite.setLayoutData(BorderLayout.EAST);
		centerComposite.setLayoutData(BorderLayout.CENTER);

		// Other
		progressBar.setMinimum(0);
		progressBar.setMaximum(100);
	}

	private static void setListeners() {
		numberSuffixCheck.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (numberSuffixCheck.getSelection()) {
					numberSuffixCheckModifiedSort.setEnabled(true);
					numberSuffixCheckRecentSort.setEnabled(true);
					numberSuffixCheckSizeSort.setEnabled(true);
				} else {
					numberSuffixCheckModifiedSort.setEnabled(false);
					numberSuffixCheckRecentSort.setEnabled(false);
					numberSuffixCheckSizeSort.setEnabled(false);
				}
			}
		});
		btnStart.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				btnCancel.setEnabled(true);
				MessageBox dialog = new MessageBox(shell, SWT.OK | SWT.CANCEL | SWT.ICON_QUESTION);
				dialog.setMessage("Confirm Starting File Deletion?");
				int returnVal = dialog.open();
				btnCancel.setEnabled(false);
				if (returnVal == 32) {
					progressBar.setSelection(5);
					if (numberSuffixCheck.getSelection()) {
						if (numberSuffixCheckSizeSort.getSelection()) {
							DupeFinder.deleteFilesWithSuffixKeepLargest();
						}
					}
					progressBar.setSelection(0);
				}
			}
		});
	}

	private static void setupShell() {
		display = Display.getDefault();
		shell = new Shell();
		shell.setSize(700, 500);
		shell.setMinimumSize(700, 500);
		shell.setLayout(new BorderLayout(0, 0));
	}

	/**
	 * @wbp.parser.entryPoint
	 */
	public static void setupUIManager() {
		try {
			setupShell();
			defineElements();
			setData();
			setListeners();
			UIManager window = new UIManager();
			window.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void open() {
		shell.open();
		shell.layout();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}
}
