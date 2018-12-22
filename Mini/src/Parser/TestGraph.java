package Parser;

import java.io.File;

public class TestGraph {
	
	public static void main(String[] args) {
		String content = "A->B;B->C;B->D;";
		createDotGraph(content, "test2");
	}
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
}
