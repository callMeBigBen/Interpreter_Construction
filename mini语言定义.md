# Mini

Mini语言支持：

- 算术和逻辑表达式
- 基本的控制结构（顺序、分支、循环）
- 基本变量声明（int, double,  boolean)
- 数组
- 自定义函数
- 语句以分号结尾

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

array data = call make_array ( 0 );
call array_add ( data , 10 );
call array_add ( data , 4 );
call array_add ( data , 5 );
call array_add ( data , -5 );
call array_add ( data , 1 );
call array_add ( data , 2 );
call array_add ( data , 3 );

call sort ( data );

int sz = call array_size ( data )
int i = 0
while (i < sz)
{
    print(data [ i ]);
    i = i + 1;
}

Output :
-5
1
2
3
4
5
10
```

This example demonstrates the implementation of sorting an array by a simple selection method. The input array is passed by reference, var keyword is used to declare a variable. return keyword is used to break function execution and return a value from function. The following built-in functions are defined to work with arrays in the language:

- make_array ( size ) - creates an array with a specified size
- array_size ( array ) - returns array size
- array_add ( array , value ) - adds an element to the array
- array_copy ( array ) - copies an array
- array_free ( array ) - frees the memory allocated for the array

Despite its simplicity, the language is turing complete, you can find other examples in examples folder, including an example of the linked list implementation.