import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RegisterLocalComponent } from './register-local.component';

describe('RegisterLocalComponent', () => {
  let component: RegisterLocalComponent;
  let fixture: ComponentFixture<RegisterLocalComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ RegisterLocalComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(RegisterLocalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
