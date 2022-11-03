import time
import requests
import polyline
import geopy.distance
import numpy as np
import json
import math
import argparse

BASE_URL = 'http://localhost:8080'

drivers = {}

class Driver:
	def __init__(self, username: str, longitude: float, latitude: float, status: str, route: list, rideId: str):
		self.username = username
		self.longitude = longitude
		self.latitude = latitude
		self.route = route
		self.state = 'WAITING'
		self.ride_status = status
		self.locations = []
		self.visiting = 0
		self.route_visited = []
		self.route_visited_colors = []
		self.ride_id = rideId

	def get_route(self, longitude, latitude, next_longitude, next_latitude):
		loc = "{},{};{},{}".format(longitude, latitude, next_longitude, next_latitude)
		url = f"http://router.project-osrm.org/route/v1/driving/{loc}"
		r = requests.get(url) 
		if r.status_code!= 200:
			return {}
		res = r.json()   
		temp_routes = polyline.decode(res['routes'][0]['geometry'])
		routes = []
		for loc in temp_routes:
			routes.append((loc[1], loc[0]))
		return routes

	def send_location(self):
		payload = {
			'longitude': self.longitude,
			'latitude': self.latitude
		}

		r = requests.put(f'{BASE_URL}/driver/location/{self.username}', json=payload) 

	def send_start(self):
		payload = {
			'longitude': self.longitude,
			'latitude': self.latitude
		}
		print("\n\nSTARTING RIDE...\n\n")
		r = requests.put(f'{BASE_URL}/ride/start/{self.ride_id}', json=payload)
		print(r)

	def send_end(self):
		payload = {
			'longitude': self.longitude,
			'latitude': self.latitude
		}
		print("\n\ENDING RIDE...\n\n")
		r = requests.put(f'{BASE_URL}/ride/end/{self.ride_id}', json=payload)
		print(r)

	def _finish_log(self):
		print("DONE!")
		f = {'arr': [self.route_visited, self.route_visited_colors]}
		with open('route_visited.json', 'w', encoding ='utf8') as json_file:
			json.dump(f, json_file, ensure_ascii = False)

	def _set_new_location(self, curr, succ):
		dist = self._distance(curr, succ) 
		if dist > 40:
			d = dist // 40
			self.longitude = (curr[0]*(d - 1) + succ[0]) / d
			self.latitude = (curr[1]*(d - 1) + succ[1]) / d
		else:
			self.longitude = succ[0]
			self.latitude = succ[1]

	def update_location(self):
		if self.state != 'DRIVING':
			return
		curr = self.longitude, self.latitude
		succ = self.locations[self.visiting]
		if self._check_visited(curr, succ):
			self.visiting += 1
			if self.visiting >= len(self.locations):
				self.route = None
				self.state = 'WAITING'
				self._finish_log()
				self.send_end()
			elif self.visiting == self.start_idx:
				self.send_start()
		else:
			self._set_new_location(curr, succ)

	def extract_locations(self):
		temp_locs = []
		curr = (self.longitude, self.latitude)
		temp_locs.append(curr)
		for location in self.route['locations']:
			lon = location['longitude']
			lat = location['latitude']
			temp_locs.append((lon, lat))

		self.locations.append(curr)
		
		for i in range(1, len(temp_locs)):
			routes = self.get_route(*temp_locs[i-1], *temp_locs[i])
			self.locations.extend(routes)
			self.visiting = 0
			if i == 1:
				self.start_idx = len(self.locations) - 1
		self.locations.append(temp_locs[-1])

	def _check_visited(self, coords1, coords2, epsilon=20):
		return self._distance(coords1, coords2) <= epsilon
	
	def _distance(self, coords1, coords2):
		return geopy.distance.geodesic(coords1, coords2).m

	def update(self):
		if self.state == 'WAITING' and self.route is not None:
			self.state = 'DRIVING'
			self.extract_locations()
			self.update_location()
		elif self.state == 'DRIVING' and self.route is not None:
			if self.ride_status != 'WAITING':
				self.update_location()
				self.route_visited.append((self.longitude, self.latitude))
				self.route_visited_colors.append('purple' if self.ride_status == 'ACCEPTED' else 'blue')
				self.log()				
		self.send_location()

	def log(self):
		print(f'({self.longitude}, {self.latitude}), ')

	def __repr__(self):
		return f'Driver({self.username=}, {self.longitude=}, {self.latitude=}, {self.state=}, {self.ride_status=}, {self.route=})'


def get_drivers():
	global drivers
	r = requests.get(f'{BASE_URL}/driver/simulation-info')
	data = r.json()
	for info in data:
		driver = Driver(**info)
		drivers[driver.username] = driver
		print(driver)

def update_drivers():
	global drivers
	r = requests.get(f'{BASE_URL}/driver/simulation-info')
	data = r.json()
	for info in data:
		if 'route' in info:
			drivers[info['username']].route = info['route']
			drivers[info['username']].ride_status = info['status']
			if info['status'] is not None:
				print(info['status'])
	
	for driver in drivers.values():
		driver.update()
		

def loop(sleep):
	while True:
		update_drivers()
		time.sleep(sleep)

def main(args):
	get_drivers()
	loop(args.sleep)

if __name__ == '__main__':
	parser = argparse.ArgumentParser(description='')
	parser.add_argument('-sleep', type=float, default=0.5, help='Sets sleep time between updates in seconds')
	args = parser.parse_args()
	main(args)