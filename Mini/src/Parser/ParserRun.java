package Parser;
import java.util.ArrayList;
import java.util.*;
import Lexer.*;
import javafx.scene.shape.Circle;

public class ParserRun {
	static ArrayList<Token> tokens ;
	//static Iterator<Token> iterator = tokens.iterator();
	static int currPos = 0;
	static int branchNum=0;
	
	public void start(ArrayList<Token> ts) {
		tokens = ts;
		Token EOF = new Token();
		EOF.setCode(-8);
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
			claimingList();
		}
		else {
			throw new Exception("Line:"+tokens.get(currPos).getLine()
					+"  Program should start with claiming!");
		}
	}
	//2.<声明列表> → <声明><声明列表次>
	public void claimingList() throws Exception{
			claiming();
			claimingList2();
	}
	//3
	public void claimingList2() throws Exception{
		if(tokens.get(currPos).getCode()==-8) {
			System.out.println("This is the end of parser");
		}
		else if(isType()) {
			claiming();
			claimingList2();
		}
		else if(isIdentifier()&&tokens.get(currPos+1).getCode()==24) {
			exprClaiming();
			claimingList2();
		}
		else if(isIdentifier()||tokens.get(currPos).getCode()==-7) {
			call();
			claimingList2();
		}
		else {
			throw new Exception("Line:"+tokens.get(currPos).getLine()
					+ " Syntax Error!");
		}
	}
	//4
	
	public void claiming() throws Exception{
		if(isType()) {
			type();
			if(isIdentifier()) {
				System.out.print(tokens.get(currPos).getContent()+" ");
				currPos++;
				subClaiming();
			}
		}
		else if(isIdentifier()) {
			call();
		}
		else {
			throw new Exception("Line:"+tokens.get(currPos).getLine()
					+ " Syntax Error!");
		}
	}
	//5
	public void subClaiming() throws Exception{
		if(tokens.get(currPos).getCode()==22||tokens.get(currPos).getCode()==20||tokens.get(currPos).getCode()==24) {
			subVarClaiming();
		}
		else if(tokens.get(currPos).getCode()==16) {
			subFuncClaiming();
		}
		else {
			throw new Exception("Line:"+tokens.get(currPos).getLine()
					+ " Syntax Error!");
		}
	}
	//6
	public void subVarClaiming() throws Exception{
		if(tokens.get(currPos).getCode()==22) {
			System.out.print(tokens.get(currPos).getContent()+" ");
			System.out.println();
			currPos++;
		}
		else if(tokens.get(currPos).getCode()==20){
			System.out.print(tokens.get(currPos).getContent()+" ");
			currPos++;
			posInt();
			if(tokens.get(currPos).getCode()==21) {
				System.out.print(tokens.get(currPos).getContent()+" ");
				currPos++;
				if(tokens.get(currPos).getCode()==22) {
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
			System.out.print(tokens.get(currPos).getContent()+" ");
			currPos++;
			logic1();
			if(tokens.get(currPos).getCode()==22) {
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
		if(tokens.get(currPos).getCode()==-3) {
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
		if(tokens.get(currPos).getCode()==-4) {
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
		if(tokens.get(currPos).getCode()==-2) {
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
		if(isType()) {
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
		System.out.print(tokens.get(currPos).getContent()+" ");
		currPos++;
		paraList();
		if(tokens.get(currPos).getCode()==17) {
			System.out.print(tokens.get(currPos).getContent()+" ");
			currPos++;
			compClaiming();
		}
		else {
			throw new Exception("Line:"+tokens.get(currPos).getLine()
					+ " Syntax Error:\")\" needed!");
		}
	}
	//12
	public void paraList() throws Exception{
		if(isType()) {
			param();
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
		if(tokens.get(currPos).getCode()==23) {
			System.out.print(tokens.get(currPos).getContent()+" ");
			currPos++;
			param();
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
		type();
		if(isIdentifier()) {
			if(tokens.get(currPos).getCode()==20) {
				System.out.print(tokens.get(currPos).getContent()+" ");
				currPos++;
				if(tokens.get(currPos).getCode()==21) {
					System.out.print(tokens.get(currPos).getContent()+" ");
					currPos++;
				}
				else {
					throw new Exception("Line:"+tokens.get(currPos).getLine()								+ " Syntax Error:Missing \"]\" !");
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
		if(tokens.get(currPos).getCode()==18) {
			System.out.print(tokens.get(currPos).getContent()+" ");
			currPos++;
			funcMultiClaiming();
			if(tokens.get(currPos).getCode()==19) {
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
		if(isType()){
			claimingList();
			retClaiming();
		}
		else if(isIdentifier()) {
			exprClaiming();
			multiClaiming2();
			retClaiming();
		}
		else if(tokens.get(currPos).getCode()==8) {
			ifExpr();
			multiClaiming2();
			retClaiming();
		}
		else if(tokens.get(currPos).getCode()==10) {
			whileExpr();
			multiClaiming2();
			retClaiming();
		}
		else {
			throw new Exception("Line:"+tokens.get(currPos).getLine()
					+ " Syntax Error: Invalid sentences") ;
		}
	}
	//15
	public void compClaiming() throws Exception{
		if(tokens.get(currPos).getCode()==18) {
			System.out.print(tokens.get(currPos).getContent()+" ");
			currPos++;
			multiClaiming();
			if(tokens.get(currPos).getCode()==19) {
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
		if(isType()){
			claimingList();
		}
		else if(isIdentifier()) {
			exprClaiming();
			multiClaiming2();
		}
		else if(tokens.get(currPos).getCode()==8) {
			ifExpr();
			multiClaiming2();
		}
		else if(tokens.get(currPos).getCode()==10) {
			whileExpr();
			multiClaiming2();
		}
		else {
			throw new Exception("Line:"+tokens.get(currPos).getLine()
					+ " Syntax Error: Invalid sentences") ;
		}
	}
	//16-2
	public void multiClaiming2()throws Exception{
		if(tokens.get(currPos).getCode()==19) {
			return;
		}
		else if(isIdentifier()) {
			exprClaiming();
			multiClaiming2();
		}
		else if(tokens.get(currPos).getCode()==8) {
			ifExpr();
			multiClaiming2();
		}
		else if(tokens.get(currPos).getCode()==10) {
			whileExpr();
			multiClaiming2();
		}
		else {
			throw new Exception("Line:"+tokens.get(currPos).getLine()
					+ " Syntax Error: Invalid sentences") ;
		}
	}
	//17
	public void exprClaiming() throws Exception{
		if(tokens.get(currPos).getCode()==22) {
			System.out.print(tokens.get(currPos).getContent()+" ");
			System.out.println();
			currPos++;
		}
		else if(isIdentifier()) {
			expr();
			if(tokens.get(currPos).getCode()==22) {
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
		var();
		if(tokens.get(currPos).getCode()==24) {
			System.out.print(tokens.get(currPos).getContent()+" ");
			currPos++;
			logic1();
		}
		else {
			throw new Exception("Line:"+tokens.get(currPos).getLine()
					+ " Syntax Error: Expected \"=\" !") ;
		}
	}
	//19
	public void logic1() throws Exception{
		if(tokens.get(currPos).getCode()==33) {
			System.out.print(tokens.get(currPos).getContent()+" ");
			currPos++;
			logic1();
		}
		else if(tokens.get(currPos).getCode()==16||isIdentifier()||isNum()||isBoolean()||tokens.get(currPos).getCode()==-5||tokens.get(currPos).getCode()==-6){
			logic2();
		}
		else {
			throw new Exception("Line:"+tokens.get(currPos).getLine()
					+ " Syntax Error: Invalid logic expression!") ;
		}
	}
	//20
	public void logic2() throws Exception{
		logic3();
		logic2sub();
	}
	//21
	public void logic2sub() throws Exception{
		if(tokens.get(currPos).getCode()==22) {
			return;
		}
		else if(tokens.get(currPos).getCode()==32||tokens.get(currPos).getCode()==31){
			System.out.print(tokens.get(currPos).getContent()+" ");
			currPos++;
			logic3();
			logic2sub();
		}
		else {
			throw new Exception("Line:"+tokens.get(currPos).getLine()
					+ " Syntax Error: Invalid logic expression!") ;
		}
	}
	//22
	public void logic3()throws Exception{
		simpleExpr();
		logic3sub();
	}
	//23
	public void logic3sub()throws Exception{
		if(tokens.get(currPos).getCode()==22||tokens.get(currPos).getCode()==31||tokens.get(currPos).getCode()==32||tokens.get(currPos).getCode()==33) {
			return;
		}
		else if(tokens.get(currPos).getCode()==31){
			System.out.print(tokens.get(currPos).getContent()+" ");
			currPos++;
			simpleExpr();
			logic3sub();
		}
		else {
			throw new Exception("Line:"+tokens.get(currPos).getLine()
					+ " Syntax Error: Invalid logic expression!") ;
		}
	}
	//24
	public void simpleExpr()throws Exception{
		if(tokens.get(currPos).getCode()==-5) {
			charExpr();
		}
		else if(tokens.get(currPos).getCode()==-6) {
			strExpr();
		}
		else if(isBoolean()) {
			System.out.print(tokens.get(currPos).getContent()+" ");
			currPos++;
		}
		else if((tokens.get(currPos).getCode()==16||isIdentifier()||isNum())&&(tokens.get(currPos+1).getCode()==17||tokens.get(currPos+1).getCode()==22)) {
			calExpr();
		}
		else if(tokens.get(currPos).getCode()==16||isIdentifier()||isNum()) {
			calExpr();
			relationOp();
			calExpr();
		}
		else {
			throw new Exception("Line:"+tokens.get(currPos).getLine()
					+ " Syntax Error: Invalid logic expression!") ;
		}
	}
	//25
	public void relationOp()throws Exception{
		if(isRelation()){
			System.out.print(tokens.get(currPos).getContent()+" ");
			currPos++;
		}
		else {
			throw new Exception("Line:"+tokens.get(currPos).getLine()
					+ " Syntax Error: Invalid relagtion operation!") ;
		}
	}
	//26
	public void calExpr()throws Exception{
		term();
		subCalExpr();
	}
	//27
	public void subCalExpr() throws Exception{
		if(isRelation()||tokens.get(currPos).getCode()==17||tokens.get(currPos).getCode()==22) {
			return;
		}
		else if(tokens.get(currPos).getCode()==34||tokens.get(currPos).getCode()==40) {
			System.out.print(tokens.get(currPos).getContent()+" ");
			currPos++;
			term();
			subCalExpr();
		}
		else {
			throw new Exception("Line:"+tokens.get(currPos).getLine()
					+ " Syntax Error: Invalid logic expression!") ;
		}
	}
	//28
	public void term()throws Exception{
		factor();
		subTerm();
	}
	//29
	public void subTerm()throws Exception{
		if(tokens.get(currPos).getCode()==34||tokens.get(currPos).getCode()==40||
				isRelation()||tokens.get(currPos).getCode()==17||tokens.get(currPos).getCode()==22) {
			return;
		}
		else if(tokens.get(currPos).getCode()==35||tokens.get(currPos).getCode()==36||tokens.get(currPos).getCode()==37) {
			System.out.print(tokens.get(currPos).getContent()+" ");
			currPos++;
			factor();
			subTerm();
		}
		else {
			throw new Exception("Line:"+tokens.get(currPos).getLine()
					+ " Syntax Error: Invalid expression!") ;
		}
	}
	//30 
	public void factor()throws Exception{
		if(tokens.get(currPos).getCode()==16) {
			System.out.print(tokens.get(currPos).getContent()+" ");
			currPos++;
			calExpr();
			if(tokens.get(currPos).getCode()==17) {
				System.out.print(tokens.get(currPos).getContent()+" ");
				currPos++;
			}
			else {
				throw new Exception("Line:"+tokens.get(currPos).getLine()
						+ " Syntax Error: Missing \")\"!") ;
			}
		}
		else if(isIdentifier()) {
			var();
		}
		else if(isNum()) {
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
		if(isIdentifier()) {
			System.out.print(tokens.get(currPos).getContent()+" ");
			currPos++;
			subVar();
		}
		else {
			throw new Exception("Line:"+tokens.get(currPos).getLine()
					+ " Syntax Error:Shoulde be variable!") ; 
		}
	}
	//32
	public void subVar()throws Exception{
		 if(tokens.get(currPos).getCode()==22||isRelation()||tokens.get(currPos).getCode()==17||
				 tokens.get(currPos).getCode()==31||tokens.get(currPos).getCode()==32||tokens.get(currPos).getCode()==34
				 ||tokens.get(currPos).getCode()==35||tokens.get(currPos).getCode()==36
				 ||tokens.get(currPos).getCode()==37||tokens.get(currPos).getCode()==24) {
			 return;
		 }
		 else if(tokens.get(currPos).getCode()==16) {
			 System.out.print(tokens.get(currPos).getContent()+" ");
				currPos++;
				posInt();
				if(tokens.get(currPos).getCode()==17) {
					System.out.print(tokens.get(currPos).getContent()+" ");
					currPos++;
				}
				else {
					throw new Exception("Line:"+tokens.get(currPos).getLine()
							+ " Missing \"]\"!") ; 

				}
		 }
		 else if(tokens.get(currPos).getCode()==20) {
			 System.out.print(tokens.get(currPos).getContent()+" ");
			 currPos++;
			 arguList();
			 if(tokens.get(currPos).getCode()==21) {
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
	public void call() throws Exception{
		if(isIdentifier()) {
			System.out.print(tokens.get(currPos).getContent()+" ");
			 currPos++;
			 if(tokens.get(currPos).getCode()==16) {
				 System.out.print(tokens.get(currPos).getContent()+" ");
				 currPos++;
				 arguList();
				 if(tokens.get(currPos).getCode()==17) {
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
							+ " Missing parameter list!") ;
			 }

		}
		else if(tokens.get(currPos).getCode()==-7) {
			 printFunc();
		}
		else {
			 throw new Exception("Line:"+tokens.get(currPos).getLine()
						+ " Missing \";\"!") ;
		}
	}
	//34
	public void arguList()throws Exception{
		if(tokens.get(currPos).getCode()==17) {
			return;
		}
		else if(tokens.get(currPos).getCode()==16||isIdentifier()||isNum()){
			calExpr();
			subArguList();
		}
		else {
			throw new Exception("Line:"+tokens.get(currPos).getLine()
					+ " Invalid argument list!") ;
		}
	}
	//35
	public void subArguList()throws Exception{
		if(tokens.get(currPos).getCode()==17) {
			return;
		}
		else if(tokens.get(currPos).getCode()==23) {
			System.out.print(tokens.get(currPos).getContent()+" ");
			 currPos++;
			 calExpr();
			 subArguList();
		}
		else {
			throw new Exception("Line:"+tokens.get(currPos).getLine()
					+ " Invalid argument list!") ;
		}
	}
	//36
	public void ifExpr()throws Exception {
		if(tokens.get(currPos).getCode()==8) {
			System.out.print(tokens.get(currPos).getContent()+" ");
			currPos++;
			if(tokens.get(currPos).getCode()==16) {
				System.out.print(tokens.get(currPos).getContent()+" ");
				currPos++;
				logic1();
				if(tokens.get(currPos).getCode()==17) {
					compClaiming();
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
		if(isIdentifier()||isType()||tokens.get(currPos).getCode()==8||tokens.get(currPos).getCode()==10) {
			return;
		}
		else if(tokens.get(currPos).getCode()==9) {
			System.out.print(tokens.get(currPos).getContent()+" ");
			currPos++;
			compClaiming();
		}
	}
	//38
	public void whileExpr()throws Exception {
		if(tokens.get(currPos).getCode()==10) {
			System.out.print(tokens.get(currPos).getContent()+" ");
			currPos++;
			if(tokens.get(currPos).getCode()==16) {
				System.out.print(tokens.get(currPos).getContent()+" ");
				currPos++;
				logic1();
				if(tokens.get(currPos).getCode()==17) {
					System.out.print(tokens.get(currPos).getContent()+" ");
					currPos++;
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
		if(tokens.get(currPos).getCode()==15) {
			System.out.print(tokens.get(currPos).getContent()+" ");
			currPos++;
			retSub();
		}
		else {
			throw new Exception("Line:"+tokens.get(currPos).getLine()
					+ " Missing return expression!") ;
		}
	}
	//40
	public void retSub()throws Exception{
		if(tokens.get(currPos).getCode()==22||isBoolean()) {
			System.out.print(tokens.get(currPos).getContent()+" ");
			if(tokens.get(currPos).getCode()==22)
				System.out.println();
			currPos++;
		}
		else if(tokens.get(currPos).getCode()==16||isIdentifier()||isNum()) {
			calExpr();
			if(tokens.get(currPos).getCode()==22) {
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
		if(tokens.get(currPos).getCode()==-7) {
			System.out.print(tokens.get(currPos).getContent()+" ");
			currPos++;
			if(tokens.get(currPos).getCode()==16) {
				System.out.print(tokens.get(currPos).getContent()+" ");
				currPos++;
				if(tokens.get(currPos).getCode()==-5||tokens.get(currPos).getCode()==-6||isIdentifier()||isNum()||isBoolean()) {
					System.out.print(tokens.get(currPos).getContent()+" ");
					currPos++;
					if(tokens.get(currPos).getCode()==17) {
						System.out.print(tokens.get(currPos).getContent()+" ");
						currPos++;
						if(tokens.get(currPos).getCode()==22) {
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
		System.out.print(tokens.get(currPos).getContent()+" ");
		currPos++;
	}
	//43
	public void charExpr() {
		System.out.print(tokens.get(currPos).getContent()+" ");
		currPos++;
	}
}
