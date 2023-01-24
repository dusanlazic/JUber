import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PastRidesTableComponent } from './past-rides-table.component';

describe('PastRidesTableComponent', () => {
  let component: PastRidesTableComponent;
  let fixture: ComponentFixture<PastRidesTableComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ PastRidesTableComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(PastRidesTableComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
