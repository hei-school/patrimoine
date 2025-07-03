package school.hei.patrimoine.visualisation.swing.ihm.google;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.function.Supplier;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import school.hei.patrimoine.cas.CasSet;
import school.hei.patrimoine.cas.CasSetAnalyzer;
import school.hei.patrimoine.compiler.CasFileCompiler;
import school.hei.patrimoine.compiler.JavaFileNameExtractor;
import school.hei.patrimoine.compiler.PatrimoineDocsCompiler;
import school.hei.patrimoine.compiler.PatrimoineFileCompiler;
import school.hei.patrimoine.google.GoogleApi;
import school.hei.patrimoine.modele.Patrimoine;
import school.hei.patrimoine.visualisation.swing.ihm.google.modele.GoogleLinkList;
import school.hei.patrimoine.visualisation.swing.ihm.google.modele.NamedID;
import school.hei.patrimoine.visualisation.swing.ihm.google.modele.NamedSnippet;

@RequiredArgsConstructor
@SuppressWarnings("all")
public class JavaGoogleLinkCompiler implements GoogleLinkListCompiler {
  private final File driveDirectory;
  private final GoogleApi googleApi;
  private final GoogleApi.GoogleAuthenticationDetails authDetails;

  @Override
  public List<Patrimoine> apply(GoogleLinkList<NamedID> ids) {
    List<NamedSnippet> codePatrimoinesVisualisables = new ArrayList<>();
    List<Patrimoine> patrimoines = new ArrayList<>();

    for (var id : ids.docsLinkList()) {
      codePatrimoinesVisualisables.add(extractSnippet(id));
    }

    for (var namedId : ids.driveLinkList()) {
      googleApi.downloadDriveFile(authDetails, new JavaFileNameExtractor(), namedId.id());
    }

    List<File> driveFiles =
        Optional.ofNullable(driveDirectory.listFiles((dir, name) -> name.endsWith(".java")))
            .map(Arrays::asList)
            .orElseGet(Collections::emptyList);
    File casSetFile = null;

    for (NamedSnippet codePatrimoine : codePatrimoinesVisualisables) {
      patrimoines.add(compilePatrimoine(codePatrimoine));
    }

    for (File driveFile : driveFiles) {
      if (isCasSetFile(driveFile)) {
        casSetFile = driveFile;
      } else if (isCasFile(driveFile)) {
        compileCas(driveFile.getAbsolutePath());
      } else {
        patrimoines.add(compilePatrimoine(driveFile.getAbsolutePath()));
      }
    }

    assert casSetFile != null;
    if (casSetFile.exists()) {
      compileCasSet(casSetFile.getAbsolutePath());
    }

    return patrimoines;
  }

  private boolean isCasFile(File file) {
    var casImport = "import school.hei.patrimoine.cas.Cas;";
    try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
      String line;
      while ((line = reader.readLine()) != null) {
        if (line.contains(casImport)) {
          return true;
        }
      }
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    return false;
  }

  private boolean isCasSetFile(File file) {
    var casSetImport = "import school.hei.patrimoine.cas.CasSet;";
    try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
      String line;
      while ((line = reader.readLine()) != null) {
        if (line.contains(casSetImport)) {
          return true;
        }
      }
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    return false;
  }

  private Patrimoine compilePatrimoine(NamedSnippet namedSnippet) {
    PatrimoineDocsCompiler patrimoineDocsCompiler = new PatrimoineDocsCompiler();
    String filename = new JavaFileNameExtractor().apply(namedSnippet.snippet());

    return (patrimoineDocsCompiler.apply(filename, namedSnippet.snippet()));
  }

  private Patrimoine compilePatrimoine(String filePath) {
    PatrimoineFileCompiler patrimoineFileCompiler = new PatrimoineFileCompiler();

    return (patrimoineFileCompiler.apply(filePath));
  }

  private void compileCas(String filePath) {
    CasFileCompiler casFileCompiler = new CasFileCompiler();

    casFileCompiler.apply(filePath);
  }

  @SneakyThrows
  private void compileCasSet(String filePath) {
    CasFileCompiler casFileCompiler = new CasFileCompiler();
    CasSetAnalyzer casSetAnalyzer = new CasSetAnalyzer();
    var casSet = casFileCompiler.apply(filePath);
    var casSetSupplier = (Supplier<CasSet>) casSet.getDeclaredConstructor().newInstance();

    casSetAnalyzer.accept(casSetSupplier.get());
  }

  public NamedSnippet extractSnippet(NamedID namedID) {
    var code = googleApi.readDocsContent(authDetails, String.valueOf(namedID.id()));
    return new NamedSnippet(namedID.name(), code);
  }
}
