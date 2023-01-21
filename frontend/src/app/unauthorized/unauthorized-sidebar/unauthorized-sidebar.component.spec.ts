import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UnathorizedSidebarComponent } from './unauthorized-sidebar.component';

describe('PassengerSidebarComponent', () => {
  let component: UnathorizedSidebarComponent;
  let fixture: ComponentFixture<UnathorizedSidebarComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ UnathorizedSidebarComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(UnathorizedSidebarComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
