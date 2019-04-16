package main;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class Main {

	public static void createdevice(Connection conn, int height, int width, int cabinetID) {
		// Create new Device
		String query = "INSERT INTO DEVICES (HEIGHT, WIDTH, CABINETID) VALUES (" + height + ", " + width + ", "
				+ cabinetID + ")";
		try {
			Statement m_Statement = conn.createStatement();
			m_Statement.executeUpdate(query);

		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			String query2 = "SELECT MAX DEVICEID FROM DEVICES";
			Statement m_Statement = conn.createStatement();
			ResultSet m_ResultSet = m_Statement.executeQuery(query2);
			System.out.println("New device created with ID:" + m_ResultSet.getString(1));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void createcabinet(Connection conn, int height, int width) {
		// Create new Cabinet
		String query = "INSERT INTO CABINETS (HEIGHT, WIDTH, NUMBEROFDEVICES) VALUES (" + height + ", " + width + ", "
				+ 0 + ")";
		try {
			Statement m_Statement = conn.createStatement();
			m_Statement.executeUpdate(query);
		} catch (Exception e1) {
			e1.printStackTrace();
		}

		try {
			String query2 = "SELECT MAX(CABINETID) FROM CABINETS";
			Statement m_Statement = conn.createStatement();
			ResultSet m_ResultSet = m_Statement.executeQuery(query2);
			System.out.println("New Cabinet created with ID" + m_ResultSet.getString(1));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void deleteCabinet(Connection conn, int cabinetId) {
		// Delete Cabinet from List
		// Delete all connected Devices first, then delete Cabinet
		try {
			String query = "DELETE FROM DEVICES WHERE CABINETID = " + cabinetId;
			Statement m_Statement = conn.createStatement();
			m_Statement.executeUpdate(query);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		try {
			String query = "DELETE FROM CABINETS WHERE CABINETID = " + cabinetId;
			Statement m_Statement = conn.createStatement();
			m_Statement.executeUpdate(query);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void adddevice(Connection conn, int deviceId, int cabinetId) {
		// Add device to Cabinet
		
	}

	public static void removedevice(Connection conn, int deviceId) {
		// Remove device from Cabinet

	}

	public static void getcabinets(Connection conn) {
		// List all Cabinets
		String query = "SELECT * FROM CABINETS";
		try {
			Statement m_Statement = conn.createStatement();
			ResultSet m_ResultSet = m_Statement.executeQuery(query);
			System.out.println("Width \t Height \t Number of devices \t ID");
			while (m_ResultSet.next()) {
				System.out.println(m_ResultSet.getString(1) + "\t" + m_ResultSet.getString(2) + "\t\t\t"
						+ m_ResultSet.getString(3) + "\t\t" + m_ResultSet.getString(4));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void CabinetDetail(Connection conn) {
		// Get details of one Cabinet
	}

	public static void showFunctions() {
		System.out.println("Functions:");
		System.out.println("1. Create new Cabinet");
		System.out.println("2. Create new Device");
		System.out.println("3. Add device to Cabinet");
		System.out.println("4. Remove device from Cabinet");
		System.out.println("5. List all Cabinets");
		System.out.println("6. Delete Cabinet");
		System.out.println("7. Detailed View of one Cabinet");
	}

	public static void main(String[] args) throws Exception {
		Class.forName("org.h2.Driver");

		Connection conn = DriverManager.getConnection(
				"jdbc:h2:D:\\Dokumente\\Bewerbungen\\Bewerbungen Job1\\FNT\\Projekt\\Database", "sa", "admin");

		int width;
		int height;
		Scanner reader = new Scanner(System.in); // Reading from System.in
		showFunctions();

		while (true) {
			int n = reader.nextInt(); // Scans the next token of the input as an int.
			// once finished
			switch (n) {
			case 1:
				System.out.println("Creating new Cabinet");
				System.out.println("Insert Height");
				height = reader.nextInt();
				while (height <= 0 || height > 50) {
					System.out.println("Height must be between 1 and 50");
					height = reader.nextInt();
				}
				System.out.println("Insert Width");
				width = reader.nextInt();
				while (width <= 0 || width > 30) {
					System.out.println("Width must be between 1 and 30");
					width = reader.nextInt();
				}
				createcabinet(conn, height, width);
				break;
			case 2:
				System.out.println("Creating new Device");
				System.out.println("Insert Height");
				height = reader.nextInt();
				while (height <= 0 || height > 30) {
					System.out.println("Height must be between 1 and 30");
					height = reader.nextInt();
				}
				System.out.println("Insert Width");
				width = reader.nextInt();
				while (width <= 0 || width > 30) {
					System.out.println("Width must be between 1 and 30");
					width = reader.nextInt();
				}
				System.out.println("Insert CabinetID to insert device");
				int cabinetId = reader.nextInt();
				createdevice(conn, height, width, cabinetId);
				break;
			case 3:
				break;
			case 4:
				break;
			case 5:
				getcabinets(conn);
				break;
			case 6:
				break;
			case 7:
				break;
			case 8:
				// Exit-Function
				reader.close();
				conn.close();
				break;
			default:
				System.out.println("Please choose a correct number.");
			}
			showFunctions();
		}
	}
}
