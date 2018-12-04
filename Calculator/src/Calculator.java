package test;

import java.util.regex.*;
import java.util.*;
import java.io.*;

public class Calculator {
	Map<String,Double> map;//映射表
	String key;
	String NBL="";
	
	public Calculator(){
		map = new HashMap<String,Double>();
	}
	
	
	//use regex to determine error and pass a string
	public String read(String str) throws IOException{
		
		String expPattern = "((((\\d+|(\\D(\\d|\\D)*))(\\D(\\d+|(\\D(\\d|\\D)*)))*)|(\\((\\d+|(\\D(\\d|\\D)*))\\D(\\d+|(\\D(\\d|\\D)*))\\)*))((\\D(\\d+|(\\D(\\d|\\D)*)))|(\\D\\((\\d+|(\\D(\\d|\\D)*))\\D(\\d+|(\\D(\\d|\\D)*))\\)))*)";
		String prtfVar = "print\\(((((\\d+|(\\D(\\d|\\D)*))(\\D(\\d+|(\\D(\\d|\\D)*)))*)|(\\((\\d+|(\\D(\\d|\\D)*))\\D(\\d+|(\\D(\\d|\\D)*))\\)*))((\\D(\\d+|(\\D(\\d|\\D)*)))|(\\D\\((\\d+|(\\D(\\d|\\D)*))\\D(\\d+|(\\D(\\d|\\D)*))\\)))*)\\)";
		String exitVar = "exit;";
		if(Pattern.matches(expPattern,str)){
			System.out.println("The string is:"+str);
			Pattern r = Pattern.compile(expPattern);
			Matcher m = r.matcher(str);
			m.find();//
			key = m.group(1);
			return m.group(3);
		}else if(Pattern.matches(expPattern, str)){
			Pattern r = Pattern.compile(expPattern);
			Matcher m = r.matcher(str);
			m.find();
			
			return "    "+m.group(1);
		}
		
		else if(Pattern.matches(prtfVar, str)){
			Pattern r = Pattern.compile(prtfVar);
			Matcher m = r.matcher(str);
			//System.out.println("The outcome of find()3:"+m.find());
			m.find();
			//System.out.println("Group1:"+m.group(1));
			
			return "    "+m.group(1);
		}
		else if(Pattern.matches(exitVar, str)){
			return "exit";
		}
		else {
			System.out.println("Error: Invalid input!(Check if you are using English or if you loss the \";\")");
			return "inputError";
		}
		
	}
	
	

