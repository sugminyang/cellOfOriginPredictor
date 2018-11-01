package test;

import org.rosuda.JRI.REXP;
import org.rosuda.JRI.Rengine;

public class Test {

	public static void main(String[] args) {
		Rengine re = new Rengine(new String[] {"--vanilla"},false,null);

		re.eval("a <- 10");
		REXP x = re.eval("a");
		System.out.println(x);

		re.eval("try(source('/Users/dean/Downloads/test.r'))");
		
		System.exit(1);
	}

}
