package ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.rmi.RemoteException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;

import rmi.RemoteHelper;


public class MainFrame extends JFrame {
	private static final long serialVersionUID = 1L;
	private static ImageIcon iconLogin = new ImageIcon("Graphics/account1.png");
	private static ImageIcon iconLoginEntered = new ImageIcon("Graphics/account2.png");
	private Font font;
	private JTextArea codeArea;
	private JTextArea inputDataArea;
	private JTextArea resultArea;
	private JPanel mainJPanel;
	private JPanel bottomJPanel;
	private JMenuBar menuBar;
	private JButton loginButton;
	private JMenu runMenu;
	private JMenu openMenu;
	private JMenu userMenu;
	private JMenu versionMenu;
	private JMenu operationMenu;
	private LoginFrame loginFrame;
	private String[] openCmdName;
	private String[] openVersionName;
	private String currentFileName;
	private String currentVersionName;
	private String[] versions;
	private String[] sortedVersions;
	//Undo
	private ArrayList<String> textlist;
	private ArrayList<String> stateNode;
	boolean isDeleteCmd=true;
	boolean isInputCmd=true;
	boolean isStateChange=false;
	boolean isUndo=false;
	boolean isUndoChange=true;
	boolean isRedoChange=false;
	int temp=0;
	int undoTimes=0;
	int redoTimes=0;
	int stateNum=0;
	boolean tempInput=false;
	boolean tempDelete=false;
	boolean tempStateChange=false;
	boolean isRemoveOnce=false;
	
	public MainFrame() {
		// 创建窗体
		this.setTitle("BF Client");
		this.setFont(font);
		this.getContentPane().setLayout(new BorderLayout());
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(600,800);
		this.setLocationRelativeTo(null);
		//初始化组件
		initComponent();
		this.setVisible(true);
	}
	
