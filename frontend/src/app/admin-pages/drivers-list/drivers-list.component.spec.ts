import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DriversListComponent } from './drivers-list.component';

describe('DriversListComponent', () => {
  let component: DriversListComponent;
  let fixture: ComponentFixture<DriversListComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ DriversListComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(DriversListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
