import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AddPalsDialogComponent } from './add-pals-dialog.component';

describe('AddPalsDialogComponent', () => {
  let component: AddPalsDialogComponent;
  let fixture: ComponentFixture<AddPalsDialogComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ AddPalsDialogComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AddPalsDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
