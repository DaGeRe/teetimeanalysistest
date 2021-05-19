package kieker.monitoring.probe.aspectj.operationExecution;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

public enum Util {
   ;

   public static final File EXAMPLE_PROJECT_FOLDER = new File("src/test/resources/");

   public static File runTestcase(final String projectName, final String testcase) throws IOException {
      File folder = new File(EXAMPLE_PROJECT_FOLDER, projectName);
      File logFolder = new File(folder, "monitoring-logs");
      if (!logFolder.exists()) {
         logFolder.mkdir();
      } else {
         FileUtils.cleanDirectory(logFolder);
      }

      String command = getGradleCall();
      ProcessBuilder processBuilder = new ProcessBuilder(command, "clean", "test", "--tests", testcase);
      processBuilder.directory(folder);
      StreamGobbler.showFullProcess(processBuilder.start());
      return logFolder;
   }

   private static String getGradleCall() {
      String command;
      if (System.getProperty("os.name").startsWith("Windows")) {
         command = ".\\gradlew.bat";
      } else {
         command = "./gradlew";
      }
      return command;
   }

}
