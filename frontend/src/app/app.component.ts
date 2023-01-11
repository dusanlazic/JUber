import { Component } from '@angular/core';
import { RideWebSocketAPI } from 'src/services/ride/ride-message.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.sass']
})
export class AppComponent {
  title = 'frontend';

              
  constructor(private webSocketAPI: RideWebSocketAPI) {
    webSocketAPI.connect();
  }
}
