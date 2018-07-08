package com.demo.projects.osgi.demo.core.service.impl;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.metatype.annotations.Designate;
import com.demo.projects.osgi.demo.core.WeatherResponse;
import com.demo.projects.osgi.demo.core.service.WeatherService;
import com.demo.projects.osgi.demo.core.service.WeatherServiceConfiguration;
import com.google.gson.Gson;
import com.demo.projects.osgi.demo.core.CityInformation;
@Component(service = WeatherService.class, immediate = true)
@Designate(ocd = WeatherServiceConfiguration.class)
public class WeatherServiceImpl implements WeatherService {
	private static final String API_URL = "https://api.openweathermap.org/data/2.5/weather?";
	WeatherServiceConfiguration weatherServiceConfiguration;
	private Map<String, CityInformation> cityLatLongMapping;
	private List<String> cities;

	private void populateCityInformation() {
		String[] mappings = this.weatherServiceConfiguration.getCityLatLongMapping();
		cityLatLongMapping = new HashMap<String, CityInformation>();
		cities = new ArrayList<String>();
		if (mappings.length > 0) {
			for (int i = 0; i < mappings.length; i++) {
				String info[] = mappings[i].split("\\|");
				CityInformation cityInfo = new CityInformation();
				cityInfo.setCityName(info[0]);
				cityInfo.setLat(Double.valueOf(info[1]));
				cityInfo.setLog(Double.valueOf(info[2]));
				cityLatLongMapping.put(cityInfo.getCityName(), cityInfo);
				cities.add(cityInfo.getCityName());		
			}
		}
	}
	@Override
	public double getWeather(String cityName) {
		String url = API_URL;
		double temp = 0;
		try {
			if (cityLatLongMapping != null) {
				CityInformation cityInfo = cityLatLongMapping.get(cityName);
				if (cityInfo != null) {
					url += "lat=" + cityInfo.getLat() + "&";
					url += "lon=" + cityInfo.getLog() + "&";
					url += "appid=" + this.weatherServiceConfiguration.getOpenAPIAppId();
					URL httpurl = new URL(url);
					HttpURLConnection connection = (HttpURLConnection) httpurl.openConnection();
					connection.setRequestMethod("GET");
					InputStream is = connection.getInputStream();
					Reader reader = new InputStreamReader(is);
					WeatherResponse wr = new Gson().fromJson(reader, WeatherResponse.class);
					temp = wr.getMain().getTemp();
					temp = temp - 273.15;
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return temp;
	}
	@Activate
	public void activate(WeatherServiceConfiguration config) {
		this.weatherServiceConfiguration = config;
		 populateCityInformation();
	}
	public List<String> getCities() {
		return cities;
	}
}
