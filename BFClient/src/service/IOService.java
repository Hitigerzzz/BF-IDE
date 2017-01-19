//服务器IOService的Stub，内容相同
package service;

import java.rmi.Remote;
import java.rmi.RemoteException;
public interface IOService extends Remote{
	public boolean writeFile(String file, String userId,String password, String fileName,String version)throws RemoteException;
	
	public String readFile(String userId, String password,String fileName,String version)throws RemoteException;
	
	public String[] readFileList(String userId,String password)throws RemoteException;
	
	public String[] readFileVersionList(String userId,String password,String fileName)throws RemoteException;
	
	public boolean createFile(String fileName,String userId,String password)throws RemoteException;
}
