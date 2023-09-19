import { ComponentFixture, TestBed } from '@angular/core/testing';

import { NewReportDialogComponent } from './new-report-dialog.component';

describe('NewReportComponent', () => {
  let component: NewReportDialogComponent;
  let fixture: ComponentFixture<NewReportDialogComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [NewReportDialogComponent]
    });
    fixture = TestBed.createComponent(NewReportDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
