import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DetailsPassengerHeaderComponent } from './details-passenger-header.component';

describe('DetailsPassengerComponent', () => {
  let component: DetailsPassengerHeaderComponent;
  let fixture: ComponentFixture<DetailsPassengerHeaderComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ DetailsPassengerHeaderComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(DetailsPassengerHeaderComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
