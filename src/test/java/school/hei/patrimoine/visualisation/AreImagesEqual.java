package school.hei.patrimoine.visualisation;

import static school.hei.Env.IS_LOCAL_ENV;

import java.awt.image.DataBuffer;
import java.io.File;
import java.util.function.BiFunction;
import javax.imageio.ImageIO;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AreImagesEqual implements BiFunction<File, File, Boolean> {

  private final double maxDiffRatio;

  public AreImagesEqual() {
    maxDiffRatio =
        IS_LOCAL_ENV ? 0.01 : 0.08 /* Typically, images gen on CI differ (prolly due to OS) */;
  }

  @SneakyThrows
  private static DataBuffer imageDb(File file) {
    var bi = ImageIO.read(file);
    return bi.getData().getDataBuffer();
  }

  public Boolean apply(File image1, File image2) {
    // https://stackoverflow.com/questions/8567905/how-to-compare-images-for-similarity-using-java
    var db1 = imageDb(image1);
    var db2 = imageDb(image2);
    var size1 = db1.getSize();
    var size2 = db2.getSize();
    if (size1 != size2) {
      log.error(
          "Image size mismatch: img1={}, img2={}, img1.size={}, img2.size={}",
          image1,
          image2,
          size1,
          size2);
      return false;
    }

    var nbDiff = 0;
    for (int i = 0; i < size1; i++) {
      if (db1.getElem(i) != db2.getElem(i)) {
        nbDiff++;
      }
    }

    var diffRatio = nbDiff / (double) size1;
    if (diffRatio > maxDiffRatio) {
      log.error(
          "Image mismatch:img1={}, img2={}, nbDiff={}, img.size={}, diffRatio={}, maxDiffRatio={}",
          image1,
          image2,
          nbDiff,
          size1,
          diffRatio,
          maxDiffRatio);
      return false;
    }
    return true;
  }
}
