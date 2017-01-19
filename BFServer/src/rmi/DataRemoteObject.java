package rmi;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import service.ExecuteService;
import service.IOService;
import service.UserService;
import serviceImpl.ExecuteServiceImpl;
import serviceImpl.IOServiceImpl;
import serviceImpl.UserServiceImpl;

public class DataRemoteObject extends UnicastRemoteObject implements IOService, UserService,ExecuteService{
	/**
	 * 
	 */
	private static final long serialVersionUID = 4029039744279087114L;
	private IOService iOService;
	private UserService userService;
	private ExecuteService executeService;
	protected DataRemoteObject() throws RemoteException {
		iOService = new IOServiceImpl();
		userService = new UserServiceImpl();
		executeService=new ExecuteServiceImpl();
	}

	@Override
	public boolean writeFile(String file, String userId,String password, String fileName,String version) throws RemoteException{
		// TODO Auto-generated method stub
		return iOService.writeFile(file, userId,password, fileName,version);
	}

	@Override
	public String readFile(String userId,String password, String fileName,String version) throws RemoteException{
		// TODO Auto-generated method stub
		return iOService.readFile(userId, password,fileName,version);
	}

	@Override
	public String[] readFileList(String userId,String password) throws RemoteException{
		// TODO Auto-generated method stub
		return iOService.readFileList(userId,password);
	}

	@Override
	public boolean login(String username, String password) throws RemoteException {
		// TODO Auto-generated method stub
		return userService.login(username, password);
	}

	@Override
	public boolean logout(String username) throws RemoteException {
		// TODO Auto-generated method stub
		return userService.logout(username);
	}

	@Override
	public boolean createNewUser(String Newusername, String Newpassword) throws RemoteException {
		// TODO Auto-generated method stub
		return userService.createNewUser(Newusername, Newpassword);
	}

	@Override
	public boolean createFile(String fileName, String userId, String password) throws RemoteException {
		// TODO Auto-generated method stub
		return iOService.createFile(fileName,userId,password);
	}

	@Override
	public String[] readFileVersionList(String userId, String password, String fileName) throws RemoteException {
		// TODO Auto-generated method stub
		return iOService.readFileVersionList(userId, password, fileName);
	}

	@Override
	public String execute(String code, String param) throws RemoteException {
		// TODO Auto-generated method stub
		return executeService.execute(code, param);
	}

}
