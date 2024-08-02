/**
 * This file is used only in AnyWeaver Classic to load the core API.
 * This is done for compatibility with the previous version of AnyWeaver.
 * Do not use this file in new (clava-js) projects.
 * Remove this file if AnyWeaver Classic has died out.
 */
const prefix = "anyweaver-js/api/";
const coreImports = [];
const sideEffectsOnlyImports = ["Joinpoints.js"];
for (const sideEffectsOnlyImport of sideEffectsOnlyImports) {
    await import(prefix + sideEffectsOnlyImport);
}
for (const coreImport of coreImports) {
    const foo = Object.entries(await import(prefix + coreImport));
    foo.forEach(([key, value]) => {
        // @ts-ignore
        globalThis[key] = value;
    });
}
export {};
//# sourceMappingURL=core.js.map