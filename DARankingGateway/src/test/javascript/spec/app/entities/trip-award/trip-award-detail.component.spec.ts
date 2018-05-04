/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';

import { DaRankingGatewayTestModule } from '../../../test.module';
import { TripAwardDetailComponent } from '../../../../../../main/webapp/app/entities/trip-award/trip-award-detail.component';
import { TripAwardService } from '../../../../../../main/webapp/app/entities/trip-award/trip-award.service';
import { TripAward } from '../../../../../../main/webapp/app/entities/trip-award/trip-award.model';

describe('Component Tests', () => {

    describe('TripAward Management Detail Component', () => {
        let comp: TripAwardDetailComponent;
        let fixture: ComponentFixture<TripAwardDetailComponent>;
        let service: TripAwardService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [DaRankingGatewayTestModule],
                declarations: [TripAwardDetailComponent],
                providers: [
                    TripAwardService
                ]
            })
            .overrideTemplate(TripAwardDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(TripAwardDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(TripAwardService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                spyOn(service, 'find').and.returnValue(Observable.of(new HttpResponse({
                    body: new TripAward(123)
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.find).toHaveBeenCalledWith(123);
                expect(comp.tripAward).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
