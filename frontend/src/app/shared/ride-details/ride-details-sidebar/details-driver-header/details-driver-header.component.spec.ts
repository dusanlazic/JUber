import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DetailsDriverHeaderComponent } from './details-driver-header.component';

describe('DetailsDriverHeaderComponent', () => {
  let component: DetailsDriverHeaderComponent;
  let fixture: ComponentFixture<DetailsDriverHeaderComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ DetailsDriverHeaderComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(DetailsDriverHeaderComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
