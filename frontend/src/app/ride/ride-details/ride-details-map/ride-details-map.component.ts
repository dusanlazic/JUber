import {
  AfterViewInit,
  Component,
  ElementRef,
  Input,
  OnChanges,
  OnDestroy,
  OnInit,
  SimpleChanges,
} from '@angular/core';
import * as L from 'leaflet';
import { filter, lastValueFrom, Observable, Subscription } from 'rxjs';
import { Point } from 'src/models/map';
import { FullRide, Place, Ride, Route } from 'src/models/ride';
import { MapService } from '../../services/map/map.service';
import { NominatimService } from '../../services/map/nominatim.service';
import { HttpRequestService } from 'src/services/util/http-request.service';
import 'leaflet-routing-machine';
import { State, StateObservable, Store } from '@ngrx/store';
import { AppState } from 'src/app/store/ride.reducer';
import { PreviewRouteSelectedAction } from 'src/app/store/ride.actions';
import { environment } from 'src/environments/environment';
import { LoggedUser } from 'src/models/user';
import { AuthService } from 'src/services/auth/auth.service';
import { ActivatedRoute } from '@angular/router';
import { LocationSocketShareService } from 'src/services/location-message/locationshare.service';

@Component({
  selector: 'app-ride-details-map',
  templateUrl: './ride-details-map.component.html',
  styleUrls: ['./ride-details-map.component.sass'],
})
export class RideDetailsMapComponent implements AfterViewInit, OnDestroy, OnChanges {

  private subscription: Subscription | undefined;

  constructor(
    private mapService: MapService,
    private httpService: HttpRequestService,
    private nominatimService: NominatimService,
    private authService: AuthService,
    private store: Store<{ state: AppState }>,
    public route: ActivatedRoute,
    private el: ElementRef,
    private locationShareService: LocationSocketShareService
  ) {
    this.authService.getCurrentUser().subscribe({
      next: (user) => {
        this.loggedUser = user;
      },
      error: (e: any) => {
        console.log(e);
      },
    });

    
  }

  private map!: L.Map;
  @Input() ride: FullRide | undefined;

  loggedUser!: LoggedUser;
  container!: HTMLElement;

  // ride!: Observable<AppState>;

  center: L.LatLng = new L.LatLng(45.2671, 19.8335);
  zoom: number = 13;

  controls: L.Control[] = [];
  lines: L.Polyline[] = [];

  myIcon = L.divIcon({ className: 'my-div-icon' });

  mapOptions = {
    show: false,
    routeWhileDragging: false,
    showAlternatives: true,
    lineOptions: {
      styles: [{ color: 'red', weight: 4 }],
      addWaypoints: false,
      extendToWaypoints: true,
      missingRouteTolerance: 0,
    },
    altLineOptions: {
      styles: [{ color: 'blue', opacity: 0.5, weight: 3 }],
      extendToWaypoints: true,
      missingRouteTolerance: 0,
    },
  };

  ngOnChanges(changes: SimpleChanges) {
    if(this.ride) {
      this.drawRide(this.ride);
      this.getAndDrawDrivers();

      if (this.loggedUser.role == 'ROLE_PASSENGER' && this.ride?.rideStatus == 'IN_PROGRESS' || this.ride?.rideStatus == 'ACCEPTED'){
        this.subscription = this.locationShareService.getNewValue().subscribe((data) => {
          let ret = JSON.parse(data)
          if (this.ride?.driver && ret.email === this.ride?.driver.email) {
            this.drawDriver(ret)
          }
        });
      }
    }
    else {
      if (this.loggedUser.role == 'ROLE_DRIVER') {
        this.subscription = this.locationShareService.getNewValue().subscribe((data) => {
          let ret = JSON.parse(data)
          if (ret.email === this.loggedUser.email) {
            this.drawDriver(ret)
          }
        });
        return;
      }
    }
    
  }

  private initMap(): void {


    this.map = L.map('details-map', {
      center: this.center,
      zoom: this.zoom,
    });

    const tiles = L.tileLayer(
      'https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png',
      {
        maxZoom: 18,
        minZoom: 3,
      }
    );

    tiles.addTo(this.map);
  }

