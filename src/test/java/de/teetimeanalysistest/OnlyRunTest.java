package de.teetimeanalysistest;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Test;

import kieker.monitoring.probe.aspectj.operationExecution.Util;

public class OnlyRunTest {
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

   @Test(timeout = 2*Constants.ONE_MINUTE_IN_MILLISECONDS)
   public void onlyRunTest() throws IOException {
      cleanBuildDirectories();

      for (int i = 0; i < 15; i++) {
         File logFolder = Util.runTestcaseMaven(Constants.OPERATION_BEFOREAFTER_PROJECT_MAVEN, "TestSimpleOperationExecution");
         
         Util.cleanFileByFile(logFolder);
         
         System.out.println("Iteration " + i + " finished");
      }
   }
}
