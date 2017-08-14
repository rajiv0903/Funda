package feature._02_interface._01_default_static;

public class MyDataImpl implements MyData {

	/**
	 * Note that isNull(String str) is a simple class method, it’s not overriding the 
	 * interface method. For example, if we will add @Override annotation to the isNull() method, 
	 * it will result in compiler error.
	 */
	public boolean isNull(String str) 
	{
		System.out.println("Impl Null Check");
		return str == null ? true : false;
	}
	
	public static void main(String args[])
	{
		/**
		 * Java interface static method is visible to interface methods only, 
		 * if we remove the isNull() method from the MyDataImpl class, 
		 * we won’t be able to use it for the MyDataImpl object. However like other static methods, 
		 * we can use interface static methods using class name. For example, a valid statement will be
		 * boolean result = MyData.isNull("abc");
		 */
		MyDataImpl obj = new MyDataImpl();
		obj.print("");
		obj.isNull("abc");
	}
}
