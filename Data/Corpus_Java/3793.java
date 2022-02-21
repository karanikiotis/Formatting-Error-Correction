package jetbrains.mps.ide.java.actions;

/*Generated by MPS */

import jetbrains.mps.workbench.action.BaseAction;
import javax.swing.Icon;
import com.intellij.openapi.actionSystem.AnActionEvent;
import java.util.Map;
import jetbrains.mps.refactoring.framework.RefactoringUtil;
import org.jetbrains.mps.openapi.model.SNode;
import jetbrains.mps.internal.collections.runtime.MapSequence;
import org.jetbrains.annotations.NotNull;
import jetbrains.mps.ide.actions.MPSCommonDataKeys;
import jetbrains.mps.project.MPSProject;
import com.intellij.featureStatistics.FeatureUsageTracker;
import org.jetbrains.mps.openapi.module.ModelAccess;
import jetbrains.mps.baseLanguage.closures.runtime.Wrappers;
import jetbrains.mps.lang.smodel.generator.smodelAdapter.SNodeOperations;
import jetbrains.mps.smodel.adapter.structure.MetaAdapterFactory;
import jetbrains.mps.lang.smodel.generator.smodelAdapter.SPropertyOperations;
import jetbrains.mps.lang.smodel.generator.smodelAdapter.SLinkOperations;
import jetbrains.mps.ide.platform.refactoring.RenameMethodDialog;
import org.jetbrains.mps.openapi.model.SNodeUtil;
import jetbrains.mps.refactoring.runtime.access.RefactoringAccess;
import jetbrains.mps.refactoring.framework.RefactoringContext;
import java.util.Arrays;

public class RenameMethod_Action extends BaseAction {
  private static final Icon ICON = null;

  public RenameMethod_Action() {
    super("Rename Method", "", ICON);
    this.setIsAlwaysVisible(false);
    this.setExecuteOutsideCommand(true);
  }
  @Override
  public boolean isDumbAware() {
    return true;
  }
  @Override
  public boolean isApplicable(AnActionEvent event, final Map<String, Object> _params) {
    return RefactoringUtil.isApplicable(RefactoringUtil.getRefactoringByClassName("jetbrains.mps.baseLanguage.refactorings" + "." + "RenameMethod"), ((SNode) MapSequence.fromMap(_params).get("target")));
  }
  @Override
  public void doUpdate(@NotNull AnActionEvent event, final Map<String, Object> _params) {
    this.setEnabledState(event.getPresentation(), this.isApplicable(event, _params));
  }
  @Override
  protected boolean collectActionData(AnActionEvent event, final Map<String, Object> _params) {
    if (!(super.collectActionData(event, _params))) {
      return false;
    }
    {
      SNode node = event.getData(MPSCommonDataKeys.NODE);
      MapSequence.fromMap(_params).put("target", node);
      if (node == null) {
        return false;
      }
    }
    {
      MPSProject p = event.getData(MPSCommonDataKeys.MPS_PROJECT);
      MapSequence.fromMap(_params).put("project", p);
      if (p == null) {
        return false;
      }
    }
    return true;
  }
  @Override
  public void doExecute(@NotNull final AnActionEvent event, final Map<String, Object> _params) {
    FeatureUsageTracker.getInstance().triggerFeatureUsed("refactoring.rename");
    ModelAccess modelAccess = ((MPSProject) MapSequence.fromMap(_params).get("project")).getRepository().getModelAccess();

    final Wrappers._T<String> oldName = new Wrappers._T<String>();
    modelAccess.runReadAction(new Runnable() {
      public void run() {
        if (SNodeOperations.isInstanceOf(((SNode) MapSequence.fromMap(_params).get("target")), MetaAdapterFactory.getInterfaceConcept(0xf3061a5392264cc5L, 0xa443f952ceaf5816L, 0x11857355952L, "jetbrains.mps.baseLanguage.structure.IMethodCall"))) {
          oldName.value = SPropertyOperations.getString(SLinkOperations.getTarget(SNodeOperations.cast(((SNode) MapSequence.fromMap(_params).get("target")), MetaAdapterFactory.getInterfaceConcept(0xf3061a5392264cc5L, 0xa443f952ceaf5816L, 0x11857355952L, "jetbrains.mps.baseLanguage.structure.IMethodCall")), MetaAdapterFactory.getReferenceLink(0xf3061a5392264cc5L, 0xa443f952ceaf5816L, 0x11857355952L, 0xf8c78301adL, "baseMethodDeclaration")), MetaAdapterFactory.getProperty(0xceab519525ea4f22L, 0x9b92103b95ca8c0cL, 0x110396eaaa4L, 0x110396ec041L, "name"));
        } else if (SNodeOperations.isInstanceOf(((SNode) MapSequence.fromMap(_params).get("target")), MetaAdapterFactory.getConcept(0xf3061a5392264cc5L, 0xa443f952ceaf5816L, 0xf8cc56b1fcL, "jetbrains.mps.baseLanguage.structure.BaseMethodDeclaration"))) {
          oldName.value = SPropertyOperations.getString(SNodeOperations.cast(((SNode) MapSequence.fromMap(_params).get("target")), MetaAdapterFactory.getConcept(0xf3061a5392264cc5L, 0xa443f952ceaf5816L, 0xf8cc56b1fcL, "jetbrains.mps.baseLanguage.structure.BaseMethodDeclaration")), MetaAdapterFactory.getProperty(0xceab519525ea4f22L, 0x9b92103b95ca8c0cL, 0x110396eaaa4L, 0x110396ec041L, "name"));
        }
      }
    });
    final RenameMethodDialog d = new RenameMethodDialog(((MPSProject) MapSequence.fromMap(_params).get("project")).getProject(), oldName.value);
    d.show();

    final String newName = d.getResultValue();
    if (newName == null) {
      return;
    }
    modelAccess.runReadInEDT(new Runnable() {
      @Override
      public void run() {
        SNode node = ((SNode) ((SNode) MapSequence.fromMap(_params).get("target")));
        if (!(SNodeUtil.isAccessible(node, ((MPSProject) MapSequence.fromMap(_params).get("project")).getRepository()))) {
          return;
        }
        RefactoringAccess.getInstance().getRefactoringFacade().execute(RefactoringContext.createRefactoringContextByName("jetbrains.mps.baseLanguage.refactorings.RenameMethod", Arrays.asList("newName"), Arrays.asList(newName), ((SNode) MapSequence.fromMap(_params).get("target")), ((MPSProject) MapSequence.fromMap(_params).get("project"))));
      }
    });

  }
}