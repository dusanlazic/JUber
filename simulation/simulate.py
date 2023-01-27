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

# TODO: COORDINATES ENCODED

class DriverState(Enum):
	WAITING = auto()
	DRIVING = auto()


class RideStatus(Enum):
	WAITING_FOR_PAYMENT = auto()
	WAIT = auto()
	ACCEPTED = auto()
	IN_PROGRESS = auto()
	SCHEDULED = auto()


class Driver(HostInterface):
	def __init__(self, username: str, latitude: float, longitude: float, status: str, places: list, rideId: str):
		self.username = username
		self.latitude = latitude
		self.longitude = longitude
		self.places = places
		self.state = DriverState.WAITING
		self.ride_status = RideStatus.WAIT if status is None else RideStatus[status]
		self.coordinates = []
		self.visiting = 0
		self.route_visited = []
		self.route_visited_colors = []
		self.ride_id = rideId
		self.random_coordinates = []

		self.start_idx = math.inf;
		self.wait_for_entered = False

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
		if self.wait_for_entered:
			return

		if self._check_visited(curr, succ):
			self.visiting += 1
			if self.visiting >= len(self.coordinates) - 1:
				self.places = None
				self.state = DriverState.WAITING
				self.coordinates = []
				self.ride_status = RideStatus.WAIT
				self.start_idx = math.inf
				self.wait_for_entered = False
				self._finish_log()
				# self.send_end()
			elif self.visiting >= self.start_idx and self.ride_status == RideStatus.ACCEPTED:
				self.wait_for_entered = True
				# self.send_start()
		else:
			self._set_new_coordinates(curr, succ)


	def extract_coordinates(self):
		for place in self.places:
			if len(place["routes"]) == 0:
				continue
			selected_route = list(filter(lambda x: x['selected'], place['routes']))[0]
			self.coordinates.extend(polyline.decode(selected_route['coordinatesEncoded']))
		temp_coords = self.get_route(self.longitude, self.latitude, self.coordinates[0][1], self.coordinates[0][0])
		self.start_idx = len(temp_coords) - 1
		temp_coords.extend(self.coordinates)
		self.coordinates = temp_coords


	def generate_random_coordinates(self):
		self.random_coordinates = []
		balans_lat, balans_lon = 45.25232843778114, 19.8375130497505
		while True:
			random_vector_angle = np.random.random() * 2 * np.pi
			magnitude = 5e-3
			random_goal_lat = self.latitude + np.sin(random_vector_angle) * magnitude
			random_goal_lon = self.longitude + np.cos(random_vector_angle) * magnitude
			if self.distance(random_goal_lat, random_goal_lon, balans_lat, balans_lon) < 5:
				break
		print(f'MOVING TO RANDOM COORDINATES .  .  . => {self.latitude} {self.longitude} {random_goal_lat}, {random_goal_lon}')
		self.random_coordinates.extend(self.get_route(self.longitude, self.latitude, random_goal_lon, random_goal_lat))
		self.random_visiting

	def distance(self, start_lat, start_lon, end_lat, end_lon):
		return geopy.distance.distance((start_lat, start_lon), (end_lat, end_lon)).km

	
	def move_randomly(self):
		if len(self.random_coordinates) == 0:
			self.random_visiting = 0
			self.generate_random_coordinates()
		
		curr = self.latitude, self.longitude
		succ = self.random_coordinates[self.random_visiting]

		if self._check_visited(curr, succ):
			self.random_visiting += 1
			if self.random_visiting >= len(self.random_coordinates) - 1:
				self.generate_random_coordinates()
				self._finish_log()
				self.random_coordinates = []
		else:
			self._set_new_coordinates(curr, succ)


	def update(self):

		color_coding = {
			RideStatus.ACCEPTED: 'purple',
			RideStatus.IN_PROGRESS: 'blue',
			RideStatus.WAIT: 'green'
		}

		self.route_visited.append((self.latitude, self.longitude))
		self.route_visited_colors.append(color_coding[self.ride_status])

		print("Doing: ", self.ride_status, self.state, self.start_idx, len(self.coordinates), self.wait_for_entered, len(self.places) if self.places else 0)
		print()
		if self.ride_status == RideStatus.WAIT or self.ride_status == RideStatus.WAITING_FOR_PAYMENT or self.ride_status == RideStatus.SCHEDULED:
			self.move_randomly()
			self.send_location()
			return

		if self.ride_status == RideStatus.IN_PROGRESS and self.wait_for_entered:
			print("Wait for entered is false, resuming ride")
			self.wait_for_entered = False

		if self.places is None or len(self.places) == 0:
			return

		if len(self.coordinates) == 0 and self.places is not None:
			print("DID EXTRACT!!!!!!!!!!!!!!!")
			self.extract_coordinates()

		self.update_coordinates()
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
		return f'Driver({self.username=}, {self.longitude=}, {self.latitude=}, {self.state=}, {self.ride_status=}, places: {len(self.places) if self.places else None})'


def get_drivers():
	global drivers
	r = requests.get(f'{BACKEND_URL}/simulation/drivers')
	data = r.json()
	for info in data:
		print(data)
		if info['username'] != 'zdravko.zdravkovic@gmail.com':
			continue
		driver = Driver(**info)
		drivers[driver.username] = driver
		if driver.places is None:
			continue
		print(json.dumps(info, indent=2))

		for place in driver.places:
			print(place['name'], len(place['routes']))
	print("Done getting drivers.")


def update_drivers():
	global drivers
	r = requests.get(f'{BACKEND_URL}/simulation/drivers')

	data = r.json()
	for info in data:
		if info['username'] != 'zdravko.zdravkovic@gmail.com':
			continue
		if 'places' in info:
			status = info['status']
			if info['rideId'] != drivers[info['username']].ride_id:
				driver = Driver(**info)
				drivers[info['username']] = driver	
				print()
			else:
				drivers[info['username']].places = info['places']
				drivers[info['username']].ride_status = RideStatus.WAIT if status is None else RideStatus[status]
	try:
		for driver in drivers.values():
			driver.update()
	except Exception as e:
		print(e)

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