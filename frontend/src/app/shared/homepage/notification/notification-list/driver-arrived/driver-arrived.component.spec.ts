import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DriverArrivedComponent } from './driver-arrived.component';

describe('DriverArrivedComponent', () => {
  let component: DriverArrivedComponent;
  let fixture: ComponentFixture<DriverArrivedComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ DriverArrivedComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(DriverArrivedComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
