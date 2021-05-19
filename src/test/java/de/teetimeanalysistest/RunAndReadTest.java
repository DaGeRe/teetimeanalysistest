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

   private static final String OPERATION_BEFOREAFTER_PROJECT = "example-operationexecutionrecord";

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
   
   @Test
   public void runAndReadTest() throws IOException {
      cleanBuildDirectories();
      
      for (int i = 0; i < 15; i++) {
         runAndClean();
      }
   }

   

   private void runAndClean() throws IOException {
      File logFolder = Util.runTestcase(OPERATION_BEFOREAFTER_PROJECT, "TestSimpleOperationExecution");

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

      FileUtils.cleanDirectory(logFolder);
   }
}
