package kieker.monitoring.probe.aspectj.operationExecution;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

public enum Util {
   ;

   public static final File EXAMPLE_PROJECT_FOLDER = new File("src/test/resources/");

   public static File runTestcaseGradle(final String projectName, final String testcase) throws IOException {
      File folder = new File(EXAMPLE_PROJECT_FOLDER, projectName);
      File logFolder = cleanLogFolder(folder);

      runGradle(testcase, folder);
      return logFolder;
   }

   public static File runTestcaseMaven(final String projectName, final String testcase) throws IOException {
      File folder = new File(EXAMPLE_PROJECT_FOLDER, projectName);
      File logFolder = cleanLogFolder(folder);

      runMaven(testcase, folder);
      return logFolder;
   }

   private static File cleanLogFolder(final File folder) throws IOException {
      File logFolder = new File(folder, "monitoring-logs");
      if (!logFolder.exists()) {
         logFolder.mkdir();
      } else {
         System.out.println("Deleting old log folder: " + logFolder.getAbsolutePath());
         for (File content : logFolder.listFiles()) {
            System.out.println("Content file: " + content.getAbsolutePath());
         }
         FileUtils.cleanDirectory(logFolder);
         System.out.println("Log folder cleaned: " + logFolder.exists());
      }
      return logFolder;
   }

   private static void runGradle(final String testcase, final File folder) throws IOException {
      ProcessBuilder processBuilder = new ProcessBuilder("gradle", "--no-watch-fs", "--info", "clean", "test", "--tests", testcase);
      processBuilder.directory(folder);
      Process process = processBuilder.start();
      StreamGobbler.showFullProcess(process);
   }
   
   private static void runMaven(final String testcase, final File folder) throws IOException {
      String mvnCall;
      if (!System.getProperty("os.name").startsWith("Windows")) {
         mvnCall = "mvn";
      } else {
         mvnCall = "mvn.cmd";
      }
      ProcessBuilder processBuilder = new ProcessBuilder(mvnCall, "clean", "test", "-Dtest=" + testcase);
      processBuilder.directory(folder);
      Process process = processBuilder.start();
      StreamGobbler.showFullProcess(process);
   }
}
