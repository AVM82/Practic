import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MentorDashboardComponent } from './admin-dashboard.component';

describe('AdminDashboardComponent', () => {
  let component: MentorDashboardComponent;
  let fixture: ComponentFixture<MentorDashboardComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [MentorDashboardComponent]
    });
    fixture = TestBed.createComponent(MentorDashboardComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
