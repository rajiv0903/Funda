package customObjectSerach;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;

public class FastBookFinder {

	/**
	 * @param args
	 */
	static List<Book> myList = new ArrayList<Book>();
	
	public static void main(String[] args) {
		
		List<String> labels = Arrays.asList(new String[] {"fiction", "drama", ", sci-fi"});
		
		Book b1 = new Book("Micro Biology", "Life", labels);
		Book b2 = new Book("Macro Biology", "Life", labels);
		Book b3 = new Book("Bio Science", "Life", labels);
		Book b4 = new Book("Mathematics", "Equation", labels);
		Book b5 = new Book("College Algebra", "", labels);
		Book b6 = new Book("Science Fiction", "", labels);
		
		myList.add(b1);
		myList.add(b2);
		myList.add(b3);
		myList.add(b4);
		myList.add(b5);
		myList.add(b6);

	    System.out.println("...findTitle...");
	    Collection<Book> booksByTile = findTitle("th");
	    for (Book b: booksByTile )
	    {
	    	System.out.println(b.getTitle());
	    }
	    
	    System.out.println("...findLabel...");
	    List<Book> booksByLabels = findLabel("fiction");
	    for (Book b: booksByLabels )
	    {
	    	System.out.println(b.getTitle());
	    }
	    
	    System.out.println("...findAny...");
	    List<Book> booksByContents = findAny("fiction");
	    for (Book b: booksByContents )
	    {
	    	System.out.println(b.getTitle());
	    }
	}

	static public Collection<Book> findTitle(String titleParm)
	{
		return   Collections2.filter(myList, titleContains(titleParm));
	}
	
	static public List<Book> findLabel(String labelParm)
	{
		List<Book> retList = new ArrayList<Book>();
		for (Book b: myList )
		{
			for (String lbl: b.getLabels())
			{
				if(lbl.equals(labelParm))
				{
					retList.add(b);
					break;
				}
			}
		}
		return retList;
	}
	
	static public List<Book> findAny(String anystringParm)
	{
		List<Book> retList = new ArrayList<Book>();
		for (Book b: myList )
		{
			if(b.getTitle().toLowerCase().contains(anystringParm.toLowerCase()))
			{
				retList.add(b);
			}
			else if (b.getDescription().toLowerCase().contains(anystringParm.toLowerCase()))
			{
				retList.add(b);
			}
			else
			{
				for (String lbl: b.getLabels())
				{
					if(lbl.equals(anystringParm))
					{
						retList.add(b);
						break;
					}
				}
			}
		}
		return retList;
	}
	
	
	static Predicate<Book> titleContains(final String name) {
	    return new Predicate<Book>() {
	        public boolean apply(Book book) {
	            return book.getTitle().toLowerCase().contains(name.toLowerCase());
	        }
	    };
	}

}
