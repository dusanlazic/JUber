import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SupportMessageComponent } from './support-message.component';

describe('SupportMessageComponent', () => {
  let component: SupportMessageComponent;
  let fixture: ComponentFixture<SupportMessageComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ SupportMessageComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(SupportMessageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
