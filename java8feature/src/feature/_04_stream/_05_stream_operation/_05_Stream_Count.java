package feature._04_stream._05_stream_operation;

import java.time.LocalDate;
import java.time.Month;
import java.util.Arrays;
import java.util.List;

public class _05_Stream_Count {

	public static void main(String[] args) {
		
		long personCount = Employee.persons().stream().count();
	    System.out.println("Person count: " + personCount);
	}
}



class Employee4 {
	public static enum Gender {
		MALE, FEMALE
	}

	private long id;
	private String name;
	private Gender gender;
	private LocalDate dob;
	private double income;

	public Employee4(long id, String name, Gender gender, LocalDate dob,
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

	public static List<Employee4> persons() {
		Employee4 p1 = new Employee4(1, "Jake", Gender.MALE, LocalDate.of(1971,
				Month.JANUARY, 1), 2343.0);
		Employee4 p2 = new Employee4(2, "Jack", Gender.MALE, LocalDate.of(1972,
				Month.JULY, 21), 7100.0);
		Employee4 p3 = new Employee4(3, "Jane", Gender.FEMALE, LocalDate.of(
				1973, Month.MAY, 29), 5455.0);
		Employee4 p4 = new Employee4(4, "Jode", Gender.MALE, LocalDate.of(1974,
				Month.OCTOBER, 16), 1800.0);
		Employee4 p5 = new Employee4(5, "Jeny", Gender.FEMALE, LocalDate.of(
				1975, Month.DECEMBER, 13), 1234.0);
		Employee4 p6 = new Employee4(6, "Jason", Gender.MALE, LocalDate.of(
				1976, Month.JUNE, 9), 3211.0);

		List<Employee4> persons = Arrays.asList(p1, p2, p3, p4, p5, p6);

		return persons;
	}
}