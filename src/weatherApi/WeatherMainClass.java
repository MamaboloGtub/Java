package weatherApi;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Scanner;
import java.util.TimeZone;

import org.json.JSONArray;
import org.json.JSONObject;

public class WeatherMainClass {
	private static final String API_KEY = System.getenv("MY_WEATHER_API_KEY");
	private static final String BASE_URL = "http://api.openweathermap.org/data/2.5/forecast?q=";
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
			System.out.print("Enter your city: ");
			String city = reader.readLine().trim();
			String urlString = BASE_URL + city + "&appid=" + API_KEY + "&units=metric";
			String dataString = fetchWeatherData(urlString);
			if (dataString != null) {
				parseAndDisplayWeatherData(dataString);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public static String fetchWeatherData(String urlString) throws IOException {
		URL url = new URL(urlString);
		StringBuilder data = new StringBuilder();
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setRequestMethod("GET");
		connection.connect();
		
		int responseCode = connection.getResponseCode();
		
		if (responseCode != 200) {
			throw new RuntimeException("HttpResponseCode: " + responseCode);
			
		} else {
			Scanner scanner = new Scanner(url.openStream());
			while (scanner.hasNext()) {
				data.append(scanner.nextLine());
			}
			scanner.close();
		}
		return data.toString();
	}
	public static void parseAndDisplayWeatherData(String data) {
        JSONObject jsonObject = new JSONObject(data);
        JSONArray weatherArray = jsonObject.getJSONArray("list");

        for (int i = 0; i < weatherArray.length(); i++) {
            JSONObject weatherObject = weatherArray.getJSONObject(i);
            JSONObject main = weatherObject.getJSONObject("main");
            long dateTime = weatherObject.getLong("dt");

            double temp = main.getDouble("temp");
            double tempMin = main.getDouble("temp_min");
            double tempMax = main.getDouble("temp_max");
            double humidity = main.getDouble("humidity");
            String weatherDate = convertTimeStamptoTime(dateTime);
            
            //.out.println("City: " + cityName);
            System.out.println("Date/Time: " + weatherDate + " GMT");
            System.out.println("Temperature: " + temp + "°C");
            System.out.println("Min Temperature: " + tempMin + "°C");
            System.out.println("Max Temperature: " + tempMax + "°C");
            System.out.println("Humidity: " + humidity + "%");
            System.out.println("-------------------------------");
        }
    }
	private static String convertTimeStamptoTime(long timeStamp) {
		Date date = new Date(timeStamp*1000);
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return simpleDateFormat.format(date);
	}
}
