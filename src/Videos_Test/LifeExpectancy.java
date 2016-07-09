package Videos_Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.data.Feature;
import de.fhpotsdam.unfolding.data.GeoJSONReader;
import de.fhpotsdam.unfolding.marker.Marker;
import de.fhpotsdam.unfolding.providers.Google;
import de.fhpotsdam.unfolding.utils.MapUtils;
import processing.core.PApplet;

public class LifeExpectancy extends PApplet {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	UnfoldingMap map;
	Map<String, Float> lifeExpByCountry;
	List<Feature> countries;
	List<Marker> countryMarkers;
	
	Map<String, Float> lifeExpMap;
	
	public void setup()
	{
		size(800,600,OPENGL);
		map = new UnfoldingMap(this, 50, 50, 700, 500, new Google.GoogleMapProvider());
		MapUtils.createDefaultEventDispatcher(this, map);
		lifeExpByCountry = loadFileExpectancyFromCSV("data/LifeExpectancyWorldBank.csv");
		countries = GeoJSONReader.loadData(this, "data/countries.geo.json");
		countryMarkers = MapUtils.createSimpleMarkers( countries );
		
		map.addMarkers(countryMarkers);
		
		lifeExpMap = lifeExpByCountry;
		
		shadeCountries();
	}
	
	public void draw()
	{
		map.draw();
	}
	
	private Map<String, Float> loadFileExpectancyFromCSV( final String fileName)
	{
		Map<String, Float> lifeExpMap = new HashMap<String, Float>();
		String[] rows = loadStrings( fileName );
		for ( String row : rows )
		{
			String[] columns = row.split(",");
			if ( !columns[5].isEmpty() )
			{
				float value = Float.parseFloat(columns[5]);
				lifeExpMap.put(columns[4], value);
			}
		}
		return lifeExpMap;
	}

	private void shadeCountries()
	{
		for ( Marker marker : countryMarkers )
		{
			String countryId = marker.getId();
			
			if ( lifeExpMap.containsKey(countryId))
			{
				float lifeExp = lifeExpMap.get(countryId);
				int colorLevel = (int) map(lifeExp, 40, 90, 10, 255);
				marker.setColor(color(255-colorLevel, 100, colorLevel));
			}
			else
			{
				marker.setColor(color(150,150,150));
			}
		}
	}
}
