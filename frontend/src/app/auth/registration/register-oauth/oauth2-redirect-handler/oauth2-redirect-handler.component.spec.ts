import { ComponentFixture, TestBed } from '@angular/core/testing';

import { Oauth2RegisterRedirectHandlerComponent } from './oauth2-redirect-handler.component';

describe('Oauth2RegisterRedirectHandlerComponent', () => {
  let component: Oauth2RegisterRedirectHandlerComponent;
  let fixture: ComponentFixture<Oauth2RegisterRedirectHandlerComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ Oauth2RegisterRedirectHandlerComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(Oauth2RegisterRedirectHandlerComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
