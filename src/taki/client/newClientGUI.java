package taki.client;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Monitor;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import taki.common.ChatMessage;
import taki.common.ChatMessage.MsgType;

public class newClientGUI implements ClientHandler, SelectionListener {
	private String _strServer;
	private int _intPort;

	Display _display;
	Shell _shell;
	GridData _gridData;

	Text _txtServer;
	Text _txtPort;
	Text _txtUsername;
	Combo dogBreed;
	Canvas dogPhoto;
	Image dogImage;
	List _cards;
	Text _txtMsgs;
	Text _txtSendMsg;
	Button _btnJoin;
	Button _btnSend;

	private boolean _isConnected = false;
	private Client _client;

	public newClientGUI(String host, int port) {
		_strServer = host;
		_intPort = port;

		initShell();
		startShell();
	}

	private void initWidgets() {
		GridLayout gridLayout = new GridLayout(3, true);
		_shell.setLayout(gridLayout);

		initLoginWidget();

		initGameWidget();

		initUsersWidget();

		initChatWidget();

		updateControls(false);
	}

	private void initUsersWidget() {
		GridLayout gridLayout = new GridLayout();
		;
		Group userGroup = new Group(_shell, SWT.NONE);
		userGroup.setText("Users");
		userGroup.setLayout(gridLayout);

		_gridData = new GridData(GridData.FILL, GridData.BEGINNING, true, true);
		_gridData.verticalSpan = 3;
		userGroup.setLayoutData(_gridData);

		_cards = new List(userGroup, SWT.SINGLE | SWT.BORDER | SWT.V_SCROLL);
		_cards.setItems(new String[] { "Best of Breed", "Prettiest Female", "Handsomest Male", "Best Dressed",
				"Fluffiest Ears", "Most Colors", "Best Performer", "Loudest Bark", "Best Behaved", "Prettiest Eyes",
				"Most Hair", "Longest Tail", "Cutest Trick" });
		_gridData = new GridData(GridData.FILL, GridData.FILL, true, true);
		// int listHeight = _cards.getItemHeight() * 12;
		// Rectangle trim = _cards.computeTrim(0, 0, 0, listHeight);
		// _gridData.heightHint = trim.height;
		_cards.setLayoutData(_gridData);

	}

