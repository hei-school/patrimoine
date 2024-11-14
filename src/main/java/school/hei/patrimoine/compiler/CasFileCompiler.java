package school.hei.patrimoine.compiler;

import lombok.SneakyThrows;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Path;
import java.util.function.Function;

import static javax.tools.ToolProvider.getSystemJavaCompiler;

public class CasFileCompiler implements Function<String, Class<?>> {

    private static final String COMPILE_DIR_NAME =
            System.getProperty("user.home") + "/.patrimoine/compile";
    private static final String DEPENDENCY_JAR =
            System.getProperty("user.home") + "/Downloads/jar/patrimoine-1.0-SNAPSHOT.jar";

    static {
        new File(COMPILE_DIR_NAME).mkdirs();
    }

    private static String getClassNameFromPath(String filePath) {
        return Path.of(filePath).getFileName().toString().replace(".java", "");
    }

    @SneakyThrows
    @Override
    public Class<?> apply(String filePath) {
        var ioDirPath = Path.of(COMPILE_DIR_NAME);
        var sourcePath = Path.of(filePath);
        var className = getClassNameFromPath(filePath);

        compile(ioDirPath, sourcePath);
        return loadClass(className);
    }

    private Class<?> loadClass(String className)
            throws ClassNotFoundException, MalformedURLException {
        var ioDirPath = Path.of(COMPILE_DIR_NAME);

        return Class.forName(className, true, URLClassLoader.newInstance(new URL[]{ioDirPath.toUri().toURL()}));
    }

    private void compile(Path ioDirPath, Path sourcePath) {
        var compiler = getSystemJavaCompiler();
        String classPath = String.join(File.pathSeparator, DEPENDENCY_JAR, COMPILE_DIR_NAME);

        int result = compiler.run(
                null,
                null,
                null,
                "-classpath", classPath,
                "-d", ioDirPath.toFile().getAbsolutePath(),
                sourcePath.toFile().getAbsolutePath()
        );

        if (result != 0) {
            throw new RuntimeException("Compilation failed. Error code=" + result);
        }
    }
}
