package jetbrains.mps.lang.editor.behavior;

/*Generated by MPS */

import jetbrains.mps.core.aspects.behaviour.BaseBHDescriptor;
import org.jetbrains.mps.openapi.language.SAbstractConcept;
import jetbrains.mps.smodel.adapter.structure.MetaAdapterFactory;
import jetbrains.mps.core.aspects.behaviour.api.BehaviorRegistry;
import jetbrains.mps.smodel.language.ConceptRegistry;
import jetbrains.mps.core.aspects.behaviour.api.SMethod;
import java.util.List;
import org.jetbrains.mps.openapi.language.SConcept;
import jetbrains.mps.core.aspects.behaviour.SMethodBuilder;
import jetbrains.mps.core.aspects.behaviour.SJavaCompoundTypeImpl;
import jetbrains.mps.core.aspects.behaviour.SModifiersImpl;
import jetbrains.mps.core.aspects.behaviour.AccessPrivileges;
import org.jetbrains.mps.openapi.model.SNode;
import java.util.Arrays;
import org.jetbrains.annotations.NotNull;
import jetbrains.mps.baseLanguage.behavior.ConceptFunction__BehaviorDescriptor;
import jetbrains.mps.internal.collections.runtime.ListSequence;
import jetbrains.mps.core.aspects.behaviour.api.SConstructor;
import org.jetbrains.annotations.Nullable;
import jetbrains.mps.core.aspects.behaviour.api.BHMethodNotFoundException;
import org.jetbrains.mps.openapi.persistence.PersistenceFacade;
import jetbrains.mps.smodel.SModelUtil_new;

public final class TransactionalPropertyHandler__BehaviorDescriptor extends BaseBHDescriptor {
  private static final SAbstractConcept CONCEPT = MetaAdapterFactory.getConcept(0x18bc659203a64e29L, 0xa83a7ff23bde13baL, 0x11b35f5eefdL, "jetbrains.mps.lang.editor.structure.TransactionalPropertyHandler");
  private static final BehaviorRegistry REGISTRY = ConceptRegistry.getInstance().getBehaviorRegistry();

  public static final SMethod<List<SConcept>> getParameterConcepts_id2xELmDxyi2v = new SMethodBuilder<List<SConcept>>(new SJavaCompoundTypeImpl((Class<List<SConcept>>) ((Class) Object.class))).name("getParameterConcepts").modifiers(SModifiersImpl.create(8, AccessPrivileges.PUBLIC)).concept(CONCEPT).id("2xELmDxyi2v").registry(REGISTRY).build();
  public static final SMethod<SNode> getExpectedReturnType_idhEwIGRD = new SMethodBuilder<SNode>(new SJavaCompoundTypeImpl((Class<SNode>) ((Class) Object.class))).name("getExpectedReturnType").modifiers(SModifiersImpl.create(8, AccessPrivileges.PUBLIC)).concept(CONCEPT).id("hEwIGRD").registry(REGISTRY).build();

  private static final List<SMethod<?>> BH_METHODS = Arrays.<SMethod<?>>asList(getParameterConcepts_id2xELmDxyi2v, getExpectedReturnType_idhEwIGRD);

  private static void ___init___(@NotNull SNode __thisNode__) {
  }

  /*package*/ static List<SConcept> getParameterConcepts_id2xELmDxyi2v(@NotNull SNode __thisNode__) {
    List<SConcept> result = ConceptFunction__BehaviorDescriptor.getParameterConcepts_id2xELmDxyi2v.invoke0(__thisNode__, MetaAdapterFactory.getConcept(0xf3061a5392264cc5L, 0xa443f952ceaf5816L, 0x108bbca0f48L, "jetbrains.mps.baseLanguage.structure.ConceptFunction"));
    ListSequence.fromList(result).addElement(MetaAdapterFactory.getConcept(0x18bc659203a64e29L, 0xa83a7ff23bde13baL, 0x10e76200a6fL, "jetbrains.mps.lang.editor.structure.ConceptFunctionParameter_editorContext"));
    ListSequence.fromList(result).addElement(MetaAdapterFactory.getConcept(0x18bc659203a64e29L, 0xa83a7ff23bde13baL, 0x10a195cc7c5L, "jetbrains.mps.lang.editor.structure.ConceptFunctionParameter_node"));
    ListSequence.fromList(result).addElement(MetaAdapterFactory.getConcept(0x18bc659203a64e29L, 0xa83a7ff23bde13baL, 0x11b35f6e2acL, "jetbrains.mps.lang.editor.structure.TransactionPropertyHandler_oldValue"));
    ListSequence.fromList(result).addElement(MetaAdapterFactory.getConcept(0x18bc659203a64e29L, 0xa83a7ff23bde13baL, 0x11b35f75b6dL, "jetbrains.mps.lang.editor.structure.TransactionPropertyHandler_newValue"));
    return result;
  }
  /*package*/ static SNode getExpectedReturnType_idhEwIGRD(@NotNull SNode __thisNode__) {
    return _quotation_createNode_3l0g0y_a0a1();
  }

  /*package*/ TransactionalPropertyHandler__BehaviorDescriptor() {
    super(REGISTRY);
  }

  @Override
  protected void initNode(@NotNull SNode node, @NotNull SConstructor constructor, @Nullable Object[] parameters) {
    ___init___(node);
  }

  @Override
  protected <T> T invokeSpecial0(@NotNull SNode node, @NotNull SMethod<T> method, @Nullable Object[] parameters) {
    int methodIndex = BH_METHODS.indexOf(method);
    if (methodIndex < 0) {
      throw new BHMethodNotFoundException(this, method);
    }
    switch (methodIndex) {
      case 0:
        return (T) ((List<SConcept>) getParameterConcepts_id2xELmDxyi2v(node));
      case 1:
        return (T) ((SNode) getExpectedReturnType_idhEwIGRD(node));
      default:
        throw new BHMethodNotFoundException(this, method);
    }
  }

  @Override
  protected <T> T invokeSpecial0(@NotNull SAbstractConcept concept, @NotNull SMethod<T> method, @Nullable Object[] parameters) {
    int methodIndex = BH_METHODS.indexOf(method);
    if (methodIndex < 0) {
      throw new BHMethodNotFoundException(this, method);
    }
    switch (methodIndex) {
      default:
        throw new BHMethodNotFoundException(this, method);
    }
  }

  @NotNull
  @Override
  public List<SMethod<?>> getDeclaredMethods() {
    return BH_METHODS;
  }

  @NotNull
  @Override
  public SAbstractConcept getConcept() {
    return CONCEPT;
  }
  private static SNode _quotation_createNode_3l0g0y_a0a1() {
    PersistenceFacade facade = PersistenceFacade.getInstance();
    SNode quotedNode_1 = null;
    quotedNode_1 = SModelUtil_new.instantiateConceptDeclaration(MetaAdapterFactory.getConcept(MetaAdapterFactory.getLanguage(0xf3061a5392264cc5L, 0xa443f952ceaf5816L, "jetbrains.mps.baseLanguage"), 0xf8cc6bf96dL, "VoidType"), null, null, false);
    return quotedNode_1;
  }
}