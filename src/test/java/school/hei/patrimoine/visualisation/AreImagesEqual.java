package school.hei.patrimoine.visualisation;

import lombok.SneakyThrows;

import javax.imageio.ImageIO;
import java.io.File;
import java.util.function.BiFunction;

public class AreImagesEqual implements BiFunction<File, File, Boolean> {
  @SneakyThrows
  @Override
  public Boolean apply(File image1, File image2) {
    // https://stackoverflow.com/questions/8567905/how-to-compare-images-for-similarity-using-java
    var biA = ImageIO.read(image1);
    var dbA = biA.getData().getDataBuffer();
    int sizeA = dbA.getSize();
    var biB = ImageIO.read(image2);
    var dbB = biB.getData().getDataBuffer();
    int sizeB = dbB.getSize();

    if (sizeA == sizeB) {
      for (int i = 0; i < sizeA; i++) {
        if (dbA.getElem(i) != dbB.getElem(i)) {
          return false;
        }
      }
      return true;
    } else {
      return false;
    }
  }
}
