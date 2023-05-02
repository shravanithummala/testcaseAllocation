package testcaseAllocation;

import java.io.File;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

public class TestcasesListsGenerator {

	private final String databaseUsername = "postgres";
	private final String databasePassword = "Root";
	private final String databaseURL = "jdbc:postgresql://localhost/testcasesinfo";
	HashMap<Integer, ArrayList<Integer>> testcasesHM = new HashMap<Integer, ArrayList<Integer>>();
	Connection conn;

	private Connection connectToDB() {

		conn = null;
		try {
			conn = DriverManager.getConnection(databaseURL, databaseUsername, databasePassword);

			if (conn != null) {
				System.out.println("Connected to the PostgreSQL server successfully.");
			} else {
				System.out.println("Failed to make connection!");
			}

		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return conn;
	}

	private void retrieveTestcases() {

		int testcaseID = 0;
		int testcaseAvgTime = 0;
		Statement stmt = null;

		try {

			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM public.testcases");

			while (rs.next()) {

				testcaseID = rs.getInt("testcaseid");
				testcaseAvgTime = rs.getInt("testcaseavgtime");

				if (testcasesHM.get(testcaseAvgTime) == null) { // gets the value for an id

					testcasesHM.put(testcaseAvgTime, new ArrayList<Integer>()); // no ArrayList assigned, create new

					testcasesHM.get(testcaseAvgTime).add(testcaseID); // adds value to list.

				} else {
					testcasesHM.get(testcaseAvgTime).add(testcaseID);
				}
			}

			rs.close();
			stmt.close();
			conn.close();

		} catch (Exception e) {

			System.err.println(e.getClass().getName() + ": " + e.getMessage());

			System.exit(0);
		}
	}

	public void generateTestcasesLists(int vmCount, int vmTimeAllocated[]) {

		connectToDB();
		retrieveTestcases();

		// generating test-cases lists for virtual machines

		int vmTime = 0; // totaltimeofVM will always have time in minutes

		String path = System.getProperty("user.home") + "\\Desktop\\";

		String folderPath = path + java.time.LocalDate.now();

		File f1 = new File(folderPath);
		f1.mkdir();

		PrintWriter writer = null;

		for (int i = 0; i < vmCount; i++) {

			vmTime = vmTimeAllocated[i];
			int temptime = vmTime;

			// creating a file for VM
			String filename = folderPath + "\\" + (i + 1) + ".txt";
			try {
				File vmFile = new File(filename);
				vmFile.createNewFile();
				writer = new PrintWriter(filename);
			} catch (Exception e) {
				System.err.println("following exception occured while creating a file" + e.getMessage());
			}

			while (vmTime != 0 && !(testcasesHM.isEmpty())) {

				// checking whether there is any test-case with execution time equal to VM
				// allocated time,if there are no such test-cases then we reduce time by one
				// minute and check again

				if (testcasesHM.containsKey(temptime)) {

					writer.println(testcasesHM.get(temptime).get(0));
					writer.println();
					testcasesHM.get(temptime).remove(0);

					if (testcasesHM.get(temptime).isEmpty()) {
						testcasesHM.remove(temptime);
					}
					vmTime = vmTime - temptime;
					temptime = vmTime;

				} else {
					temptime--;
				}
			}
			writer.close();
		}
		generateLogs();
	}

	private void generateLogs() {

		String path = System.getProperty("user.home") + "\\Desktop\\";
		String folderPath = path + java.time.LocalDate.now();

		FileHandler handler;
		try {
			handler = new FileHandler(folderPath + "\\" + "run.log", true);
			Logger logger = Logger.getLogger("com.javacodegeeks.snippets.core");
			logger.addHandler(handler);
			logger.info("Program executed sucessfully, testcases lists for each VM is generated");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
