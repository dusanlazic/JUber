import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RideDetailsMapComponent } from './ride-details-map.component';

describe('RideDetailsMapComponent', () => {
  let component: RideDetailsMapComponent;
  let fixture: ComponentFixture<RideDetailsMapComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ RideDetailsMapComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(RideDetailsMapComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
