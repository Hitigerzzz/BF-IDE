package runner;

import java.io.File;
import java.util.ArrayList;

import rmi.RemoteHelper;

public class ServerRunner {
	//�����洢�û���Ϣ�����ʸ���
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
	 * ��ȡ�������û�����Ϣ������List�洢
	 */
	public static void readUserInfo(){
		String path="User";
		File file=new File(path);
		String[] userNameAndPassword = file.list();
		for (int i = 0; i < userNameAndPassword.length; i++) {
			userInfo=new ArrayList<String>();
			//���û���������ֿ�
			String[] splitNameAndPassword=userNameAndPassword[i].split("_");
			//0 ���û���
			userInfo.add(splitNameAndPassword[0]);
			//1 ������
			userInfo.add(splitNameAndPassword[1]);
			userList.add(userInfo);
			splitNameAndPassword=null;
		}
	}
}
