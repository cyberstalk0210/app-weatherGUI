package org.example;

import com.fasterxml.jackson.databind.JsonNode;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class WeatherAppGUI extends JFrame {

    private JsonNode weatherData;


    public WeatherAppGUI() {
        super("Weather App");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
//        Set size GUI of our GUI (in pixels)
        setSize(450, 650);
//        load our gui at the center of the screen
        setLocationRelativeTo(null);
//      make our layout manageer null too manually position our components within the gui
        setLayout(null);
        setResizable(false);
        addGuiComponents();

    }

    private void addGuiComponents() {
        JTextField searchTextField = new JTextField();
        searchTextField.setBounds(15,15,351,45);
        searchTextField.setFont(new Font("Dialog",Font.PLAIN,24));
        add(searchTextField);


        JLabel weatherConditionImage = new JLabel(loadImage("src/assets/cloudy.png"));
        weatherConditionImage.setBounds(0,125,450,217);
        add(weatherConditionImage);

        JLabel tempratureText = new JLabel("10 C");
        tempratureText.setBounds(0,350,450,54);
        tempratureText.setFont(new Font("Dialog",Font.BOLD,48));
        tempratureText.setHorizontalAlignment(SwingConstants.CENTER);
        add(tempratureText);

        JLabel weatherConditionDesc = new JLabel("Cloudy");
        weatherConditionDesc.setBounds(0,405,450,36);
        weatherConditionDesc.setFont(new Font("Dialog",Font.PLAIN,32));
        weatherConditionDesc.setHorizontalAlignment(SwingConstants.CENTER);
        add(weatherConditionDesc);

        JLabel humidityImage = new JLabel(loadImage("src/assets/humidity.png"));
        humidityImage.setBounds(15,500,74,66);
        add(humidityImage);

        JLabel humidityText = new JLabel("<html><b>Humidity</b> 100%  </html>");
        humidityText.setBounds(90,500,85,55);
        humidityText.setFont(new Font("Dialog",Font.BOLD,16));
        add(humidityText);

        JLabel windSpeedImage = new JLabel(loadImage("src/assets/windspeed.png"));
        windSpeedImage.setBounds(220,500,74,66);
        add(windSpeedImage);

        JLabel windSpeedText = new JLabel("<html><b>Windspeed </b> 15km/h </html>");
        windSpeedText.setBounds(310,500,85,55);
        windSpeedText.setFont(new Font("Dialog",Font.PLAIN,16));
        add(windSpeedText);

        JButton searchButton = new JButton(loadImage("src/assets/search.png"));
        searchButton.setBounds(15,40,351,45);
        searchButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        searchButton.setBounds(375,13,47,45);

        searchButton.addActionListener(e -> {
             String userInput = searchTextField.getText();

             if (userInput.replaceAll("\\s","").length() <=0) return;

             weatherData = WeatherApp.getWeatherData(userInput);
             String weatherCondition = weatherData.get("weatherCode").asText();

             switch (weatherCondition){
                 case "Cloudy":
                     weatherConditionImage.setIcon(loadImage("src/assets/cloudy.png"));
                     break;
                 case "Clear":
                     weatherConditionImage.setIcon(loadImage("src/assets/clear.png"));
                     break;
                 case "Rain":
                     weatherConditionImage.setIcon(loadImage("src/assets/rain.png"));
                     break;
                 case "Snow":
                     weatherConditionImage.setIcon(loadImage("src/assets/snow.png"));
                     break;
             }

             double temperature = weatherData.get("temperature").asDouble();
             tempratureText.setText(temperature+" °C");

             long humidity = weatherData.get("humidity").asLong();
             humidityText.setText("<html><b>Humidity </b> "+humidity+"%</html>");

             double windSpeed =weatherData.get("windSpeed").asDouble();
             windSpeedText.setText("<html><b>windSpeed </b> "+windSpeed+"km/h </html>");

             weatherConditionDesc.setText(weatherCondition);
         });
        add(searchButton);
    }

    private ImageIcon loadImage(String path) {
        try{
            BufferedImage image = ImageIO.read(new File(path));
            return new ImageIcon(image);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Couldn't load image");
        return null;
    }
}

