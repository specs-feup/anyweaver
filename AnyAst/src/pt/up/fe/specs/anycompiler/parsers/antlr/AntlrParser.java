package pt.up.fe.specs.anycompiler.parsers.antlr;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.ParseTree;
import pt.up.fe.specs.anycompiler.AnyParser;
import pt.up.fe.specs.anycompiler.ast.AnyNode;
import pt.up.fe.specs.util.SpecsSystem;

import java.net.URLClassLoader;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class AntlrParser implements AnyParser {


    private final Class<? extends Lexer> lexerClass;
    private final Class<? extends Parser> parserClass;
    private final String parserRule;

    public AntlrParser(Class<? extends Lexer> lexerClass, Class<? extends Parser> parserClass, String parserRule) {
        this.lexerClass = lexerClass;
        this.parserClass = parserClass;
        this.parserRule = parserRule;
    }

    public static AntlrParser newInstance(String lexerQualifiedName, String parserQualifiedName, String parserRule, URLClassLoader classLoader) {

        // Try to instantiate lexer and parser
        try {
            var lexerClass = (Class<? extends Lexer>) classLoader.loadClass(lexerQualifiedName);
            var parserClass = (Class<? extends Parser>) classLoader.loadClass(parserQualifiedName);

            return new AntlrParser(lexerClass, parserClass, parserRule);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public AnyNode parse(String code) {

        // Convert code string into a character stream
        var input = new ANTLRInputStream(code);


        try {


            // Transform characters into tokens using the lexer
            var lex = lexerClass.getDeclaredConstructor(CharStream.class).newInstance(input);

            // Wrap lexer around a token stream
            var tokens = new CommonTokenStream(lex);

            // Transforms tokens into a parse tree
            var parser = parserClass.getDeclaredConstructor(TokenStream.class).newInstance(tokens);

            lex.removeErrorListeners();
            lex.addErrorListener(new ThrowingErrorListener());

            var ruleMethod = SpecsSystem.findMethod(parser.getClass(), parserRule);

            var node = (ParseTree) ruleMethod.invoke(parser);

            //var node = (ParseTree) SpecsSystem.invoke(parser, parserRule);
            if (parser.getNumberOfSyntaxErrors() > 0) {
                return null;
            } else {

                System.out.println(parser);

                var root = AntlrToJmmNodeConverter.convert(node, parser);
                List<String> ignoreList = AntlrParser.getIgnoreList(parser);
                if (!ignoreList.isEmpty()) {
                    clean(root, ignoreList);
                }

                return root;


            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }


    }

    private void clean(AnyNode root, List<String> ignoreList) {
        var cleanup = new JmmNodeCleanup(ignoreList);
        cleanup.visit(root);
    }

    /*
    public static String parse(String code, String lexerQualifiedName, String parserQualifiedName, String parserRule) {
        // Instantiate JmmParser
        //SimpleParser parser = new SimpleParser();
        JmmParser parser = new AntlrParser(parserRule);

        // Parse stage
        JmmParserResult parserResult = parser.parse(srlCode, Map.of());

        var errorReport = parserResult.getReports().stream()
                .filter(r -> r.getType() == ReportType.ERROR)
                .findFirst()
                .orElse(null);

        if (errorReport != null) {
            if (errorReport.getException().isPresent()) {
                System.out.println("Parsing error:" + errorReport.getException().get());
                return "null";
            } else {
                throw new RuntimeException("Found at least one error report: " + errorReport);
            }
        }

        var jmmNodeToJson = new JmmNodeToJson();
        return jmmNodeToJson.toJson(parserResult.getRootNode());
    }
*/

    public static List<String> getIgnoreList(Parser parser) {
        try {

            var fields = Arrays.asList(parser.getClass().getDeclaredFields())
                    .stream()
                    .filter(f -> f.getName().equals("ignoreList"))
                    .findFirst()
                    .map(f -> {
                        try {
                            f.setAccessible(true);
                            return ((String[]) f.get(parser));
                        } catch (IllegalAccessException e) {
                            return new String[]{};
                        }
                    }).orElse(new String[]{});

            return Arrays.asList(fields);
            // if(root.)
        } catch (Exception e) {

            // No list of ignores specified.
            return Collections.emptyList();
        }
    }

}
