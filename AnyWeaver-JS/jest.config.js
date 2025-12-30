const config = {
  preset: "ts-jest/presets/default-esm",
  testEnvironment: "node",
  coverageDirectory: "coverage",
  collectCoverageFrom: ["**/*[^.d].(t|j)s"],
  moduleNameMapper: {
    "(.+)\\.js": "$1",
  },
};

export default config;
