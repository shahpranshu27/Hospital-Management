package hospitalManagementSystem;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class HospitalManagementSystem {
	private static final String URL_STRING = "jdbc:mysql://localhost:3306/hospital";
	private static final String USERNAME_STRING = "root";
	private static final String PASSWORD_STRING = "Rays@123";
	
	
	public static void main(String[] args) throws SQLException, IOException {
		Scanner scanner = new Scanner(System.in);
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		try {
			Connection conn = DriverManager.getConnection(URL_STRING, USERNAME_STRING, PASSWORD_STRING);
			
			Patient patient = new Patient(conn, scanner);
			Doctor doctor = new Doctor(conn);
			
			while(true) {
				System.out.println("Hospital Management System");
				System.out.println("1. Add patient");
				System.out.println("2. View patient");
				System.out.println("3. View doctors");
				System.out.println("4. Book Appointment");
				System.out.println("5. View appointments");
				System.out.println("6. Exit");
				System.out.println("Enter your choice: ");
				int choice = scanner.nextInt();
				switch (choice) {
				case 1: {
					// add patient
					patient.addPatient();
					System.out.println();
					break;
				}
				case 2:{
					// view patient
					patient.viewPatients();
					System.out.println();
					break;
				}
				case 3:{
					// view doctors
					doctor.viewDoctors();
					System.out.println();
					break;
				}
				case 4:{
					// book appointment
					bookAppointment(patient, doctor, conn, scanner);
					break;
				}
				case 5:
					// view appointments
					viewAppointment(conn);
					break;
				case 6:
					break;
				default:
					System.out.println("Enter valid choice!!");
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void bookAppointment(Patient patient, Doctor doctor, Connection connection, Scanner scanner) {
		System.out.println("Enter patient id: ");
		int patientId = scanner.nextInt();
		
		System.out.println("Enter doctor's id: ");
		int  doctorId = scanner.nextInt();
		
		System.out.println("Enter appointment date (yyyy-mm-dd) : ");
		String appointmentDate = scanner.next();
		
		if(patient.getPatientById(patientId) && doctor.getDoctorById(doctorId)) {
			if(checkDoctorAvailability(doctorId, appointmentDate, connection)) {
				String appointment = "insert into appointment(patient_id, doctor_id, appointment_date) values(?,?,?)";
				try {
					PreparedStatement ps = connection.prepareStatement(appointment);
					ps.setInt(1, patientId);
					ps.setInt(2, doctorId);
					ps.setString(3, appointmentDate);
					
					int rows=ps.executeUpdate();
					if(rows>0) {
						System.out.println("Appointment booked!");
					}
					else {
						System.out.println("Failed to book an appointment!");
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			else {
				System.out.println("Doctor is not available on this particular date!");
			}
		}
		else {
			System.out.println("Either doctor or patient doesn't exist!");
		}
		
	}

	public static boolean checkDoctorAvailability(int doctorId, String appointmentDate, Connection connection) {
		String query = "select count(*) from appointment where doctor_id=? and appointment_date=?";
		try {
			
			PreparedStatement ps = connection.prepareStatement(query);
			ps.setInt(1, doctorId);
			ps.setString(2, appointmentDate);
			
			ResultSet rs = ps.executeQuery();
			if(rs.next()) {
				int rows = rs.getInt(1);
				if(rows==0) {
					return true;
				}
				else {
					return false;
				}
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public static void viewAppointment(Connection connection) {
		String query = "select * from appointment";
		
		try {
			PreparedStatement ps = connection.prepareStatement(query);
			ResultSet rs = ps.executeQuery();
			System.out.println("Appointments : ");
			System.out.println("+----+------------------------+-----------+------------------+");
			System.out.println("| Id | Patient Id             | Doctor Id | Appointment Date |");
			System.out.println("+----+------------------------+-----------+------------------+");
			while(rs.next()) {
				int id = rs.getInt("id");
				int patient_id = rs.getInt("patient_id");
				int doctor_id = rs.getInt("doctor_id");
				String appointment_date = rs.getString("appointment_date");
				System.out.printf("|%-4s|%-24s|%-11s|%-18s|\n",id, patient_id, doctor_id, appointment_date);
			}
			System.out.println("+----+------------------------+-----------+------------------+");
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
