import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PassengerRideInvitationComponent } from './passenger-ride-invitation.component';

describe('PassengerRideInvitationComponent', () => {
  let component: PassengerRideInvitationComponent;
  let fixture: ComponentFixture<PassengerRideInvitationComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ PassengerRideInvitationComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(PassengerRideInvitationComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
