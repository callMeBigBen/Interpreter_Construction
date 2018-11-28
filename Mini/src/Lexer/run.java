package syntax;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
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
	//static HashMap<String,Integer> tokens = new HashMap<String,Integer>();
	static ArrayList<Token> tokens = new ArrayList<Token>();
	//LineNum
	static int lineNum = 0;
	//添加值
	public static void fill_list(){
		//character:-1
		//number:-2
		//
		reserved_word.add("boolean"); //1 
		reserved_word.add("char");	   //2
		reserved_word.add("int");//3
		reserved_word.add("double");//4
		reserved_word.add("string");//5
		reserved_word.add("true");//6
		reserved_word.add("false");//7
		reserved_word.add("if");//8
		reserved_word.add("else");//9
		reserved_word.add("while");//10
		reserved_word.add("for");//11
		reserved_word.add("break");//12
		reserved_word.add("continue");//13
		reserved_word.add("void");//14
		reserved_word.add("return");//15
		
		operator.add("(");//16
		operator.add(")");//17
		operator.add("{");//18
		operator.add("}");//19
		operator.add("[");//20
		operator.add("]");//21
		operator.add(";");//22
		operator.add(",");//23
		operator.add("=");//24
		operator.add(">");//25
		operator.add("<");//26
		operator.add("==");//27
		operator.add("<=");//28
		operator.add(">=");//29
		operator.add("!=");//30
		operator.add("&&");//31
		operator.add("||");//32
		operator.add("!");//33
		operator.add("+");//34
		operator.add("*");//35
		operator.add("/");//36
		operator.add("%");//37
		operator.add("\"");//38
		operator.add("\'");//39
		//minus"-"40
		//negative "-"41
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
		StringBuffer newCode = new StringBuffer();
		
		for(int i=0;i<len;i++){
			//跳过单行注释
			if(sourceCode.charAt(i)=='/'&&sourceCode.charAt(i+1)=='/'){
				i+=2;
				while(sourceCode.charAt(i)!='\n'){
					i++;
				}
				//lineNum++;
			}
			//跳过多行注释
			if((sourceCode.charAt(i)=='/'&&sourceCode.charAt(i+1)=='*')){
				i+=2;
				while(!(sourceCode.charAt(i)=='*'&&sourceCode.charAt(i+1)=='/')){
					if(sourceCode.charAt(i)=='\n'){
						lineNum++;
					}
					i++;//继续扫描
					if (i >= len)
					{
					System.out.println("注释出错，没有找到 */，程序结束！！！\n");
					System.exit(0);
					}
				}
				i+=2;
			}
			//正常收集
			if(sourceCode.charAt(i)!='\t' 
			   ){
				newCode.append(sourceCode.charAt(i));
			}
		}
		
		//源代码转换
		return newCode.toString();	
	}
	
	
	/*
	 * 词法分类函数：分出单个token，得到对应的类型并且保存
	 * 类型：1.保留字; 2.标识符; 3.数字;4.符号,5.字符串,6.字符
	 */
	/**
	 * @param sourceCode
	 * @param currPosition
	 * @return
	 */
	public static int scanner(String sourceCode,int currPosition){
		//int tag = -1;
		int movement = 0;//用于返回的移动量，即读取token之后要改变读取位置，功能类似C的指针
		int len = sourceCode.length();
		StringBuffer token = new StringBuffer();
		char ch = sourceCode.charAt(currPosition);
		
		//过滤空格
		while(ch==' '){
			if(currPosition<len-1){
				currPosition++;
				ch = sourceCode.charAt(currPosition);
				movement++;
			}
			else{
				movement++;
				break;
			}
			
		}
		
		//字母开头，开始记录token
		if(isLetter(ch)&&currPosition<len-1){
			token.append(sourceCode.charAt(currPosition));
			currPosition++;
			//后面是数字或者字母，继续记录token
			while((isLetter(sourceCode.charAt(currPosition))
					||isNumber(sourceCode.charAt(currPosition)))
					&&currPosition<len-1){
				token.append(sourceCode.charAt(currPosition));
				currPosition++;
			}
			//token.append('\0');
			String tmpstr=token.toString();		
			movement+=tmpstr.length();
			
			//查保留字表,修改字符码
			Token tokenObj = new Token();
			tokenObj.put(tmpstr,lineNum);
			tokenObj.confirmCode();
			tokens.add(tokenObj);
			
		}
		//数字开头
		else if(isNumber(ch)&&currPosition<len-1){
			token.append(sourceCode.charAt(currPosition));
			currPosition++;
			boolean doted =false;		
			while((currPosition<(len-1)&&isNumber(sourceCode.charAt(currPosition)))
					||sourceCode.charAt(currPosition)=='.'){
				if(isNumber(sourceCode.charAt(currPosition))){
					token.append(sourceCode.charAt(currPosition));
				}
				else if(!doted&&sourceCode.charAt(currPosition)=='.'){
					token.append(sourceCode.charAt(currPosition));
					doted=true;
				}
				else if(doted&&sourceCode.charAt(currPosition)=='.'){
					System.out.println("Line:"+lineNum+" Error, more than one dot. At "+(currPosition+1));
					System.exit(0);
				}
				currPosition++;
			}
			if(isLetter(sourceCode.charAt(currPosition))){
				System.out.println("Error:Line:"+lineNum+" Number can't be followed by a letter!");
				System.exit(0);
			}
			//token.append('\0');

			String tmpstr = token.toString();
			movement+=tmpstr.length();
			
			Token tokenObj = new Token();
			if(tokens.get(tokens.size()-1).code==41){
				tokens.get(tokens.size()-1).put("-"+tmpstr,lineNum);
				tokens.get(tokens.size()-1).confirmCode();
			}
			else{
				tokenObj.put(tmpstr,lineNum);
				tokenObj.confirmCode();
				tokens.add(tokenObj);
			}		
		}
		//单符号开头
		else if(ch=='+'||ch=='*'||ch=='/'
				||ch=='('||ch==')'||ch=='['||ch==']'||ch=='{'
				||ch=='}'||ch=='%'||ch==','||ch==';'){
			token.append(ch);		
			String tmpstr = token.toString();
			movement++;
			
			Token tokenObj = new Token();
			tokenObj.put(tmpstr,lineNum);
			tokenObj.confirmCode();
			tokens.add(tokenObj);
			
		}
		//可能的复合符号开头
		else if((ch=='!'||ch=='>'||ch=='<'||ch=='&'||ch=='|'||ch=='='||ch=='-')
				&&currPosition<len-1){
			if(ch=='!'&&sourceCode.charAt(currPosition+1)=='='){
				token.append("!=");
				String tmpstr = token.toString();
				movement +=2;
				
				Token tokenObj = new Token();
				tokenObj.put(tmpstr,lineNum);
				tokenObj.confirmCode();
				tokens.add(tokenObj);
			}
			else if(ch=='!'&&sourceCode.charAt(currPosition+1)!='='){
				token.append("!");
				String tmpstr = token.toString();
				movement++;
				
				Token tokenObj = new Token();
				tokenObj.put(tmpstr,lineNum);
				tokenObj.confirmCode();
				tokens.add(tokenObj);
			}
			//识别“-”为负号，但是先要防止tokens为空出错(因为要检测tokens中前一个元素，可能没有元素）
			else if(ch=='-'&&tokens.isEmpty()){
				System.out.println("Line:"+lineNum+"At"+currPosition+"  Syntax Error: '-' can not be the head of the line!");
			}
			//识别“-”为减号
			else if(ch=='-'&&(tokens.get(tokens.size()-1).code==0)){
				token.append(ch);		
				String tmpstr = token.toString();
				movement++;
				
				Token tokenObj = new Token();
				tokenObj.put(tmpstr,lineNum);
				tokenObj.confirmCode();
				tokens.add(tokenObj);
			}
			//识别“-”为负号，前面是左括号或者比较、赋值符号
			else if(ch=='-'&&(
					tokens.get(tokens.size()-1).code==16
					||tokens.get(tokens.size()-1).code==24
					||tokens.get(tokens.size()-1).code==25
					||tokens.get(tokens.size()-1).code==26
					||tokens.get(tokens.size()-1).code==27
					||tokens.get(tokens.size()-1).code==28
					||tokens.get(tokens.size()-1).code==29
					||tokens.get(tokens.size()-1).code==30)){
				token.append(sourceCode.charAt(currPosition));
				currPosition++;
				//识别为负号之后，后面如果不是标识符或数字或左括号，报错
				if((currPosition<len-1)
						&&!isLetter(sourceCode.charAt(currPosition))
						&&!isNumber(sourceCode.charAt(currPosition))){
					System.out.println("Line:"+lineNum+" At "+currPosition+" Syntax Error:Unknown '-'");
					System.exit(0);
				}
				
				String tmpstr = token.toString();
				movement+=tmpstr.length();

				Token tokenObj = new Token();
				tokenObj.put(tmpstr+"_",lineNum);
				tokenObj.confirmCode();
				tokens.add(tokenObj);
			}
			//识别未知负号
			else if(ch=='-'){
				System.out.println("Line:"+lineNum+" At "+currPosition+" unknown '-'");
				System.exit(0);
			}
			else if(ch=='='&&sourceCode.charAt(currPosition+1)!='='){
				token.append("=");
				String tmpstr = token.toString();
				movement++;
				
				Token tokenObj = new Token();
				tokenObj.put(tmpstr,lineNum);
				tokenObj.confirmCode();
				tokens.add(tokenObj);
			}
			else if(ch=='='&&sourceCode.charAt(currPosition+1)=='='){
				token.append("==");
				String tmpstr = token.toString();
				movement +=2;
				
				Token tokenObj = new Token();
				tokenObj.put(tmpstr,lineNum);
				tokenObj.confirmCode();
				tokens.add(tokenObj);
			}
			else if(ch=='>'&&sourceCode.charAt(currPosition+1)=='='){
				token.append(">=");
				String tmpstr = token.toString();
				movement+=2;
				
				Token tokenObj = new Token();
				tokenObj.put(tmpstr,lineNum);
				tokenObj.confirmCode();
				tokens.add(tokenObj);
			}else if(ch=='>'&&sourceCode.charAt(currPosition+1)!='='){
				token.append(">");
				String tmpstr = token.toString();
				movement++;
				
				Token tokenObj = new Token();
				tokenObj.put(tmpstr,lineNum);
				tokenObj.confirmCode();
				tokens.add(tokenObj);
			}else if(ch=='<'&&sourceCode.charAt(currPosition+1)=='='){
				token.append("<=");
				String tmpstr = token.toString();
				movement+=2;
				
				Token tokenObj = new Token();
				tokenObj.put(tmpstr,lineNum);
				tokenObj.confirmCode();
				tokens.add(tokenObj);
			}else if(ch=='<'&&sourceCode.charAt(currPosition+1)!='='){
				token.append("<");
				String tmpstr = token.toString();
				movement+=2;
				
				Token tokenObj = new Token();
				tokenObj.put(tmpstr,lineNum);
				tokenObj.confirmCode();
				tokens.add(tokenObj);
			}else if(ch=='&'&&sourceCode.charAt(currPosition+1)=='&'){
				token.append("&&");
				String tmpstr = token.toString();
				movement+=2;
				
				Token tokenObj = new Token();
				tokenObj.put(tmpstr,lineNum);
				tokenObj.confirmCode();
				tokens.add(tokenObj);
			}else if(ch=='|'&&sourceCode.charAt(currPosition+1)=='|'){
				token.append("||");
				String tmpstr = token.toString();
				movement+=tmpstr.length();
				
				Token tokenObj = new Token();
				tokenObj.put(tmpstr,lineNum);
				tokenObj.confirmCode();
				tokens.add(tokenObj);
			}
		}
		//双引号
		else if(ch=='\"'&&currPosition<len-1){
			token.append(sourceCode.charAt(currPosition));
			currPosition++;
		    while(sourceCode.charAt(currPosition)!='\"'){
		    	token.append(sourceCode.charAt(currPosition));
		    	currPosition++;
		    	if(currPosition>=len-1){
		    		System.out.println("Error:Line: "+lineNum+" There is only one '\"'");
		    		System.exit(0);
		    	}
		    }
		    token.append(sourceCode.charAt(currPosition));
		    String tmpstr = token.toString();
		    movement+=tmpstr.length();
		    
		    Token tokenObj = new Token();
			tokenObj.put(tmpstr,lineNum);
			tokenObj.confirmCode();
			tokens.add(tokenObj);		
		}
		//单引号
		else if(ch=='\''&&currPosition<len-1){
			token.append(sourceCode.charAt(currPosition));
			currPosition++;
			int charLength = 1;
			while(sourceCode.charAt(currPosition)!='\''){
				if(charLength>1){
				    System.out.println("Line:"+lineNum+" 字符超长，语法错误，位置："+currPosition+":"+sourceCode.charAt(currPosition));
				    //System.exit(0);
				}
				token.append(sourceCode.charAt(currPosition));
				charLength++;
				currPosition++;
				if(currPosition>=len-1){
				    System.out.println("Error:Line: "+lineNum+" There is only one '\''");
				    System.exit(0);
				}
			}
			token.append(sourceCode.charAt(currPosition));
			String tmpstr = token.toString();
			movement+=tmpstr.length();
				    
			Token tokenObj = new Token();
			tokenObj.put(tmpstr,lineNum);
			tokenObj.confirmCode();
			tokens.add(tokenObj);			
		}
		
		else if(ch==' '){
			//do nothing
		}
		else{
			//不能识别的未知符号，如中文引号
			System.out.println("Line:"+lineNum+" Error, unknown character. At "+(currPosition+1));
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
		try {
			//读入文本
			//预处理：消除注释
			String inputStr = preProcess(readToString("D:/code.txt"));
			File file = new File("D:/code2.txt");
			file.createNewFile();
			FileWriter writer = new FileWriter(file);
			writer.write(inputStr);
			writer.flush();
			writer.close();	
			BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(new File("D:/code2.txt"))));
			
			//扫描
			String newSrc = reader.readLine();
			
			lineNum++;
			while(newSrc!=null){
				newSrc+=" ";//在每行末尾加一个无意义空格
				int i = 0;
				int len = newSrc.length();
				while(i<len){
					i+=scanner(newSrc,i);
				}
				newSrc = reader.readLine();
				lineNum++;
			}
			reader.close();
			
			for(Token tk:tokens){
				System.out.println("line:"+tk.line+",  "+tk.content+",  Code:"+tk.code);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}

}
