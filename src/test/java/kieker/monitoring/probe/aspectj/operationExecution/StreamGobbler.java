package kieker.monitoring.probe.aspectj.operationExecution;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Combines the streams of a process.
 * 
 * @author reichelt
 *
 */
public class StreamGobbler extends Thread {

   private final InputStream is;
   private final boolean showOutput;

   private StreamGobbler(final InputStream is, final boolean showOutput) {
      super("Gobbler");
      this.is = is;
      this.showOutput = showOutput;
   }

   @Override
   public void run() {
      try (final InputStreamReader isr = new InputStreamReader(is)) {
         final BufferedReader br = new BufferedReader(isr);
         String line = null;
         while ((line = br.readLine()) != null) {
            if (showOutput) {
               System.out.println(line);
            }
         }
      } catch (final IOException ioe) {
         ioe.printStackTrace();
      }
   }

   public static void showFullProcess(final Process process) {
      getFullProcess(process, true);
   }

   /**
    * Combines the streams of the process and eventually shows the output. Parallel calls to this method will lead to mixed outputs.
    * 
    * @param process The process that should be printed
    * @param showOutput Whether the output should be printed directly to System.out
    * @return The combined output of the streams of the process
    */
   public static void getFullProcess(final Process process, final boolean showOutput) {
      final StreamGobbler errorGobbler = new StreamGobbler(process.getErrorStream(), showOutput);
      final StreamGobbler outputGobbler = new StreamGobbler(process.getInputStream(), showOutput);

      outputGobbler.start();
      errorGobbler.start();

      try {
         outputGobbler.join();
         errorGobbler.join();
      } catch (final InterruptedException e) {
         e.printStackTrace();
      }
   }

}
