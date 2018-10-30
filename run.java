package syntax;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;

public class run {
	
	//定义保留字
	static ArrayList<String> reserved_word = new ArrayList<String>();
	//定义运算符
	static ArrayList<String> operator = new ArrayList<String>();
	//定义标识符表
	static ArrayList<String> identifier = new ArrayList<String>();
	//定义token映射表
	static HashMap<String,Integer> tokens = new HashMap<String,Integer>();
	//添加值
	public static void fill_list(){
		reserved_word.add("boolean");  
		reserved_word.add("char");	   
		reserved_word.add("int");
		reserved_word.add("double");
		reserved_word.add("string");
		reserved_word.add("true");
		reserved_word.add("false");
		reserved_word.add("if");
		reserved_word.add("else");
		reserved_word.add("while");
		reserved_word.add("for");
		reserved_word.add("break");
		reserved_word.add("continue");
		reserved_word.add("void");
		reserved_word.add("return");
		
		operator.add("(");
		operator.add(")");
		operator.add("{");
		operator.add("}");
		operator.add("[");
		operator.add("]");
		operator.add(";");
		operator.add(",");
		operator.add("=");
		operator.add(">");
		operator.add("<");
		operator.add("==");
		operator.add("<=");
		operator.add(">=");
		operator.add("!=");
		operator.add("&&");
		operator.add("||");
		operator.add("!");
		operator.add("+");
		operator.add("*");
		operator.add("/");
		operator.add("%");
	}
	
	//查找是否为保留字,并且返回对应的类别码
	public static boolean isReserved(String str){
		for(String rsvdwd:reserved_word){
			if(rsvdwd.equals(str)){
				return true;	//是保留字则返回T
			}	
		}
		return false;//不是保留字，返回F
	}
	
	//判断是否为字母
	public static boolean isLetter(char letter){
		if(letter>='a'&&letter<='z' ||letter>='A'&&letter<='Z' )
			return true;
		else
			return false;
	}
	
	//判断是否为数字
	public static boolean isNumber(char letter){
		if(letter>='0'&&letter<='9')
			return true;
		else
			return false;
	}
	
