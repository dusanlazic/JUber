import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PassengerMapComponent } from './passenger-map.component';

describe('PassengerMapComponent', () => {
  let component: PassengerMapComponent;
  let fixture: ComponentFixture<PassengerMapComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ PassengerMapComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(PassengerMapComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
