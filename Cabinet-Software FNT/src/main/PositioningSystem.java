package main;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;

public class PositioningSystem {

	public static int[] findPosition(Connection conn, int cabinetId, int deviceId, int newdeviceHeight, int newdeviceWidth) {

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

			int[][] cabinetSlots = new int[cabinetWidth][cabinetHeight];
			for (int row[] : cabinetSlots)
				Arrays.fill(row, 0);

			// Get Height, Width and position of all devices in the Cabinet
			m_Statement = conn.createStatement();
			query = "SELECT XPOSITION, YPOSITION, HEIGHT, WIDTH FROM DEVICES WHERE CABINETID = " + cabinetId;
			m_ResultSet = m_Statement.executeQuery(query);
			while (m_ResultSet.next()) {
				int existingdeviceHeight = Integer.parseInt(m_ResultSet.getString(3));
				int existingdeviceWidth = Integer.parseInt(m_ResultSet.getString(4));
				deviceXPosition = Integer.parseInt(m_ResultSet.getString(1));
				deviceYPosition = Integer.parseInt(m_ResultSet.getString(2));
				cabinetSlots[deviceXPosition][deviceYPosition] = deviceId;
				for (int i = 0; i < existingdeviceHeight; i++) {
					for (int j = 0; j < existingdeviceWidth; j++) {
						cabinetSlots[deviceYPosition + i][deviceXPosition + j] = deviceId;
					}
				}
			}
			for (int row = 0; row < cabinetSlots.length; row++) {
				for (int column = 0; column < cabinetSlots[row].length; column++) {
					for (int col = 0; col < cabinetSlots[row].length; col++) {
						System.out.printf("%4d", cabinetSlots[row][col]);
					}
					System.out.println();
				}
			}
			// Add new device to a free position in cabinet

			// Find free Position
			for (int i = 0; i < cabinetHeight - newdeviceHeight; i++) {
				for (int j = 0; j < cabinetWidth - newdeviceWidth; j++) {
					if (cabinetSlots[i][j] == 0) {
						// Check if device fits into found slot
						int k = 0, l = 0;
						boolean flag = true;
						for (k = 0; k < i + newdeviceHeight && flag; k++) {
							for (l = 0; l < j + newdeviceWidth && flag; l++) {
								if (cabinetSlots[k][l] != 0)
									flag = false;
							}
						}
						if (flag == true) {
							// Device fits into this slot
							// Return position parameters
							int[] position = { i, j };
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
