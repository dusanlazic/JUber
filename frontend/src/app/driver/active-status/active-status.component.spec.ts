import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ActiveStatusComponent } from './active-status.component';

describe('ActiveStatusComponent', () => {
  let component: ActiveStatusComponent;
  let fixture: ComponentFixture<ActiveStatusComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ActiveStatusComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ActiveStatusComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
