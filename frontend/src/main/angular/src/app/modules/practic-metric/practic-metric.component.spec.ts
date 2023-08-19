import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PracticMetricComponent } from './practic-metric.component';

describe('ReadyPracticMetricComponent', () => {
  let component: PracticMetricComponent;
  let fixture: ComponentFixture<PracticMetricComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [PracticMetricComponent]
    });
    fixture = TestBed.createComponent(PracticMetricComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
