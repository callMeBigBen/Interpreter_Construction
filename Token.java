package syntax;

public class Token {
	String content ;
	int type;
	
	Token(){
		
	}
	
	public void put(String _token,int _type){
		content = _token;
		type = _type;
	}
}
