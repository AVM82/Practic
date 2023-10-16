import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ChangingReportDialogComponent } from './changing-report-dialog.component';

describe('ChangingReportDialogComponent', () => {
  let component: ChangingReportDialogComponent;
  let fixture: ComponentFixture<ChangingReportDialogComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ChangingReportDialogComponent]
    });
    fixture = TestBed.createComponent(ChangingReportDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
