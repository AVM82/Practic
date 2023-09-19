import { TestBed } from '@angular/core/testing';

import { InfoMessagesService } from './info-messages.service';

describe('InfoMessagesService', () => {
  let service: InfoMessagesService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(InfoMessagesService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
