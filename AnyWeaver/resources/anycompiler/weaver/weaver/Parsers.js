/* eslint-disable @typescript-eslint/no-unsafe-return */
/* eslint-disable @typescript-eslint/no-unsafe-call */
/* eslint-disable @typescript-eslint/no-unsafe-assignment */
/* eslint-disable @typescript-eslint/no-unsafe-member-access */
import { wrapJoinPoint } from "lara-js/api/LaraJoinPoint.js";
import JavaTypes from "lara-js/api/lara/util/JavaTypes.js";
export default class Parsers {
    static json(code, options = { "kindAttr": "type", "childrenAttr": "children", "isFlat": true }) {
        const kindAttr = options["kindAttr"] ?? "type";
        const childrenAttr = options["childrenAttr"] ?? "children";
        const isFlat = options["isFlat"] ?? true;
        const JsonParser = JavaTypes.getType("pt.up.fe.specs.anycompiler.parsers.JsonParser");
        const parser = new JsonParser(kindAttr, childrenAttr, isFlat);
        const anyNode = parser.parse(code);
        const AnyJoinpoints = JavaTypes.getType("pt.up.fe.specs.anycompiler.weaver.AnyJoinpoints");
        return wrapJoinPoint(AnyJoinpoints.create(anyNode));
    }
}
//# sourceMappingURL=Parsers.js.map