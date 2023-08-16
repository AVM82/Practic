import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ReadyPracticMetricComponent } from './ready-practic-metric.component';

describe('ReadyPracticMetricComponent', () => {
  let component: ReadyPracticMetricComponent;
  let fixture: ComponentFixture<ReadyPracticMetricComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [ReadyPracticMetricComponent]
    });
    fixture = TestBed.createComponent(ReadyPracticMetricComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
