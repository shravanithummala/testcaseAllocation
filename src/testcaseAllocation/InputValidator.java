package testcaseAllocation;

import java.io.File;
import java.util.Arrays;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

public class InputValidator {

	int vmCount;
	int vmTimeAllocated[];

	public static void main(String[] args) {

		InputValidator inputValidator = new InputValidator();

		System.out.println(Arrays.toString(args));

		inputValidator.vmCount = Integer.valueOf(args[0]);
		String[] st = args[1].split(",");

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
			}
			generateTestcasesLists();
		}

	}

	private void generateFailureLogs(String error) {

		String path = System.getProperty("user.home") + "\\Desktop\\";
		String folderPath = path + java.time.LocalDate.now();
		
		//creates folder
		File f1 = new File(folderPath);
		f1.mkdir();

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
