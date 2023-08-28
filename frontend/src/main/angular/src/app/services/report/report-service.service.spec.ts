import { TestBed } from '@angular/core/testing';

import { ReportServiceService } from './report-service.service';

describe('ReportServiceService', () => {
  let service: ReportServiceService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ReportServiceService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
