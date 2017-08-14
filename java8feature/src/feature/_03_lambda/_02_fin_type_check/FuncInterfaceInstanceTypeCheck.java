package feature._03_lambda._02_fin_type_check;

public class FuncInterfaceInstanceTypeCheck {

	 public static void main(String[] argv) {
		 
		 Processor stringProcessor  = s -> s.length();
		 
		 SecondProcessor  secondProcessor  = s -> s.length();
		 
		 //compiler errror
		// stringProcessor = secondProcessor;
		 
		 
		 String name = "Java Lambda";
		 int length = stringProcessor.getStringLength(name);
	     System.out.println(length);
	     
	     System.out.println(secondProcessor.noName(name));
	 }
}
