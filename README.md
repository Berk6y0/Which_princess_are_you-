# Princess Matcher
I upload a pdf for this project which we used in ComputerVision Lecture.
## Overview
Princess Matcher is a Java application that determines which princess a user resembles the most based on facial recognition and image comparison. The project uses OpenCV for face detection and histogram comparison to find the best match. Additionally, a graphical user interface (GUI) is provided to allow users to drag and drop their images for analysis.

## Features
- Face detection using OpenCV Haar cascades
- Image similarity comparison based on color histograms
- Drag-and-drop functionality for user image input
- GUI built with Swing for ease of use
- Automatic image resizing and histogram matching

## Technologies Used
- **Programming Language:** Java
- **Libraries:** OpenCV, Swing, AWT
- **Image Processing:** OpenCV (Histogram comparison, Face detection)
- **GUI Framework:** Java Swing

## Installation & Setup

### Prerequisites
- Install [OpenCV](https://opencv.org/) and configure it for Java.
- Ensure Java is installed (JDK 8 or higher).

### Steps
1. Clone the repository:
   ```sh
   git clone https://github.com/yourusername/princess-matcher.git
   cd princess-matcher
   ```
2. Set up OpenCV native library:
   - Download OpenCV and extract it.
   - Add OpenCV `.jar` and native libraries to your project's classpath.
3. Compile and run the application:
   ```sh
   javac -cp ".;opencv-xxx.jar" whichPrincessAreU/*.java
   java -cp ".;opencv-xxx.jar" whichPrincessAreU.PrincessMatcherGUI
   ```

## How It Works
1. The user drags and drops their image into the application.
2. OpenCV detects the face in the uploaded image.
3. The application compares the user's facial features with a predefined set of princess images.
4. The best-matching princess is displayed along with the image.

## Usage
- Run the application.
- Drag and drop your photo into the designated area.
- Click the "Run" button to start the matching process.
- The most similar princess image is displayed as a result.

## File Structure
```
princess-matcher/
│── src/
│   ├── whichPrincessAreU/
│   │   ├── PrincessMatcher.java
│   │   ├── PrincessMatcherGUI.java
│   ├── user/
│   │   ├── user.png (Uploaded user image)
│   ├── images/
│   │   ├── (Princess images)
│   ├── resources/
│   │   ├── haarcascade_frontalface_default.xml
```

## Future Improvements
- Enhance facial similarity comparison using deep learning models.
- Allow users to upload and store their results.
- Improve GUI responsiveness and UX.

## License
This project is licensed under the MIT License.

