package runner;

import java.io.File;
import java.util.ArrayList;

import rmi.RemoteHelper;

public class ServerRunner {
	//用来存储用户信息，访问更快
	public static ArrayList<ArrayList<String>> userList=new ArrayList<ArrayList<String>>();
	public static ArrayList<String> userInfo;
	
	public ServerRunner() {
		new RemoteHelper();
	}
	
	public static void main(String[] args) {
		new ServerRunner();
		readUserInfo();
		System.out.println("linkedss");
	}
	/**
	 * 读取服务器用户的信息，并用List存储
	 */
	public static void readUserInfo(){
		String path="User";
		File file=new File(path);
		String[] userNameAndPassword = file.list();
		for (int i = 0; i < userNameAndPassword.length; i++) {
			userInfo=new ArrayList<String>();
			//将用户名和密码分开
			String[] splitNameAndPassword=userNameAndPassword[i].split("_");
			//0 存用户名
			userInfo.add(splitNameAndPassword[0]);
			//1 存密码
			userInfo.add(splitNameAndPassword[1]);
			userList.add(userInfo);
			splitNameAndPassword=null;
		}
	}
}
