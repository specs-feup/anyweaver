import type WeaverConfiguration from "@specs-feup/lara/code/WeaverConfiguration.js";
import path from "path";
import { fileURLToPath } from "url";

export const weaverConfig: WeaverConfiguration = {
  weaverName: "anyweaver",
  weaverPrettyName: "AnyWeaver",
  jarPath: path.join(
    path.dirname(path.dirname(fileURLToPath(import.meta.url))),
    "./java-binaries/"
  ),
  javaWeaverQualifiedName: "pt.up.fe.specs.anycompiler.weaver.AnyWeaver",
  importForSideEffects: ["@specs-feup/anyweaver/api/Joinpoints.js"],
};
