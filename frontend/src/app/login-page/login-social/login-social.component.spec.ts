import { ComponentFixture, TestBed } from '@angular/core/testing';

import { LoginSocialComponent } from './login-social.component';

describe('LoginSocialComponent', () => {
  let component: LoginSocialComponent;
  let fixture: ComponentFixture<LoginSocialComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ LoginSocialComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(LoginSocialComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
