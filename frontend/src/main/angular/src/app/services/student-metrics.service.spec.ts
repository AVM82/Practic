import { TestBed } from '@angular/core/testing';

import { StudentMetricsService } from './student-metrics.service';

describe('StudentMetricsService', () => {
  let service: StudentMetricsService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(StudentMetricsService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
