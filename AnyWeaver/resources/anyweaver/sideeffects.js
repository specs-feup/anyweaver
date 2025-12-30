import { registerJoinpointMapperFunction } from "@specs-feup/lara/api/LaraJoinPoint.js";
import { Any, App } from "./Joinpoints.js";
// Register jp mapper that always returns Any expect for App
registerJoinpointMapperFunction((jpTypename) => {
    if (jpTypename === "Joinpoint 'app'") {
        return App;
    }
    return Any;
});
//# sourceMappingURL=sideeffects.js.map