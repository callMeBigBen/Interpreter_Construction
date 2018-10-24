import java.util.regex.*;
import java.util.*;
import java.io.*;


public class Calculator{
	Map<String,Double> map;//ӳ���(�����������ű�)
	String key;
	
	public Calculator(){
		map = new HashMap<String,Double>();
	}
	
	
	//use regex to determine error and pass a string
	public String read(String str) throws Exception{
		//the pattern of variable is:(\\D(\\d|\\D)*),v or number is:(\\d+|(\\D(\\d|\\D)*))		
		//String pattern = "((\\d+(\\D\\d+)*)|(\\(\\d+\\D\\d+\\)*))((\\D\\d+)|(\\D\\(\\d+\\D\\d+\\)))*";
		//replace all "\\d+" by "(\\d+|(\\D(\\d|\\D)*))"
		String pattern ="^(\\(*\\d+(.\\d+)*\\)*(\\+|-|/|\\*))+\\d+(.\\d+)*\\)*$";
		//String pattern = "(((\\d+|(\\D(\\d|\\D)*))(\\D(\\d+|(\\D(\\d|\\D)*)))*)|(\\((\\d+|(\\D(\\d|\\D)*))\\D(\\d+|(\\D(\\d|\\D)*))\\)*))((\\D(\\d+|(\\D(\\d|\\D)*)))|(\\D\\((\\d+|(\\D(\\d|\\D)*))\\D(\\d+|(\\D(\\d|\\D)*))\\)))*;";
		//String prtfVar = "print\\(((((\\d+|(\\D(\\d|\\D)*))(\\D(\\d+|(\\D(\\d|\\D)*)))*)|(\\((\\d+|(\\D(\\d|\\D)*))\\D(\\d+|(\\D(\\d|\\D)*))\\)*))((\\D(\\d+|(\\D(\\d|\\D)*)))|(\\D\\((\\d+|(\\D(\\d|\\D)*))\\D(\\d+|(\\D(\\d|\\D)*))\\)))*)\\);";
		//String exitVar = "exit;";
		if(Pattern.matches(pattern,str)){
			System.out.println("The string is:"+str);
			Pattern r = Pattern.compile(pattern);
			//ץȡ��
			Matcher m = r.matcher(str);
			//System.out.println("The outcome of find()1:"+m.find());
			m.find();//��仰�������һ��
			System.out.println("Group1:"+m.group(1));
			System.out.println("Group2:"+m.group(3));
			key = m.group(1);
			return m.group(3);
		}
//		else if(Pattern.matches(prtfVar, str)){
//			Pattern r = Pattern.compile(prtfVar);
//			Matcher m = r.matcher(str);
//			//System.out.println("The outcome of find()3:"+m.find());
//			m.find();
//			//System.out.println("Group1:"+m.group(1));
//			return "    "+m.group(1);
//		}
		else {
			throw new Exception("Error: Invalid input!(Check if you are using English or if you loss the \";\")");

		}
		
	}
	
	

	//insert blank into the string 
	public String insertBlanks(String s){
		String result = "";
		for(int i=0;i<s.length();i++){
			if(s.charAt(i)=='('
					   ||s.charAt(i)==')'
					   ||s.charAt(i)=='+'
					   ||s.charAt(i)=='-'
					   ||s.charAt(i)=='*'
					   ||s.charAt(i)=='/'){
				result +=" "+s.charAt(i)+" ";
			}
			else
				result +=s.charAt(i);
		}
		return result;
	}
	
	
	//������
    public double calculate(String exprs) throws Exception{
    	Stack<Double> operands = new Stack<>();
    	Stack<Character> operators = new Stack<>();
    	String variableOrNumber = "[a-zA-Z](\\d|[a-zA-Z])*";
    	String[] tokens = exprs.split(" ");
    	int idx = 0;//��λ�ű�
    	for(String token:tokens){
    		idx++;
    		
    		//������Ч�ַ�����pass
    		if(token.length()==0)
    			continue;
    		//����������������ڹ�ϣ���м������������滻�����������򱨴�
    		else if(Pattern.matches(variableOrNumber,token)){
    			System.out.println(token);
    			if(map.containsKey(token)){
        			Double value = map.get(token);
        			operands.push(value);
        		}
    			else{
    				idx = idx+1+key.length();
    				throw new Exception("Invalid input: \""+token+"\""+" at position: "+idx+", error output:");//׷��λ�ñ���
    			}
    		}
    		
    		//����ǼӼ��Ļ�����Ϊ�Ӽ������ȼ���ͣ���������ֻҪ�����Ӽ��ţ����۲�����ջ�е���ʲô�������Ҫ����
    		else if(token.charAt(0)=='+'||token.charAt(0)=='-'){
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
    		else if (token.charAt(0) == '*' || token.charAt(0) == '/') {
    			System.out.println(token);
                while (!operators.isEmpty()
                		&&(operators.peek() == '/' || operators.peek() == '*')) {
                    processAnOperator(operands, operators);
                }
                operators.push(token.charAt(0));   //����ǰ��������ջ
            }
    		//�ո�ֱ��Խ��
    		else if(token.trim().length()==0)
    			continue;
            //����������ŵĻ�ֱ����ջ��ʲôҲ���ò���,trim()����������ȥ���ո�ģ���������ķָ�������ܻ�����������пո�
    		//���ǽ�����������������������������žͻᵼ�¶��һ���ո��ַ����Ӷ�����idxλ���ű��λ��ҪУ��
            else if (token.trim().charAt(0) == '(') {
            	System.out.println(token);
                operators.push('(');
                idx--;//У������λ��
            }
            //����������ŵĻ������ջ�е������ֱ��������
            else if (token.trim().charAt(0) == ')') {
            	System.out.println(token);
            	idx--;//У������λ��
                while (operators.peek() != '(') {
                    processAnOperator(operands, operators);
                }
                operators.pop();   //������֮�����������
            }
            //����ֱ����ջ
            else {
            	System.out.println(token);
                operands.push(Double.parseDouble(token));   //�������ַ���ת��������Ȼ��ѹ��ջ��
            }
        }
        //���ջ
        while (!operators.isEmpty()) {
            processAnOperator(operands, operators);
        }
        double value = operands.peek().doubleValue();
        operands.pop();
        if(!operands.isEmpty()){
        	throw new Exception("ֵ格式有误!左括号前面不能有变量或数值\r\n" );
        }
        //����ӳ���
        if(map.containsKey(key)){
        	map.remove(key);
        	map.put(key, value);
        }
        else
        	map.put(key, value);
    	return value;
    }
    
  //������������þ��Ǵ���ջ�е��������ݣ�Ȼ��ջ�е�������������֮�󽫽���洢��ջ��
    public void processAnOperator(Stack<Double> operandStack, Stack<Character> operatorStack){
        char op = operatorStack.pop();  //����һ��������
        double op1 = operandStack.pop();  //�Ӵ洢���ݵ�ջ�е������������������Ͳ�����op����
        double op2 = operandStack.pop();
        if (op == '*')  //���������Ϊ+��ִ�м�����
            operandStack.push(op1 * op2);
        else if (op == '-')
            operandStack.push(op2 - op1);   //��Ϊ�����ջ�Ľṹ����Ȼ������������Ǻ���ģ������op2-op1
        else if (op == '/')
            operandStack.push(op2 / op1);
        else if (op == '+')
            operandStack.push(op1 + op2);
        
    }


}
