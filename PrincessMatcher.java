package whichPrincessAreU;

import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;

import java.io.File;
import java.util.Arrays;

public class PrincessMatcher {

    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    public String findMostSimilarPrincess(String userPhotoPath, String imagePath, String haarCascadePath) {
        CascadeClassifier faceDetector = new CascadeClassifier(haarCascadePath);

        // Kullanıcı fotoğrafını yükleme
        Mat userImage = Imgcodecs.imread(userPhotoPath);
        if (userImage.empty()) {
            System.out.println("Kullanıcı fotoğrafı yüklenemedi!");
            return null;
        }

        // Kullanıcı fotoğrafındaki yüzü algılama
        MatOfRect userFaces = new MatOfRect();
        faceDetector.detectMultiScale(userImage, userFaces);

        if (userFaces.toArray().length == 0) {
            System.out.println("Kullanıcı fotoğrafında yüz algılanamadı!");
            return null;
        }

        // Kullanıcının yüz bölgesini kırpma
        Rect userFace = userFaces.toArray()[0];
        Mat userFaceImage = new Mat(userImage, userFace);

        // Prenses fotoğraflarını analiz etme
        File folder = new File(imagePath);
        File[] princessFiles = folder.listFiles((dir, name) -> name.endsWith(".png"));
        if (princessFiles == null || princessFiles.length == 0) {
            System.out.println("Prenses görselleri bulunamadı!");
            return null;
        }

        double bestMatchScore = Double.MIN_VALUE;
        String bestMatchPrincess = null;

        for (File princessFile : princessFiles) {
            System.out.println("Karşılaştırılıyor: " + princessFile.getName());

            Mat princessImage = Imgcodecs.imread(princessFile.getAbsolutePath());
            if (princessImage.empty()) {
                System.out.println("Prenses görseli yüklenemedi: " + princessFile.getName());
                continue;
            }

            // Görsellerin boyutlarını eşitleme
            Mat resizedPrincessImage = new Mat();
            Imgproc.resize(princessImage, resizedPrincessImage, userFaceImage.size());

            // Histogram karşılaştırma
            double similarity = compareHistograms(userFaceImage, resizedPrincessImage);
            System.out.println("Benzerlik skoru (" + princessFile.getName() + "): " + similarity);

            if (similarity > bestMatchScore) {
                bestMatchScore = similarity;
                bestMatchPrincess = princessFile.getName();
            }
        }

        return bestMatchPrincess;
    }

    private double compareHistograms(Mat img1, Mat img2) {
        // Renkleri HSV formatına dönüştürme
        Mat hsvImg1 = new Mat();
        Mat hsvImg2 = new Mat();
        Imgproc.cvtColor(img1, hsvImg1, Imgproc.COLOR_BGR2HSV);
        Imgproc.cvtColor(img2, hsvImg2, Imgproc.COLOR_BGR2HSV);

        // Maske oluşturma ve saç renklerine odaklanma
        Mat mask1 = new Mat();
        Mat mask2 = new Mat();
        Core.inRange(hsvImg1, new Scalar(0, 10, 0), new Scalar(180, 255, 50), mask1); // Saç rengi için maskeleme
        Core.inRange(hsvImg2, new Scalar(0, 10, 0), new Scalar(180, 255, 50), mask2);

        Mat filteredImg1 = new Mat();
        Mat filteredImg2 = new Mat();
        Core.bitwise_and(hsvImg1, hsvImg1, filteredImg1, mask1); // Maskeyi görüntüye uygula
        Core.bitwise_and(hsvImg2, hsvImg2, filteredImg2, mask2);

        // Histogram hesaplama
        Mat histImg1 = new Mat();
        Mat histImg2 = new Mat();
        Imgproc.calcHist(Arrays.asList(filteredImg1), new MatOfInt(0, 1), new Mat(), histImg1, new MatOfInt(50, 60), new MatOfFloat(0, 180, 0, 256));
        Imgproc.calcHist(Arrays.asList(filteredImg2), new MatOfInt(0, 1), new Mat(), histImg2, new MatOfInt(50, 60), new MatOfFloat(0, 180, 0, 256));

        Core.normalize(histImg1, histImg1, 0, 1, Core.NORM_MINMAX);
        Core.normalize(histImg2, histImg2, 0, 1, Core.NORM_MINMAX);

        // Histogram karşılaştırma (korelasyon yöntemi)
        return Imgproc.compareHist(histImg1, histImg2, Imgproc.HISTCMP_CORREL);
    }
    public static void main(String[] args) {
        PrincessMatcher matcher = new PrincessMatcher();

        // Kullanıcı ve prenses görsellerinin yollarını tanımlayın
        String userPhotoPath = "src/user/user.png";
        String imagePath = "src/images";
        String haarCascadePath = "src/resources/haarcascade_frontalface_default.xml";

        // Benzerlik hesaplaması
        String bestMatchPrincess = matcher.findMostSimilarPrincess(userPhotoPath, imagePath, haarCascadePath);

        if (bestMatchPrincess != null) {
            System.out.println("En çok benzeyen prenses: " + bestMatchPrincess);
        } else {
            System.out.println("Bir eşleşme bulunamadı veya bir hata oluştu.");
        }
    }
}