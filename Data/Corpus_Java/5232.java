package jetbrains.mps.lang.typesystem.editor;

/*Generated by MPS */

import jetbrains.mps.nodeEditor.menus.substitute.SubstituteMenuBase;
import org.jetbrains.annotations.NotNull;
import java.util.List;
import jetbrains.mps.lang.editor.menus.MenuPart;
import jetbrains.mps.openapi.editor.menus.substitute.SubstituteMenuItem;
import jetbrains.mps.openapi.editor.menus.substitute.SubstituteMenuContext;
import java.util.ArrayList;
import jetbrains.mps.lang.editor.menus.substitute.ConstraintsFilteringSubstituteMenuPartDecorator;
import jetbrains.mps.smodel.adapter.structure.MetaAdapterFactory;
import jetbrains.mps.lang.editor.menus.EditorMenuDescriptorBase;
import jetbrains.mps.smodel.SNodePointer;
import jetbrains.mps.lang.editor.menus.substitute.ReferenceScopeSubstituteMenuPart;
import jetbrains.mps.lang.editor.menus.substitute.ReferenceScopeSubstituteMenuItem;
import org.jetbrains.mps.openapi.model.SNode;
import jetbrains.mps.openapi.editor.menus.EditorMenuTraceInfo;
import org.jetbrains.mps.openapi.language.SConcept;
import org.jetbrains.mps.openapi.language.SReferenceLink;
import jetbrains.mps.lang.smodel.generator.smodelAdapter.SPropertyOperations;
import jetbrains.mps.lang.smodel.generator.smodelAdapter.SConceptOperations;
import jetbrains.mps.lang.smodel.generator.smodelAdapter.SNodeOperations;

public class InequationReference_SmartReference extends SubstituteMenuBase {
  @NotNull
  @Override
  protected List<MenuPart<SubstituteMenuItem, SubstituteMenuContext>> getParts(final SubstituteMenuContext _context) {
    List<MenuPart<SubstituteMenuItem, SubstituteMenuContext>> result = new ArrayList<MenuPart<SubstituteMenuItem, SubstituteMenuContext>>();
    result.add(new ConstraintsFilteringSubstituteMenuPartDecorator(new InequationReference_SmartReference.SMP_ReferenceScope_g0kter_a(), MetaAdapterFactory.getConcept(0x7a5dda6291404668L, 0xab76d5ed1746f2b2L, 0x42501924d0bd1913L, "jetbrains.mps.lang.typesystem.structure.InequationReference")));
    return result;
  }

  @NotNull
  @Override
  public List<SubstituteMenuItem> createMenuItems(@NotNull SubstituteMenuContext context) {
    context.getEditorMenuTrace().pushTraceInfo();
    context.getEditorMenuTrace().setDescriptor(new EditorMenuDescriptorBase("named substitute menu " + "InequationReference_SmartReference", new SNodePointer("r:00000000-0000-4000-0000-011c895902b0(jetbrains.mps.lang.typesystem.editor)", "4747359941571311564")));
    try {
      return super.createMenuItems(context);
    } finally {
      context.getEditorMenuTrace().popTraceInfo();
    }
  }


  public static class SMP_ReferenceScope_g0kter_a extends ReferenceScopeSubstituteMenuPart {

    public SMP_ReferenceScope_g0kter_a() {
      super(MetaAdapterFactory.getConcept(0x7a5dda6291404668L, 0xab76d5ed1746f2b2L, 0x42501924d0bd1913L, "jetbrains.mps.lang.typesystem.structure.InequationReference"), MetaAdapterFactory.getReferenceLink(0x7a5dda6291404668L, 0xab76d5ed1746f2b2L, 0x42501924d0bd1913L, 0x42501924d0bd1914L, "inequation"));
    }
    @NotNull
    @Override
    public List<SubstituteMenuItem> createItems(SubstituteMenuContext context) {
      context.getEditorMenuTrace().pushTraceInfo();
      context.getEditorMenuTrace().setDescriptor(new EditorMenuDescriptorBase("reference scope substitute menu part", new SNodePointer("r:00000000-0000-4000-0000-011c895902b0(jetbrains.mps.lang.typesystem.editor)", "4747359941571311562")));
      try {
        return super.createItems(context);
      } finally {
        context.getEditorMenuTrace().popTraceInfo();
      }
    }

    @Override
    @NotNull
    protected ReferenceScopeSubstituteMenuItem createItem(SubstituteMenuContext context, SNode referencedNode) {
      return new InequationReference_SmartReference.SMP_ReferenceScope_g0kter_a.Item(context, referencedNode, getConcept(), getReferenceLink());
    }
    private static class Item extends ReferenceScopeSubstituteMenuItem {
      private final SubstituteMenuContext _context;
      private final SNode referencedNode;
      private EditorMenuTraceInfo myTraceInfo;

      private Item(SubstituteMenuContext context, SNode refNode, SConcept concept, SReferenceLink referenceLink) {
        super(concept, context.getParentNode(), context.getCurrentTargetNode(), refNode, referenceLink, context.getEditorContext());
        _context = context;
        referencedNode = refNode;
        myTraceInfo = context.getEditorMenuTrace().getTraceInfo();
      }
      @Override
      public String getMatchingText(String pattern) {
        if (isNotEmptyString(SPropertyOperations.getString(referencedNode, MetaAdapterFactory.getProperty(0x7a5dda6291404668L, 0xab76d5ed1746f2b2L, 0x11a342c1412L, 0x42501924d0bd6481L, "label")))) {
          return SPropertyOperations.getString(referencedNode, MetaAdapterFactory.getProperty(0x7a5dda6291404668L, 0xab76d5ed1746f2b2L, 0x11a342c1412L, 0x42501924d0bd6481L, "label")) + " " + SConceptOperations.conceptAlias(SNodeOperations.getConcept(referencedNode));
        } else {
          return SConceptOperations.conceptAlias(SNodeOperations.getConcept(referencedNode));
        }
      }
      @Override
      public String getVisibleMatchingText(String pattern) {
        return getMatchingText(pattern);
      }

      @Override
      public EditorMenuTraceInfo getTraceInfo() {
        return myTraceInfo;
      }
      private static boolean isNotEmptyString(String str) {
        return str != null && str.length() > 0;
      }
    }
  }
}