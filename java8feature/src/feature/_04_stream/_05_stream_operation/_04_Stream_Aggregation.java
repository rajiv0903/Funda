package feature._04_stream._05_stream_operation;

import java.time.LocalDate;
import java.time.Month;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class _04_Stream_Aggregation {

	public static void main(String[] args) {
		
		Optional<Employee> person = Employee.persons().stream()
		        .max(Comparator.comparingDouble(Employee::getIncome));

	    if (person.isPresent()) {
	      System.out.println("Highest earner: " + person.get());
	    } else {
	      System.out.println("Could not  get   the   highest earner.");
	    }

	}

}

class Employee3 {
	public static enum Gender {
		MALE, FEMALE
	}

	private long id;
	private String name;
	private Gender gender;
	private LocalDate dob;
	private double income;

	public Employee3(long id, String name, Gender gender, LocalDate dob,
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

	public static List<Employee3> persons() {
		Employee3 p1 = new Employee3(1, "Jake", Gender.MALE, LocalDate.of(1971,
				Month.JANUARY, 1), 2343.0);
		Employee3 p2 = new Employee3(2, "Jack", Gender.MALE, LocalDate.of(1972,
				Month.JULY, 21), 7100.0);
		Employee3 p3 = new Employee3(3, "Jane", Gender.FEMALE, LocalDate.of(
				1973, Month.MAY, 29), 5455.0);
		Employee3 p4 = new Employee3(4, "Jode", Gender.MALE, LocalDate.of(1974,
				Month.OCTOBER, 16), 1800.0);
		Employee3 p5 = new Employee3(5, "Jeny", Gender.FEMALE, LocalDate.of(
				1975, Month.DECEMBER, 13), 1234.0);
		Employee3 p6 = new Employee3(6, "Jason", Gender.MALE, LocalDate.of(
				1976, Month.JUNE, 9), 3211.0);

		List<Employee3> persons = Arrays.asList(p1, p2, p3, p4, p5, p6);

		return persons;
	}
}