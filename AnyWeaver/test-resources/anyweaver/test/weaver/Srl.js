laraImport("weaver.Query");

for (const l of Query.search("STREAM::load")) {
    console.log(l.getValue("args"));
}

console.log("Hello")