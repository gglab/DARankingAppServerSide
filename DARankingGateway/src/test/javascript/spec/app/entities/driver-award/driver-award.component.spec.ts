/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { DaRankingGatewayTestModule } from '../../../test.module';
import { DriverAwardComponent } from '../../../../../../main/webapp/app/entities/driver-award/driver-award.component';
import { DriverAwardService } from '../../../../../../main/webapp/app/entities/driver-award/driver-award.service';
import { DriverAward } from '../../../../../../main/webapp/app/entities/driver-award/driver-award.model';

describe('Component Tests', () => {

    describe('DriverAward Management Component', () => {
        let comp: DriverAwardComponent;
        let fixture: ComponentFixture<DriverAwardComponent>;
        let service: DriverAwardService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [DaRankingGatewayTestModule],
                declarations: [DriverAwardComponent],
                providers: [
                    DriverAwardService
                ]
            })
            .overrideTemplate(DriverAwardComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(DriverAwardComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(DriverAwardService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN
                const headers = new HttpHeaders().append('link', 'link;link');
                spyOn(service, 'query').and.returnValue(Observable.of(new HttpResponse({
                    body: [new DriverAward(123)],
                    headers
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.query).toHaveBeenCalled();
                expect(comp.driverAwards[0]).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
