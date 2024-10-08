/* eslint-disable @typescript-eslint/no-unsafe-return */
/* eslint-disable @typescript-eslint/no-unsafe-call */
/* eslint-disable @typescript-eslint/no-unsafe-assignment */
/* eslint-disable @typescript-eslint/no-unsafe-member-access */

import { wrapJoinPoint } from "@specs-feup/lara/api/LaraJoinPoint.js";
import JavaTypes from "@specs-feup/lara/api/lara/util/JavaTypes.js";
import Weaver from "@specs-feup/lara/api/weaver/Weaver.js"
//import type { Joinpoint } from "../Joinpoints.js";

export default class Parsers {
  public static json(code: string, options = { kindAttr: "type", childrenAttr: "children", isFlat: true }){//: Joinpoint {
    const kindAttr = options.kindAttr ?? "type";
    const childrenAttr = options.childrenAttr ?? "children";
    const isFlat = options.isFlat ?? true;

    const JsonParser = JavaTypes.getType(
      "pt.up.fe.specs.anycompiler.parsers.JsonParser"
    );

    const parser = new JsonParser(kindAttr, childrenAttr, isFlat);

    const anyNode = parser.parse(code);

    const AnyJoinpoints = JavaTypes.getType(
      "pt.up.fe.specs.anycompiler.weaver.AnyJoinpoints"
    );

    return wrapJoinPoint(AnyJoinpoints.create(anyNode));// as Joinpoint;
  }

  public static antlr(code: string, rule: string, grammarName: string, grammarPackage: string){//: Joinpoint {
      // Build lexer qualifier name
      const lexerQualifierName = `${grammarPackage}.${grammarName}Lexer`;

      // Build parser qualifier name
      const parserQualifierName = `${grammarPackage}.${grammarName}Parser`;

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

      return AnyJoinpoints.create(anyRoot);// as Joinpoint;
    }
}
