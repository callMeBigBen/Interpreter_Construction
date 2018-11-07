package syntax;

public class Token {
	String content ;
	int type;
	int line;
	Token(){
		
	}
	
	public void put(String _token,int _type,int _line){
		content = _token;
		type = _type;
		line = _line;
	}
}
