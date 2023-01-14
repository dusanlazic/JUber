import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RideAcceptedComponent } from './ride-accepted.component';

describe('RideAcceptedComponent', () => {
  let component: RideAcceptedComponent;
  let fixture: ComponentFixture<RideAcceptedComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ RideAcceptedComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(RideAcceptedComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
