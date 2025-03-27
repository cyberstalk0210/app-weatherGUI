# WeatherApp GUI

## Overview
WeatherApp GUI is a simple desktop application that provides real-time weather updates based on user input. The application fetches weather data from an external API and displays essential weather information such as temperature, humidity, wind speed, and weather conditions using a graphical user interface.

## Features
- User-friendly graphical interface.
- Search functionality to get weather data for any location.
- Displays temperature, humidity, wind speed, and weather conditions.
- Dynamic weather icons that change based on the weather condition.

## Technologies Used
- Java (Swing for GUI)
- Jackson Library for JSON parsing
- OpenWeather API (or any other weather API used)

## Installation
1. Clone the repository:
   ```sh
   git clone https://github.com/your-username/weatherapp-gui.git
   ```
2. Navigate to the project directory:
   ```sh
   cd weatherapp-gui
   ```
3. Ensure you have Java installed (JDK 8 or later).
4. Install dependencies (Jackson library):
   ```sh
   mvn install
   ```

## Usage
1. Run the application:
   ```sh
   java -jar WeatherAppGUI.jar
   ```
2. Enter a city name in the search box and press the search button.
3. The app will fetch and display the current weather details.

## Troubleshooting
- If you get a `NullPointerException`, ensure the API key is correctly set in the `WeatherApp` class.
- If a `400 Bad Request` error occurs, check the API request URL and parameters.
- Make sure all images for weather conditions are correctly placed in the `src/assets/` directory.

## Contribution
Feel free to fork this repository and submit pull requests for improvements or bug fixes.

## License
This project is licensed under the MIT License.

## Author
Developed by [Your Name]

