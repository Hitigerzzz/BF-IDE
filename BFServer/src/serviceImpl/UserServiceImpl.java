package serviceImpl;

import java.io.File;
import java.rmi.RemoteException;
import java.util.ArrayList;

import runner.ServerRunner;
import service.UserService;

public class UserServiceImpl implements UserService{
	ArrayList<String> loginList=new ArrayList<String>();
	@Override
	public boolean login(String username, String password) throws RemoteException {
		String path="User";
		File file=new File(path);
		String[] fileName = file.list();
		for (int i = 0; i < fileName.length; i++) {
			if(fileName[i].equals(username+ "_" + password)){
				for (int j = 0; j <loginList.size(); j++) {
					if (loginList.get(j).equals(username)) {
						return false;
					}
				}
				loginList.add(username);
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean logout(String username) throws RemoteException {
		return true;
	}

	@Override
	public boolean createNewUser(String Newusername, String Newpassword) throws RemoteException {
		for (int i = 0; i < ServerRunner.userList.size(); i++) {
			//检查新建账号是否已经存在 
			if(ServerRunner.userList.get(i).get(0).equals(Newusername)||Newusername.equals("")||Newpassword.equals(""))
			{
				return false;
			}
		}
		File newFile = new File("User\\"+Newusername+"_"+Newpassword);
		newFile.mkdir();
		return true;
	}

}
