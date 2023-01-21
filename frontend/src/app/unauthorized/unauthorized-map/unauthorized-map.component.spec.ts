import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UnathorizedMapComponent } from './unauthorized-map.component';

describe('PassengerMapComponent', () => {
  let component: UnathorizedMapComponent;
  let fixture: ComponentFixture<UnathorizedMapComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ UnathorizedMapComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(UnathorizedMapComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
