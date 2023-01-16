import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AdminSupportComponent } from './admin-support.component';

describe('AdminSupportComponent', () => {
  let component: AdminSupportComponent;
  let fixture: ComponentFixture<AdminSupportComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ AdminSupportComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AdminSupportComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
