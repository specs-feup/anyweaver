import { wrapJoinPoint, } from "@specs-feup/lara/api/LaraJoinPoint.js";
import AnyJavaTypes from "./AnyJavaTypes.js";
/**
 * Utility methods related with the creation of new join points.
 *
 */
export default class AnyJoinPoints {
    static toJoinPoint(node) {
        return wrapJoinPoint(AnyJavaTypes.AnyJoinPoints.createFromLara(node));
    }
    /**
     * Creates a new, empty Any join point.
     *
     * @param kind
     * @returns
     */
    static any(kind) {
        return wrapJoinPoint(AnyJavaTypes.AnyJpFactory.anyNode(kind));
    }
}
//# sourceMappingURL=AnyJoinPoints.js.map