import { TestBed } from '@angular/core/testing';

import { CalendarEventService } from './calendar-event.service';

describe('CalendarEventService', () => {
  let service: CalendarEventService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(CalendarEventService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
