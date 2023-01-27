import { ComponentFixture, TestBed } from '@angular/core/testing';

import { NavigationPageTemplateComponent } from './navigation-page-template.component';

describe('NavigationPageTemplateComponent', () => {
  let component: NavigationPageTemplateComponent;
  let fixture: ComponentFixture<NavigationPageTemplateComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ NavigationPageTemplateComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(NavigationPageTemplateComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
