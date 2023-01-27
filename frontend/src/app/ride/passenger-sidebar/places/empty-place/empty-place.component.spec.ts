import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EmptyPlaceComponent } from './empty-place.component';

describe('EmptyPlaceComponent', () => {
  let component: EmptyPlaceComponent;
  let fixture: ComponentFixture<EmptyPlaceComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ EmptyPlaceComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(EmptyPlaceComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
