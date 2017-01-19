package serviceImpl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.rmi.RemoteException;
import java.text.SimpleDateFormat;
import java.util.Date;

import service.IOService;

public class IOServiceImpl implements IOService{
	
	@Override
	public boolean writeFile(String file, String userId,String password, String fileName,String version) {
		File f = new File("User/"+userId + "_" + password+"/"+fileName+"/"+version);
		
		try {
			FileWriter fw = new FileWriter(f,false);
			fw.write(file);
			fw.flush();
			fw.close();
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public String readFile(String userId,String password, String fileName,String version) {
		File f = new File("User/"+userId + "_" + password+"/"+fileName+"/"+version);
		StringBuilder fileContent=new StringBuilder();
		try {
			BufferedReader reader = new BufferedReader(new FileReader(f));
			String line=null;
			while((line=reader.readLine())!=null){
				fileContent.append(line);
			}
			reader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return fileContent.toString();
	}

	@Override
	public String[] readFileList(String userId,String password) {
		String path="User/"+userId+"_"+password;
		File file=new File(path);
		String[] oldFileName = file.list();
		if(oldFileName!=null){
			return oldFileName;
		}else{
			return null;
		}
	}

	@Override
	public boolean createFile(String fileName, String userId, String password) throws RemoteException {
		//����Ƿ������˵�½
		if(userId==""||password==""){
			return false;
		}
		//����Ƿ����ļ�����
		String path="User/"+userId+"_"+password;
		File file=new File(path);
		String[] oldFileName = file.list();
		if(oldFileName!=null){
		for (int i = 0; i < oldFileName.length; i++) {
			if( oldFileName[i].equals(fileName)){
				return false;
			}
		}
	}
		//�������ļ�
		File newFileFolder=new File("User/"+userId+"_"+password+"/"+fileName);
		newFileFolder.mkdir();
 		Date date= new Date();//����һ��ʱ����󣬻�ȡ����ǰ��ʱ��
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");//����ʱ����ʾ��ʽ
		String str = sdf.format(date);//����ǰʱ���ʽ��Ϊ��Ҫ������
		String filePath="User/"+userId+"_"+password+"/"+fileName+"/"+str;
		File newFile=new File(filePath);
		try {
			
			FileWriter fw = new FileWriter(newFile, false);
			fw.write("");
			fw.flush();
			fw.close();
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public String[] readFileVersionList(String userId, String password, String fileName) throws RemoteException {
		String path="User/"+userId+"_"+password+"/"+fileName;
		File file=new File(path);
		String[] fileVersionName = file.list();
		if(fileVersionName!=null){
			return fileVersionName;
		}else{
			return null;  
		}
	}
	
}
