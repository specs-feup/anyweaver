import { wrapJoinPoint, } from "@specs-feup/lara/api/LaraJoinPoint.js";
//import Clava from "./Clava.js";
import AnyWeaverJavaTypes from "./AnyWeaverJavaTypes.js";
/**
 * Utility methods related with the creation of new join points.
 *
 */
export default class AnyWeaverJoinPoints {
    static toJoinPoint(node) {
        return wrapJoinPoint(AnyWeaverJavaTypes.CxxJoinPoints.createFromLara(node));
    }
}
//# sourceMappingURL=AnyWeaverJoinPoints.js.map