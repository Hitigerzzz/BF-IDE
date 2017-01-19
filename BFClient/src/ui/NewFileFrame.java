package ui;


import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.rmi.RemoteException;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import rmi.RemoteHelper;
/**
 * 创建新文件窗口
 */
public class NewFileFrame extends JDialog {
	 private MainFrame mainFrame;
	 private JLabel promptLabel;
	 private JTextField newFileNameField;
	 private JButton confirmButton;
	 private JButton cancelButton;
	 private String userId;
	 private String password;
	 public static String fileName;
     public NewFileFrame(MainFrame mainFrame,String username,String password){
    	 super(mainFrame,"new File",true);
    	 this.mainFrame=mainFrame;
    	 this.userId=username;
    	 this.password=password;
		 this.setBounds(mainFrame.getX()+160,mainFrame.getY()+260,300,230);
		 this.setResizable(false);
		 this.getContentPane().setLayout(null);
		 
		 promptLabel=new JLabel("Input the name of new file:");
		 promptLabel.setBounds(30,30,270,30);
		 Font font=new Font("微软雅黑",Font.BOLD,15);
		 promptLabel.setFont(font);
		 
		 newFileNameField=new JTextField(15);
		 newFileNameField.setBounds(30,90,240,30);
		 
		 confirmButton=new JButton("confirm");
		 confirmButton.setBounds(50,140, 80, 30);
		 confirmButton.addMouseListener(new ButtonListener());
		 cancelButton=new JButton("cancel");
		 cancelButton.setBounds(170,140, 80, 30);
		 cancelButton.addMouseListener(new ButtonListener());
		 this.getContentPane().add(newFileNameField);
	     this.getContentPane().add(promptLabel);
	     this.getContentPane().add(confirmButton);
	     this.getContentPane().add(cancelButton);
     }
     class ButtonListener extends MouseAdapter{
    	 @Override
    	 public void mouseClicked(MouseEvent e){
    		 if(e.getSource()==confirmButton){
    			 fileName=newFileNameField.getText();
    			 try {
					if(RemoteHelper.getInstance().getIOService().createFile(fileName,userId,password)){
						NewFileFrame.this.dispose();
						JOptionPane.showMessageDialog(null,"Create a file sucessfully!","Sucessful!",JOptionPane.INFORMATION_MESSAGE);
						mainFrame.initOpen(userId, password);
					}else{
						NewFileFrame.this.dispose();
						JOptionPane.showMessageDialog(null,"The file name has been used!","Failed!",JOptionPane.WARNING_MESSAGE);
					}
				} catch (RemoteException e1) {
					e1.printStackTrace();
				}
    			 newFileNameField.setText(null);
    		 }
    		 if(e.getSource()==cancelButton){
    			 NewFileFrame.this.dispose();
    		 }
    	 }
     }
}
