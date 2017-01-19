//请不要修改本文件名
package serviceImpl;

import java.rmi.RemoteException;
import java.util.Stack;

import service.ExecuteService;

public class ExecuteServiceImpl implements ExecuteService {

	/**
	 * 请实现该方法
	 */
	@Override
	public String execute(String code, String param) throws RemoteException {
		int inputPos = 0;
		int commandPos = 0;
		int pointerPos = 0;
		char[] result=new char[code.length()];
	    String output = "";
	    //
	    for (int i = 0; i < result.length; i++) {
			result[i]=0;
		}
	    //匹配括号
	    Stack<Integer> stackLeft = new Stack<Integer>();
	    Stack<Integer> stackRight = new Stack<Integer>();
	    for(int i = 0; i < code.length(); i++) {
	    	stackRight.push(-1);
	    	}
	    for(int i = 0; i < code.length(); i++) {
	        if(code.charAt(i)== '[') {
	        	stackLeft.push(i);
	        } else if(code.charAt(i)== ']') {
	        	stackRight.set(i, stackLeft.pop()-1);
	        	stackRight.set(stackRight.get(i)+1,i);
	        }
	    }
	    
	    while(commandPos< code.length()) {
			switch (code.charAt(commandPos)) {
			
			case '>': {
				pointerPos++;
			}break;
	        case '<': {
	        	pointerPos--;
	        }break;
	        case '+': {
	        	result[pointerPos]+=1;
	        }break;
	        case '-': {
	        	result[pointerPos]-=1;
	        }break;
	        case '.': {
	        	output+=result[pointerPos];
	        }break;
	        case ',': {
	        	result[pointerPos]=param.charAt(inputPos);
	        	inputPos++;
	        }break;
	        case '[': {
	        	if(result[pointerPos]==0){
	        		commandPos = stackRight.get(commandPos);
	        	}
	        }break;
	        case ']': {
	        	if(result[pointerPos]!=0){
	        		commandPos = stackRight.get(commandPos);
	        	}
	        }break;
			default:
				break;
			}
			 commandPos++;
		}
		return output;
	}

}