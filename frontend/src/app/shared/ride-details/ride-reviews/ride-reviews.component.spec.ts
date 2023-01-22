import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RideReviewsComponent } from './ride-reviews.component';

describe('RideReviewsComponent', () => {
  let component: RideReviewsComponent;
  let fixture: ComponentFixture<RideReviewsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ RideReviewsComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(RideReviewsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
