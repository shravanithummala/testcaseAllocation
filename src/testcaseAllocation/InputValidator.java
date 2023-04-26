package testcaseAllocation;

import java.util.Scanner;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

public class InputValidator {

	int vmCount;
	int vmTimeAllocated[];

	public static void main(String[] args) {

		InputValidator inputValidator = new InputValidator();

		// Using Scanner for Getting Input from User
		Scanner in = new Scanner(System.in);

		inputValidator.vmCount = in.nextInt();
		String[] st = in.nextLine().split(",");

		in.close();

		inputValidator.validateInputs(st);

	}

	public void validateInputs(String[] st) {

		if (st.length != vmCount) {
			generateFailureLogs("VM allocated time for all VMs are not provided");
		} else {

			vmTimeAllocated = new int[st.length];

			// generating failure logs if there is any error in format of given data
			try {
				for (int i = 0; i < st.length; i++) {
					vmTimeAllocated[i] = Integer.parseInt(st[i].trim());
				}
			} catch (Exception e) {
				generateFailureLogs(e.getMessage());
				System.exit(0);
				;
			}

			generateTestcasesLists();
		}

	}

	private void generateFailureLogs(String error) {

		String path = "C:\\Users\\preet\\Desktop\\";
		String folderPath = path + java.time.LocalDate.now();

		FileHandler handler;
		try {
			handler = new FileHandler(folderPath + "\\" + "failure.log", true);
			Logger logger = Logger.getLogger("com.javacodegeeks.snippets.core");
			logger.addHandler(handler);
			logger.severe(error);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void generateTestcasesLists() {

		TestcasesListsGenerator testcasesListsGenerator = new TestcasesListsGenerator();

		testcasesListsGenerator.generateTestcasesLists(vmCount, vmTimeAllocated);

	}

}
