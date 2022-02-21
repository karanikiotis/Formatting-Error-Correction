package jetbrains.mps.baseLanguage.builders.constraints;

/*Generated by MPS */

import jetbrains.mps.smodel.runtime.BaseConstraintsAspectDescriptor;
import jetbrains.mps.smodel.runtime.ConstraintsDescriptor;
import org.jetbrains.mps.openapi.language.SAbstractConcept;
import jetbrains.mps.smodel.runtime.base.BaseConstraintsDescriptor;
import jetbrains.mps.lang.smodel.ConceptSwitchIndex;
import jetbrains.mps.lang.smodel.ConceptSwitchIndexBuilder;
import jetbrains.mps.smodel.adapter.ids.MetaIdFactory;

public class ConstraintsAspectDescriptor extends BaseConstraintsAspectDescriptor {
  public ConstraintsAspectDescriptor() {
  }

  @Override
  public ConstraintsDescriptor getConstraints(SAbstractConcept concept) {
    SAbstractConcept cncpt_a0c = concept;
    switch (index_2qnle6_a0c.index(cncpt_a0c)) {
      case 0:
        return new AsTypeBuilder_Constraints();
      case 1:
        return new BaseSimpleBuilderDeclaration_Constraints();
      case 2:
        return new BeanBuilder_Constraints();
      case 3:
        return new BeanPropertyBuilder_Constraints();
      case 4:
        return new ResultExpression_Constraints();
      case 5:
        return new SimpleBuilder_Constraints();
      case 6:
        return new SimpleBuilderChildExpression_Constraints();
      case 7:
        return new SimpleBuilderDeclaration_Constraints();
      case 8:
        return new SimpleBuilderExpression_Constraints();
      case 9:
        return new SimpleBuilderParameterReference_Constraints();
      case 10:
        return new SimpleBuilderPropertyBuilder_Constraints();
      case 11:
        return new SimpleBuilderPropertyExpression_Constraints();
      default:
    }
    return new BaseConstraintsDescriptor(concept);
  }
  private static final ConceptSwitchIndex index_2qnle6_a0c = new ConceptSwitchIndexBuilder().put(MetaIdFactory.conceptId(0x132aa4d8a3f7441cL, 0xa7eb3fce23492c6aL, 0x4acc05c8d721d314L), MetaIdFactory.conceptId(0x132aa4d8a3f7441cL, 0xa7eb3fce23492c6aL, 0x56cd40dfa78d35b1L), MetaIdFactory.conceptId(0x132aa4d8a3f7441cL, 0xa7eb3fce23492c6aL, 0x5c83892592e1ebbfL), MetaIdFactory.conceptId(0x132aa4d8a3f7441cL, 0xa7eb3fce23492c6aL, 0x252efd34f8a58ec7L), MetaIdFactory.conceptId(0x132aa4d8a3f7441cL, 0xa7eb3fce23492c6aL, 0x6524536b2e18dae0L), MetaIdFactory.conceptId(0x132aa4d8a3f7441cL, 0xa7eb3fce23492c6aL, 0x6524536b2e24c0baL), MetaIdFactory.conceptId(0x132aa4d8a3f7441cL, 0xa7eb3fce23492c6aL, 0x6524536b2e1d353eL), MetaIdFactory.conceptId(0x132aa4d8a3f7441cL, 0xa7eb3fce23492c6aL, 0x6524536b2e1a1e38L), MetaIdFactory.conceptId(0x132aa4d8a3f7441cL, 0xa7eb3fce23492c6aL, 0x6524536b2e1d3540L), MetaIdFactory.conceptId(0x132aa4d8a3f7441cL, 0xa7eb3fce23492c6aL, 0x4b4c01fdd9029ce4L), MetaIdFactory.conceptId(0x132aa4d8a3f7441cL, 0xa7eb3fce23492c6aL, 0x4acc05c8d72ec05fL), MetaIdFactory.conceptId(0x132aa4d8a3f7441cL, 0xa7eb3fce23492c6aL, 0x4acc05c8d72c48e2L)).seal();
}