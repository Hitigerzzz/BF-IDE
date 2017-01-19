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
 * 创建新用户窗口
 */
public class NewUserFrame extends JDialog {
	private MainFrame mainFrame;
	private JLabel userNewLabel;
	private JLabel passwordNewLabel;
	private JTextField userNewField;
	private JPasswordField passwordNewField;
	private JButton confirmButton;
	private JButton cancelButton;
     public NewUserFrame(MainFrame mainFrame){
    	 super(mainFrame,"new user",true);
    	 userNewLabel=new JLabel("新用户:");
 		 passwordNewLabel=new JLabel("新密码:");
 		 userNewField=new JTextField(15);
 		 passwordNewField=new JPasswordField(15);
 		 confirmButton=new JButton("confirm");
		 cancelButton=new JButton("cancel");
		 this.mainFrame=mainFrame;
		 this.setBounds(mainFrame.getX()+160,mainFrame.getY()+260,300,230);
		
		 this.setResizable(false);
		 this.getContentPane().setLayout(null);
		 createNewUserButton();
		 createNewUserTextField();
		 createNewLabel();
     }
     /**
      * TODO 新账号窗口的按钮
      */
     private void createNewUserButton(){
     	 confirmButton.setBounds(60, 150, 80, 30);
     	 confirmButton.addMouseListener(new ButtonListener());
     	 this.getContentPane().add(confirmButton);
     	 cancelButton.setBounds(160, 150, 80, 30);
     	 cancelButton.addMouseListener(new ButtonListener());
     	 this.getContentPane().add(cancelButton);
     }
     /**
      * 新账号窗口文本域
      */
     private void createNewUserTextField(){
     	userNewField.setBounds(90, 30,180,30);
     	this.getContentPane().add(userNewField);
     	passwordNewField.setBounds(90, 90,180,30);
     	this.getContentPane().add(passwordNewField);
     }
     private void createNewLabel(){
     	userNewLabel.setBounds(30,30,50,30);
     	this.getContentPane().add(userNewLabel);
     	passwordNewLabel.setBounds(30,90,50,30);
     	this.getContentPane().add(passwordNewLabel);
     }
     class ButtonListener extends MouseAdapter{
    	 @Override
    	 public void mouseClicked(MouseEvent e) {
     		if(e.getSource()==confirmButton){
     			String newUserName=userNewField.getText();
     			String newPassword=String.valueOf(passwordNewField.getPassword());
     			
     			 try {
     				 if(RemoteHelper.getInstance().getUserService().createNewUser(newUserName,newPassword)){
     					 NewUserFrame.this.dispose();
     					 JOptionPane.showMessageDialog(null,"register sucessfully!","Sucessful!",JOptionPane.INFORMATION_MESSAGE);
     				 }
     			 else{
     				     NewUserFrame.this.dispose();
     				    JOptionPane.showMessageDialog(null,"Failed to register!","Failed!",JOptionPane.ERROR_MESSAGE);
     				 }
     				 userNewField.setText(null);
     				 passwordNewField.setText(null);
     			} catch (RemoteException e1) {
     				e1.printStackTrace();
     			}
     			new LoginFrame(mainFrame).setVisible(true);
     		}
     		if(e.getSource()==cancelButton){
     			NewUserFrame.this.dispose();
     		}
    	 }
     }
}
