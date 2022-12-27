import time
import requests
import polyline
import geopy.distance
import numpy as np
import json
import math
import argparse
from enum import Enum, auto
from host_interface import HostInterface, BACKEND_URL


drivers = {}


class DriverState(Enum):
	WAITING = auto()
	DRIVING = auto()


class RideStatus(Enum):
	WAITING = auto()
	ACCEPTED = auto()
	IN_PROGRESS = auto()


class Driver(HostInterface):
	def __init__(self, username: str, latitude: float, longitude: float, status: str, places: list, rideId: str):
		self.username = username
		self.longitude = longitude
		self.latitude = latitude
		self.places = places
		self.state = DriverState.WAITING
		self.ride_status = None if status is None else RideStatus[status]
		self.coordinates = []
		self.visiting = 0
		self.route_visited = []
		self.route_visited_colors = []
		self.ride_id = rideId


	def _set_new_coordinates(self, curr, succ):
		dist = self._distance(curr, succ) 
		if dist > 40:
			d = dist // 40
			self.latitude = (curr[0]*(d - 1) + succ[0]) / d
			self.longitude = (curr[1]*(d - 1) + succ[1]) / d
		else:
			self.latitude = succ[0]
			self.longitude = succ[1]


	def update_coordinates(self):
		curr = self.latitude, self.longitude
		succ = self.coordinates[self.visiting]
		if self._check_visited(curr, succ):
			self.visiting += 1
			if self.visiting >= len(self.coordinates):
				self.route = None
				self.state = DriverState.WAITING
				self._finish_log()
				self.send_end()
			elif self.visiting > self.start_idx and self.ride_status == RideStatus.ACCEPTED:
				self.send_start()
		else:
			self._set_new_coordinates(curr, succ)


	def extract_coordinates(self):
		for place in self.places:
			if len(place["routes"]) == 0:
				continue
			selected_route = list(filter(lambda x: x['selected'], place['routes']))[0]
			self.coordinates.extend(polyline.decode(selected_route['coordinates']))
		self.start_idx = len(self.coordinates)
		temp_coords = self.get_route(self.longitude, self.latitude, self.coordinates[0][1], self.coordinates[0][0])
		temp_coords.extend(self.coordinates)
		self.coordinates = temp_coords
		print(f"MY COORDINATES {self.coordinates}")


	def update(self):

		if self.places is None or len(self.places) == 0:
			return

		if len(self.coordinates) == 0 and self.places is not None:
			self.extract_coordinates()
		print(self.latitude, self.longitude)
		if self.ride_status != RideStatus.WAITING:
			self.update_coordinates()
			self.route_visited.append((self.longitude, self.latitude))
			self.route_visited_colors.append('purple' if self.ride_status == RideStatus.ACCEPTED else 'blue')
			self.log()
				
		self.send_location()


	def _finish_log(self):
		print("DONE!")
		f = {'arr': [self.route_visited, self.route_visited_colors]}
		with open('route_visited.json', 'w', encoding ='utf8') as json_file:
			json.dump(f, json_file, ensure_ascii = False)


	def log(self):
		print(f'({self.latitude}, {self.longitude})')


	def _check_visited(self, coords1, coords2, epsilon=10):
		return self._distance(coords1, coords2) <= epsilon


	def _distance(self, coords1, coords2):
		return geopy.distance.geodesic(coords1, coords2).m


	def __repr__(self):
		return f'Driver({self.username=}, {self.longitude=}, {self.latitude=}, {self.state=}, {self.ride_status=}, places: {len(self.places)})'


def get_drivers():
	global drivers
	r = requests.get(f'{BACKEND_URL}/simulation/drivers')
	data = r.json()
	for info in data:
		driver = Driver(**info)
		drivers[driver.username] = driver
		if driver.places is None:
			continue
		print(json.dumps(info, indent=2))

		for place in driver.places:
			print(place['name'], len(place['routes']))


def update_drivers():
	global drivers
	r = requests.get(f'{BACKEND_URL}/simulation/drivers')
	data = r.json()
	for info in data:
		if 'places' in info:
			status = info['status']
			drivers[info['username']].route = info['places']
			drivers[info['username']].ride_status = None if status is None else RideStatus[status]
	
	for driver in drivers.values():
		driver.update()
		

def loop(sleep):
	# while True:
	# 	update_drivers()
	# 	time.sleep(sleep)
	pass


def main(args):
	get_drivers()
	# loop(args.sleep)


if __name__ == '__main__':
	parser = argparse.ArgumentParser(description='')
	parser.add_argument('-sleep', type=float, default=0.5, help='Sets sleep time between updates in seconds')
	args = parser.parse_args()
	main(args)