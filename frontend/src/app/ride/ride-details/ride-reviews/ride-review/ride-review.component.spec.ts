import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RideReviewComponent } from './ride-review.component';

describe('RideReviewComponent', () => {
  let component: RideReviewComponent;
  let fixture: ComponentFixture<RideReviewComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ RideReviewComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(RideReviewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
