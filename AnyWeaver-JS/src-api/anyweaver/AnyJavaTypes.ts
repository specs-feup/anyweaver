import JavaTypes, {
  JavaClasses,
} from "@specs-feup/lara/api/lara/util/JavaTypes.js";

// eslint-disable-next-line @typescript-eslint/no-namespace
export namespace AnyJavaClasses {
  /* eslint-disable @typescript-eslint/no-empty-interface */
  export interface AnyJoinPoints extends JavaClasses.JavaClass {}
  export interface AnyJpFactory extends JavaClasses.JavaClass {}
  /* eslint-enable @typescript-eslint/no-empty-interface */
}

/**
 * Static variables with class names of Java classes used in the AnyWeaver API.
 *
 */
export default class AnyJavaTypes {
  static get AnyJoinPoints() {
    return JavaTypes.getType(
      "pt.up.fe.specs.anycompiler.weaver.AnyJoinpoints"
    ) as AnyJavaClasses.AnyJoinPoints;
  }

static get AnyJpFactory() {
    return JavaTypes.getType(
      "pt.up.fe.specs.anycompiler.weaver.AnyJpFactory"
    ) as AnyJavaClasses.AnyJpFactory;
  }  

 
}
