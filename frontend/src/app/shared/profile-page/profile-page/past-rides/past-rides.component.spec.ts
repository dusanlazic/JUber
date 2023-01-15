import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PastRidesComponent } from './past-rides.component';

describe('PastRidesComponent', () => {
  let component: PastRidesComponent;
  let fixture: ComponentFixture<PastRidesComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ PastRidesComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(PastRidesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
