package ui;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.rmi.RemoteException;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import rmi.RemoteHelper;
/**
 * 登录窗口
 **/
public class LoginFrame extends JDialog {
	private static final long serialVersionUID = 1L;

	public JButton loginButton;
	private JButton loginB;
	private JButton cancelB;
	private JButton newUserB;
	
	private JLabel userLabel;
	private JLabel passwordLabel;
	
	private JTextField userField;
	private JPasswordField passwordField;
	
	private MainFrame mainFrame;
	public String  username;
	public String  password;
	public LoginFrame(MainFrame mainFrame){
		super(mainFrame,"Login",true);
		loginB=new JButton("login");
    	cancelB=new JButton("cancel");
    	newUserB=new JButton("new");
    	userLabel=new JLabel("用户:");
    	passwordLabel=new JLabel("密码:");
    	userField=new JTextField(15);
    	passwordField=new JPasswordField(15);
    	
		this.mainFrame=mainFrame;
		this.setBounds(mainFrame.getX()+160,mainFrame.getY()+260,300,230);
		this.setResizable(false);
		this.getContentPane().setLayout(null);
		createLabel();
		createTextField();
		createFunButton();
	}
    /**
     * 标签
     */
    private void createLabel(){
    	userLabel.setBounds(30,30,50,30);
    	this.getContentPane().add(userLabel);
    	passwordLabel.setBounds(30,90,50,30);
    	this.getContentPane().add(passwordLabel);
    }
    /**
     * 文本域
     */
    private void createTextField(){
    	userField.setBounds(90, 30,180,30);
    	this.getContentPane().add(userField);
    	passwordField.setBounds(90, 90,180,30);
    	this.getContentPane().add(passwordField);
    }
    /**
     *  登录，取消，新账号按钮
     */
    private void createFunButton(){
    	loginB.setBounds(15, 150, 80, 30);
    	loginB.addMouseListener(new ButtonListener());
    	this.getContentPane().add(loginB);
    	cancelB.setBounds(105, 150, 80, 30);
    	cancelB.addMouseListener(new ButtonListener());
    	this.getContentPane().add(cancelB);
    	newUserB.setBounds(195, 150, 80, 30);
    	newUserB.addMouseListener(new ButtonListener());
    	this.getContentPane().add(newUserB);
    }
 
    
    class ButtonListener extends MouseAdapter{
    	@Override
    	public void mouseClicked(MouseEvent e) {
    		if(e.getSource()==loginB){
    			 username=userField.getText();
    			 password=String.valueOf(passwordField.getPassword());
    			 try {
    				 if(RemoteHelper.getInstance().getUserService().login(username, password)){
    					 LoginFrame.this.dispose();
    					 JOptionPane.showMessageDialog(null,"Login sucessfully!","Success!",JOptionPane.INFORMATION_MESSAGE);
    					 mainFrame.setAccountMenu(username);
    					 mainFrame.repaint();
    				 }else{
    					 JOptionPane.showMessageDialog(null,"Failed to login!","Failed!",JOptionPane.ERROR_MESSAGE);
    				 }
    				 userField.setText(null);
    				 passwordField.setText(null);
    				 
    				 //调用MainFrame中的initOpen方法，目的是获得文件目录
    				 mainFrame.initOpen(username,password);
    			} catch (RemoteException e1) {
    				e1.printStackTrace();
    			}
    		}
    		if(e.getSource()==cancelB){
    			LoginFrame.this.dispose();
    		}
    		if(e.getSource()==newUserB){
    			LoginFrame.this.dispose();
    			new NewUserFrame(mainFrame).setVisible(true);
    		}
    
    	}

    }
	

}
