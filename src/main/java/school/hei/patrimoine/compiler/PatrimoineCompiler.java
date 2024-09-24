package school.hei.patrimoine.compiler;

import static javax.tools.ToolProvider.getSystemJavaCompiler;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.BiFunction;
import java.util.function.Supplier;
import lombok.SneakyThrows;
import school.hei.patrimoine.modele.Patrimoine;

public class PatrimoineCompiler implements BiFunction<String, String, Patrimoine> {

  private static final String COMPILE_DIR_NAME =
      System.getProperty("user.home") + "/.patrimoine/compile";

  static {
    new File(COMPILE_DIR_NAME).mkdirs();
  }

  @SneakyThrows
  @Override
  public Patrimoine apply(String className, String javaSource) {
    var ioDirPath = Path.of(COMPILE_DIR_NAME);
    var sourcePath = Path.of(ioDirPath + "/" + className + ".java");
    Files.write(sourcePath, javaSource.getBytes());

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
