class Parsers {
  static json(code, options) {
    const kindAttr = options["kindAttr"] ?? "type";
    const childrenAttr = options["childrenAttr"] ?? "children";
    const isFlat = options["isFlat"] ?? true;

    const JsonParser = Java.type(
      "pt.up.fe.specs.anycompiler.parsers.JsonParser"
    );

    const parser = new JsonParser(kindAttr, childrenAttr, isFlat);

    const anyNode = parser.parse(code);

    const AnyJoinpoints = Java.type(
      "pt.up.fe.specs.anycompiler.weaver.AnyJoinpoints"
    );

    return AnyJoinpoints.create(anyNode);
  }
  /*
  static jsonParser(kindAttr, childrenAttr, isFlat) {
    kindAttr = kindAttr ?? "type";
    childrenAttr = childrenAttr ?? "children";
    isFlat = isFlat ?? true;

    var JsonParser = Java.type("pt.up.fe.specs.anycompiler.parsers.JsonParser");

    return new JsonParser(kindAttr, childrenAttr, isFlat);
  }
  */
}
