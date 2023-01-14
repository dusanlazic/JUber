import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RideInviteComponent } from './ride-invite.component';

describe('RideInviteComponent', () => {
  let component: RideInviteComponent;
  let fixture: ComponentFixture<RideInviteComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ RideInviteComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(RideInviteComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
