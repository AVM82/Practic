import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ReportButtonComponent } from './report-button.component';

describe('ReportButtonComponent', () => {
  let component: ReportButtonComponent;
  let fixture: ComponentFixture<ReportButtonComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [ReportButtonComponent]
    });
    fixture = TestBed.createComponent(ReportButtonComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
