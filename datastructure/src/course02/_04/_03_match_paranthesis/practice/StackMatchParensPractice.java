package course02._04._03_match_paranthesis.practice;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;



public class StackMatchParensPractice {
	
	private static final Map<Character, Character> matchingParenMap = new HashMap<>();
    private static final Set<Character> openingParenSet = new HashSet<>();

    static {
        matchingParenMap.put(')', '(');
        matchingParenMap.put(']', '[');
        matchingParenMap.put('}', '{');
        openingParenSet.addAll(matchingParenMap.values());
    }
    
	public static void main(String[] args) {
		
		 System.out.println(String.format(
	                "Has matching parens %s? : %s", "(abcd)", hasMatchingParens("(abcd)")));
	        System.out.println(String.format(
	                "Has matching parens %s? : %s", "{{{{}}", hasMatchingParens("{{{{}}")));
	        System.out.println(String.format(
	                "Has matching parens %s? : %s", "{{{{}}})", hasMatchingParens("{{{{}}})")));
	        System.out.println(String.format(
	                "Has matching parens %s? : %s", "{{{}}}()", hasMatchingParens("{{{}}}()")));
	        System.out.println(String.format(
	                "Has matching parens %s? : %s", "{{{}}]()", hasMatchingParens("{{{}}]()")));
	        System.out.println(String.format(
	                "Has matching parens %s? : %s", "{{}}([]){}{}{}{}{[[[[]]]]}",
	                hasMatchingParens("{{}}([]){}{}{}{}{[[[[]]]]}")));
	}

	 public static boolean hasMatchingParens(String input) {

		try 
		{
			StackUsingLinkedListPractice<Character> stack = new StackUsingLinkedListPractice<Character>(40);

			for (int i = 0; i < input.length(); i++) 
			{
				char ch = input.charAt(i);
				
				if (openingParenSet.contains(ch)) {
					stack.push(ch);
				}
				if (matchingParenMap.containsKey(ch)) 
				{
					Character lastcharacter = stack.pop();
					if(lastcharacter != matchingParenMap.get(ch)){
                        return false;
                    }
				}
			}
			
			return stack.isEmpty();
		} 
		catch (StackUsingLinkedListPractice.StackOverflowException soe) {
			System.err.println("Stack Overflow");
		} 
		catch (StackUsingLinkedListPractice.StackUnderflowException sue) {
			System.err.println("Stack Underflow");
		}

		return false;
	 }
}
