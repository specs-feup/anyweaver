#!/usr/bin/env node
import WeaverLauncher from "@specs-feup/lara/code/WeaverLauncher.js";
import { registerJoinpointMapperFunction } from "@specs-feup/lara/api/LaraJoinPoint.js";
import { weaverConfig } from "./WeaverConfiguration.js";
import { Any, App } from "../api/Joinpoints.js";

// Register jp mapper that always returns Any expect for App
registerJoinpointMapperFunction((jpTypename: string) => {
  if (jpTypename === "Joinpoint 'app'") {
    return App;
  }

  return Any;
});

const weaverLauncher = new WeaverLauncher(weaverConfig);

await weaverLauncher.execute();
