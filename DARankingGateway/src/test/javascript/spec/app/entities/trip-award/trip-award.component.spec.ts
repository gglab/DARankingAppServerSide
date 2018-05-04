/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { DaRankingGatewayTestModule } from '../../../test.module';
import { TripAwardComponent } from '../../../../../../main/webapp/app/entities/trip-award/trip-award.component';
import { TripAwardService } from '../../../../../../main/webapp/app/entities/trip-award/trip-award.service';
import { TripAward } from '../../../../../../main/webapp/app/entities/trip-award/trip-award.model';

describe('Component Tests', () => {

    describe('TripAward Management Component', () => {
        let comp: TripAwardComponent;
        let fixture: ComponentFixture<TripAwardComponent>;
        let service: TripAwardService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [DaRankingGatewayTestModule],
                declarations: [TripAwardComponent],
                providers: [
                    TripAwardService
                ]
            })
            .overrideTemplate(TripAwardComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(TripAwardComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(TripAwardService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN
                const headers = new HttpHeaders().append('link', 'link;link');
                spyOn(service, 'query').and.returnValue(Observable.of(new HttpResponse({
                    body: [new TripAward(123)],
                    headers
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.query).toHaveBeenCalled();
                expect(comp.tripAwards[0]).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
