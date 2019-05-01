package main;

import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class Tests {
	final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
	final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
	final PrintStream originalOut = System.out;
	final PrintStream originalErr = System.err;
	int newCabinetId;
	int newDeviceId;

	@Before
	public void setUp() throws Exception {

	}

	@After
	public void tearDown() throws Exception {
		System.setOut(originalOut);
		System.setErr(originalErr);
	}

	@Test
	public void A_CreateWorkingCabinetTest() {
		try {
			Class.forName("org.h2.Driver");

			Connection conn = DriverManager.getConnection(
					"jdbc:h2:D:\\Dokumente\\Bewerbungen\\Bewerbungen Job1\\FNT\\Projekt\\Database", "sa", "admin");
			newCabinetId = Main.createCabinet(conn, 50, 30);
			String query = "SELECT MAX(CABINETID) FROM CABINETS";
			Statement m_Statement = conn.createStatement();
			ResultSet m_ResultSet = m_Statement.executeQuery(query);
			m_ResultSet.next();
			assertEquals(newCabinetId, Integer.parseInt(m_ResultSet.getString(1)));
			conn.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	@Test
	public void B_CreateInvalidCabinetWidthTest() {
		try {
			Class.forName("org.h2.Driver");

			Connection conn = DriverManager.getConnection(
					"jdbc:h2:D:\\Dokumente\\Bewerbungen\\Bewerbungen Job1\\FNT\\Projekt\\Database", "sa", "admin");
			assertEquals(Main.createCabinet(conn, 50, -1), -1);
			conn.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	@Test
	public void C_CreateInvalidCabinetHeightTest() {
		try {
			Class.forName("org.h2.Driver");

			Connection conn = DriverManager.getConnection(
					"jdbc:h2:D:\\Dokumente\\Bewerbungen\\Bewerbungen Job1\\FNT\\Projekt\\Database", "sa", "admin");
			assertEquals(Main.createCabinet(conn, -1, 30), -1);
			conn.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	@Test
	public void D_CreateWorkingDeviceTest() {
		try {
			Class.forName("org.h2.Driver");
			Connection conn = DriverManager.getConnection(
					"jdbc:h2:D:\\Dokumente\\Bewerbungen\\Bewerbungen Job1\\FNT\\Projekt\\Database", "sa", "admin");
			newDeviceId = Main.createDevice(conn, 30, 30, newCabinetId);
			String query = "SELECT MAX(DEVICEID) FROM DEVICES";
			Statement m_Statement = conn.createStatement();
			ResultSet m_ResultSet = m_Statement.executeQuery(query);
			m_ResultSet.next();
			assertEquals(newDeviceId, Integer.parseInt(m_ResultSet.getString(1)));
			conn.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	@Test
	public void E_InvalidDeviceWidthTest() {
		try {
			Class.forName("org.h2.Driver");
			Connection conn = DriverManager.getConnection(
					"jdbc:h2:D:\\Dokumente\\Bewerbungen\\Bewerbungen Job1\\FNT\\Projekt\\Database", "sa", "admin");
			assertEquals(Main.createDevice(conn, 30, -1, newCabinetId), -1);
			conn.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	@Test
	public void F_InvalidDeviceHeightTest() {
		try {
			Class.forName("org.h2.Driver");
			Connection conn = DriverManager.getConnection(
					"jdbc:h2:D:\\Dokumente\\Bewerbungen\\Bewerbungen Job1\\FNT\\Projekt\\Database", "sa", "admin");
			assertEquals(Main.createDevice(conn, -1, 30, newCabinetId), -1);
			conn.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	@Test
	public void G_ListExistingCabinetsTest() {
		try {
			Class.forName("org.h2.Driver");

			Connection conn = DriverManager.getConnection(
					"jdbc:h2:D:\\Dokumente\\Bewerbungen\\Bewerbungen Job1\\FNT\\Projekt\\Database", "sa", "admin");
			assertEquals(Main.getCabinets(conn), true);
			conn.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	@Test
	public void H_DeleteInvalidCabinet() {
		try {
			Class.forName("org.h2.Driver");

			Connection conn = DriverManager.getConnection(
					"jdbc:h2:D:\\Dokumente\\Bewerbungen\\Bewerbungen Job1\\FNT\\Projekt\\Database", "sa", "admin");
			assertEquals(Main.deleteCabinet(conn, -1), false);
			conn.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	@Test
	public void I_DeleteInvalidDevice() {
		try {
			Class.forName("org.h2.Driver");

			Connection conn = DriverManager.getConnection(
					"jdbc:h2:D:\\Dokumente\\Bewerbungen\\Bewerbungen Job1\\FNT\\Projekt\\Database", "sa", "admin");
			assertEquals(Main.deleteDevice(conn, -1), false);
			conn.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	@Test
	public void I_DeleteValidDevice() {
		try {
			Class.forName("org.h2.Driver");

			Connection conn = DriverManager.getConnection(
					"jdbc:h2:D:\\Dokumente\\Bewerbungen\\Bewerbungen Job1\\FNT\\Projekt\\Database", "sa", "admin");
			assertEquals(Main.deleteDevice(conn, newDeviceId), true);
			conn.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	@Test
	public void I_DeleteValidCabinet() {
		try {
			Class.forName("org.h2.Driver");

			Connection conn = DriverManager.getConnection(
					"jdbc:h2:D:\\Dokumente\\Bewerbungen\\Bewerbungen Job1\\FNT\\Projekt\\Database", "sa", "admin");
			assertEquals(Main.deleteDevice(conn, newCabinetId), true);
			conn.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

}
