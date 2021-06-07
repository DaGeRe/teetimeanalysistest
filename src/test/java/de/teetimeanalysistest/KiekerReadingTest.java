package de.teetimeanalysistest;

import java.io.File;

import org.hamcrest.MatcherAssert;
import org.hamcrest.core.IsCollectionContaining;
import org.junit.Test;

import teetime.framework.Execution;

/**
 * Unit test for simple App.
 */
public class KiekerReadingTest {

   @Test(timeout = Constants.ONE_MINUTE_IN_MILLISECONDS)
   public void testReading() {
      File inputFile = new File("src/test/resources/kieker-20210107-215803-8575705594504340-UTC--KIEKER-KoPeMe");

      for (int i = 0; i < 15; i++) {
         KiekerReader app = new KiekerReader();
         GetExecutionsStage resultStage = app.exampleReader(inputFile);

         Execution execution = new Execution(app);
         execution.executeBlocking();

         System.out.println(resultStage.getSignatures());

         MatcherAssert.assertThat(resultStage.getSignatures(), IsCollectionContaining.hasItem("public new viewtest.TestMe$InnerClass.<init>(viewtest.TestMe)"));
      }
   }

   @Test(timeout = Constants.ONE_MINUTE_IN_MILLISECONDS)
   public void testReadingWithNewModifier() {
      File inputFile = new File("src/test/resources/kieker-20210321-103510-3402657433607-UTC--KIEKER-KoPeMe");
      for (int i = 0; i < 15; i++) {
         KiekerReader app = new KiekerReader();
         GetExecutionsStage resultStage = app.exampleReader(inputFile);

         Execution execution = new Execution(app);
         execution.executeBlocking();

         System.out.println(resultStage.getSignatures());
         MatcherAssert.assertThat(resultStage.getSignatures(), IsCollectionContaining.hasItem("public new defaultpackage.NormalDependency.<init>()"));
      }
   }
}
