package Parser;
import java.util.ArrayList;
import java.io.File;
import java.util.*;
import Lexer.*;
public class ParserRun {
	static ArrayList<Token> tokens ;
	//static Iterator<Token> iterator = tokens.iterator();
	static int currPos = 0;
	//code for AST generation
	static int nodeNum = 0;
	static String dotFormat = "";
	public static void createDotGraph(String dotFormat,String fileName)
	{
	    GraphViz gv=new GraphViz();
	    gv.addln(gv.start_graph());
	    gv.add(dotFormat);
	    gv.addln(gv.end_graph());
	    String type = "jpg";  //输出图文件的格式，以.jpg为例
	    gv.decreaseDpi();
	    gv.decreaseDpi();
	    File out = new File(fileName+"."+ type); 
	    gv.writeGraphToFile( gv.getGraph( gv.getDotSource(), type ), out );
	}
	
	//iterative subPrograms
	public static int getNext() {
		nodeNum++;
		return nodeNum-1;
	}
	public void start(ArrayList<Token> ts) {
		tokens = ts;
		Token EOF = new Token();
		int line = tokens.get(tokens.size()-1).getLine();
		EOF.setCode(-8);
		EOF.setLine(line);
		tokens.add(EOF);
		try {
			program();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	public boolean isType() {
		return tokens.get(currPos).getCode()==1||tokens.get(currPos).getCode()==2||
				tokens.get(currPos).getCode()==3||tokens.get(currPos).getCode()==4||
				tokens.get(currPos).getCode()==5;
	}
	public boolean isIdentifier() {
		return tokens.get(currPos).getCode()==-1;
	}
	public boolean isNum() {
		return tokens.get(currPos).getCode()==-2||tokens.get(currPos).getCode()==-3||tokens.get(currPos).getCode()==-4;
	}
	public boolean  isBoolean() {
		return tokens.get(currPos).getCode()==6||tokens.get(currPos).getCode()==7;
	}
	public boolean isRelation() {
		return tokens.get(currPos).getCode()==25||tokens.get(currPos).getCode()==26
				||tokens.get(currPos).getCode()==27||tokens.get(currPos).getCode()==28||
				tokens.get(currPos).getCode()==29||tokens.get(currPos).getCode()==30;
	}
	//1<程序> → <声明列表> 
	public void program() throws Exception {
		if(isType()) {
			dotFormat+="<program-"+getNext()+">-><claimingList-"+getNext()+">;";
			claimingList();
		}
		else {
			throw new Exception("Line:"+tokens.get(currPos).getLine()
					+"  Program should start with claiming!");
		} 
	}
	//2.<声明列表> → <声明><声明列表次>
	public void claimingList() throws Exception{
		int curNodeNum = nodeNum-1;
		dotFormat+="<claimingList-"+curNodeNum+">-><claiming-"+getNext()+">;";
		claiming();
		dotFormat+="<claimingList-"+curNodeNum+">-><claimingList2-"+getNext()+">;";
		claimingList2();
	}
	//3
	public void claimingList2() throws Exception{
		int curNodeNum = nodeNum-1;
		if(tokens.get(currPos).getCode()==-8) {
			dotFormat+="<claimingList2-"+curNodeNum+">-><end-"+getNext()+">;";
			System.out.println();
			System.out.println();
			System.out.println("This is the end of parser");
			System.out.println("*********************************************");
			System.out.println("Below is the AST");
			//dotFormat = dotFormat.substring(0,dotFormat.indexOf(";", 200)+1);
			createDotGraph(dotFormat, "DotGraph");
		}
		else if(isType()) {
			dotFormat+="<claimingList2-"+curNodeNum+">-><claiming-"+getNext()+">;";
			claiming();
			dotFormat+="<claimingList2-"+curNodeNum+">-><claimingList2-"+getNext()+">;";
			claimingList2();
		}
		else if(isIdentifier()&&tokens.get(currPos+1).getCode()==24) {
			dotFormat+="<claimingList2-"+curNodeNum+">-><exprClaiming-"+getNext()+">;";
			exprClaiming();
			dotFormat+="<claimingList2-"+curNodeNum+">-><claimingList2-"+getNext()+">;";
			claimingList2();
		}
		else if(isIdentifier()||tokens.get(currPos).getCode()==-7) {
			dotFormat+="<claimingList2-"+curNodeNum+">-><claiming-"+getNext()+">;";
			claiming();
			dotFormat+="<claimingList2-"+curNodeNum+">-><claimingList2-"+getNext()+">;";
			claimingList2();
		}
		else if(tokens.get(currPos).getCode()==8||tokens.get(currPos).getCode()==10) {
			return;
		}
		else {
			throw new Exception("Line:"+tokens.get(currPos).getLine()
					+ " Syntax Error!");
		}
	}
	//4
	
	public void claiming() throws Exception{
		int curNodeNum = nodeNum-1;
		if(isType()) {
			dotFormat+="<claiming-"+curNodeNum+">-><type-"+getNext()+">;";
			type();
			if(isIdentifier()) {
				dotFormat+="<claiming-"+curNodeNum+">-><"+tokens.get(currPos).getContent()+"-"+getNext()+">;";
				System.out.print(tokens.get(currPos).getContent()+" ");
				currPos++;
				dotFormat+="<claiming-"+curNodeNum+">-><subClaiming-"+getNext()+">;";
				subClaiming();
			}
		}
//		else if(isType()&&tokens.get(currPos+1).getCode()==20) {
//			type();
//			System.out.print(tokens.get(currPos).getContent()+" ");
//			currPos++;
//			if(isType()&&tokens.get(currPos+1).getCode()==21) {
//				System.out.print(tokens.get(currPos).getContent()+" ");
//				currPos++;
//				isIdentifier();
//				subClaiming();
//			}
//			else {
//				throw new Exception("Line:"+tokens.get(currPos).getLine()
//						+ " Missing \"]\"!");
//			}
//		}
		else if(isIdentifier()) {
			call();
			if(tokens.get(currPos).getCode()==22) {
				dotFormat+="<claiming-"+curNodeNum+">-><"+tokens.get(currPos).getContent()+"-"+getNext()+">;";
				System.out.print(tokens.get(currPos).getContent()+" ");
				currPos++;
			}
			else {
				throw new Exception("Line:"+tokens.get(currPos).getLine()
						+ "Missing \";\"!");
			}
		}
		else if(tokens.get(currPos).getCode()==-7) {
			dotFormat+="<claiming-"+curNodeNum+">-><printFunc-"+getNext()+">;";
			printFunc();
			if(tokens.get(currPos).getCode()==22) {
				dotFormat+="<claiming-"+curNodeNum+">-><"+tokens.get(currPos).getContent()+"-"+getNext()+">;";
				System.out.print(tokens.get(currPos).getContent()+" ");
				currPos++;
			}
			else {
				throw new Exception("Line:"+tokens.get(currPos).getLine()
						+ "Missing \";\"!");
			}
		}
		else {
			throw new Exception("Line:"+tokens.get(currPos).getLine()
					+ " Syntax Error!");
		}
	}
	//5
	public void subClaiming() throws Exception{
		int curNodeNum = nodeNum-1;
		if(tokens.get(currPos).getCode()==22||tokens.get(currPos).getCode()==20||tokens.get(currPos).getCode()==24) {
			dotFormat+="<subClaiming-"+curNodeNum+">-><subVarClaiming-"+getNext()+">;";
			subVarClaiming();
		}
		else if(tokens.get(currPos).getCode()==16) {
			dotFormat+="<subClaiming-"+curNodeNum+">-><subFuncClaiming-"+getNext()+">;";
			subFuncClaiming();
		}
		else {
			throw new Exception("Line:"+tokens.get(currPos).getLine()
					+ " Syntax Error!");
		}
	}
	//6
	public void subVarClaiming() throws Exception{
		int curNodeNum = nodeNum-1;
		if(tokens.get(currPos).getCode()==22) {
			dotFormat+="<subVarClaiming-"+curNodeNum+">-><"+tokens.get(currPos).getContent()+"-"+getNext()+">;";
			System.out.print(tokens.get(currPos).getContent()+" ");
			System.out.println();
			currPos++;
		}
		else if(tokens.get(currPos).getCode()==20&&tokens.get(currPos+3).getCode()==24) {
			dotFormat+="<subVarClaiming-"+curNodeNum+">-><"+tokens.get(currPos).getContent()+"-"+getNext()+">;";
			System.out.print(tokens.get(currPos).getContent()+" ");
			currPos++;
			dotFormat+="<subVarClaiming-"+curNodeNum+">-><posInt-"+getNext()+">;";
			posInt();
			if(tokens.get(currPos).getCode()==21) {
				dotFormat+="<subVarClaiming-"+curNodeNum+">-><"+tokens.get(currPos).getContent()+"-"+getNext()+">;";
				System.out.print(tokens.get(currPos).getContent()+" ");
				currPos++;
				dotFormat+="<subVarClaiming-"+curNodeNum+">-><"+tokens.get(currPos).getContent()+"-"+getNext()+">;";
				System.out.print(tokens.get(currPos).getContent()+" ");
				currPos++;
				dotFormat+="<subVarClaiming-"+curNodeNum+">-><logic1-"+getNext()+">;";
				logic1();
				if(tokens.get(currPos).getCode()==22) {
					dotFormat+="<subVarClaiming-"+curNodeNum+">-><"+tokens.get(currPos).getContent()+"-"+getNext()+">;";
					System.out.print(tokens.get(currPos).getContent()+" ");
					System.out.println();
					currPos++;
				}
				else {
					throw new Exception("Line:"+tokens.get(currPos).getLine()
							+ " Syntax Error: \";\" needed at the end of every sentence.");
				}
			}
			else {
				throw new Exception("Line:"+tokens.get(currPos).getLine()
						+ " Syntax Error: \"]\" needed ");
			}
		}
		else if(tokens.get(currPos).getCode()==20) {
			dotFormat+="<subVarClaiming-"+curNodeNum+">-><"+tokens.get(currPos).getContent()+"-"+getNext()+">;";
			System.out.print(tokens.get(currPos).getContent()+" ");
			currPos++;
			dotFormat+="<subVarClaiming-"+curNodeNum+">-><posInt-"+getNext()+">;";
			posInt();
			if(tokens.get(currPos).getCode()==21) {
				dotFormat+="<subVarClaiming-"+curNodeNum+">-><"+tokens.get(currPos).getContent()+"-"+getNext()+">;";
				System.out.print(tokens.get(currPos).getContent()+" ");
				currPos++;
				if(tokens.get(currPos).getCode()==22) {
					dotFormat+="<subVarClaiming-"+curNodeNum+">-><"+tokens.get(currPos).getContent()+"-"+getNext()+">;";
					System.out.print(tokens.get(currPos).getContent()+" ");
					System.out.println();
					currPos++;
				}
				else {
					throw new Exception("Line:"+tokens.get(currPos).getLine()
							+ " Syntax Error: \";\" needed at the end of every sentence.");
				}
			}
			else {
				throw new Exception("Line:"+tokens.get(currPos).getLine()
						+ " Syntax Error: \"]\" needed ");
			}
		}
		else if(tokens.get(currPos).getCode()==24) {
			dotFormat+="<subVarClaiming-"+curNodeNum+">-><"+tokens.get(currPos).getContent()+"-"+getNext()+">;";
			System.out.print(tokens.get(currPos).getContent()+" ");
			currPos++;
			dotFormat+="<subVarClaiming-"+curNodeNum+">-><logic1-"+getNext()+">;";
			logic1();
			if(tokens.get(currPos).getCode()==22) {
				dotFormat+="<subVarClaiming-"+curNodeNum+">-><"+tokens.get(currPos).getContent()+"-"+getNext()+">;";
				System.out.print(tokens.get(currPos).getContent()+" ");
				System.out.println();
				currPos++;
			}
			else {
				throw new Exception("Line:"+tokens.get(currPos).getLine()
						+ " Syntax Error: \";\" needed at the end of every sentence.");
			}
		}
		else {
			throw new Exception("Line:"+tokens.get(currPos).getLine()
					+ " Syntax Error!");
		}
	}
	//7
	public void posInt() throws Exception{
		int curNodeNum = nodeNum-1;
		if(tokens.get(currPos).getCode()==-3) {
			dotFormat+="<posInt-"+curNodeNum+">-><"+tokens.get(currPos).getContent()+"-"+getNext()+">;";
			System.out.print(tokens.get(currPos).getContent()+" ");
			currPos++;
		}
		else {
			throw new Exception("Line:"+tokens.get(currPos).getLine()
					+ " Syntax Error:Only positive integer allowed!");
		}
	}
	//8
	public void Int() throws Exception{
		int curNodeNum = nodeNum-1;
		if(tokens.get(currPos).getCode()==-4) {
			dotFormat+="<Int-"+curNodeNum+">-><"+tokens.get(currPos).getContent()+"-"+getNext()+">;";
			System.out.print(tokens.get(currPos).getContent()+" ");
			currPos++;
		}
		else {
			throw new Exception("Line:"+tokens.get(currPos).getLine()
					+ " Syntax Error:Only integer allowed!");
		}
	}
	//9
	public void floatNum() throws Exception{
		int curNodeNum = nodeNum-1;
		if(tokens.get(currPos).getCode()==-2) {
			dotFormat+="<floatNum-"+curNodeNum+">-><"+tokens.get(currPos).getContent()+"-"+getNext()+">;";
			System.out.print(tokens.get(currPos).getContent()+" ");
			currPos++;
		}
		else {
			throw new Exception("Line:"+tokens.get(currPos).getLine()
					+ " Syntax Error:Only float allowed!");
		}
	}
	//10
	public void type() throws Exception{
		int curNodeNum = nodeNum-1;
		if(isType()) {
			dotFormat+="<type-"+curNodeNum+">-><"+tokens.get(currPos).getContent()+"-"+getNext()+">;";
			System.out.print(tokens.get(currPos).getContent()+" ");
			currPos++;
		}
		else {
			throw new Exception("Line:"+tokens.get(currPos).getLine()
					+ " Syntax Error:Invalid data type!");
		}
	}
	//11
	public void subFuncClaiming() throws Exception{
		int curNodeNum = nodeNum-1;
		dotFormat+="<subFuncClaiming-"+curNodeNum+">-><"+tokens.get(currPos).getContent()+"-"+getNext()+">;";
		System.out.print(tokens.get(currPos).getContent()+" ");
		currPos++;
		dotFormat+="<subFuncClaiming-"+curNodeNum+">-><paraList-"+getNext()+">;";
		paraList();
		if(tokens.get(currPos).getCode()==17) {
			dotFormat+="<subFuncClaiming-"+curNodeNum+">-><"+tokens.get(currPos).getContent()+"-"+getNext()+">;";
			System.out.print(tokens.get(currPos).getContent()+" ");
			currPos++;
			dotFormat+="<subFuncClaiming-"+curNodeNum+">-><compClaiming-"+getNext()+">;";
			compClaiming();
			System.out.println();
		}
		else {
			throw new Exception("Line:"+tokens.get(currPos).getLine()
					+ " Syntax Error:\")\" needed!");
		}
	}
	//12
	public void paraList() throws Exception{
		int curNodeNum = nodeNum-1;
		if(isType()) {
			dotFormat+="<paraList-"+curNodeNum+">-><param-"+getNext()+">;";
			param();
			dotFormat+="<paraList-"+curNodeNum+">-><paraList2-"+getNext()+">;";
			paraList2();
		}
		else if(tokens.get(currPos).getCode()==17) {
			return;
		}
		else {
			throw new Exception("Line:"+tokens.get(currPos).getLine()
					+ " Syntax Error:invalid parameter list!");
		}
	}
	//13
	public void paraList2() throws Exception{
		int curNodeNum = nodeNum-1;
		if(tokens.get(currPos).getCode()==23) {
			dotFormat+="<paraList2-"+curNodeNum+">-><"+tokens.get(currPos).getContent()+"-"+getNext()+">;";
			System.out.print(tokens.get(currPos).getContent()+" ");
			currPos++;
			dotFormat+="<paraList2-"+curNodeNum+">-><param-"+getNext()+">;";
			param();
			dotFormat+="<paraList2-"+curNodeNum+">-><paraList2-"+getNext()+">;";
			paraList2();
		}
		else if(tokens.get(currPos).getCode()==17) {
			return;
		}
		else {
			throw new Exception("Line:"+tokens.get(currPos).getLine()
					+ " Syntax Error:Invalid parameter list!");
		}
	}
	//14
	public void param() throws Exception{
		int curNodeNum = nodeNum-1;
		dotFormat+="<param-"+curNodeNum+">-><type-"+getNext()+">;";
		type();
		if(isIdentifier()) {
			dotFormat+="<param-"+curNodeNum+">-><"+tokens.get(currPos).getContent()+"-"+getNext()+">;";
			System.out.print(tokens.get(currPos).getContent()+" ");
			currPos++;
			if(tokens.get(currPos).getCode()==20) {
				dotFormat+="<param-"+curNodeNum+">-><"+tokens.get(currPos).getContent()+"-"+getNext()+">;";
				System.out.print(tokens.get(currPos).getContent()+" ");
				currPos++;
				if(tokens.get(currPos).getCode()==21) {
					dotFormat+="<param-"+curNodeNum+">-><"+tokens.get(currPos).getContent()+"-"+getNext()+">;";
					System.out.print(tokens.get(currPos).getContent()+" ");
					currPos++;
				}
				else {
					throw new Exception("Line:"+tokens.get(currPos).getLine()
							+ " Syntax Error:Missing \"]\" !");
				}
			}
		}
		else {
			throw new Exception("Line:"+tokens.get(currPos).getLine()
					+ " Syntax Error:Missing identifier !");
		}
	}
	//15
	public void funcCompClaiming()throws Exception{
		int curNodeNum = nodeNum-1;
		if(tokens.get(currPos).getCode()==18) {
			dotFormat+="<funcCompClaiming-"+curNodeNum+">-><"+tokens.get(currPos).getContent()+"-"+getNext()+">;";
			System.out.print(tokens.get(currPos).getContent()+" ");
			currPos++;
			System.out.println();
			dotFormat+="<funcCompClaiming-"+curNodeNum+">-><funcMultiClaiming-"+getNext()+">;";
			funcMultiClaiming();
			if(tokens.get(currPos).getCode()==19) {
				dotFormat+="<funcCompClaiming-"+curNodeNum+">-><"+tokens.get(currPos).getContent()+"-"+getNext()+">;";
				System.out.print(tokens.get(currPos).getContent()+" ");
				currPos++;
			}
			else {
				throw new Exception("Line:"+tokens.get(currPos).getLine()
						+ " Syntax rror:Missing \"}\" !");
			}
		}
		else {
			throw new Exception("Line:"+tokens.get(currPos).getLine()
					+ " Syntax Error:Missing \"{\" !") ;
		}
	}
	//16
	public void funcMultiClaiming()throws Exception{
		int curNodeNum = nodeNum-1;
		if(isType()){
			dotFormat+="<funcMultiClaiming-"+curNodeNum+">-><claimingList-"+getNext()+">;";
			claimingList();
			dotFormat+="<funcMultiClaiming-"+curNodeNum+">-><multiClaiming2-"+getNext()+">;";
			multiClaiming2();
			dotFormat+="<funcMultiClaiming-"+curNodeNum+">-><retClaiming-"+getNext()+">;";
			retClaiming();
		}
		else if(isIdentifier()) {
			dotFormat+="<funcMultiClaiming-"+curNodeNum+">-><exprClaiming-"+getNext()+">;";
			exprClaiming();
			dotFormat+="<funcMultiClaiming-"+curNodeNum+">-><multiClaiming2-"+getNext()+">;";
			multiClaiming2();
			dotFormat+="<funcMultiClaiming-"+curNodeNum+">-><retClaiming-"+getNext()+">;";
			retClaiming();
		}
		else if(tokens.get(currPos).getCode()==8) {
			dotFormat+="<funcMultiClaiming-"+curNodeNum+">-><ifExpr-"+getNext()+">;";
			ifExpr();
			dotFormat+="<funcMultiClaiming-"+curNodeNum+">-><multiClaiming2-"+getNext()+">;";
			multiClaiming2();
			dotFormat+="<funcMultiClaiming-"+curNodeNum+">-><retClaiming-"+getNext()+">;";
			retClaiming();
		}
		else if(tokens.get(currPos).getCode()==10) {
			dotFormat+="<funcMultiClaiming-"+curNodeNum+">-><whileExpr-"+getNext()+">;";
			whileExpr();
			dotFormat+="<funcMultiClaiming-"+curNodeNum+">-><multiClaiming2-"+getNext()+">;";
			multiClaiming2();
			dotFormat+="<funcMultiClaiming-"+curNodeNum+">-><retClaiming-"+getNext()+">;";
			retClaiming();
		}
		else {
			throw new Exception("Line:"+tokens.get(currPos).getLine()
					+ " Syntax Error: Invalid sentences") ;
		}
	}
	//15
	public void compClaiming() throws Exception{
		int curNodeNum = nodeNum-1;
		if(tokens.get(currPos).getCode()==18) {
			dotFormat+="<compClaiming-"+curNodeNum+">-><"+tokens.get(currPos).getContent()+getNext()+">;";
			System.out.print(tokens.get(currPos).getContent()+" ");
			currPos++;
			System.out.println();
			dotFormat+="<compClaiming-"+curNodeNum+">-><multiClaiming-"+getNext()+">;";
			multiClaiming();
			if(tokens.get(currPos).getCode()==19) {
				dotFormat+="<compClaiming-"+curNodeNum+">-><"+tokens.get(currPos).getContent()+getNext()+">;";
				System.out.print(tokens.get(currPos).getContent()+" ");
				currPos++;
			}
			else {
				throw new Exception("Line:"+tokens.get(currPos).getLine()
						+ " Syntax rror:Missing \"}\" !");
			}
		}
		else {
			throw new Exception("Line:"+tokens.get(currPos).getLine()
					+ " Syntax Error:Missing \"{\" !") ;
		}
	}
	//16
	public void multiClaiming() throws Exception{
		int curNodeNum = nodeNum-1;
		if(isType()){
			dotFormat+="<multiClaiming-"+curNodeNum+">-><claimingList-"+getNext()+">;";
			claimingList();
			dotFormat+="<multiClaiming-"+curNodeNum+">-><multiClaiming2-"+getNext()+">;";
			multiClaiming2();
		}
		else if(isIdentifier()) {
			dotFormat+="<multiClaiming-"+curNodeNum+">-><exprClaiming-"+getNext()+">;";
			exprClaiming();
			dotFormat+="<multiClaiming-"+curNodeNum+">-><multiClaiming2-"+getNext()+">;";
			multiClaiming2();
		}
		else if(tokens.get(currPos).getCode()==8) {
			dotFormat+="<multiClaiming-"+curNodeNum+">-><ifExpr-"+getNext()+">;";
			ifExpr();
			dotFormat+="<multiClaiming-"+curNodeNum+">-><multiClaiming2-"+getNext()+">;";
			multiClaiming2();
		}
		else if(tokens.get(currPos).getCode()==10) {
			dotFormat+="<multiClaiming-"+curNodeNum+">-><whileExpr-"+getNext()+">;";
			whileExpr();
			dotFormat+="<multiClaiming-"+curNodeNum+">-><multiClaiming2-"+getNext()+">;";
			multiClaiming2();
		}
		else if(tokens.get(currPos).getCode()==15) {
			dotFormat+="<multiClaiming-"+curNodeNum+">-><retClaiming-"+getNext()+">;";
			retClaiming();
		}
		else {
			throw new Exception("Line:"+tokens.get(currPos).getLine()
					+ " Syntax Error: Invalid sentences") ;
		}
	}
	//16-2
	public void multiClaiming2()throws Exception{
		int curNodeNum = nodeNum-1;
		if(tokens.get(currPos).getCode()==19) {
			return;
		}
		else if(isIdentifier()) {
			dotFormat+="<multiClaiming2-"+curNodeNum+">-><exprClaiming-"+getNext()+">;";
			exprClaiming();
			dotFormat+="<multiClaiming2-"+curNodeNum+">-><multiClaiming2-"+getNext()+">;";
			multiClaiming2();
		}
		else if(tokens.get(currPos).getCode()==8) {
			dotFormat+="<multiClaiming2-"+curNodeNum+">-><ifExpr-"+getNext()+">;";
			ifExpr();
			dotFormat+="<multiClaiming2-"+curNodeNum+">-><multiClaiming2-"+getNext()+">;";
			multiClaiming2();
		}
		else if(tokens.get(currPos).getCode()==10) {
			dotFormat+="<multiClaiming2-"+curNodeNum+">-><whileExpr-"+getNext()+">;";
			whileExpr();
			dotFormat+="<multiClaiming2-"+curNodeNum+">-><multiClaiming2-"+getNext()+">;";
			multiClaiming2();
		}
		else {
			throw new Exception("Line:"+tokens.get(currPos).getLine()
					+ " Syntax Error: Invalid sentences") ;
		}
	}
	//17
	public void exprClaiming() throws Exception{
		int curNodeNum = nodeNum-1;
		if(tokens.get(currPos).getCode()==22) {
			dotFormat+="<exprClaiming-"+curNodeNum+">-><"+tokens.get(currPos).getContent()+"-"+getNext()+">;";
			System.out.print(tokens.get(currPos).getContent()+" ");
			System.out.println();
			currPos++;
		}
		else if(isIdentifier()) {
			dotFormat+="<exprClaiming-"+curNodeNum+">-><expr-"+getNext()+">;";
			expr();
			if(tokens.get(currPos).getCode()==22) {
				dotFormat+="<exprClaiming-"+curNodeNum+">-><"+tokens.get(currPos).getContent()+"-"+getNext()+">;";
				System.out.print(tokens.get(currPos).getContent()+" ");
				System.out.println();
				currPos++;
			}
			else {
				throw new Exception("Line:"+tokens.get(currPos).getLine()
						+ " Syntax Error: \";\" needed at the end of every sentence.");
			}
		}
		else {
			throw new Exception("Line:"+tokens.get(currPos).getLine()
					+ " Invalid sentences!") ;
		}
		
	}
	//18
	public void expr()throws Exception{
		int curNodeNum = nodeNum-1;
		dotFormat+="<expr-"+curNodeNum+">-><var-"+getNext()+">;";
		var();
		if(tokens.get(currPos).getCode()==24) {
			dotFormat+="<expr-"+curNodeNum+">-><"+tokens.get(currPos).getContent()+"-"+getNext()+">;";
			System.out.print(tokens.get(currPos).getContent()+" ");
			currPos++;
			dotFormat+="<expr-"+curNodeNum+">-><logic1-"+getNext()+">;";
			logic1();
		}
		else {
			throw new Exception("Line:"+tokens.get(currPos).getLine()
					+ " Syntax Error: Expected \"=\" !") ;
		}
	}
	//19
	public void logic1() throws Exception{
		int curNodeNum = nodeNum-1;
		if(tokens.get(currPos).getCode()==33) {
			dotFormat+="<logic1-"+curNodeNum+">-><"+tokens.get(currPos).getContent()+"-"+getNext()+">;";
			System.out.print(tokens.get(currPos).getContent()+" ");
			currPos++;
			dotFormat+="<logic1-"+curNodeNum+">-><logic1-"+getNext()+">;";
			logic1();
		}
		else if(tokens.get(currPos).getCode()==16||isIdentifier()||isNum()||isBoolean()||tokens.get(currPos).getCode()==-5||tokens.get(currPos).getCode()==-6){
			dotFormat+="<logic1-"+curNodeNum+">-><logic2-"+getNext()+">;";
			logic2();
		}
		else if(tokens.get(currPos).getCode()==18) {
			dotFormat+="<logic1-"+curNodeNum+">-><arrayAss-"+getNext()+">;";
			arrayAss();
		}
		else {
			throw new Exception("Line:"+tokens.get(currPos).getLine()
					+ " Syntax Error: Invalid logic expression!") ;
		}
	}
	//20
	public void logic2() throws Exception{
		int curNodeNum = nodeNum-1;
		dotFormat+="<logic2-"+curNodeNum+">-><logic3-"+getNext()+">;";
		logic3();
		dotFormat+="<logic2-"+curNodeNum+">-><logic2sub-"+getNext()+">;";
		logic2sub();
	}
	//21
	public void logic2sub() throws Exception{
		int curNodeNum = nodeNum-1;
		if(tokens.get(currPos).getCode()==22||tokens.get(currPos).getCode()==17) {
			return;
		}
		else if(tokens.get(currPos).getCode()==32||tokens.get(currPos).getCode()==31){
			dotFormat+="<logic2sub-"+curNodeNum+">-><"+tokens.get(currPos).getContent()+"-"+getNext()+">;";
			System.out.print(tokens.get(currPos).getContent()+" ");
			currPos++;
			dotFormat+="<logic2sub-"+curNodeNum+">-><logic3-"+getNext()+">;";
			logic3();
			dotFormat+="<logic2sub-"+curNodeNum+">-><logic2sub-"+getNext()+">;";
			logic2sub();
		}
		else {
			throw new Exception("Line:"+tokens.get(currPos).getLine()
					+ " Syntax Error: Invalid logic expression!") ;
		}
	}
	//22
	public void logic3()throws Exception{
		int curNodeNum = nodeNum-1;
		dotFormat+="<logic3-"+curNodeNum+">-><simpleExpr-"+getNext()+">;";
		simpleExpr();
		dotFormat+="<logic3-"+curNodeNum+">-><logic3sub-"+getNext()+">;";
		logic3sub();
	}
	//23
	public void logic3sub()throws Exception{
		int curNodeNum = nodeNum-1;
		if(tokens.get(currPos).getCode()==22||tokens.get(currPos).getCode()==31||tokens.get(currPos).getCode()==32||tokens.get(currPos).getCode()==17) {
			return;
		}
		else if(tokens.get(currPos).getCode()==31){
			dotFormat+="<logic3sub-"+curNodeNum+">-><"+tokens.get(currPos).getContent()+"-"+getNext()+">;";
			System.out.print(tokens.get(currPos).getContent()+" ");
			currPos++;
			dotFormat+="<logic3sub-"+curNodeNum+">-><simpleExpr-"+getNext()+">;";
			simpleExpr();
			dotFormat+="<logic3sub-"+curNodeNum+">-><logic3sub-"+getNext()+">;";
			logic3sub();
		}
		else {
			throw new Exception("Line:"+tokens.get(currPos).getLine()
					+ " Syntax Error: Invalid logic expression!") ;
		}
	}
	//24
	public void simpleExpr()throws Exception{
		int curNodeNum = nodeNum-1;
		if(tokens.get(currPos).getCode()==-5) {
			dotFormat+="<simpleExpr-"+curNodeNum+">-><charExpr-"+getNext()+">;";
			charExpr();
		}
		else if(tokens.get(currPos).getCode()==-6) {
			dotFormat+="<simpleExpr-"+curNodeNum+">-><strExpr-"+getNext()+">;";
			strExpr();
		}
		else if(isBoolean()) {
			dotFormat+="<simpleExpr-"+curNodeNum+">-><"+tokens.get(currPos).getContent()+"-"+getNext()+">;";
			System.out.print(tokens.get(currPos).getContent()+" ");
			currPos++;
		}
		else if((tokens.get(currPos).getCode()==16||isIdentifier()||isNum())&&(tokens.get(currPos+1).getCode()==17||tokens.get(currPos+1).getCode()==19||tokens.get(currPos+1).getCode()==22||tokens.get(currPos+1).getCode()==23
				||tokens.get(currPos+1).getCode()==40||tokens.get(currPos+1).getCode()==34||
				tokens.get(currPos+1).getCode()==35||tokens.get(currPos+1).getCode()==36||
				tokens.get(currPos+1).getCode()==37||tokens.get(currPos+1).getCode()==16)) {
			dotFormat+="<simpleExpr-"+curNodeNum+">-><calExpr-"+getNext()+">;";
			calExpr();
		}
		else if(tokens.get(currPos).getCode()==16||isIdentifier()||isNum()) {
			dotFormat+="<simpleExpr-"+curNodeNum+">-><calExpr-"+getNext()+">;";
			calExpr();
			dotFormat+="<simpleExpr-"+curNodeNum+">-><relationOp-"+getNext()+">;";
			relationOp();
			dotFormat+="<simpleExpr-"+curNodeNum+">-><calExpr-"+getNext()+">;";
			calExpr();
		}
		else if(tokens.get(currPos).getCode()==19) {
			return;
		}
		else {
			throw new Exception("Line:"+tokens.get(currPos).getLine()
					+ " Syntax Error: Invali"
					+ "d logic expression!") ;
		}
	}
	//25
	public void relationOp()throws Exception{
		int curNodeNum = nodeNum-1;
		if(isRelation()){
			if(tokens.get(currPos).getCode()==25) {
				dotFormat+="<relationOp-"+curNodeNum+">-><Greater-"+getNext()+">;";
			}
			else if(tokens.get(currPos).getCode()==26) {
				dotFormat+="<relationOp-"+curNodeNum+">-><Less-"+getNext()+">;";
			}
			else if(tokens.get(currPos).getCode()==28) {
				dotFormat+="<relationOp-"+curNodeNum+">-><LessOrEqual-"+getNext()+">;";
			}
			else if(tokens.get(currPos).getCode()==29) {
				dotFormat+="<relationOp-"+curNodeNum+">-><GreaterOrEqual-"+getNext()+">;";
			}
			System.out.print(tokens.get(currPos).getContent()+" ");
			currPos++;
		}
		else {
			throw new Exception("Line:"+tokens.get(currPos).getLine()
					+ " Syntax Error: Invalid relation operation!") ;
		}
	}
	//26
	public void calExpr()throws Exception{
		int curNodeNum = nodeNum-1;
		dotFormat+="<calExpr-"+curNodeNum+">-><term-"+getNext()+">;";
		term();
		dotFormat+="<calExpr-"+curNodeNum+">-><subCalExpr-"+getNext()+">;";
		subCalExpr();
	}
	//27
	public void subCalExpr() throws Exception{
		int curNodeNum = nodeNum-1;
		if(isRelation()||tokens.get(currPos).getCode()==17||tokens.get(currPos).getCode()==22||tokens.get(currPos).getCode()==23||tokens.get(currPos).getCode()==19) {
			return;
		}
		else if(tokens.get(currPos).getCode()==34||tokens.get(currPos).getCode()==40) {
			dotFormat+="<subCalExpr-"+curNodeNum+">-><"+tokens.get(currPos).getContent()+"-"+getNext()+">;";
			System.out.print(tokens.get(currPos).getContent()+" ");
			currPos++;
			dotFormat+="<subCalExpr-"+curNodeNum+">-><term-"+getNext()+">;";
			term();
			dotFormat+="<subCalExpr-"+curNodeNum+">-><subCalExpr-"+getNext()+">;";
			subCalExpr();
		}
		else {
			throw new Exception("Line:"+tokens.get(currPos).getLine()
					+ " Syntax Error: Invalid logic expression!") ;
		}
	}
	//28
	public void term()throws Exception{
		int curNodeNum = nodeNum-1;
		dotFormat+="<term-"+curNodeNum+">-><factor-"+getNext()+">;";
		factor();
		dotFormat+="<term-"+curNodeNum+">-><subTerm-"+getNext()+">;";
		subTerm();
	}
	//29
	public void subTerm()throws Exception{
		int curNodeNum = nodeNum-1;
		if(tokens.get(currPos).getCode()==34||tokens.get(currPos).getCode()==40||
				isRelation()||tokens.get(currPos).getCode()==17||tokens.get(currPos).getCode()==22||tokens.get(currPos).getCode()==23||tokens.get(currPos).getCode()==19) {
			return;
		}
		else if(tokens.get(currPos).getCode()==35||tokens.get(currPos).getCode()==36||tokens.get(currPos).getCode()==37) {
			dotFormat+="<subTerm-"+curNodeNum+">-><"+tokens.get(currPos).getContent()+"-"+getNext()+">;";
			System.out.print(tokens.get(currPos).getContent()+" ");
			currPos++;
			dotFormat+="<term-"+curNodeNum+">-><factor-"+getNext()+">;";
			factor();
			dotFormat+="<term-"+curNodeNum+">-><subTerm-"+getNext()+">;";
			subTerm();
		}
		else {
			throw new Exception("Line:"+tokens.get(currPos).getLine()
					+ " Syntax Error: Invalid expression!") ;
		}
	}
	//30 
	public void factor()throws Exception{
		int curNodeNum = nodeNum-1;
		if(tokens.get(currPos).getCode()==16) {
			dotFormat+="<factor-"+curNodeNum+">-><"+tokens.get(currPos).getContent()+"-"+getNext()+">;";
			System.out.print(tokens.get(currPos).getContent()+" ");
			currPos++;
			dotFormat+="<factor-"+curNodeNum+">-><calExpr-"+getNext()+">;";
			calExpr();
			if(tokens.get(currPos).getCode()==17) {
				dotFormat+="<factor-"+curNodeNum+">-><"+tokens.get(currPos).getContent()+"-"+getNext()+">;";
				System.out.print(tokens.get(currPos).getContent()+" ");
				currPos++;
			}
			else {
				throw new Exception("Line:"+tokens.get(currPos).getLine()
						+ " Syntax Error: Missing \")\"!") ;
			}
		}
		else if(isIdentifier()&&tokens.get(currPos+1).getCode()!=16) {
			dotFormat+="<factor-"+curNodeNum+">-><var-"+getNext()+">;";
			var();
		}
		else if(isIdentifier()) {
			dotFormat+="<factor-"+curNodeNum+">-><call-"+getNext()+">;";
			call();
		}
		else if(isNum()) {		
			dotFormat+="<factor-"+curNodeNum+">-><calExpr-"+getNext()+">;";
			System.out.print(tokens.get(currPos).getContent()+" ");
			currPos++;
		}
		else {
			throw new Exception("Line:"+tokens.get(currPos).getLine()
					+ " Invalid!") ;
		}
	}
	//31
	public void var()throws Exception{
		int curNodeNum = nodeNum-1;
		if(isIdentifier()) {
			dotFormat+="<var-"+curNodeNum+">-><"+tokens.get(currPos).getContent()+"-"+getNext()+">;";
			System.out.print(tokens.get(currPos).getContent()+" ");
			currPos++;
			dotFormat+="<var-"+curNodeNum+">-><subVar-"+getNext()+">;";
			subVar();
		}
		else {
			throw new Exception("Line:"+tokens.get(currPos).getLine()
					+ " Syntax Error:Shoulde be variable!") ; 
		}
	}
	//32
	public void subVar()throws Exception{
		int curNodeNum = nodeNum-1;
		 if(tokens.get(currPos).getCode()==22||isRelation()||tokens.get(currPos).getCode()==17||
				 tokens.get(currPos).getCode()==31||tokens.get(currPos).getCode()==32||tokens.get(currPos).getCode()==34
				 ||tokens.get(currPos).getCode()==35||tokens.get(currPos).getCode()==36
				 ||tokens.get(currPos).getCode()==37||tokens.get(currPos).getCode()==24
				 ||tokens.get(currPos).getCode()==40||tokens.get(currPos).getCode()==41){
			 return;
		 }
		 else if(tokens.get(currPos).getCode()==16) {
				dotFormat+="<subVar-"+curNodeNum+">-><"+tokens.get(currPos).getContent()+"-"+getNext()+">;";
				System.out.print(tokens.get(currPos).getContent()+" ");
				currPos++;
				dotFormat+="<subVar-"+curNodeNum+">-><posInt-"+getNext()+">;";
				posInt();
				if(tokens.get(currPos).getCode()==17) {
					dotFormat+="<subVar-"+curNodeNum+">-><"+tokens.get(currPos).getContent()+"-"+getNext()+">;";
					System.out.print(tokens.get(currPos).getContent()+" ");
					currPos++;
				}
				else {
					throw new Exception("Line:"+tokens.get(currPos).getLine()
							+ " Missing \"]\"!") ; 

				}
		 }
		 else if(tokens.get(currPos).getCode()==20) {
			 dotFormat+="<subVar-"+curNodeNum+">-><"+tokens.get(currPos).getContent()+"-"+getNext()+">;";
			 System.out.print(tokens.get(currPos).getContent()+" ");
			 currPos++;
			 dotFormat+="<subVar-"+curNodeNum+">-><arguList-"+getNext()+">;";
			 arguList();
			 if(tokens.get(currPos).getCode()==21) {
				 dotFormat+="<subVar-"+curNodeNum+">-><"+tokens.get(currPos).getContent()+"-"+getNext()+">;";
				 System.out.print(tokens.get(currPos).getContent()+" ");
				 currPos++;
			 }
			 else {
				 throw new Exception("Line:"+tokens.get(currPos).getLine()
							+ " Missing \")\"!") ;
			 }
		 }
		 else {
			 throw new Exception("Line:"+tokens.get(currPos).getLine()
						+ " Invalid") ;
		 }
	}
	//33
	public void call() throws Exception {
		int curNodeNum = nodeNum - 1;
		if (isIdentifier()) {
			dotFormat += "<call-" + curNodeNum + ">-><" + tokens.get(currPos).getContent() + "-" + getNext() + ">;";
			System.out.print(tokens.get(currPos).getContent() + " ");
			currPos++;
			if (tokens.get(currPos).getCode() == 16) {
				dotFormat += "<call-" + curNodeNum + ">-><" + tokens.get(currPos).getContent() + "-" + getNext() + ">;";
				System.out.print(tokens.get(currPos).getContent() + " ");
				currPos++;
				dotFormat+="<call-"+curNodeNum+">-><arguList-"+getNext()+">;";
				arguList();
				if (tokens.get(currPos).getCode() == 17) {
					dotFormat += "<call-" + curNodeNum + ">-><" + tokens.get(currPos).getContent() + "-" + getNext() + ">;";
					System.out.print(tokens.get(currPos).getContent() + " ");
					currPos++;
				} else {
					throw new Exception("Line:" + tokens.get(currPos).getLine() + " Missing \")\"!");
				}
			} else {
				throw new Exception("Line:" + tokens.get(currPos).getLine() + " Missing parameter list!");
			}

		} else if (tokens.get(currPos).getCode() == -7) {
			dotFormat+="<call-"+curNodeNum+">-><printFunc-"+getNext()+">;";
			printFunc();
		} else {
			throw new Exception("Line:" + tokens.get(currPos).getLine() + " Missing \";\"!");
		}
	}
	//34
	public void arguList()throws Exception{
		int curNodeNum = nodeNum - 1;
		if(tokens.get(currPos).getCode()==17) {
			return;
		}
		else if(tokens.get(currPos).getCode()==16||isIdentifier()||isNum()){
			dotFormat+="<arguList-"+curNodeNum+">-><calExpr-"+getNext()+">;";
			calExpr();
			dotFormat+="<arguList-"+curNodeNum+">-><subArguList-"+getNext()+">;";
			subArguList();
		}
		else {
			throw new Exception("Line:"+tokens.get(currPos).getLine()
					+ " Invalid argument list!") ;
		}
	}
	//35
	public void subArguList()throws Exception{
		int curNodeNum = nodeNum - 1;
		if(tokens.get(currPos).getCode()==17) {
			return;
		}
		else if(tokens.get(currPos).getCode()==23) {
			dotFormat += "<subArguList-" + curNodeNum + ">-><" + tokens.get(currPos).getContent() + "-" + getNext() + ">;";
			System.out.print(tokens.get(currPos).getContent()+" ");
			currPos++;
			dotFormat+="<subArguList-"+curNodeNum+">-><calExpr-"+getNext()+">;";
			calExpr();
			dotFormat+="<subArguList-"+curNodeNum+">-><subArguList-"+getNext()+">;";
			subArguList();
		}
		else {
			throw new Exception("Line:"+tokens.get(currPos).getLine()
					+ " Invalid argument list!") ;
		}
	}
	//36
	public void ifExpr()throws Exception {
		int curNodeNum = nodeNum - 1;
		if(tokens.get(currPos).getCode()==8) {
			dotFormat += "<ifExpr-" + curNodeNum + ">-><" + tokens.get(currPos).getContent() + "-" + getNext() + ">;";
			System.out.print(tokens.get(currPos).getContent()+" ");
			currPos++;
			if(tokens.get(currPos).getCode()==16) {
				dotFormat += "<ifExpr-" + curNodeNum + ">-><" + tokens.get(currPos).getContent() + "-" + getNext() + ">;";
				System.out.print(tokens.get(currPos).getContent()+" ");
				currPos++;
				dotFormat += "<ifExpr-" + curNodeNum + ">-><logic1-" + getNext() + ">;";
				logic1();
				if(tokens.get(currPos).getCode()==17) {
					dotFormat += "<ifExpr-" + curNodeNum + ">-><" + tokens.get(currPos).getContent() + "-" + getNext() + ">;";
					System.out.print(tokens.get(currPos).getContent()+" ");
					currPos++;
					dotFormat += "<ifExpr-" + curNodeNum + ">-><compClaiming-" + getNext() + ">;";
					compClaiming();
					System.out.println();
					dotFormat += "<ifExpr-" + curNodeNum + ">-><elseExpr-" + getNext() + ">;";
					elseExpr();
				}
				else {
					throw new Exception("Line:"+tokens.get(currPos).getLine()
							+ " Missing \")\"!") ;
				}
			}
			else {
				throw new Exception("Line:"+tokens.get(currPos).getLine()
						+ " Missing logic expression!") ;
			}
		}
	}
	//37
	public void elseExpr()throws Exception{
		int curNodeNum = nodeNum - 1;
		if(isIdentifier()||isType()||tokens.get(currPos).getCode()==8||tokens.get(currPos).getCode()==10) {
			return;
		}
		else if(tokens.get(currPos).getCode()==9) {
			dotFormat += "<elseExpr-" + curNodeNum + ">-><" + tokens.get(currPos).getContent() + "-" + getNext() + ">;";
			System.out.print(tokens.get(currPos).getContent()+" ");
			currPos++;
			dotFormat += "<elseExpr-" + curNodeNum + ">-><compClaiming-" + getNext() + ">;";
			compClaiming();
			System.out.println();
		}
	}
	//38
	public void whileExpr()throws Exception {
		int curNodeNum = nodeNum - 1;
		if(tokens.get(currPos).getCode()==10) {
			dotFormat += "<whileExpr-" + curNodeNum + ">-><" + tokens.get(currPos).getContent() + "-" + getNext() + ">;";
			System.out.print(tokens.get(currPos).getContent()+" ");
			currPos++;
			if(tokens.get(currPos).getCode()==16) {
				dotFormat += "<whileExpr-" + curNodeNum + ">-><" + tokens.get(currPos).getContent() + "-" + getNext() + ">;";
				System.out.print(tokens.get(currPos).getContent()+" ");
				currPos++;
				dotFormat += "<whileExpr-" + curNodeNum + ">-><logic1-" + getNext() + ">;";
				logic1();
				if(tokens.get(currPos).getCode()==17) {
					dotFormat += "<whileExpr-" + curNodeNum + ">-><" + tokens.get(currPos).getContent() + "-" + getNext() + ">;";
					System.out.print(tokens.get(currPos).getContent()+" ");
					currPos++;
					dotFormat += "<whileExpr-" + curNodeNum + ">-><compClaiming-" + getNext() + ">;";
					compClaiming();
				}
				else {
					throw new Exception("Line:"+tokens.get(currPos).getLine()
							+ " Missing \")\" !") ;
				}
			}
			else {
				throw new Exception("Line:"+tokens.get(currPos).getLine()
						+ " Missing logic expression!") ;
			}
		}
		else {
			throw new Exception("Line:"+tokens.get(currPos).getLine()
					+ " Invalid expression!") ;
		}
	}
	//39
	public void retClaiming()throws Exception{
		int curNodeNum = nodeNum - 1;
		if(tokens.get(currPos).getCode()==15) {
			dotFormat += "<retClaiming-" + curNodeNum + ">-><" + tokens.get(currPos).getContent() + "-" + getNext() + ">;";
			System.out.print(tokens.get(currPos).getContent()+" ");
			currPos++;
			dotFormat += "<retClaiming-" + curNodeNum + ">-><retSub-" + getNext() + ">;";
			retSub();
		}
		else {
			throw new Exception("Line:"+tokens.get(currPos).getLine()
					+ " Missing return expression!") ;
		}
	}
	//40
	public void retSub()throws Exception{
		int curNodeNum = nodeNum - 1;
		if(tokens.get(currPos).getCode()==22||isBoolean()) {
			dotFormat += "<retSub-" + curNodeNum + ">-><" + tokens.get(currPos).getContent() + "-" + getNext() + ">;";
			System.out.print(tokens.get(currPos).getContent()+" ");
			if(tokens.get(currPos).getCode()==22)
				System.out.println();
			currPos++;
		}
		else if(tokens.get(currPos).getCode()==16||isIdentifier()||isNum()) {
			dotFormat += "<retSub-" + curNodeNum + ">-><calExpr-" + getNext() + ">;";
			calExpr();
			if(tokens.get(currPos).getCode()==22) {
				dotFormat += "<retSub-" + curNodeNum + ">-><" + tokens.get(currPos).getContent() + "-" + getNext() + ">;";
				System.out.print(tokens.get(currPos).getContent()+" ");
				System.out.println();
				currPos++;
			}
			else {
				throw new Exception("Line:"+tokens.get(currPos).getLine()
						+ " Missing \";\" !") ;
			}
		}
		else {
			throw new Exception("Line:"+tokens.get(currPos).getLine()
					+ " Missing \";\" !") ;
		}
	}
	//41
	public void printFunc()throws Exception{
		int curNodeNum = nodeNum - 1;
		if(tokens.get(currPos).getCode()==-7) {
			dotFormat += "<printFunc-" + curNodeNum + ">-><" + tokens.get(currPos).getContent() + "-" + getNext() + ">;";
			System.out.print(tokens.get(currPos).getContent()+" ");
			currPos++;
			if(tokens.get(currPos).getCode()==16) {
				dotFormat += "<printFunc-" + curNodeNum + ">-><" + tokens.get(currPos).getContent() + "-" + getNext() + ">;";
				System.out.print(tokens.get(currPos).getContent()+" ");
				currPos++;
				if(tokens.get(currPos).getCode()==-5||tokens.get(currPos).getCode()==-6||isIdentifier()||isNum()||isBoolean()) {
					dotFormat += "<printFunc-" + curNodeNum + ">-><" + tokens.get(currPos).getContent() + "-" + getNext() + ">;";
					System.out.print(tokens.get(currPos).getContent()+" ");
					currPos++;
					if(tokens.get(currPos).getCode()==17) {
						dotFormat += "<printFunc-" + curNodeNum + ">-><" + tokens.get(currPos).getContent() + "-" + getNext() + ">;";
						System.out.print(tokens.get(currPos).getContent()+" ");
						currPos++;
//						if(tokens.get(currPos).getCode()==22) {
//							dotFormat += "<printFunc-" + curNodeNum + ">-><" + tokens.get(currPos).getContent() + "-" + getNext() + ">;";
//							System.out.print(tokens.get(currPos).getContent()+" ");
//							System.out.println();
//							currPos++;
//						}
//						else {
//							throw new Exception("Line:"+tokens.get(currPos).getLine()
//									+ " Missing \";\" !") ;
//						}
					}
					else {
						throw new Exception("Line:"+tokens.get(currPos).getLine()
								+ " Missing \")\" !") ;
					}
				}
				else {
					throw new Exception("Line:"+tokens.get(currPos).getLine()
							+ " Invalid type to print!") ;
				}
				
			}
			else {
				throw new Exception("Line:"+tokens.get(currPos).getLine()
						+ " \"()\" needed!") ;
			}
			
		}
		else {
			throw new Exception("Line:"+tokens.get(currPos).getLine()
					+ " print needed!") ;
		}
	}
	//42
	public void strExpr() {
		int curNodeNum = nodeNum - 1;
		dotFormat += "<strExpr-" + curNodeNum + ">-><" + tokens.get(currPos).getContent() + "-" + getNext() + ">;";
		System.out.print(tokens.get(currPos).getContent()+" ");
		currPos++;
	}
	//43
	public void charExpr() {
		int curNodeNum = nodeNum - 1;
		dotFormat += "<charExpr-" + curNodeNum + ">-><" + tokens.get(currPos).getContent() + "-" + getNext() + ">;";
		System.out.print(tokens.get(currPos).getContent()+" ");
		currPos++;
	}
	//44
	public void arrayAss()throws Exception{
		int curNodeNum = nodeNum - 1;
		dotFormat += "<arrayAss-" + curNodeNum + ">-><" + tokens.get(currPos).getContent() + "-" + getNext() + ">;";
		System.out.print(tokens.get(currPos).getContent()+" ");
		currPos++;
		dotFormat += "<arrayAss-" + curNodeNum + ">-><simpleExpr-" + getNext() + ">;";
		simpleExpr();
		dotFormat += "<arrayAss-" + curNodeNum + ">-><subSimpleExpr-" + getNext() + ">;";
		subSimpleExpr();
		if(tokens.get(currPos).getCode()==19) {
			dotFormat += "<arrayAss-" + curNodeNum + ">-><" + tokens.get(currPos).getContent() + "-" + getNext() + ">;";
			System.out.print(tokens.get(currPos).getContent()+" ");
			currPos++;
		}
		else {
			throw new Exception("Line:"+tokens.get(currPos).getLine()
					+ " Missing \"}\"!") ;
		}
	}
	public void subSimpleExpr()throws Exception{
		int curNodeNum = nodeNum - 1;
		if(tokens.get(currPos).getCode()==23) {
			dotFormat += "<subSimpleExpr-" + curNodeNum + ">-><" + tokens.get(currPos).getContent() + "-" + getNext() + ">;";
			System.out.print(tokens.get(currPos).getContent()+" ");
			currPos++;
			dotFormat += "<subSimpleExpr-" + curNodeNum + ">-><simpleExpr-" + getNext() + ">;";
			simpleExpr();
			dotFormat += "<subSimpleExpr-" + curNodeNum + ">-><subSimpleExpr-" + getNext() + ">;";
			subSimpleExpr();
			if(tokens.get(currPos).getCode()==19) {
				return;
			}
			else {
				throw new Exception("Line:"+tokens.get(currPos).getLine()
						+ "Missing \"}\" ") ;
			}
		}
		else if(tokens.get(currPos).getCode()==19){
			return;
		}
		else {
			throw new Exception("Line:"+tokens.get(currPos).getLine()
					+ "Invalid expression!") ;
		}
	}
}
