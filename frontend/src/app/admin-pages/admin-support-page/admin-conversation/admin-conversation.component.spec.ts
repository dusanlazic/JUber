import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AdminConversationComponent } from './admin-conversation.component';

describe('AdminConversationComponent', () => {
  let component: AdminConversationComponent;
  let fixture: ComponentFixture<AdminConversationComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ AdminConversationComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AdminConversationComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
