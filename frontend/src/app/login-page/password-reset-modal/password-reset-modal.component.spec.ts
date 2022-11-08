import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PasswordResetModalComponent } from './password-reset-modal.component';

describe('PasswordResetModalComponent', () => {
  let component: PasswordResetModalComponent;
  let fixture: ComponentFixture<PasswordResetModalComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ PasswordResetModalComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(PasswordResetModalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