  timer!: NodeJS.Timer;

  ngAfterViewInit(): void {
    this.initMap();
    this.container = this.el.nativeElement.querySelector('#details-map');
    this.getAndDrawDrivers();
  }

  ngOnDestroy(): void {
    this.map.remove();
    this.map.off()
    this.subscription?.unsubscribe();
    this.container!.parentNode!.removeChild(this.container);
  }

  drawRide(ride: FullRide) {
    if (ride.places.length > 1) {
      let i = 1;
      this.drawMarker(ride.places[0].point!, this.mapService.colors[0]);
      for (; i < ride.places.length; i++) {
        let curr = ride.places[i];
        if (curr.editing) continue;
        let selectedRoute = curr.routes.filter((x) => x.selected).at(0);
        this.drawPolyline(
          selectedRoute!.coordinates,
          this.mapService.colors[i]
        );
        this.drawMarker(curr.point!, this.mapService.colors[i]);
      }
    } else if (ride.places.length == 1) {
      this.drawMarker(ride.places[0].point!, this.mapService.colors[0]);
    }
  }

  clearMap() {
    this.center = this.map.getCenter();
    this.zoom = this.map.getZoom();
    this.map = this.map.remove();
    this.initMap();
  }

  drawMarker(point: Point, color: string) {
    const markerHtmlStyles = `
			background-color: ${color};
			width: 2rem;
			height: 2rem;
			display: block;
			left: -1.5rem;
			top: -1.5rem;
			position: relative;
			border-radius: 2rem 2rem 0;
			transform: rotate(45deg);
			border: 1px solid #FFFFFF
		`;

    const icon = L.divIcon({
      className: 'my-custom-pin',
      iconAnchor: [0, 24],
      popupAnchor: [0, -36],
      html: `<span style="${markerHtmlStyles}" />`,
    });
    L.marker([point.latitude, point.longitude], { icon: icon }).addTo(this.map);
  }

  drawPolyline(
    points: Array<Point>,
    color = 'red',
    weight = 4,
    selectable = false,
    route?: Route,
    place?: Place
  ) {
    let leafletPts: Array<[number, number]> = [];

    for (const point of points) {
      leafletPts.push([point.latitude, point.longitude]);
    }

    let polyline = new L.Polyline(leafletPts, {
      color: color,
      weight: weight,
    });

    this.lines.push(polyline);

    if (selectable && route && place) {
      polyline.on('click', (event) => {
        this.store.dispatch(PreviewRouteSelectedAction({ route: route }));
      });
    }

    polyline.addTo(this.map);
  }

  getAndDrawDrivers() {
    if (this.loggedUser.role === 'ROLE_DRIVER') {
      this.drawMyself();
    }

    if (!this.ride || !this.ride.driver || !this.ride.driver.id) return;

    this.httpService
      .get(
        environment.API_BASE_URL +
          '/accounts/drivers/location/' +
          this.ride.driver.id
      )
      .subscribe((data) => {
        this.drawDriver(data);
	});
  }

  drawMyself() {
    this.httpService
      .get(
        environment.API_BASE_URL +
          '/accounts/drivers/location/' +
          this.loggedUser.id
      )
      .subscribe((data) => {
        this.drawDriver(data);
	});
  }

  driverIcon = L.icon({
    iconUrl: '/assets/images/driver-pin.png',
    iconAnchor: [34, 34], // point of the icon which will correspond to marker's location
    iconSize: [38, 38], // size of the icon
    popupAnchor: [-3, -3], // point from which the popup should open relative to the iconAnchor
  });

  driverMarkers: L.Marker[] = [];

  drawDriver(data: { latitude: number; longitude: number; email: string}) {
    
    if(data.email !== this.ride?.driver?.email && this.loggedUser.role !== 'ROLE_DRIVER') 
      return;

    for (let driverMaker of this.driverMarkers) {
      this.map.removeLayer(driverMaker);
    }

    let driver = data;
    
    let marker = L.marker([driver.latitude, driver.longitude], {
      icon: this.driverIcon,
    }).addTo(this.map);
    marker.bindPopup(driver.email);
    this.driverMarkers.push(marker);
  }
}
