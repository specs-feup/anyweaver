import { WeaverLegacyTester } from "@specs-feup/lara/jest/WeaverLegacyTester.js";
import path from "path";

// FIXME: Since this is a single test, we can move it to the new test harness instead of using the legacy one.

/* eslint-disable jest/expect-expect */
describe("Legacy Integration Tests", () => {
    function newTester() {
        return new WeaverLegacyTester(
            path.resolve("../AnyWeaver/test-resources/anyweaver/test/weaver")
        )
            .setResultPackage("results")
            .setSrcPackage("src");
    }

    it("Basic", async () => {
        await newTester().test("Srl.js", "srl-ast.json");
    });
});
