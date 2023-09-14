import { ComponentFixture, TestBed } from '@angular/core/testing';
import { AdditionalMaterialsComponent } from './additional-materials.component';

describe('CAdditionalMaterialsComponent', () => {
  let component: AdditionalMaterialsComponent;
  let fixture: ComponentFixture<AdditionalMaterialsComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [AdditionalMaterialsComponent]
    });
    fixture = TestBed.createComponent(AdditionalMaterialsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
