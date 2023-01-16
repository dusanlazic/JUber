import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AdminSupportPageComponent } from './admin-support-page.component';

describe('AdminSupportPageComponent', () => {
  let component: AdminSupportPageComponent;
  let fixture: ComponentFixture<AdminSupportPageComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ AdminSupportPageComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AdminSupportPageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
