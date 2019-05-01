package main;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class Main {

	public static int createDevice(Connection conn, int height, int width, int cabinetId) {
		// Create new Device
		try {
			Statement m_Statement = conn.createStatement();
			// Check for valid Position in Cabinet
			int[] position = PositioningSystem.findPosition(conn, cabinetId, height, width);
			if (position == null) {
				System.out.println(
						"No fitting Position for device found or process aborted. Device has not been created.");
			} else {
				// Create new device-entry with height and width
				String query = "INSERT INTO DEVICES (HEIGHT, WIDTH, XPOSITION, YPOSITION, CABINETID) VALUES (" + height
						+ ", " + width + ", " + position[1] + ", " + position[0] + ", " + cabinetId + ")";
				m_Statement.executeUpdate(query);
				String query2 = "SELECT MAX(DEVICEID) FROM DEVICES";
				ResultSet m_ResultSet = m_Statement.executeQuery(query2);
				m_ResultSet.next();
				System.out.println("New device created with ID: " + m_ResultSet.getString(1));
				return Integer.parseInt(m_ResultSet.getString(1));
			}
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
		return -1;
	}

	public static int createCabinet(Connection conn, int height, int width) {
		// Create new Cabinet
		if (height < 1 || height > 50 || width < 1 || width > 30) {
			System.out.println("Invalid Cabinet proportions");
		} else {
			try {
				String query = "INSERT INTO CABINETS (HEIGHT, WIDTH) VALUES (" + height + ", " + width + ")";
				Statement m_Statement = conn.createStatement();
				m_Statement.executeUpdate(query);

				String query2 = "SELECT MAX(CABINETID) FROM CABINETS";
				m_Statement = conn.createStatement();
				ResultSet m_ResultSet = m_Statement.executeQuery(query2);
				m_ResultSet.next();
				System.out.println("New Cabinet created with ID " + m_ResultSet.getString(1));
				return Integer.parseInt(m_ResultSet.getString(1));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return -1;
	}

	public static boolean deleteCabinet(Connection conn, int cabinetId) {
		// Delete Cabinet from List
		try {
			//Check if CabinetId exists
			String query = "SELECT * FROM CABINETS WHERE CABINETID = " + cabinetId;
			Statement m_Statement = conn.createStatement();
			ResultSet m_ResultSet = m_Statement.executeQuery(query);
			if(m_ResultSet.next() == false) {
				System.out.println("Cabinet with ID " + cabinetId + " does not exist.");
				return false;
			}
			// Delete all connected Devices first
			query = "DELETE FROM DEVICES WHERE CABINETID = " + cabinetId;
			m_Statement.executeUpdate(query);
			// Delete the Cabinet
			query = "DELETE FROM CABINETS WHERE CABINETID = " + cabinetId;
			m_Statement = conn.createStatement();
			m_Statement.executeUpdate(query);
			System.out.println("Cabinet successfully deleted.");
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	public static boolean deleteDevice(Connection conn, int deviceId) {
		try {
			//Check if device exists
			String query = "SELECT FROM DEVICES WHERE DEVICEID = " + deviceId;
			Statement m_Statement = conn.createStatement();
			ResultSet m_ResultSet = m_Statement.executeQuery(query);
			if(m_ResultSet.next() == false) {
				System.out.println("Device with ID " + deviceId + " does not exist.");
				return false;
			}
			// Remove device from Cabinet
			query = "DELETE FROM DEVICES WHERE DEVICEID = " + deviceId;
			if (m_Statement.executeUpdate(query) >= 1) {
				System.out.println("Error: Multiple Lines affected");
				return false;
			}
			if (m_Statement.executeUpdate(query) == 1) {
				System.out.println("Device successfully deleted.");
				return true;
			}
		} catch (SQLException e) {
			System.out.println("No device with that ID found.");
			return false;
		}
		return false;
	}

	public static boolean getCabinets(Connection conn) {
		// List all Cabinets
		try {
			String query = "SELECT * FROM CABINETS";
			Statement m_Statement = conn.createStatement();
			ResultSet m_ResultSet = m_Statement.executeQuery(query);
			System.out.println(String.format("%s %10s %10s", "ID", "Height", "Width"));
			while (m_ResultSet.next()) {
				System.out.println(String.format("%s %10s %10s", m_ResultSet.getString(1), m_ResultSet.getString(2),
						m_ResultSet.getString(3)));
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public static boolean cabinetDetail(Connection conn, int cabinetId) {
		// Get details of one Cabinet
		try {
			int[][] cabinetSlots = null;
			int cabinetWidth = 0;
			int cabinetHeight = 0;
			String query = "SELECT * FROM CABINETS WHERE CABINETID = " + cabinetId;
			Statement m_Statement = conn.createStatement();
			ResultSet m_ResultSet = m_Statement.executeQuery(query);
			String query2 = "SELECT COUNT(*) FROM DEVICES WHERE CABINETID = " + cabinetId;
			Statement m_Statement2 = conn.createStatement();
			ResultSet m_ResultSet2 = m_Statement2.executeQuery(query2);
			m_ResultSet2.next();
			if (m_ResultSet.next() == false) {
				System.out.println("No Cabinet with this ID found: " + cabinetId);
				return false;
			} else {
				do {
					cabinetWidth = Integer.parseInt(m_ResultSet.getString(3));
					cabinetHeight = Integer.parseInt(m_ResultSet.getString(2));
					cabinetSlots = new int[cabinetHeight][cabinetWidth];
					System.out.println(String.format("%s %10s %10s %10s", "ID", "Height", "Width", "Devices"));
					System.out.println(String.format("%s %10s %10s %10s", m_ResultSet.getString(1),
							m_ResultSet.getString(2), m_ResultSet.getString(3), m_ResultSet2.getString(1)));
				} while (m_ResultSet.next());

				// Get details of installed devices and create diagram of cabinet
				query = "SELECT WIDTH, HEIGHT, XPOSITION, YPOSITION, DEVICEID FROM DEVICES WHERE CABINETID = "
						+ cabinetId;
				m_Statement = conn.createStatement();
				m_ResultSet = m_Statement.executeQuery(query);
				if (m_ResultSet.next() == false) {
					System.out.println("No connected devices.");
				} else {
					System.out.println("Connected Devices:");
					System.out.println(
							String.format("%s %10s %10s %10s %5s", "Width", "Height", "XPosition", "YPosition", "ID"));
					do {
						System.out.println(String.format("%s %10s %10s %10s %10s", m_ResultSet.getString(1),
								m_ResultSet.getString(2), m_ResultSet.getString(3), m_ResultSet.getString(4),
								m_ResultSet.getString(5)));
						int existingdeviceHeight = Integer.parseInt(m_ResultSet.getString(2));
						int existingdeviceWidth = Integer.parseInt(m_ResultSet.getString(1));
						int existingdeviceId = Integer.parseInt(m_ResultSet.getString(5));
						int deviceXPosition = Integer.parseInt(m_ResultSet.getString(3));
						int deviceYPosition = Integer.parseInt(m_ResultSet.getString(4));
						for (int i = 0; i < existingdeviceHeight; i++) {
							for (int j = 0; j < existingdeviceWidth; j++) {
								cabinetSlots[deviceYPosition + i][deviceXPosition + j] = existingdeviceId;
							}
						}
					} while (m_ResultSet.next());
					
					// Print current diagramm of devices
					System.out.println("Current positions of devices:");
					for (int row = 0; row < cabinetHeight; row++) {
						for (int column = 0; column < cabinetWidth; column++) {
							System.out.printf("%4d", cabinetSlots[row][column]);
						}
						System.out.println();
					}
				}
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public static void showFunctions() {
		System.out.println("Functions:");
		System.out.println("1. Create new Cabinet");
		System.out.println("2. Create new Device");
		System.out.println("3. Delete Cabinet");
		System.out.println("4. Delete Device");
		System.out.println("5. List all Cabinets");
		System.out.println("6. Detailed View of one Cabinet");
		System.out.println("7. Exit");
	}

	public static void main(String[] args) throws Exception {
		try {
			Class.forName("org.h2.Driver");

			Connection conn = DriverManager.getConnection(
					"jdbc:h2:D:\\Dokumente\\Bewerbungen\\Bewerbungen Job1\\FNT\\Projekt\\Database", "sa", "admin");

			int width;
			int height;
			Scanner reader = new Scanner(System.in); // Reading from System.in
			showFunctions();

			while (true) {
				int n = reader.nextInt();
				// Scans the next token of the input as an int.
				// once finished
				switch (n) {
				case 1:
					System.out.println("Creating new Cabinet");
					System.out.println("Insert Height");
					height = reader.nextInt();
					System.out.println("Insert Width");
					width = reader.nextInt();
					createCabinet(conn, height, width);
					break;
				case 2:
					System.out.println("Creating new Device");
					System.out.println("Insert Height");
					height = reader.nextInt();
					System.out.println("Insert Width");
					width = reader.nextInt();
					
					System.out.println("Insert CabinetId");
					int cabinetId = reader.nextInt();
					System.out.println("Creating device...");
					createDevice(conn, height, width, cabinetId);
					break;
				case 3:
					System.out.println("Enter Id of Cabinet you wish to delete:");
					cabinetId = reader.nextInt();
					deleteCabinet(conn, cabinetId);
					break;
				case 4:
					System.out.println("Enter Id of Device you wish to delete:");
					int deviceId = reader.nextInt();
					deleteDevice(conn, deviceId);
					break;
				case 5:
					System.out.println("Listing all Cabinets:");
					getCabinets(conn);
					break;
				case 6:
					System.out.println("Enter CabinetId");
					cabinetId = reader.nextInt();
					cabinetDetail(conn, cabinetId);
					break;
				case 7:
					// Exit-Function
					System.out.println("Exiting...");
					reader.close();
					conn.close();
					System.exit(0);
				default:
					System.out.println("Please choose a correct number.");
				}
				showFunctions();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
