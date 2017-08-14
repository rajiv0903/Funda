package feature._04_stream._05_stream_operation;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class _06_Stream_Collectors {
	
	/**
	 * <R> R collect(Supplier<R> supplier, BiConsumer<R,? super T> accumulator, BiConsumer<R,R> combiner)
	 * <R,A> R collect(Collector<?  super T,A,R> collector)
	 * 
	 * A supplier that supplies a mutable container to store the results.
	 * An accumulator that accumulates the results into the mutable container.
	 * A combiner that combines the partial results when used in parallel.
	 */
	public static void main(String[] args) {
		List<String> names = Employee5.persons()
		        .stream()
		        .map(Employee5::getName)
		        .collect(ArrayList::new,  ArrayList::add, ArrayList::addAll);
		    System.out.println(names);
	}

}

class Employee5 {
	public static enum Gender {
		MALE, FEMALE
	}

	private long id;
	private String name;
	private Gender gender;
	private LocalDate dob;
	private double income;

	public Employee5(long id, String name, Gender gender, LocalDate dob,
			double income) {
		this.id = id;
		this.name = name;
		this.gender = gender;
		this.dob = dob;
		this.income = income;
	}

	public String getName() {
		return name;
	}

	public static List<Employee5> persons() {
		Employee5 p1 = new Employee5(1, "Jake", Gender.MALE, LocalDate.of(1971,
				Month.JANUARY, 1), 2343.0);
		Employee5 p2 = new Employee5(2, "Jack", Gender.MALE, LocalDate.of(1972,
				Month.JULY, 21), 7100.0);
		Employee5 p3 = new Employee5(3, "Jane", Gender.FEMALE, LocalDate.of(
				1973, Month.MAY, 29), 5455.0);
		Employee5 p4 = new Employee5(4, "Jode", Gender.MALE, LocalDate.of(1974,
				Month.OCTOBER, 16), 1800.0);
		Employee5 p5 = new Employee5(5, "Jeny", Gender.FEMALE, LocalDate.of(
				1975, Month.DECEMBER, 13), 1234.0);
		Employee5 p6 = new Employee5(6, "Jason", Gender.MALE, LocalDate.of(
				1976, Month.JUNE, 9), 3211.0);

		List<Employee5> persons = Arrays.asList(p1, p2, p3, p4, p5, p6);

		return persons;
	}
}
