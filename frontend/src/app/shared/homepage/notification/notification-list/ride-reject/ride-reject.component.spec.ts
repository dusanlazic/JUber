import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RideRejectComponent } from './ride-reject.component';

describe('RideRejectComponent', () => {
  let component: RideRejectComponent;
  let fixture: ComponentFixture<RideRejectComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ RideRejectComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(RideRejectComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
