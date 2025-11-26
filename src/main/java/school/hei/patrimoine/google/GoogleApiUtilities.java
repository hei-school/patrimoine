package school.hei.patrimoine.google;

import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import java.io.File;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GoogleApiUtilities {
  private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();

  public static JsonFactory getJsonFactory() {
    return JSON_FACTORY;
  }

  public static String getUserHome() {
    return System.getProperty("user.home");
  }

  public static String getTempDirectory() {
    return System.getProperty("java.io.tmpdir");
  }

  public static String getApplicationName() {
    return "patrimoine";
  }

  public static String getDownloadDirectoryPath() {
    return getApplicationHomeDirectory() + "/download";
  }

  public static String getTokenDirectoryPath() {
    return getApplicationHomeDirectory() + "/google/tokens";
  }

  public static String getCredentialFilePath() {
    return getApplicationHomeDirectory() + "/google/client.json";
  }

  public static String getCacheDirectoryPath() {
    return getApplicationHomeDirectory() + "/cache";
  }

  public static String getStagingDirectoryPath() {
    return getApplicationHomeDirectory() + "/staged";
  }

  public static String getApplicationHomeDirectory() {
    return getUserHome() + "/." + getApplicationName();
  }

  public static void setup() {
    var credentialsDirectory = new File(getTokenDirectoryPath());
    if (!credentialsDirectory.exists() && !credentialsDirectory.mkdirs()) {
      log.warn("Failed to create credentials directory");
    }

    var downloadDirectory = new File(getDownloadDirectoryPath());
    if (!downloadDirectory.exists() && !downloadDirectory.mkdirs()) {
      log.warn("Failed to create download directory");
    }

    var cacheDirectory = new File(getCacheDirectoryPath());
    if (!cacheDirectory.exists() && !cacheDirectory.mkdirs()) {
      log.warn("Failed to create cache directory");
    }

    var stagedDirectory = new File(getStagingDirectoryPath());
    if (!stagedDirectory.exists() && !stagedDirectory.mkdirs()) {
      log.warn("Failed to create staged directory");
    }
  }
}
