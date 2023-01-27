import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RegisterStep2Component } from './register-step2.component';

describe('RegisterStep2Component', () => {
  let component: RegisterStep2Component;
  let fixture: ComponentFixture<RegisterStep2Component>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ RegisterStep2Component ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(RegisterStep2Component);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
