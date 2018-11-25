# Mini

## Mini语言支持：

- 算术和逻辑表达式
- 基本的控制结构（顺序、分支、循环）
- 基本数据类型（int, double,boolean,char,string)
- 数组
- 自定义函数
- 语句以分号结尾 ;


## 词汇表：（ 一符一码 ）
         
        EOF,
        ERROR,
        IDENTIFIER(Tokens.Token.Tag.NAMED),

        BOOLEAN("boolean", Tokens.Token.Tag.NAMED),
        CHAR("char", Tokens.Token.Tag.NAMED)
        INT("int", Tokens.Token.Tag.NAMED), 
        DOUBLE("double", Tokens.Token.Tag.NAMED),
        STRING(“string”,Tokens.Token.Tag.NAMED),

        INTLITERAL(Tokens.Token.Tag.NUMERIC),
        DOUBLELITERAL(Tokens.Token.Tag.NUMERIC),
        CHARLITERAL(Tokens.Token.Tag.NUMERIC),
        STRINGLITERAL(Tokens.Token.Tag.STRING),

        TRUE("true", Tokens.Token.Tag.NAMED),
        FALSE("false", Tokens.Token.Tag.NAMED),

        IF("if"),
        ELSE("else"),

        WHILE("while"),
        BREAK("break"),
        CONTINUE("continue"),
        
        RETURN("return"),
     
        LPAREN("("),
        RPAREN(")"),
        LBRACE("{"),
        RBRACE("}"),
        LBRACKET("["),
        RBRACKET("]"),
        SEMI(";"),
        COMMA(","),
        
        
        EQ("="),
        GT(">"),
        LT("<"),
        
        EQEQ("=="),
        LTEQ("<="),
        GTEQ(">="),
        BANGEQ("!="),
        
        AMPAMP("&&"),
        BARBAR("||"),
        BANG("!"),

        PLUS("+"),
        SUB("-"),
        STAR("*"),
        SLASH("/"),
        PERCENT("%"),
       

## 词法规则形式化定义：（ 正规式 ）
 
1. <程序> → <声明列表> 

2. <声明列表> → <声明><声明列表次>

3.<声明列表次> → <声明><声明列表次>|空

4. <声明> → <变量声明> | <函数声明>

5. <变量声明> → <类型分类符> IDENTIFIER <变量声明子式>

6. <变量声明子式> →  ';' |'[' <数字> ']' ';' 

6. <数字> → 0 | 1 | 2 | 3 | 4 | 5 | 6 | 7 | 8 | 9 |...

7. <类型分类符> → "int" | "double" | "boolean"|”char”|”string”

8. <函数声明> → <类型分类符> IDENTIFIER '(' <参数列表> ')' <复合声明>       

9. <参数列表> →  空|<参数><参数列表次>

10.<参数列表次> → ',' <参数><参数列表次> | 空

11. <参数> → <类型分类符> IDENTIFIER | <类型分类符> IDENTIFIER '[' ']'

12. <复合声明> → '{' <声明列表> | <表达式声明>| <if语句声明> | <while语句声明> '}'

13. <表达式声明> → <表达式> ';' | ';'    

14. <表达式> → <变量> "=" <逻辑式1>

15. <逻辑式1> → "!"<逻辑式1> | <逻辑式2>     

16. <逻辑式2> → <逻辑式3><逻辑式2次>

17.<逻辑式2次> → "||" <逻辑式3><逻辑式2次>|空

18. <逻辑式3> →<简单式><逻辑式3次>

19.<逻辑式3次> → "&&"<简单式><逻辑式3次>|空

20. <简单式> → <算术式> <关系操作> <算术式>|True|False 

21. <关系操作> → "<=" | '<' | '>' | ">=" | "==" | "!="

22. <算术式> → <term><算术式次>

23.<算术式次> → <'+' | '-'> <term><算术式次>|空 

24. <term> → <因子><term次>

25. <term次> → < '*' | '/' | '%'> <因子> <term次> | 空

26. <因子> → '(' <算术式> ')' | <变量> | <调用> | <数字> 

27. <变量> → IDENTIFIER<变量子式>

28. <变量子式> → '[' <数字> ']' |空

28. <调用> → IDENTIFIER '(' <形式参数列表> ')' ';

29. <形式参数列表> → 空|<算术式><形式参数列表次>

30. <形式参数列表次> → ","<算术式><形式参数列表次>|空

31. <if语句声明> → "if" '(' <逻辑式> ')' <复合声明> <else语句>

32. <else语句> → "else"<复合声明>|空

32. <while语句声明> → "while" '(' <逻辑式> ')' <复合声明>

33. <break语句声明> → "break" ';'

34. <continue语句声明> → "continue" ';' 

35. <return语句声明> → "return" <return子句>

36. <return子句> → ";"|<算术式>";"

37. <显示函数> → "print" '(' `"` <字符串> `"` ')' ';'

Example 1:

```
int gcd ( m , n )
{
	while  (m != n )
	{
		if (m > n) 
			{ m = m - n ;}
		else
			{ n = n - m; }
	}
	return m;
}
print(call gcd ( 100 , 40 ));

Output: 20
```

Example 2:

```
void sort ( a )
{
	int sz = call array_size ( a );
	if (sz < 2)
	{
		return 0;
	}
	int i = 0
	int j = 0
	int t = 0
	while (i < sz)
	{
		j = i + 1;
		while (j < sz)
		{
			if (a [ j ] < a [ i ])
			{
				t = a [ i ];
				a [ i ] = a [ j ];
				a [ j ] = t;
			}
			j = j + 1;
		}		
		i = i + 1;
	}
}