	/**
	 * 初始化GUI组件
	 */
    private void initComponent(){
		font=new Font("微软雅黑",Font.PLAIN,15);
		
		menuBar = new JMenuBar();
		
		JMenu fileMenu = new JMenu("File");
		fileMenu.setFont(font);
		menuBar.add(fileMenu);
		
		runMenu = new JMenu("Run");
		runMenu.setFont(font);
		JMenuItem runMenuItem = new JMenuItem("Execute");
		runMenu.add(runMenuItem);
		menuBar.add(runMenu);
		
		versionMenu = new JMenu("Version");
		versionMenu.setFont(font);
		menuBar.add(versionMenu);
		
		operationMenu = new JMenu("Operate");
		operationMenu.setFont(font);
		menuBar.add(operationMenu);
		
		JMenuItem newMenuItem = new JMenuItem("New");
		fileMenu.add(newMenuItem);
		openMenu = new JMenu("Open");
		fileMenu.add(openMenu);
		JMenuItem saveMenuItem = new JMenuItem("Save");
		fileMenu.add(saveMenuItem);
		JMenuItem exitMenuItem = new JMenuItem("Exit");
		fileMenu.add(exitMenuItem);
		
		
		JMenuItem undoMenuItem = new JMenuItem("undo");
		operationMenu.add(undoMenuItem);
		JMenuItem redoMenuItem = new JMenuItem("redo");
		operationMenu.add(redoMenuItem);
		this.setJMenuBar(menuBar);

		//添加事件监听
		newMenuItem.addActionListener(new MenuItemActionListener());
		runMenuItem.addActionListener(new MenuItemActionListener());
		saveMenuItem.addActionListener(new SaveActionListener());
		exitMenuItem.addActionListener(new MenuItemActionListener());
		undoMenuItem.addActionListener(new MenuItemActionListener());
		redoMenuItem.addActionListener(new MenuItemActionListener());
		//登录按钮，设置间隔
		menuBar.add(Box.createHorizontalGlue());
		loginButton=new JButton();
		loginButton.setBorder(null);
		loginButton.setSize(20,20);
		loginButton.setContentAreaFilled(false);
		loginButton.setBorderPainted(false);
		loginButton.setIcon(iconLogin);
		loginButton.setRolloverIcon(iconLoginEntered);
		loginButton.addActionListener(new ButtonActionListener());
		menuBar.add(loginButton);
		loginFrame=new LoginFrame(this);
		
		mainJPanel = new JPanel();
		mainJPanel.setPreferredSize(new Dimension(0, 500));
        mainJPanel.setLayout(new BorderLayout());
		//代码区
		codeArea = new JTextArea();
		codeArea.setMargin(new Insets(10, 10, 10, 10));
		codeArea.setBackground(Color.WHITE);
		codeArea.setLineWrap(true);
		codeArea.setFont(new Font("宋体", Font.PLAIN, 13));
		codeArea.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY,3));
		codeArea.addKeyListener(new undoListener());
		codeArea.addCaretListener(new cursorListener());
		JScrollPane scroller=new JScrollPane(codeArea);
		scroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		mainJPanel.add(scroller);
		this.getContentPane().add(mainJPanel, BorderLayout.CENTER);
		
		textlist=new ArrayList<String>();
		textlist.add(codeArea.getText());
		stateNode=new ArrayList<String>();
		
		bottomJPanel = new JPanel();
        bottomJPanel.setPreferredSize(new Dimension(0, 300));
        bottomJPanel.setLayout(new GridLayout());
		//输入数据区
		inputDataArea = new JTextArea();
		inputDataArea.setBackground(Color.white);
		inputDataArea.setBorder(BorderFactory.createTitledBorder("Parameter"));
		bottomJPanel.add(inputDataArea);
		// 输出数据区，显示结果
		resultArea = new JTextArea();
		resultArea.setBackground(Color.white);
		resultArea.setBorder(BorderFactory.createTitledBorder("Result"));
		resultArea.setEditable(false);
		bottomJPanel.add(resultArea);
		this.getContentPane().add(bottomJPanel, BorderLayout.SOUTH);
    }
    
    /**
     * 设置账户菜单
     * @param username 用户名
     */
	public void setAccountMenu(String username){
		menuBar.remove(loginButton);
		userMenu=new JMenu("0");
		menuBar.add(userMenu);
		userMenu.setText(username);
		JMenuItem changeMenuItem = new JMenuItem("Change");
		JMenuItem outMenuItem = new JMenuItem("Out");
		
		changeMenuItem.addActionListener(new MenuItemActionListener());
		outMenuItem.addActionListener(new MenuItemActionListener());
		
		userMenu.add(changeMenuItem);
		userMenu.add(outMenuItem);
	}
	
	/**
	 * 获得文件目录,并在open中显示
	 * @param userId 用户名
	 * @param password 用户密码
	 */
	public void initOpen(String userId,String password){
		openMenu.removeAll();
		try {
			String[] fileItems=RemoteHelper.getInstance().getIOService().readFileList(userId,password);
			
			if(fileItems!=null){
				openCmdName=new String[fileItems.length];
				for (int i = 0; i < fileItems.length; i++) {
					openCmdName[i]=fileItems[i];
					JMenuItem openMenuItem = new JMenuItem(fileItems[i]);
					openMenuItem.addActionListener(new MenuItemActionListener());
					openMenu.add(openMenuItem);
				}
			}else{
				return;
			}
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 子菜单响应事件
	 */
	class MenuItemActionListener implements ActionListener {
		
		@Override
		public void actionPerformed(ActionEvent e) {
			String cmd = e.getActionCommand();
			/*
			 * 执行
			 */
			if (cmd.equals("Execute")) {
				String code = codeArea.getText();
				String param=inputDataArea.getText();
				try {
					String result=RemoteHelper.getInstance().getExecuteService().execute(code, param);
					resultArea.setText(result);
				} catch (RemoteException e1) {
					e1.printStackTrace();
				}
				
			}
			/*
			 * 创建新文件，需要先登陆
			 */
			else if(cmd.equals("New")){
				if(loginFrame.username!=null&&loginFrame.password!=null){
					if(loginFrame.username!=""||loginFrame.password!=""){
				    new NewFileFrame(MainFrame.this,loginFrame.username,loginFrame.password).setVisible(true);
				   }else{
					   JOptionPane.showMessageDialog(null,"Please login first!","Failed!",JOptionPane.WARNING_MESSAGE);
				   }
				}else{
					JOptionPane.showMessageDialog(null,"Please login first!","Failed!",JOptionPane.WARNING_MESSAGE);
				}
			}
			/*
			 * 关闭程序
			 */
			else if (cmd.equals("Exit")) {
				System.exit(0);
			}
			/*
			 * 切换账号
			 */
			else if (cmd.equals("Change")) {
				menuBar.remove(userMenu);
				menuBar.add(loginButton);
				menuBar.repaint();
				loginFrame.username="";
				loginFrame.password="";
				currentFileName=null;
				codeArea.setBorder(null);
				codeArea.setText(null);
				openMenu.removeAll();
				versionMenu.removeAll();
				loginFrame=new LoginFrame(MainFrame.this);
				loginFrame.setVisible(true);
				
			}	
			/*
			 * 退出账号
			 */
			else if (cmd.equals("Out")) {
				menuBar.remove(userMenu);
				menuBar.add(loginButton);
				menuBar.repaint();
				loginFrame.username="";
				loginFrame.password="";
				currentFileName=null;
				codeArea.setBorder(null);
				codeArea.setText(null);
				openMenu.removeAll();
				versionMenu.removeAll();  
			}
			/*
			 * open 打开相应文件
			 */
			if(openCmdName!=null){
			for(int i = 0; i < openCmdName.length; i++){
				if(cmd.equals(openCmdName[i])){
					String codeFileContent;
					//将当前文本域清空
					codeArea.setText(null);
					//将参数域清空
					inputDataArea.setText("");
					//初始化Version
					initVersion(openCmdName[i]);
					//要换文件名
					currentFileName=openCmdName[i];
					codeArea.setBorder(BorderFactory.createTitledBorder(currentFileName));
					//将currentFile下的最近version的代码显示到文本域
					//要换版本名
					currentVersionName=sortedVersions[sortedVersions.length-1];
					try {
						codeFileContent = RemoteHelper.getInstance().getIOService().readFile(loginFrame.username,loginFrame.password,currentFileName,sortedVersions[sortedVersions.length-1]);
						codeArea.setText(codeFileContent);
						//将打开文件的内容放入list中,撤销重做初始化
						textlist.clear();
						stateNode.clear();
						textlist.add(codeFileContent);
						stateNum=0;
						isDeleteCmd=true;
						isInputCmd=true;
						isStateChange=false;
						isUndo=false;
						isUndoChange=true;
						isRedoChange=false;
						undoTimes=0;
						redoTimes=0;
						stateNum=0;
						tempInput=false;
						tempDelete=false;
						tempStateChange=false;
						isRemoveOnce=false;
					} catch (RemoteException e1) {
						e1.printStackTrace();
					} 
				}
			  }
			}
			/*
			 * version打开相应文件
			 */
			if(openVersionName!=null){
			for (int i = 0; i < openVersionName.length; i++) {
				if(cmd.equals(openVersionName[i])){
					String codeFileContent;
					try {
						codeFileContent = RemoteHelper.getInstance().getIOService().readFile(loginFrame.username,loginFrame.password,currentFileName,sortedVersions[i]);
						codeArea.setText(codeFileContent);
						inputDataArea.setText("");
						//将打开文件的内容放入list中,重新初始化
						textlist.clear();
						stateNode.clear();
						textlist.add(codeFileContent);
						
						//撤销重做初始化
						stateNum=0;
						isDeleteCmd=true;
						isInputCmd=true;
						isStateChange=false;
						isUndo=false;
						isUndoChange=true;
						isRedoChange=false;
						undoTimes=0;
						redoTimes=0;
						stateNum=0;
						tempInput=false;
						tempDelete=false;
						tempStateChange=false;
						isRemoveOnce=false;
					} catch (RemoteException e1) {
						e1.printStackTrace();
					}
					//要换版本名
					currentVersionName=openVersionName[i];
				}
			}
		}
			
			/*
			 * undo
			 */
			if(cmd.equals("undo")){
				tempInput=isInputCmd;
				tempDelete=isDeleteCmd;
				
				undoTimes++;
//				System.out.println(stateNode.size()-undoTimes);
				if(stateNode.size()-undoTimes<0){
					undoTimes--;
					return;
				}
				if(stateNum==0){
					undoTimes--;
					return;
				}
  				//保存撤销前的状态
				if(isUndoChange){
					if(!isRedoChange){
				stateNode.add(textlist.get(textlist.size()-1));
				isUndoChange=false;
				isRedoChange=true;
				}
				}
				codeArea.setText(stateNode.get(stateNode.size()-undoTimes-1));
				isDeleteCmd=tempDelete;
				isInputCmd=tempInput;
				tempStateChange=true;
				isUndo=true;
				isRemoveOnce=false;
				stateNum--;
			}
			/*
			 * redo
			 */
			if(cmd.equals("redo")){
				tempInput=isInputCmd;
				tempDelete=isDeleteCmd;
				redoTimes++;
				if(undoTimes==0){
					redoTimes=0;
					return;
				}
				if(redoTimes<=undoTimes){
					codeArea.setText(stateNode.get(stateNode.size()-undoTimes));
					//remove掉最后一个
					if(!isRemoveOnce){
					    stateNode.remove(stateNode.size()-undoTimes);
					    isRemoveOnce=true;
					}
					undoTimes--;
					isUndo=false;
					isUndoChange=true;
					isInputCmd=tempInput;
					isDeleteCmd=tempDelete;
					tempStateChange=false;
					stateNum++;
				}
				if(undoTimes==0){
					isRedoChange=false;
				}
				redoTimes=0;
			}
	}


	}
    /**
     * 初始化Version列表
     * @param fileName 文件名
     */
	private void initVersion(String fileName) {
		try {
			versions=RemoteHelper.getInstance().getIOService().readFileVersionList(loginFrame.username,loginFrame.password, fileName);
			versionMenu.removeAll();
			if(versions!=null){
				List<String> list = new ArrayList<String>(versions.length);
				for (int i = 0; i < versions.length; i++) {
				list.add(versions[i]);
				}
				Collections.sort(list);
				sortedVersions=list.toArray(versions);
				openVersionName=new String[sortedVersions.length];
				for (int i = 0; i < sortedVersions.length; i++) {
					openVersionName[i]=sortedVersions[i];
					JMenuItem openVersionMenuItem = new JMenuItem(sortedVersions[i]);
					openVersionMenuItem.addActionListener(new MenuItemActionListener());
					versionMenu.add(openVersionMenuItem);
				}
			}else{
				return;
			}
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}
/**
 * 保存事件监听
 *
 */
	class SaveActionListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			String code = codeArea.getText();
			
			try {
				if(currentFileName==null){
					JOptionPane.showMessageDialog(null,"Please open a file first!","Failed!",JOptionPane.WARNING_MESSAGE);
					return;
				}
				//如果两次保存之间代码没有修改则不做任何操作
				if(code.equals(RemoteHelper.getInstance().getIOService().readFile(loginFrame.username,loginFrame.password,currentFileName,sortedVersions[sortedVersions.length-1]))){
					return;
				}
				if(loginFrame.username!=null&&loginFrame.password!=null){
					if(loginFrame.username!=""||loginFrame.password!=""){
						
						Date date= new Date();//创建一个时间对象，获取到当前的时间
						SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");//设置时间显示格式
						String str = sdf.format(date);//将当前时间格式化为需要的类型
						RemoteHelper.getInstance().getIOService().writeFile(code,loginFrame.username,loginFrame.password,currentFileName,str);
				       //初始化version
						initVersion(currentFileName);
						JOptionPane.showMessageDialog(null,"Save successfully!","Success!",JOptionPane.INFORMATION_MESSAGE);
					}else{
						JOptionPane.showMessageDialog(null,"Please login first!","Failed!",JOptionPane.WARNING_MESSAGE);
				   }
				}else{
					JOptionPane.showMessageDialog(null,"Please login first!","Failed!",JOptionPane.WARNING_MESSAGE);
				}
				
			} catch (RemoteException e1) {
				e1.printStackTrace();
			}
		}

	}
	class ButtonActionListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			 loginFrame.setVisible(true);
	            }
		}
	/**
	 * 撤销重做键盘监听
	 */
	class undoListener extends KeyAdapter {
		@Override
		public void keyReleased(KeyEvent e2) {
			if(e2.getKeyCode()==8){
				if(temp!=0){
				   if((isInputCmd&&isStateChange)||tempStateChange){
					   if(isUndo){ 
//						   System.out.println("here");
						   for(int i=0;i<undoTimes;i++){
								stateNode.remove(stateNode.size()-1);
								}
							isUndo=false;
					   }else{
					   stateNode.add(textlist.get(textlist.size()-1));
					   }
					   tempStateChange=false;
					   isStateChange=false;
					   stateNum++;
				   }
				   undoTimes=0;
				   isInputCmd=false;
				   isDeleteCmd=true;
				   isUndoChange=true;
				   isRedoChange=false;
				   textlist.add(codeArea.getText());
				   
				}
			}else{ 
				if((isDeleteCmd&&!isStateChange)||tempStateChange){
					if(isUndo){
//						System.out.println("chexiao"+undoTimes);
						for(int i=0;i<undoTimes;i++){
//							System.out.println("size"+stateNode.size());
//							System.out.println(stateNode.size()-1);
						stateNode.remove(stateNode.size()-1);
						}
						isUndo=false;
					}
					else{
						stateNode.add(textlist.get(textlist.size()-1));
				      }
					tempStateChange=false;
					isStateChange=true;
					stateNum++;
				}
				
				   undoTimes=0;
			       isInputCmd=true;
			       isDeleteCmd=false;
			       isUndoChange=true;
			       isRedoChange=false;
			       textlist.add(codeArea.getText());
			}
//			for (int i = 0; i < stateNode.size(); i++) {
//				System.out.print(stateNode.get(i)+"|");
//			}
//			System.out.println();
		}
		@Override
		public void keyTyped(KeyEvent e) {
		}


	}
	class cursorListener implements CaretListener{

		@Override
		public void caretUpdate(CaretEvent e) {
			 temp=e.getMark();
		}
		
	}
}
