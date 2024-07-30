/* eslint-disable @typescript-eslint/no-unsafe-return */
/* eslint-disable @typescript-eslint/no-unsafe-call */
/* eslint-disable @typescript-eslint/no-unsafe-assignment */
/* eslint-disable @typescript-eslint/no-unsafe-member-access */

import { wrapJoinPoint } from "lara-js/api/LaraJoinPoint.js";
import JavaTypes from "lara-js/api/lara/util/JavaTypes.js";
import Weaver from "lara-js/api/weaver/Weaver.js"

export default class Parsers {
  static json(code: string, options = { "kindAttr": "type", "childrenAttr": "children", "isFlat": true }) {
    const kindAttr = options["kindAttr"] ?? "type";
    const childrenAttr = options["childrenAttr"] ?? "children";
    const isFlat = options["isFlat"] ?? true;

    const JsonParser = JavaTypes.getType(
      "pt.up.fe.specs.anycompiler.parsers.JsonParser"
    );

    const parser = new JsonParser(kindAttr, childrenAttr, isFlat);

    const anyNode = parser.parse(code);

    const AnyJoinpoints = JavaTypes.getType(
      "pt.up.fe.specs.anycompiler.weaver.AnyJoinpoints"
    );

    return wrapJoinPoint(AnyJoinpoints.create(anyNode));
  }

  static antlr(code: string, rule: string, grammarName: string, grammarPackage: string) {
      // Build lexer qualifier name
      const lexerQualifierName = grammarPackage + "." + grammarName + "Lexer";

      // Build parser qualifier name
      const parserQualifierName = grammarPackage + "." + grammarName + "Parser";

      // Get custom classloader
      const weaver = Weaver.getWeaverEngine();
      const weaverState = weaver.getLaraWeaverState();
      const classLoader = weaverState.getClassLoader();

      // Get ANTLR parser class
      const parserClass = weaver.getClass("pt.up.fe.specs.anycompiler.parsers.antlr.AntlrParser");

      // Get ANTLR parser static constructor 
      const stringClass = weaver.getClass("java.lang.String");
      const parserConstructor = parserClass.getMethod("newInstance", stringClass, stringClass, stringClass, classLoader.getClass());

      // Create ANTLR parser
      const antlrParser = parserConstructor.invoke(null, lexerQualifierName, parserQualifierName, rule, classLoader);

      // Parse code
      const anyRoot = antlrParser.parse(code);

      // Create Java and JS join point
      const AnyJoinpoints = JavaTypes.getType("pt.up.fe.specs.anycompiler.weaver.AnyJoinpoints");

      return wrapJoinPoint(AnyJoinpoints.create(anyRoot));
    }
}
