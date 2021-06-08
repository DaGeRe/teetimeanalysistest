package kieker.monitoring.probe.aspectj.operationExecution;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public enum Util {
   ;

   private static final Logger LOG = LogManager.getLogger(Util.class);
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
         LOG.info("Deleting old log folder: " + logFolder.getAbsolutePath());
         for (File kiekerLogFolder : logFolder.listFiles()) {
            for (File containedFile : kiekerLogFolder.listFiles()) {
               LOG.info("Inner log file: " + containedFile);
            }
            LOG.info("Kieker log folder: " + kiekerLogFolder.getAbsolutePath());
            boolean success = kiekerLogFolder.delete();
            LOG.info("Deleted: " + success + " Exists: " + kiekerLogFolder.exists());
         }
         FileUtils.cleanDirectory(logFolder);
         LOG.info("Log folder cleaned: " + logFolder.exists());
      }
      return logFolder;
   }

   public static void cleanFileByFile(final File logFolder) throws IOException {
      for (File containedFile : logFolder.listFiles()[0].listFiles()) {
         LOG.info("Deleting " + containedFile.getAbsolutePath());
         boolean success = containedFile.delete();
         LOG.info("Deleted: " + success + " Exists: " + containedFile.exists());
      }
      for (File kiekerLogFolder : logFolder.listFiles()) {
         LOG.info("Deleting log folder: " + kiekerLogFolder.getAbsolutePath());
         boolean success = kiekerLogFolder.delete();
         LOG.info("Deleted: " + success + " Exists: " + kiekerLogFolder.exists());
      }
      
//      System.out.println("Deleting log folder: " + logFolder.getAbsolutePath());
//      boolean success = logFolder.delete();
//      System.out.println("Deleted: " + success + " " + logFolder.exists());
//      System.out.println("Deleting " + logFolder.getAbsolutePath());
//      FileUtils.cleanDirectory(logFolder);
//      System.out.println("Existing after deletion: " + logFolder.exists());
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
