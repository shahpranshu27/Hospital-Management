package hospitalManagementSystem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Patient {
	private Connection connection;
	private Scanner scanner;
	public Patient(Connection connection, Scanner scanner) {
//		super();
		this.connection = connection;
		this.scanner = scanner;
	}
	
	public void addPatient() {
		System.out.print("Enter patient name: ");
		String name = scanner.next();
		
		System.out.println("Enter patient age: ");
		int age = scanner.nextInt();
		
		System.out.println("Enter patient gender: ");
		String gender = scanner.next();
		
		try {
			String query = "insert into patients(name, age, gender) values (?,?,?)";
			PreparedStatement ps = connection.prepareStatement(query);
			ps.setString(1, name);
			ps.setInt(2, age);
			ps.setString(3, gender);
			
			int rows = ps.executeUpdate();
			if(rows>0) {
				System.out.println("Patient added successfully!");
			}
			else {
				System.out.println("Failed to add patient!");
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void viewPatients() {
		String query = "select * from patients";
		
		try {
			
			PreparedStatement ps = connection.prepareStatement(query);
			ResultSet rs = ps.executeQuery();
			System.out.println("Patients : ");
			System.out.println("+------------+--------------------+----------+------------+");
			System.out.println("| Patient Id | Name               | Age      | Gender     |");
			System.out.println("+------------+--------------------+----------+------------+");
			while(rs.next()) {
				int id = rs.getInt("id");
				String name = rs.getString("name");
				int age = rs.getInt("age");
				String gender = rs.getString("gender");
				System.out.printf("|%-12s|%-20s|%-10s|%-12s|\n",id, name, age, gender);
			}
			System.out.println("+------------+--------------------+----------+------------+");
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public boolean getPatientById(int id) {
		String query = "select * from patients where id=?";
		try {
			
			PreparedStatement ps = connection.prepareStatement(query);
			ps.setInt(1, id);
			
			ResultSet rs = ps.executeQuery();
			if(rs.next()) {
				return true;
			}
			else {
				return false;
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	
}
