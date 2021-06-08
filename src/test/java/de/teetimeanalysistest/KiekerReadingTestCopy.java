package de.teetimeanalysistest;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.hamcrest.MatcherAssert;
import org.hamcrest.core.IsCollectionContaining;
import org.junit.Test;

import teetime.framework.Execution;

/**
 * Unit test for simple App.
 */
public class KiekerReadingTestCopy {

   private final File testFolder = new File("target/current");

   @Test(timeout = Constants.ONE_MINUTE_IN_MILLISECONDS)
   public void testReading() throws IOException {
      File inputFile = new File("src/test/resources/kieker-20210107-215803-8575705594504340-UTC--KIEKER-KoPeMe");

      for (int i = 0; i < 15; i++) {
         cleanup();
         FileUtils.copyDirectory(inputFile, testFolder);

         GetExecutionsStage resultStage = executeReading();

         MatcherAssert.assertThat(resultStage.getSignatures(), IsCollectionContaining.hasItem("public new viewtest.TestMe$InnerClass.<init>(viewtest.TestMe)"));
      }

   }

   @Test(timeout = Constants.ONE_MINUTE_IN_MILLISECONDS)
   public void testReadingWithNewModifier() throws IOException {
      File inputFile = new File("src/test/resources/kieker-20210321-103510-3402657433607-UTC--KIEKER-KoPeMe");

      for (int i = 0; i < 15; i++) {
         cleanup();
         FileUtils.copyDirectory(inputFile, testFolder);

         GetExecutionsStage resultStage = executeReading();

         MatcherAssert.assertThat(resultStage.getSignatures(), IsCollectionContaining.hasItem("public new defaultpackage.NormalDependency.<init>()"));
      }
   }

   private GetExecutionsStage executeReading() {
      KiekerReader app = new KiekerReader();
      GetExecutionsStage resultStage = app.exampleReader(testFolder);

      Execution execution = new Execution(app);
      execution.executeBlocking();

      System.out.println(resultStage.getSignatures());
      return resultStage;
   }

   private void cleanup() throws IOException {
      if (testFolder.exists()) {
         FileUtils.deleteDirectory(testFolder);
      }
   }
}
