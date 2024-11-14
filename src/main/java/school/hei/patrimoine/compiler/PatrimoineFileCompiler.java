package school.hei.patrimoine.compiler;

import lombok.SneakyThrows;
import school.hei.patrimoine.modele.Patrimoine;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Path;
import java.util.function.Function;
import java.util.function.Supplier;

import static javax.tools.ToolProvider.getSystemJavaCompiler;

public class PatrimoineFileCompiler implements Function<String, Patrimoine> {

  private static final String COMPILE_DIR_NAME =
      System.getProperty("user.home") + "/.patrimoine/compile";

  static {
    new File(COMPILE_DIR_NAME).mkdirs();
  }

  @SneakyThrows
  @Override
  public Patrimoine apply(String filePath) {
    var ioDirPath = Path.of(COMPILE_DIR_NAME);
    var sourcePath = Path.of(filePath);
    var className = getClassNameFromPath(filePath);

    compile(ioDirPath, sourcePath);

    var dynamicClass = loadClass(className, ioDirPath);
    var patrimoineSupplier =
            (Supplier<Patrimoine>) dynamicClass.getDeclaredConstructor().newInstance();
    return patrimoineSupplier.get();
  }

  private Class<?> loadClass(String className, Path ioDirPath)
      throws MalformedURLException, ClassNotFoundException {
    var classLoader = URLClassLoader.newInstance(new URL[] {ioDirPath.toUri().toURL()});

    return Class.forName(className, true, classLoader);
  }

  private static String getClassNameFromPath(String filePath) {
    var path = Path.of(filePath);
    String fileName = path.getFileName().toString();
    return fileName.substring(0, fileName.lastIndexOf('.'));
  }

  private void compile(Path ioDirPath, Path sourcePath) {
    var compiler = getSystemJavaCompiler();
    int result =
        compiler.run(
            null,
            null,
            null,
            "-d",
            ioDirPath.toFile().getAbsolutePath(),
            sourcePath.toFile().getAbsolutePath());

    if (result != 0) {
      throw new RuntimeException("Compilation failed. Error code=" + result);
    }
  }
}
