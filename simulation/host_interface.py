import requests
import polyline
from abc import ABC

BACKEND_URL = 'http://localhost:8080'
ROUTING_URL = 'http://router.project-osrm.org/route/v1/driving'


class HostInterface(ABC):
	def __init__(self) -> None:
		pass

	def send_location(self):
		payload = {
			'longitude': self.longitude,
			'latitude': self.latitude
		}

		r = requests.put(f'{BACKEND_URL}/driver/location/{self.username}', json=payload) 

	def send_start(self):
		payload = {
			'longitude': self.longitude,
			'latitude': self.latitude
		}
		print("\n\nSTARTING RIDE...\n\n")
		r = requests.put(f'{BACKEND_URL}/ride/start/{self.ride_id}', json=payload)
		print(r)

	def send_end(self):
		payload = {
			'longitude': self.longitude,
			'latitude': self.latitude
		}
		print("\n\ENDING RIDE...\n\n")
		r = requests.put(f'{BACKEND_URL}/ride/end/{self.ride_id}', json=payload)
		print(r)

	def get_route(self, longitude, latitude, next_longitude, next_latitude):
		loc = "{},{};{},{}".format(longitude, latitude, next_longitude, next_latitude)
		url = f"{ROUTING_URL}/{loc}"
		r = requests.get(url) 
		if r.status_code!= 200:
			return {}
		res = r.json()   
		temp_routes = polyline.decode(res['routes'][0]['geometry'])
		routes = []
		for loc in temp_routes:
			routes.append((loc[1], loc[0]))
		return routes