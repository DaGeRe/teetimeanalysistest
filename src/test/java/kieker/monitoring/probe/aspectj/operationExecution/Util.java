package kieker.monitoring.probe.aspectj.operationExecution;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

public enum Util {
   ;

   private static final File EXAMPLE_PROJECT_FOLDER = new File("src/test/resources/");

   public static File runTestcase(final String projectName, final String testcase) throws IOException {
      File folder = new File(EXAMPLE_PROJECT_FOLDER, projectName);
      File logFolder = new File(folder, "monitoring-logs");
      if (!logFolder.exists()) {
         logFolder.mkdir();
      } else {
         FileUtils.cleanDirectory(logFolder);
      }

      ProcessBuilder processBuilder = new ProcessBuilder("gradle", "clean", "test", "--tests",
            testcase);
      processBuilder.directory(folder);
      StreamGobbler.showFullProcess(processBuilder.start());
      return logFolder;
   }

}
