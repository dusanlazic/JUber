import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PasswordResetRequestSuccessComponent } from './password-reset-request-success.component';

describe('PasswordResetRequestSuccessComponent', () => {
  let component: PasswordResetRequestSuccessComponent;
  let fixture: ComponentFixture<PasswordResetRequestSuccessComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ PasswordResetRequestSuccessComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(PasswordResetRequestSuccessComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
