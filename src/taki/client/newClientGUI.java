package taki.client;

import java.util.ArrayList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Monitor;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import taki.common.ChatMessage;
import taki.common.ChatMessage.MsgType;
import taki.common.GameMessage;
import taki.common.GameMessage.CardType;

public class newClientGUI implements ClientHandler, SelectionListener {
	private String _strServer;
	private int _intPort;

	Display _display;
	Shell _shell;
	GridData _gridData;

	Text _txtServer;
	Text _txtPort;
	Text _txtUsername;
	Combo _card;
	Canvas _gameBoard;
	Image dogImage;
	List _users;
	Text _txtMsgs;
	Text _txtSendMsg;
	Button _btnJoin;
	Button _btnSend;
	
	private boolean _isDisposing = false;

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

		_users = new List(userGroup, SWT.SINGLE | SWT.BORDER | SWT.V_SCROLL);
		_gridData = new GridData(GridData.FILL, GridData.FILL, true, true);
		// int listHeight = _cards.getItemHeight() * 12;
		// Rectangle trim = _cards.computeTrim(0, 0, 0, listHeight);
		// _gridData.heightHint = trim.height;
		_users.setLayoutData(_gridData);

	}

	private void initGameWidget() {
		GridLayout gridLayout = new GridLayout(2, true);
		Group gameGroup = new Group(_shell, SWT.NONE);
		gameGroup.setText("Game");
		gameGroup.setLayout(gridLayout);
		_gridData = new GridData(GridData.FILL, GridData.FILL, true, true);
		_gridData.horizontalSpan = 2;
		_gridData.verticalSpan = 3;
		gameGroup.setLayoutData(_gridData);

		_gameBoard = new Canvas(gameGroup, SWT.BORDER);
		_gridData = new GridData(GridData.FILL, GridData.FILL, true, true);
		_gridData.minimumHeight = 200;
		// _gridData.widthHint = 80;
		// _gridData.heightHint = 80;
		_gridData.horizontalSpan = 2;
		_gameBoard.setLayoutData(_gridData);
		_gameBoard.addPaintListener(new PaintListener() {
			public void paintControl(final PaintEvent event) {
				if (dogImage != null) {
					event.gc.drawImage(dogImage, 0, 0);
				}
			}
		});

		_card = new Combo(gameGroup, SWT.PUSH);
		
		CardType[] cardTypes = GameMessage.CardType.values();
		String[] cardNames = new String[cardTypes.length];
		
		for (int i = 0; i < cardTypes.length; i++) {
		    cardNames[i] = cardTypes[i].name();
		}
		_card.setItems(cardNames);
		
//		Button browse = new Button(photoGroup, SWT.PUSH);
//		browse.setText("Browse...");
		_gridData = new GridData(GridData.FILL, GridData.CENTER, true, false);
		// _gridData.horizontalIndent = 5;
//		browse.setLayoutData(_gridData);
//		browse.addSelectionListener(new SelectionAdapter() {
//			public void widgetSelected(SelectionEvent event) {
//				String fileName = new FileDialog(_shell).open();
//				if (fileName != null) {
//					dogImage = new Image(_display, fileName);
//				}
//			}
//		});

		Button choose = new Button(gameGroup, SWT.PUSH);
		choose.setText("Choose");
		_gridData = new GridData(GridData.FILL, GridData.BEGINNING, true, false);
		_gridData.horizontalIndent = 5;
		choose.setLayoutData(_gridData);
		choose.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				if (dogImage != null) {
					dogImage.dispose();
					dogImage = null;
					_gameBoard.redraw();
				}
			}
		});
		drawDeck();
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

		// /*_shell.addListener(SWT.Close, new Listener() {
		// public void handleEvent(Event event) {
		// Display.getCurrent().asyncExec(new Runnable() {
		//
		// @Override
		// public void run() {
		//
		// }
		// });
		// }
		// });*/

		initWidgets();
	}

	private void startShell() {

		// Open Shell
		_shell.open();
		while (!_shell.isDisposed()) {
			if (!_display.readAndDispatch())
				_display.sleep();
		}
		_isDisposing = true;
		if (_client != null)
			_client.disconnect();
		_display.dispose();
	}

	private void drawDeck() {
		Image image = new Image(_display, "./images/new-deck-small.jpg");
		Cursor handCursor = new Cursor(_display, SWT.CURSOR_HAND);
		Cursor arrowCursor = new Cursor(_display, SWT.CURSOR_ARROW);
		
		_gameBoard.addPaintListener(new PaintListener() {
		  public void paintControl(PaintEvent e) {
		    e.gc.drawImage(image, 0, 0);
		  }
		});
		
		_gameBoard.addListener(SWT.MouseMove, new Listener() {
			
			@Override
			public void handleEvent(Event e) {
				Point pnt = new Point(e.x, e.y);
				if (image.getBounds().contains(pnt)) {
				 _shell.setCursor(handCursor);
				} else {
					_shell.setCursor(arrowCursor);
				}
			}
		});
		
		_gameBoard.addListener(SWT.MouseDown, new Listener() {
			
			@Override
			public void handleEvent(Event e) {
				if (image.getBounds().contains(new Point(e.x, e.y))) {
					System.out.println("CLICKED!");
				}
			}
		});
	}
	
	private void updateControls(boolean isConnected) {
		_isConnected = isConnected;

		_txtServer.setEnabled(!isConnected);
		_txtPort.setEnabled(!isConnected);
		_txtUsername.setEnabled(!isConnected);

		if (isConnected) {
			_btnJoin.setText("Disconnect");

			_txtSendMsg.setFocus();
			_shell.setDefaultButton(_btnSend);
			
		} else {
			_btnJoin.setText("Join");
			_users.removeAll();
			
			_txtUsername.setFocus();
			_txtUsername.selectAll();
			_shell.setDefaultButton(_btnJoin);
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
				_txtMsgs.append(strText + "\n");
				_txtSendMsg.setFocus();
			}
		});
	}

	public void widgetSelected(SelectionEvent e) {
		if (e.widget == _btnJoin) {
			if (!_isConnected) {
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
	
	public void onConnectionFailed(String strMsg) {
		Display.getDefault().asyncExec(new Runnable() {

			@Override
			public void run() {
				if (!_isDisposing) {
					updateControls(false);
					_txtMsgs.append(strMsg + "\n");

					// MessageBox msg = new MessageBox(_shell, SWT.ERROR |
					// SWT.OK);
					// msg.setMessage(strMsg);
					// msg.setText("Connection failed");
					// msg.open();

				}
			}
		});
	}

	public void onConnected(String strMsg) {
		Display.getDefault().asyncExec(new Runnable() {

			@Override
			public void run() {
				updateControls(true);
				_txtMsgs.append(strMsg + "\n");
			}
		});
	}

	@Override
	public void onUserListRecieved(ArrayList<String> alUsers) {
		Display.getDefault().asyncExec(new Runnable() {

			@Override
			public void run() {
				String[] strUsers = new String[alUsers.size()];
				_users.setItems(alUsers.toArray(strUsers));
			}
		});
	}
}
