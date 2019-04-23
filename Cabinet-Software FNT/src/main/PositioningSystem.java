package main;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;

public class PositioningSystem {

	public static int[] findPosition(Connection conn, int cabinetId, int deviceId, int newdeviceHeight,
			int newdeviceWidth) {

		try {
			int cabinetWidth;
			int cabinetHeight;
			int deviceXPosition;
			int deviceYPosition;

			// Get Height and Width of Cabinet
			Statement m_Statement;
			m_Statement = conn.createStatement();
			String query = "SELECT HEIGHT, WIDTH FROM CABINETS WHERE CABINETID = " + cabinetId;
			ResultSet m_ResultSet = m_Statement.executeQuery(query);
			m_ResultSet.next();
			cabinetHeight = Integer.parseInt(m_ResultSet.getString(1));
			cabinetWidth = Integer.parseInt(m_ResultSet.getString(2));

			int[][] cabinetSlots = new int[cabinetHeight][cabinetWidth];
			for (int row[] : cabinetSlots)
				Arrays.fill(row, 0);

			// Get Height, Width and position of all devices in the Cabinet
			m_Statement = conn.createStatement();
			query = "SELECT XPOSITION, YPOSITION, HEIGHT, WIDTH, DEVICEID FROM DEVICES WHERE CABINETID = " + cabinetId;
			m_ResultSet = m_Statement.executeQuery(query);
			while (m_ResultSet.next()) {
				int existingdeviceHeight = Integer.parseInt(m_ResultSet.getString(3));
				int existingdeviceWidth = Integer.parseInt(m_ResultSet.getString(4));
				int existingdeviceId = Integer.parseInt(m_ResultSet.getString(5));
				deviceXPosition = Integer.parseInt(m_ResultSet.getString(1));
				deviceYPosition = Integer.parseInt(m_ResultSet.getString(2));
				cabinetSlots[deviceYPosition][deviceXPosition] = deviceId;
				for (int i = 0; i < existingdeviceHeight; i++) {
					for (int j = 0; j < existingdeviceWidth; j++) {
						cabinetSlots[deviceYPosition + i][deviceXPosition + j] = existingdeviceId;
					}
				}
			}
			// Print current layout of cabinet
			for (int row = 0; row < cabinetHeight; row++) {
				for (int column = 0; column < cabinetWidth; column++) {
					System.out.printf("%4d", cabinetSlots[row][column]);
				}
				System.out.println();
			}

			// Find free Position for new device
			for (int i = 0; i <= cabinetHeight - newdeviceHeight; i++) {
				for (int j = 0; j <= cabinetWidth - newdeviceWidth; j++) {
					if (cabinetSlots[i][j] == 0) {
						// Check if device fits into found slot
						int k = 0, l = 0;
						boolean flag = true;
						for (k = i; k < i + newdeviceHeight && flag; k++) {
							for (l = j; l < j + newdeviceWidth && flag; l++) {
								if (cabinetSlots[k][l] != 0)
									flag = false;
							}
						}
						if (flag == true) {
							// Device fits into this slot
							// Return position parameters
							int[] position = { i, j };
							System.out.println("Position found: {" + i + "}, {" + j + "}");
							return position;
						}
					}
				}
			}
			// No valid Position has been found
			return null;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
}
