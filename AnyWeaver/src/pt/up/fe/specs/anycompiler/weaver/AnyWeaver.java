package pt.up.fe.specs.anycompiler.weaver;

import org.lara.interpreter.weaver.ast.AstMethods;
import org.lara.interpreter.weaver.interf.AGear;
import org.lara.interpreter.weaver.interf.JoinPoint;
import org.lara.interpreter.weaver.options.WeaverOption;
import org.lara.interpreter.weaver.utils.LaraResourceProvider;
import org.lara.language.specification.dsl.LanguageSpecification;
import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.anycompiler.ast.AnyNode;
import pt.up.fe.specs.anycompiler.ast.GenericAnyNode;
import pt.up.fe.specs.anycompiler.parsers.JsonParser;
import pt.up.fe.specs.anycompiler.weaver.abstracts.weaver.AAnyWeaver;
import pt.up.fe.specs.util.SpecsIo;
import pt.up.fe.specs.util.SpecsLogs;
import pt.up.fe.specs.util.providers.ResourceProvider;

import java.io.File;
import java.util.*;

/**
 * Weaver Implementation for SmaliWeaver<br>
 * Since the generated abstract classes are always overwritten, their implementation should be done by extending those
 * abstract classes with user-defined classes.<br>
 * The abstract class {@link pt.up.fe.specs.anycompiler.weaver.abstracts.AAnyWeaverJoinPoint} can be used to add
 * user-defined methods and fields which the user intends to add for all join points and are not intended to be used in
 * LARA aspects.
 *
 * @author Lara Weaver Generator
 */
public class AnyWeaver extends AAnyWeaver {

    private static final String WEAVER_NAME = "AnyWeaver";
    private static final String WEAVER_API_NAME = "@specs-feup/anyweaver";

    private DataStore args;
    private AnyNode root;

    public static LanguageSpecification buildLanguageSpecification() {
        return LanguageSpecification.newInstance(() -> "anycompiler/weaverspecs/joinPointModel.xml",
                () -> "anycompiler/weaverspecs/artifacts.xml",
                () -> "anycompiler/weaverspecs/actionModel.xml");
    }

    public AnyWeaver() {
        root = null;
    }

    /**
     * Warns the lara interpreter if the weaver accepts a folder as the application or only one file at a time.
     *
     * @return true if the weaver is able to work with several files, false if only works with one file
     */
    @Override
    public boolean handlesApplicationFolder() {
        // Can the weaver handle an application folder?
        return true;
    }

    /**
     * Set a file/folder in the weaver if it is valid file/folder type for the weaver.
     *
     * @param sources   the file with the source code
     * @param outputDir output directory for the generated file(s)
     * @param args      arguments to start the weaver
     * @return true if the file type is valid
     */
    @Override
    public boolean begin(List<File> sources, File outputDir, DataStore args) {
        this.args = args;

        // For now, using json parser

        var sourceFiles = getSourceFiles(sources);

        // Parse each source file
        var parser = new JsonParser("type", "children", true);

        var fileNodes = sourceFiles.stream()
                .map(f -> parser.parse(SpecsIo.read(f)))
                .toList();

        root = new GenericAnyNode("app", fileNodes);

        return true;
    }


    private LanguageSpecification buildLangSpec() {
        // TODO Auto-generated method stub
        return null;
    }

    private List<File> getSourceFiles(List<File> sources) {
        var sourceFiles = new ArrayList<File>();

        for (var source : sources) {
            if (!source.exists()) {
                SpecsLogs.info("Could not find source '" + source + "'");
                continue;
            }

            sourceFiles.addAll(SpecsIo.getFilesRecursive(source, Set.of("json")));
        }

        return sourceFiles;
    }

    @Override
    public AstMethods getAstMethods() {
        // TODO Auto-generated method stub
        return super.getAstMethods();
    }

    /**
     * Return a JoinPoint instance of the language root, i.e., an instance of APlaceholder
     *
     * @return an instance of the join point root/program
     */
    @Override
    public JoinPoint select() {
        return null;
    }

    /**
     * Closes the weaver to the specified output directory location, if the weaver generates new file(s)
     *
     * @return if close was successful
     */
    @Override
    public boolean close() {
        // Terminate weaver execution with final steps required and writing output files
        return true;
    }

    /**
     * Returns a list of Gears associated to this weaver engine
     *
     * @return a list of implementations of {@link AGear} or null if no gears are available
     */
    @Override
    public List<AGear> getGears() {
        return Collections.emptyList(); // i.e., no gears currently being used
    }

    /**
     * Returns Weaving Engine as a SmaliWeaver
     */
    public static AnyWeaver getAnyWeaver() {
        return (AnyWeaver) getThreadLocalWeaver();
    }

    @Override
    public List<WeaverOption> getOptions() {
        return List.of();
    }

    @Override
    protected LanguageSpecification buildLangSpecs() {
        return buildLanguageSpecification();
    }

    @Override
    public String getName() {
        return WEAVER_NAME;
    }

    @Override
    public String getWeaverApiName() {
        return WEAVER_API_NAME;
    }

    @Override
    public List<ResourceProvider> getAspectsAPI() {
        return Collections.emptyList();
    }

    @Override
    public JoinPoint getRootJp() {
        return AnyJoinpoints.create(root);
    }

    @Override
    protected List<LaraResourceProvider> getWeaverNpmResources() {
        return Arrays.asList(AnyWeaverApiJsResource.values());
    }
}
