package de.teetimeanalysistest;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.hamcrest.MatcherAssert;
import org.hamcrest.core.IsCollectionContaining;
import org.junit.Before;
import org.junit.Test;

import kieker.monitoring.probe.aspectj.operationExecution.Util;
import teetime.framework.Execution;

public class RunAndReadTest {

   @Before
   public void cleanBuildDirectories() throws IOException {
      File buildFolder = new File(Util.EXAMPLE_PROJECT_FOLDER, "build");
      if (buildFolder.exists()) {
         FileUtils.deleteDirectory(buildFolder);
      }
      File dotGradleFolder = new File(Util.EXAMPLE_PROJECT_FOLDER, ".gradle");
      if (dotGradleFolder.exists()) {
         FileUtils.deleteDirectory(dotGradleFolder);
      }
   }

   @Test(timeout = 2 * Constants.ONE_MINUTE_IN_MILLISECONDS)
   public void runAndReadTest() throws IOException {
      cleanBuildDirectories();

      for (int i = 0; i < 15; i++) {
         runAndClean();
      }
   }

   private void runAndClean() throws IOException {
      File logFolder = Util.runTestcaseMaven(Constants.OPERATION_BEFOREAFTER_PROJECT_MAVEN, "TestSimpleOperationExecution");

      read(logFolder);

      cleanFileByFile(logFolder);
   }

   private void read(final File logFolder) {
      KiekerReader app = new KiekerReader();
      File kiekerTraceFolder = logFolder.listFiles()[0];
      GetExecutionsStage resultStage = app.exampleReader(kiekerTraceFolder);

      Execution execution = new Execution(app);
      execution.executeBlocking();

      System.out.println(kiekerTraceFolder);
      for (File file : kiekerTraceFolder.listFiles()) {
         System.out.println(file);
      }
      System.out.println(resultStage.getSignatures());

      MatcherAssert.assertThat(resultStage.getSignatures(), IsCollectionContaining.hasItem("public void net.example.Instrumentable.callee()"));
   }

   public static void cleanFileByFile(final File logFolder) throws IOException {
      for (File containedFile : logFolder.listFiles()[0].listFiles()) {
         System.out.println("Deleting " + containedFile.getAbsolutePath());
         containedFile.delete();
      }
      
      System.out.println("Deleting " + logFolder.getAbsolutePath());
      FileUtils.cleanDirectory(logFolder);
   }
}
