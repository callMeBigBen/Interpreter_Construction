package Lexer;
import Parser.*;
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
	
	public run() {
		
	}
	
	//���屣����
	static ArrayList<String> reserved_word = new ArrayList<String>();
	//���������
	static ArrayList<String> operator = new ArrayList<String>();
	//�����ʶ����
	static ArrayList<String> identifier = new ArrayList<String>();
	//����tokenӳ���
	//static HashMap<String,Integer> tokens = new HashMap<String,Integer>();
	static ArrayList<Token> tokens = new ArrayList<Token>();
	//LineNum
	static int lineNum = 0;
	//���
	public static boolean isError=false;
	public static String error = "";
	
	public ArrayList<Token> getTokens(){
		return tokens;
	}
	public static void fill_list(){
		//character:-1
		//double number:-2
		//integer number:-3
		//char:-4
		//string:-5
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
	
	
	//查找保留字
	public static boolean isReserved(String str){
		for(String rsvdwd:reserved_word){
			if(rsvdwd.equals(str)){
				return true;	//�Ǳ������򷵻�T
			}	
		}
		return false;//���Ǳ����֣�����F
	}
	
	//识别字母
	public static boolean isLetter(char letter){
		if(letter>='a'&&letter<='z' ||letter>='A'&&letter<='Z' )
			return true;
		else
			return false;
	}
	
	//识别数字
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
			//消除单行注释
			if(sourceCode.charAt(i)=='/'&&sourceCode.charAt(i+1)=='/'){
				i+=2;
				while(sourceCode.charAt(i)!='\n'){
					i++;
				}
				//lineNum++;
			}
			//消除多行注释
			if((sourceCode.charAt(i)=='/'&&sourceCode.charAt(i+1)=='*')){
				i+=2;
				while(!(sourceCode.charAt(i)=='*'&&sourceCode.charAt(i+1)=='/')){
					if(sourceCode.charAt(i)=='\n'){
						lineNum++;
					}
					i++;
					if (i >= len)
					{
					error+=("缺少多行注释结尾符号\n");
					isError = true;
					}
				}
				i+=2;
			}
			//识别其他符号
			if(sourceCode.charAt(i)!='\t' 
			   ){
				newCode.append(sourceCode.charAt(i));
			}
		}
		
		//代码转换
		return newCode.toString();	
	}
	
	
	/*
	 * 扫描
	 * 多种类型分支识别
	 */
	/**
	 * @param sourceCode
	 * @param currPosition
	 * @return
	 */
	public static int scanner(String sourceCode,int currPosition){
		//int tag = -1;
				int movement = 0;//位移，相当于指针移动的功能
				int len = sourceCode.length();
				StringBuffer token = new StringBuffer();
				char ch = sourceCode.charAt(currPosition);
				
				//跳过空格
				while(ch==' '){
					if(currPosition<len-1){
						currPosition++;
						ch = sourceCode.charAt(currPosition);
						movement++;
					}
					else{
						movement++;//末尾空格
						break;
					}
					
				}
				
				//字母开头
		if(isLetter(ch)&&currPosition<len-1){
			token.append(sourceCode.charAt(currPosition));
			currPosition++;
			//后续字母
			while((isLetter(sourceCode.charAt(currPosition))
					||isNumber(sourceCode.charAt(currPosition)))
					&&currPosition<len-1){
				token.append(sourceCode.charAt(currPosition));
				currPosition++;
			}
			//token.append('\0');
			String tmpstr=token.toString();		
			movement+=tmpstr.length();
			
			//加入token
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
					error+=("Line:"+lineNum+" Error, more than one dot. At "+(currPosition+1));
					isError = true;
				}
				currPosition++;
			}
			if(isLetter(sourceCode.charAt(currPosition))){
				error+=("Error:Line:"+lineNum+" Number can't be followed by a letter!");
				isError=true;
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
		//可能的双符号开头
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
			//防止第一个符号是负号，因为后面检测了前一个token
			else if(ch=='-'&&tokens.isEmpty()){
				error+=("Line:"+lineNum+"At"+currPosition+"  Syntax Error: '-' can not be the head of the line!");
				isError=true;
			}
			//识别为减号
			else if(ch=='-'&&(tokens.get(tokens.size()-1).code==-1
								||tokens.get(tokens.size()-1).code==-2
								||tokens.get(tokens.size()-1).code==-3
								||tokens.get(tokens.size()-1).code==-4)){
				token.append(ch);		
				String tmpstr = token.toString();
				movement++;
				
				Token tokenObj = new Token();
				tokenObj.put(tmpstr,lineNum);
				tokenObj.confirmCode();
				tokens.add(tokenObj);
			}
			//识别为负号
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
				//负号后面是不匹配的符号
				if((currPosition<len-1)
						&&!isLetter(sourceCode.charAt(currPosition))
						&&!isNumber(sourceCode.charAt(currPosition))){
					error+=("Line:"+lineNum+" At "+currPosition+" Syntax Error:Unknown '-'");
					isError=true;
				}
				
				String tmpstr = token.toString();
				movement+=tmpstr.length();

				Token tokenObj = new Token();
				tokenObj.put(tmpstr+"_",lineNum);
				tokenObj.confirmCode();
				tokens.add(tokenObj);
			}
			//连续负号
			else if(ch=='-'){
				error+=("Line:"+lineNum+" At "+currPosition+" unknown '-'");
				isError=true;
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
				movement++;
				
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
			else {
				error+=("Error:Line: "+lineNum+" Error, unknown character. At "+(currPosition+1));
	    		isError=true;
	    		movement++;
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
		    		error+=("Error:Line: "+lineNum+" There is only one '\"'");
		    		isError=true;
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
				    error+=("Line:"+lineNum+" char is too long. At"+currPosition+":"+sourceCode.charAt(currPosition));
				    isError=true;
				}
				token.append(sourceCode.charAt(currPosition));
				charLength++;
				currPosition++;
				if(currPosition>=len-1){
				    error+=("Error:Line: "+lineNum+" There is only one '\''");
				    isError=true;
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
			//未知符号,如中文字符
			error+=("Line:"+lineNum+" Error, unknown character. At "+(currPosition+1));
			isError=true;
		}
		
		return movement;
	}
	
	//��ȡ�ļ�����
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
	
	
	//methods for GUI usage
	public String getPreprocessedContent(String content) {
		String inputStr = preProcess(content);
		File file = new File("code2.txt");
		try {
			file.createNewFile();
			FileWriter writer = new FileWriter(file);
			writer.write(inputStr);
			writer.flush();
			writer.close();	
		} catch (Exception e) {
			// TODO: handle exception
			return "oops! something went wrong when reading the file.";
		}
		return inputStr;
	}
	static String retStr="";
	
	public String runLexer() {
		retStr = "";
		tokens.clear();
		lineNum=0;
		isError=false;
		error="";
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(new File("code2.txt"))));
			//ɨ��
			String newSrc = reader.readLine();
			
			lineNum++;
			while(newSrc!=null){
				newSrc+=" ";//��ÿ��ĩβ��һ��������ո�
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
				retStr+="line:"+tk.line+",  "+tk.content+",  Code:"+tk.code+"\n";
			}
			retStr+="\n\n\n";
			return retStr;
		} catch (Exception e) {
			// TODO: handle exception
			return "oops! something went wrong when running lexer!";
		}
		
	}


	public static void main(String[] args) {
//		run aRun = new run();
//		aRun.getPreprocessedContent("int fun5 (int a)\r\n" + 
//				"{ \r\n" + 
//				"  if（a>0){\r\n" + 
//				"      a = a + 1;\r\n" + 
//				"  }\r\n" + 
//				"  if(b==0){\r\n" + 
//				";\r\n" + 
//				"}\r\n" + 
//				"}");
//		System.out.println(aRun.runLexer());
		
	}

}
