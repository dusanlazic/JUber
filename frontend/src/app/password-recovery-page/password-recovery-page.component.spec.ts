import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PasswordRecoveryPageComponent } from './password-recovery-page.component';

describe('PasswordRecoveryPageComponent', () => {
  let component: PasswordRecoveryPageComponent;
  let fixture: ComponentFixture<PasswordRecoveryPageComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ PasswordRecoveryPageComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(PasswordRecoveryPageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
