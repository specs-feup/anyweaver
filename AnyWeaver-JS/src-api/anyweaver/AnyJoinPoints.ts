import {
  wrapJoinPoint,
} from "@specs-feup/lara/api/LaraJoinPoint.js";

import * as Joinpoints from "../Joinpoints.js";
import AnyJavaTypes from "./AnyJavaTypes.js";

/**
 * Utility methods related with the creation of new join points.
 *
 */
export default class AnyJoinPoints {
  static toJoinPoint(node: any): Joinpoints.Joinpoint {
    return wrapJoinPoint(AnyJavaTypes.AnyJoinPoints.createFromLara(node));
  }

  /**
   * Creates a new, empty Any join point.
   *
   * @param kind
   * @returns
   */
  static any(kind: string): Joinpoints.Any {
    return wrapJoinPoint(AnyJavaTypes.AnyJpFactory.anyNode(kind));
  }

  
}
