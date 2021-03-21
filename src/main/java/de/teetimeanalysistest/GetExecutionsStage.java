package de.teetimeanalysistest;

import java.util.HashSet;
import java.util.Set;

import kieker.analysis.trace.AbstractTraceProcessingStage;
import kieker.common.util.signature.Signature;
import kieker.model.repository.SystemModelRepository;
import kieker.model.system.model.Execution;
import kieker.model.system.model.ExecutionTrace;

class GetExecutionsStage extends AbstractTraceProcessingStage<ExecutionTrace> {

   public GetExecutionsStage(final SystemModelRepository systemModelRepository) {
      super(systemModelRepository);
   }

   private final Set<String> signatures = new HashSet<>();

   @Override
   protected void execute(final ExecutionTrace element) throws Exception {
      for (Execution execution : element.getTraceAsSortedExecutionSet()) {
         String signature = getKiekerSignature(execution);
         signatures.add(signature);
      }
   }

   private String getKiekerSignature(final Execution execution) {
      StringBuilder signatureBuilder = new StringBuilder();
      for (String modifier : execution.getOperation().getSignature().getModifier()) {
         signatureBuilder.append(modifier);
         signatureBuilder.append(' ');
      }
      if (execution.getOperation().getSignature().getReturnType().equals(Signature.NO_RETURN_TYPE)) {
         if (!signatureBuilder.toString().contains("new ")) {
            signatureBuilder.append("new");
            signatureBuilder.append(' ');
         }
      } else {
         signatureBuilder.append(execution.getOperation().getSignature().getReturnType());
         signatureBuilder.append(' ');
      }
      signatureBuilder.append(execution.getOperation().getComponentType().getFullQualifiedName());
      signatureBuilder.append('.');
      signatureBuilder.append(execution.getOperation().getSignature().getName());
      signatureBuilder.append('(');
      boolean first = true;
      for (final String paramType : execution.getOperation().getSignature().getParamTypeList()) {
         if (!first) {
            signatureBuilder.append(',');
         } else {
            first = false;
         }
         signatureBuilder.append(paramType);
      }
      signatureBuilder.append(')');

      String signature = signatureBuilder.toString();
      return signature;
   }

   public Set<String> getSignatures() {
      return signatures;
   }
}