	//insert blank into the string 
	public String insertBlanks(String s){
		String result = "";
		for(int i=0;i<s.length();i++){
			if(s.charAt(i)=='('){
				result +=" "+s.charAt(i)+" ";
			}
			else if(s.charAt(i)==')'){
				result +=" "+s.charAt(i)+" ";
			}
			else if(s.charAt(i)=='+'){
				result +=" "+s.charAt(i)+" ";
			}
			else if(i==0&&s.charAt(i)=='-'){//�׸���
				result +=" "+s.charAt(i);
			}
			else if(s.charAt(i)=='-'&&s.charAt(i-1)=='('){//����
				result +=" "+s.charAt(i);
			}
			else if(s.charAt(i)=='*'){
				result +=" "+s.charAt(i)+" ";
			}
			else if(s.charAt(i)=='/'){
				result +=" "+s.charAt(i)+" ";
			}
			else if(s.charAt(i)=='-'){
				result +=" "+s.charAt(i)+" ";
			}
			else
				result +=s.charAt(i);
		}
		return result;
	}
	
	
	//计算
    public double calculate(String exprs){
    	Stack<Node> operands = new Stack<>();
    	Stack<Character> operators = new Stack<>();
    	String variableOrNumber = "[a-zA-Z](\\d|[a-zA-Z])*";
    	String[] tokens = exprs.split(" ");
    	int idx = 0;//报错标记位置
    	for(String token:tokens){
    		idx++;
    		
    		//空白内容
    		if(token.length()==0)
    			continue;
    		/*判断符号表数字压栈
    		else if(Pattern.matches(variableOrNumber,token)){
    			System.out.println(token);
    			if(map.containsKey(token)){
        			Double value = map.get(token);
        			operands.push(value);
        		}
    			else{
    				idx = idx+1+key.length();
    				System.out.print("Invalid input: \""+token+"\""+" at position: "+idx+", error output:");
    				return 0;
    			}
    		}*/
    		
    		//判断符号并且压栈
    		else if(token.length()==1&&(token.charAt(0)=='+'||token.charAt(0)=='-')){
    			System.out.println(token);
    			 while (!operators.isEmpty()
    				     &&(operators.peek() == '-' 
    					    || operators.peek() == '+' 
    					    || operators.peek() == '/' 
    					    || operators.peek() == '*')) {
                     processAnOperator(operands, operators);   
                 }
    			 operators.push(token.charAt(0));
    		}  		
    		else if (token.length()==1&&(token.charAt(0) == '*' || token.charAt(0) == '/')) {
    			System.out.println(token);
                while (!operators.isEmpty()
                		&&(operators.peek() == '/' || operators.peek() == '*')) {
                    processAnOperator(operands, operators);
                }
                operators.push(token.charAt(0));   
            }
    		//跳过空格
    		else if(token.trim().length()==0)
    			continue;
            //左括号压栈
            else if (token.trim().charAt(0) == '(') {
            	System.out.println(token);
                operators.push('(');
                idx--;//У������λ��
            }
            //右括号出栈
            else if (token.trim().charAt(0) == ')') {
            	System.out.println(token);
            	idx--;//位置变化
                while (operators.peek() != '(') {
                    processAnOperator(operands, operators);
                }
                operators.pop();   
            }
            //否则压数字栈
            else {
            	System.out.println(token);
            	Node newNode = new Node();
            	double number = Double.parseDouble(token);
            	newNode.setValue(number);
            	newNode.setExpr(String.valueOf(number));//多此一举是为了消除可能多余的无用0
                operands.push(newNode);  
            }
        }
        //清算剩下的栈元素（此时是从栈顶开始）
        while (!operators.isEmpty()) {
            processAnOperator(operands, operators);
        }
        
        
        double value = operands.peek().value;
        NBL=operands.peek().expr;
        //String regex = "(.{1})";
        //NBL = NBL.replaceAll (regex, "$1 ");
        //返回逆波兰式
        System.out.println("逆波兰式："+NBL);
        
        operands.pop();
        if(!operands.isEmpty()||!operators.isEmpty()){
        	System.out.println("Invalid expression, check your operands!");
        	return 0;
        }
        //刷新符值映射
        if(map.containsKey(key)){
        	map.remove(key);
        	map.put(key, value);
        }
        else
        	map.put(key, value);
    	return value;
    }
    
  //执行一次运算,type表示不同的位置的运算
    public void processAnOperator(Stack<Node> operandStack, Stack<Character> operatorStack){
        char op = operatorStack.pop();  //操作符
        Node node1= operandStack.pop();
        Node node2= operandStack.pop();
        double op1 = node1.value;  //操作数
        double op2 = node2.value;
        
        if (op == '*'){
        	Node newNode = new Node();
        	newNode.setExpr(node2.expr+"_"+node1.expr+"_"+op);
        	newNode.setValue(op1*op2);
        	operandStack.push(newNode);
        }
            
        else if (op == '-'){
        	Node newNode = new Node();
        	newNode.setExpr(node2.expr+"_"+node1.expr+"_"+op);
        	newNode.setValue(op1-op2);
        	operandStack.push(newNode);
        }   //注意方向op2-op1
        else if (op == '/'){
        	Node newNode = new Node();
        	newNode.setExpr(node2.expr+"_"+node1.expr+"_"+op);
        	newNode.setValue(op1/op2);
        	operandStack.push(newNode);
        }
        else if (op == '+'){
        	Node newNode = new Node();
        	newNode.setExpr(node2.expr+"_"+node1.expr+"_"+op);
        	newNode.setValue(op1+op2);
        	operandStack.push(newNode);
        }
        
    }
}