	private void initGameWidget() {
		GridLayout gridLayout = new GridLayout();
		Group photoGroup = new Group(_shell, SWT.NONE);
		photoGroup.setText("Game");
		gridLayout.numColumns = 2;
		photoGroup.setLayout(gridLayout);
		_gridData = new GridData(GridData.FILL, GridData.FILL, true, true);
		_gridData.horizontalSpan = 2;
		_gridData.verticalSpan = 3;
		photoGroup.setLayoutData(_gridData);

		dogPhoto = new Canvas(photoGroup, SWT.BORDER);
		_gridData = new GridData(GridData.FILL, GridData.FILL, true, true);
		_gridData.minimumHeight = 200;
		// _gridData.widthHint = 80;
		// _gridData.heightHint = 80;
		_gridData.horizontalSpan = 2;
		dogPhoto.setLayoutData(_gridData);
		dogPhoto.addPaintListener(new PaintListener() {
			public void paintControl(final PaintEvent event) {
				if (dogImage != null) {
					event.gc.drawImage(dogImage, 0, 0);
				}
			}
		});

		Button browse = new Button(photoGroup, SWT.PUSH);
		browse.setText("Browse...");
		_gridData = new GridData(GridData.FILL, GridData.CENTER, true, false);
		// _gridData.horizontalIndent = 5;
		browse.setLayoutData(_gridData);
		browse.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				String fileName = new FileDialog(_shell).open();
				if (fileName != null) {
					dogImage = new Image(_display, fileName);
				}
			}
		});

		Button delete = new Button(photoGroup, SWT.PUSH);
		delete.setText("Delete");
		_gridData = new GridData(GridData.FILL, GridData.BEGINNING, true, false);
		_gridData.horizontalIndent = 5;
		delete.setLayoutData(_gridData);
		delete.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				if (dogImage != null) {
					dogImage.dispose();
					dogImage = null;
					dogPhoto.redraw();
				}
			}
		});
	}

	private void initLoginWidget() {
		GridLayout gridLayout;
		Group loginInfo = new Group(_shell, SWT.NONE);
		loginInfo.setText("Login");
		gridLayout = new GridLayout();
		gridLayout.numColumns = 4;
		loginInfo.setLayout(gridLayout);
		_gridData = new GridData(GridData.FILL, GridData.BEGINNING, true, false);
		loginInfo.setLayoutData(_gridData);

		new Label(loginInfo, SWT.NONE).setText("Server:");
		_txtServer = new Text(loginInfo, SWT.SINGLE | SWT.BORDER);
		_txtServer.setText(_strServer);
		_txtServer.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false));

		new Label(loginInfo, SWT.NONE).setText("Port:");
		_txtPort = new Text(loginInfo, SWT.SINGLE | SWT.BORDER);
		_txtPort.setText("" + _intPort);
		_gridData = new GridData(GridData.FILL, GridData.CENTER, true, false);
		_txtPort.setLayoutData(_gridData);

		new Label(loginInfo, SWT.NONE).setText("Userame:");
		_txtUsername = new Text(loginInfo, SWT.SINGLE | SWT.BORDER);
		_txtUsername.setText("Anonymous");
		_gridData = new GridData(GridData.FILL, GridData.CENTER, true, false);
		_txtUsername.setLayoutData(_gridData);

		_btnJoin = new Button(loginInfo, SWT.PUSH);
		_btnJoin.setText("Join");
		_gridData = new GridData(GridData.FILL, GridData.CENTER, true, false);
		_gridData.horizontalSpan = 2;
		_btnJoin.setLayoutData(_gridData);

		_btnJoin.addSelectionListener(this);
	}

	private void initChatWidget() {
		GridLayout gridLayout;
		Group ownerInfo = new Group(_shell, SWT.NONE);
		ownerInfo.setText("Chat");
		gridLayout = new GridLayout();
		gridLayout.numColumns = 3;
		ownerInfo.setLayout(gridLayout);
		_gridData = new GridData(GridData.FILL, GridData.END, true, false);
		_gridData.horizontalSpan = 2;
		_gridData.verticalSpan = 2;
		ownerInfo.setLayoutData(_gridData);

		// new Label(ownerInfo, SWT.NONE).setText("Name:");
		_txtMsgs = new Text(ownerInfo, SWT.MULTI | SWT.BORDER | SWT.WRAP | SWT.V_SCROLL);
		_txtMsgs.setEditable(false);
		_gridData = new GridData(GridData.FILL, GridData.CENTER, true, false);
		_gridData.heightHint = 90;
		_gridData.horizontalSpan = 2;
		_txtMsgs.setLayoutData(_gridData);

		_btnSend = new Button(ownerInfo, SWT.PUSH);
		_btnSend.setText("Send");
		_gridData = new GridData(GridData.FILL, GridData.FILL, false, false);
		_gridData.verticalSpan = 2;
		_btnSend.setLayoutData(_gridData);
		_btnSend.addSelectionListener(this);

		new Label(ownerInfo, SWT.NONE).setText("Send:");
		_txtSendMsg = new Text(ownerInfo, SWT.SINGLE | SWT.BORDER);
		_txtSendMsg.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false));

	}

	private void initShell() {

		// Define shell
		_display = new Display();
		_shell = new Shell(_display, SWT.CLOSE | SWT.TITLE);
		_shell.setText("Taki Game");
		_shell.setSize(800, 500);

		// Center shell
		_shell.setLocation(findMonitorCenter());

		initWidgets();
	}

	private void startShell() {

		// Open Shell
		_shell.open();
		while (!_shell.isDisposed()) {
			if (!_display.readAndDispatch())
				_display.sleep();
		}
		_display.dispose();
	}

	private void updateControls(boolean isConnected) {
		_isConnected = isConnected;

		_txtServer.setEnabled(!isConnected);
		_txtPort.setEnabled(!isConnected);
		_txtUsername.setEnabled(!isConnected);

		if (isConnected) {
			_btnJoin.setText("Disconnect");
		} else {
			_btnJoin.setText("Join");
		}

		// // disable login button
		// login.setEnabled(!isConnected);
		// // enable the 2 buttons
		// logout.setEnabled(isConnected);
		// whoIsIn.setEnabled(isConnected);
		// // disable the Server and Port JTextField
		// tfServer.setEditable(!isConnected);
		// tfPort.setEditable(!isConnected);
		// // Action listener for when the user enter a message
		// chooseCard.setEnabled(isConnected);
		// if (isConnected) {
		// tf.addActionListener(this);
		//
		// tf.setText("");
		// label.setText("Enter your message below");
		// }
		// else {
		// tf.removeActionListener(this);
		//
		// label.setText("Enter your username below");
		//
		// tf.setText("Anonymous");
		// // reset port number and host name as a construction time
		// tfPort.setText("" + defaultPort);
		// tfServer.setText(defaultHost);
		// // let the user change them
		// // don't react to a <CR> after the username
		// }
	}

	private Point findMonitorCenter() {
		Monitor primary = _display.getPrimaryMonitor();
		Rectangle bounds = primary.getBounds();
		Rectangle rect = _shell.getBounds();

		int x = bounds.x + (bounds.width - rect.width) / 2;
		int y = bounds.y + (bounds.height - rect.height) / 2;

		Point pntCenter = new Point(x, y);

		return pntCenter;
	}

	public void append(String strText) {
		Display.getDefault().asyncExec(new Runnable() {

			@Override
			public void run() {
				_txtMsgs.append(strText);
			}
		});
	}

	public void onConnectionFailed(String strMsg) {
		Display.getDefault().asyncExec(new Runnable() {

			@Override
			public void run() {
				updateControls(false);

				// MessageBox msg = new MessageBox(_shell, SWT.ERROR | SWT.OK);
				// msg.setMessage(strMsg);
				// msg.setText("Connection failed");
				// msg.open();
			}
		});
	}

	public void onConnected() {
		Display.getDefault().asyncExec(new Runnable() {

			@Override
			public void run() {
				updateControls(true);
			}
		});
	}

	public void widgetSelected(SelectionEvent e) {
		if (e.widget == _btnJoin) {
			if ( !_isConnected) {
			_client = new Client(_txtServer.getText(), Integer.parseInt(_txtPort.getText()), _txtUsername.getText(),
					this);
			// test if we can start the Client
			if (!_client.start())
				return; 
			} else {
				_client.sendMessage(new ChatMessage(MsgType.LOGOUT, ""));
				return;
			}
			
		} else if (e.widget == _btnSend && _isConnected) {
			_client.sendMessage(new ChatMessage(MsgType.MESSAGE, _txtSendMsg.getText()));
			_txtSendMsg.setText("");
			return;
		}
	}

	public void widgetDefaultSelected(SelectionEvent e) {
		// TODO Auto-generated method stub

	}
}
