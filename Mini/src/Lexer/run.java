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
	//���ֵ
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
	
	
	//�����Ƿ�Ϊ������,���ҷ��ض�Ӧ�������
	public static boolean isReserved(String str){
		for(String rsvdwd:reserved_word){
			if(rsvdwd.equals(str)){
				return true;	//�Ǳ������򷵻�T
			}	
		}
		return false;//���Ǳ����֣�����F
	}
	
	//�ж��Ƿ�Ϊ��ĸ
	public static boolean isLetter(char letter){
		if(letter>='a'&&letter<='z' ||letter>='A'&&letter<='Z' )
			return true;
		else
			return false;
	}
	
	//�ж��Ƿ�Ϊ����
	public static boolean isNumber(char letter){
		if(letter>='0'&&letter<='9')
			return true;
		else
			return false;
	}
	
	//Ԥ����
	public static String preProcess(String sourceCode){
		int len = sourceCode.length();
		StringBuffer newCode = new StringBuffer();
		
		for(int i=0;i<len;i++){
			//��������ע��
			if(sourceCode.charAt(i)=='/'&&sourceCode.charAt(i+1)=='/'){
				i+=2;
				while(sourceCode.charAt(i)!='\n'){
					i++;
				}
				//lineNum++;
			}
			//��������ע��
			if((sourceCode.charAt(i)=='/'&&sourceCode.charAt(i+1)=='*')){
				i+=2;
				while(!(sourceCode.charAt(i)=='*'&&sourceCode.charAt(i+1)=='/')){
					if(sourceCode.charAt(i)=='\n'){
						lineNum++;
					}
					i++;//����ɨ��
					if (i >= len)
					{
					System.out.println("ע�ͳ���û���ҵ� */���������������\n");
					System.exit(0);
					}
				}
				i+=2;
			}
			//�����ռ�
			if(sourceCode.charAt(i)!='\t' 
			   ){
				newCode.append(sourceCode.charAt(i));
			}
		}
		
		//Դ����ת��
		return newCode.toString();	
	}
	
	
	/*
	 * �ʷ����ຯ�����ֳ�����token���õ���Ӧ�����Ͳ��ұ���
	 * ���ͣ�1.������; 2.��ʶ��; 3.����;4.����,5.�ַ���,6.�ַ�
	 */
	/**
	 * @param sourceCode
	 * @param currPosition
	 * @return
	 */
	public static int scanner(String sourceCode,int currPosition){
		//int tag = -1;
		int movement = 0;//���ڷ��ص��ƶ���������ȡtoken֮��Ҫ�ı��ȡλ�ã���������C��ָ��
		int len = sourceCode.length();
		StringBuffer token = new StringBuffer();
		char ch = sourceCode.charAt(currPosition);
		
		//���˿ո�
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
		
		//��ĸ��ͷ����ʼ��¼token
		if(isLetter(ch)&&currPosition<len-1){
			token.append(sourceCode.charAt(currPosition));
			currPosition++;
			//���������ֻ�����ĸ��������¼token
			while((isLetter(sourceCode.charAt(currPosition))
					||isNumber(sourceCode.charAt(currPosition)))
					&&currPosition<len-1){
				token.append(sourceCode.charAt(currPosition));
				currPosition++;
			}
			//token.append('\0');
			String tmpstr=token.toString();		
			movement+=tmpstr.length();
			
			//�鱣���ֱ�,�޸��ַ���
			Token tokenObj = new Token();
			tokenObj.put(tmpstr,lineNum);
			tokenObj.confirmCode();
			tokens.add(tokenObj);
			
		}
		//���ֿ�ͷ
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
		//�����ſ�ͷ
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
		//���ܵĸ��Ϸ��ſ�ͷ
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
			//ʶ��-��Ϊ���ţ�������Ҫ��ֹtokensΪ�ճ���(��ΪҪ���tokens��ǰһ��Ԫ�أ�����û��Ԫ�أ�
			else if(ch=='-'&&tokens.isEmpty()){
				System.out.println("Line:"+lineNum+"At"+currPosition+"  Syntax Error: '-' can not be the head of the line!");
			}
			//ʶ��-��Ϊ����
			else if(ch=='-'&&(tokens.get(tokens.size()-1).code==-1||tokens.get(tokens.size()-1).code==-2||tokens.get(tokens.size()-1).code==-4)){
				token.append(ch);		
				String tmpstr = token.toString();
				movement++;
				
				Token tokenObj = new Token();
				tokenObj.put(tmpstr,lineNum);
				tokenObj.confirmCode();
				tokens.add(tokenObj);
			}
			//ʶ��-��Ϊ���ţ�ǰ���������Ż��߱Ƚϡ���ֵ����
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
				//ʶ��Ϊ����֮�󣬺���������Ǳ�ʶ�������ֻ������ţ�����
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
			//ʶ��δ֪����
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
		}
		//˫����
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
		//������
		else if(ch=='\''&&currPosition<len-1){
			token.append(sourceCode.charAt(currPosition));
			currPosition++;
			int charLength = 1;
			while(sourceCode.charAt(currPosition)!='\''){
				if(charLength>1){
				    System.out.println("Line:"+lineNum+" �ַ��������﷨����λ�ã�"+currPosition+":"+sourceCode.charAt(currPosition));
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
			//����ʶ���δ֪���ţ�����������
			System.out.println("Line:"+lineNum+" Error, unknown character. At "+(currPosition+1));
			System.exit(0);
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
	

	public static void main(String[] args) {
		fill_list();
		//System.out.println(operator.get(0));
		//��ȡ����
		try {
			//�����ı�
			//Ԥ��������ע��
			String inputStr = preProcess(readToString("code.txt"));
			File file = new File("code2.txt");
			file.createNewFile();
			FileWriter writer = new FileWriter(file);
			writer.write(inputStr);
			writer.flush();
			writer.close();	
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
				System.out.println("line:"+tk.line+",  "+tk.content+",  Code:"+tk.code);
			}
			System.out.println();
			System.out.println();
			System.out.println();
			ParserRun obj = new ParserRun();
			obj.start(tokens);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}

}
