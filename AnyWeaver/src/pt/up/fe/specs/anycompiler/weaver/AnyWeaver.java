package pt.up.fe.specs.anycompiler.weaver;

import org.lara.interpreter.weaver.interf.AGear;
import org.lara.interpreter.weaver.interf.JoinPoint;
import org.lara.interpreter.weaver.options.WeaverOption;
import org.lara.language.specification.dsl.LanguageSpecification;
import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.anycompiler.ast.AnyNode;
import pt.up.fe.specs.anycompiler.ast.GenericAnyNode;
import pt.up.fe.specs.anycompiler.parsers.JsonParser;
import pt.up.fe.specs.anycompiler.weaver.abstracts.weaver.AAnyWeaver;
import pt.up.fe.specs.util.SpecsIo;
import pt.up.fe.specs.util.SpecsLogs;

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
    public JoinPoint getRootJp() {
        return AnyJoinpoints.create(root, this);
    }
}
