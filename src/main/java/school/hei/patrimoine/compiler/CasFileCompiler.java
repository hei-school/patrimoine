package school.hei.patrimoine.compiler;

import static java.io.File.pathSeparator;
import static javax.tools.ToolProvider.getSystemJavaCompiler;
import static school.hei.patrimoine.google.GoogleApi.COMPILE_DIR_NAME;
import static school.hei.patrimoine.google.GoogleApi.PATRIMOINE_JAR_PATH;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Path;
import java.util.function.Function;
import lombok.SneakyThrows;

public class CasFileCompiler implements Function<String, Class<?>> {

  private static final PackageNameExtractor packageNameExtractor = new PackageNameExtractor();

  static {
    File tokensDirectory = new File(COMPILE_DIR_NAME);
    if (!tokensDirectory.exists()) {
      tokensDirectory.mkdirs();
    }
  }

  private static String getClassNameFromPath(String filePath) {
    var path = Path.of(filePath);
    String fileName = path.getFileName().toString();
    String className = fileName.substring(0, fileName.lastIndexOf('.'));
    String packageName = packageNameExtractor.apply(filePath);
    return packageName + "." + className;
  }

  @Override
  public Class<?> apply(String filePath) {
    var ioDirPath = Path.of(COMPILE_DIR_NAME);
    var sourcePath = Path.of(filePath);
    var className = getClassNameFromPath(filePath);

    compile(ioDirPath, sourcePath);
    return loadClass(className);
  }

  @SneakyThrows
  private Class<?> loadClass(String className) {
    var ioDirPath = Path.of(COMPILE_DIR_NAME);
    boolean INITIALIZE_CLASS = true;

    return Class.forName(
        className,
        INITIALIZE_CLASS,
        URLClassLoader.newInstance(new URL[] {ioDirPath.toUri().toURL()}));
  }

  private void compile(Path ioDirPath, Path sourcePath) {
    var compiler = getSystemJavaCompiler();
    String classPath = String.join(pathSeparator, PATRIMOINE_JAR_PATH, COMPILE_DIR_NAME);

    int result =
        compiler.run(
            null,
            null,
            null,
            "-classpath",
            classPath,
            "-d",
            ioDirPath.toFile().getAbsolutePath(),
            sourcePath.toFile().getAbsolutePath());

    if (result != 0) {
      throw new RuntimeException("Compilation failed. Error code=" + result);
    }
  }
}
