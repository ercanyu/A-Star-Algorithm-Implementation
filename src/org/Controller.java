package org;

import java.util.ArrayList;
import java.util.Random;

public class Controller {
	public Controller() {

	}

	public ArrayList<City> generateCities() {
		ArrayList<City> cities = new ArrayList<City>();
		Random generator = new Random();
		int x, y;

		while (cities.size() < Application.CITY_COUNT) {
			x = generator.nextInt(Application.MAP_SIZE);
			y = generator.nextInt(Application.MAP_SIZE);

			if (!isGenerated(cities, x, y)) {
				cities.add(new City(cities.size(), x, y));
			}
		}
		return cities;
	}

	public boolean isGenerated(ArrayList<City> cities, int x, int y) {
		boolean generated = false;
		for (City city : cities) {
			if (city.getX() == x && city.getY() == y) {
				generated = true;
			}
		}
		return generated;
	}

	public void connectCities(ArrayList<City> cities) {
		for (City c1 : cities) {
			for (City c2 : cities) {
				if (c1 != c2) {
					Path path = new Path(c2, airDistance(c1, c2));
					c1.getPathList().add(path);
				}
			}
		}
	}

	public double airDistance(City city1, City city2) {
		double xDiff = city1.getX() - city2.getX();
		double yDiff = city1.getY() - city2.getY();
		return Math.sqrt(xDiff * xDiff + yDiff * yDiff);
	}

	// updates the cities after finding MST and adds the roads
	public void updateCities(ArrayList<City> MST, ArrayList<City> cities) {

		for (City c : cities) {
			c.getPathList().clear();
		}
		double distance;

		for (int i = 0; i < MST.size(); i++) {
			for (City city : cities) {
				if (MST.get(i).getCityId() == city.getCityId()) {
					city.getPathList().add(MST.get(i).getPathList().get(0));
					distance = MST.get(i).getPathList().get(0).getDistance();
					cities.get(MST.get(i).getPathList().get(0).getConnectedCity().getCityId()).getPathList()
							.add(new Path(city, distance));
				}
			}
		}
	}

	public void generateRandomPaths(ArrayList<City> cities, int count) {
		Random generator = new Random();
		int city1, city2;
		int pathCount = 0;
		double distance;

		while (pathCount < count) {
			city1 = generator.nextInt(Application.CITY_COUNT);
			city2 = generator.nextInt(Application.CITY_COUNT);

			if (city1 != city2) {
				distance = airDistance(cities.get(city1), cities.get(city2));
				Path path1 = new Path(cities.get(city1), distance);
				Path path2 = new Path(cities.get(city2), distance);
				cities.get(city1).getPathList().add(path2);
				cities.get(city2).getPathList().add(path1);
				pathCount++;
			}
		}
	}

}
