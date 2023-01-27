import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DriverInfoComponent } from './driver-info.component';

describe('DriverInfoComponent', () => {
  let component: DriverInfoComponent;
  let fixture: ComponentFixture<DriverInfoComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ DriverInfoComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(DriverInfoComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
