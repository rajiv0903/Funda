package course02._04._02_stack_implementation.practice;



public class ElementPractice<T> {

	private T data;
    private ElementPractice<T> next;
    
    public ElementPractice(T data, ElementPractice<T> next) {
        this.data = data;
        this.next = next;
    }
    
	public T getData() {
		return data;
	}
	public void setData(T data) {
		this.data = data;
	}
	public ElementPractice<T> getNext() {
		return next;
	}
	public void setNext(ElementPractice<T> next) {
		this.next = next;
	}
	
	@Override
	public String toString() {
		return "ElementPractice [data=" + data + "]";
	}
    
	
	
    
    
}
