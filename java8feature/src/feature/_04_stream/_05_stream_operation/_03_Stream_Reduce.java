package feature._04_stream._05_stream_operation;

import java.time.LocalDate;
import java.time.Month;
import java.util.Arrays;
import java.util.List;

public class _03_Stream_Reduce {

	public static void main(String[] args) {

		double sum = Employee.persons()
		        .stream()
		        .map(Employee::getIncome)
		        .reduce(0.0, Double::sum);
		    System.out.println(sum);
	}

}

class Employee2 {
	public static enum Gender {
		MALE, FEMALE
	}

	private long id;
	private String name;
	private Gender gender;
	private LocalDate dob;
	private double income;

	public Employee2(long id, String name, Gender gender, LocalDate dob,
			double income) {
		this.id = id;
		this.name = name;
		this.gender = gender;
		this.dob = dob;
		this.income = income;
	}

	public double getIncome() {
		return income;
	}

	public static List<Employee2> persons() {
		Employee2 p1 = new Employee2(1, "Jake", Gender.MALE, LocalDate.of(1971,
				Month.JANUARY, 1), 2343.0);
		Employee2 p2 = new Employee2(2, "Jack", Gender.MALE, LocalDate.of(1972,
				Month.JULY, 21), 7100.0);
		Employee2 p3 = new Employee2(3, "Jane", Gender.FEMALE, LocalDate.of(1973,
				Month.MAY, 29), 5455.0);
		Employee2 p4 = new Employee2(4, "Jode", Gender.MALE, LocalDate.of(1974,
				Month.OCTOBER, 16), 1800.0);
		Employee2 p5 = new Employee2(5, "Jeny", Gender.FEMALE, LocalDate.of(1975,
				Month.DECEMBER, 13), 1234.0);
		Employee2 p6 = new Employee2(6, "Jason", Gender.MALE, LocalDate.of(1976,
				Month.JUNE, 9), 3211.0);

		List<Employee2> persons = Arrays.asList(p1, p2, p3, p4, p5, p6);

		return persons;
	}
}