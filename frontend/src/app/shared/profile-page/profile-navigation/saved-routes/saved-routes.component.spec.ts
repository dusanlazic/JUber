import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SavedRoutesComponent } from './saved-routes.component';

describe('SavedRoutesComponent', () => {
  let component: SavedRoutesComponent;
  let fixture: ComponentFixture<SavedRoutesComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ SavedRoutesComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(SavedRoutesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
