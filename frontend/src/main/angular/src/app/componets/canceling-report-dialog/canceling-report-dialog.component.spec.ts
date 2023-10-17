import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CancelingReportDialogComponent } from './canceling-report-dialog.component';

describe('CancelingReportDialogComponent', () => {
  let component: CancelingReportDialogComponent;
  let fixture: ComponentFixture<CancelingReportDialogComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [CancelingReportDialogComponent]
    });
    fixture = TestBed.createComponent(CancelingReportDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});