import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RideDetailsSidebarComponent } from './ride-details-sidebar.component';

describe('RideDetailsSidebarComponent', () => {
  let component: RideDetailsSidebarComponent;
  let fixture: ComponentFixture<RideDetailsSidebarComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ RideDetailsSidebarComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(RideDetailsSidebarComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
