import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RideDetailsPlaceComponent } from './ride-details-place.component';

describe('RideDetailsPlaceComponent', () => {
  let component: RideDetailsPlaceComponent;
  let fixture: ComponentFixture<RideDetailsPlaceComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ RideDetailsPlaceComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(RideDetailsPlaceComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
