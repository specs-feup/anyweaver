import Query from "@specs-feup/lara/api/weaver/Query.js";

for (const l of Query.search("STREAM::load")) {
    console.log(l.getValue("args"));
}