	//预处理
	public static String preProcess(String sourceCode){
		int len = sourceCode.length();
		int count = 0;
		StringBuffer newCode = new StringBuffer();
		
		for(int i=0;i<len;i++){
			//跳过单行注释
			if(sourceCode.charAt(i)=='/'&&sourceCode.charAt(i+1)=='/'){
				i+=2;
				while(sourceCode.charAt(i)!='\n'){
					i++;
				}
			}
			//跳过多行注释
			if(sourceCode.charAt(i)=='/'&&sourceCode.charAt(i+1)=='*'){
				i+=2;
				while(sourceCode.charAt(i)=='*'&&sourceCode.charAt(i+1)=='/'){
					i++;//继续扫描
					if (sourceCode.charAt(i) == '$')
					{
					System.out.println("注释出错，没有找到 */，程序结束！！！\n");
					System.exit(0);
					}
				}
				i+=2;
			}
			//正常收集
			if(sourceCode.charAt(i)!='\n' 
			   &&sourceCode.charAt(i)!='\t' 
			   &&sourceCode.charAt(i)!='\r'){
				newCode.append(sourceCode.charAt(i));
			}
		}
		
		//源代码转换
		return newCode.toString();	
	}
	
	
	/*
	 * 词法分类函数：分出单个token，得到对应的类型并且保存
	 * 类型：1.保留字; 2.标识符; 3.数字;4.符号
	 */
	public static int scanner(String sourceCode,int currPosition){
		int tag = -1;
		int movement = 0;//用于返回的移动量，即读取token之后要改变读取位置，功能类似C的指针
		int len = sourceCode.length();
		StringBuffer token = new StringBuffer();
		char ch = sourceCode.charAt(currPosition);
		
		//过滤空格
		while(ch==' '){
			currPosition++;
			ch = sourceCode.charAt(currPosition);
			movement++;
		}
		
		//字母开头，开始记录token
		if(isLetter(ch)){
			token.append(sourceCode.charAt(currPosition));
			currPosition++;
			//后面是数字或者字母，继续记录token
			while(isLetter(sourceCode.charAt(currPosition))
					||isNumber(sourceCode.charAt(currPosition))){
				token.append(sourceCode.charAt(currPosition));
				currPosition++;
			}
			//token.append('\0');
			String tmpstr=token.toString();
			movement+=tmpstr.length();
			
			//查保留字表，若是，修改类型标签为1,否则为2
			if(isReserved(tmpstr)){
				tag = 1;
				tokens.put(tmpstr, tag);
			}
			else{
				tag = 2;
				tokens.put(tmpstr, tag);
			}
		}
		//数字开头
		else if(isNumber(ch)){
			token.append(sourceCode.charAt(currPosition));
			while(isNumber(sourceCode.charAt(currPosition+1))){
				token.append(sourceCode.charAt(currPosition));
			}
			//token.append('\0');
			String tmpstr = token.toString();
			movement+=tmpstr.length();
			tag = 3;
			tokens.put(tmpstr, tag);
		}
		//单符号开头
		else if(ch=='+'||ch=='-'||ch=='*'||ch=='/'
				||ch=='('||ch==')'||ch=='['||ch==']'||ch=='{'
				||ch=='}'||ch=='%'||ch=='='||ch==','||ch==';'){
			tag = 4;
			token.append(ch);		
			String tmpstr = token.toString();
			movement++;
			tokens.put(tmpstr,tag);
		}
		//可能的复合符号开头
		else if(ch=='!'||ch=='>'||ch=='<'||ch=='&'||ch=='|'){
			tag=4;
			if(ch=='!'&&sourceCode.charAt(currPosition+1)=='='){
				token.append("!=");
				String tmpstr = token.toString();
				movement +=2;
				tokens.put(tmpstr, tag);
			}
			else if(ch=='!'&&sourceCode.charAt(currPosition+1)!='='){
				token.append("!");
				String tmpstr = token.toString();
				movement++;
				tokens.put(tmpstr, tag);
			}
			else if(ch=='>'&&sourceCode.charAt(currPosition+1)=='='){
				token.append(">=");
				String tmpstr = token.toString();
				movement+=2;
				tokens.put(tmpstr, tag);
			}else if(ch=='>'&&sourceCode.charAt(currPosition+1)!='='){
				token.append(">");
				String tmpstr = token.toString();
				movement++;
				tokens.put(tmpstr, tag);
			}else if(ch=='<'&&sourceCode.charAt(currPosition+1)=='='){
				token.append("<=");
				String tmpstr = token.toString();
				movement+=2;
				tokens.put(tmpstr, tag);
			}else if(ch=='<'&&sourceCode.charAt(currPosition+1)!='='){
				token.append("<");
				String tmpstr = token.toString();
				movement+=2;
				tokens.put(tmpstr, tag);
			}else if(ch=='&'&&sourceCode.charAt(currPosition+1)=='&'){
				token.append("&&");
				String tmpstr = token.toString();
				movement+=2;
				tokens.put(tmpstr, tag);
			}else if(ch=='|'&&sourceCode.charAt(currPosition+1)=='|'){
				token.append("||");
				String tmpstr = token.toString();
				movement+=2;
				tokens.put(tmpstr, tag);
			}
		}
		else if(ch=='$'){
			tag=0;//结束符
		}
		else{
			System.out.println("Error letter,can't distinguish.");
			System.exit(0);
		}
		
		return movement;
	}
	
	//读取文件函数
	public static String readToString(String fileName) {  
        String encoding = "UTF-8";  
        File file = new File(fileName);  
        Long filelength = file.length();  
        byte[] filecontent = new byte[filelength.intValue()];  
        try {  
            FileInputStream in = new FileInputStream(file);  
            in.read(filecontent);  
            in.close();  
        } catch (FileNotFoundException e) {  
            e.printStackTrace();  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
        try {  
            return new String(filecontent, encoding);  
        } catch (UnsupportedEncodingException e) {  
            System.err.println("The OS does not support " + encoding);  
            e.printStackTrace();  
            return null;  
        }  
    }
	

	public static void main(String[] args) {
		fill_list();
		//System.out.println(operator.get(0));
		//读取代码
		String src = readToString("D:/code.txt");
		//预处理
		String newSrc = preProcess(src);
		int i = 0;
		int len = newSrc.length();
		while(i<len){
			i+=scanner(newSrc,i);
		}
		
		for(String key:tokens.keySet()){
			System.out.println(key+",Type:"+tokens.get(key));
		}
		
	}

}
