/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';

import { DaRankingGatewayTestModule } from '../../../test.module';
import { DriverAwardDetailComponent } from '../../../../../../main/webapp/app/entities/driver-award/driver-award-detail.component';
import { DriverAwardService } from '../../../../../../main/webapp/app/entities/driver-award/driver-award.service';
import { DriverAward } from '../../../../../../main/webapp/app/entities/driver-award/driver-award.model';

describe('Component Tests', () => {

    describe('DriverAward Management Detail Component', () => {
        let comp: DriverAwardDetailComponent;
        let fixture: ComponentFixture<DriverAwardDetailComponent>;
        let service: DriverAwardService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [DaRankingGatewayTestModule],
                declarations: [DriverAwardDetailComponent],
                providers: [
                    DriverAwardService
                ]
            })
            .overrideTemplate(DriverAwardDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(DriverAwardDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(DriverAwardService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                spyOn(service, 'find').and.returnValue(Observable.of(new HttpResponse({
                    body: new DriverAward(123)
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.find).toHaveBeenCalledWith(123);
                expect(comp.driverAward).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
